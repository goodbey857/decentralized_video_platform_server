package top.kingdon.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.kingdon.dataobject.po.VideoLike;
import top.kingdon.dataobject.po.VideoStar;
import top.kingdon.service.FollowService;
import top.kingdon.service.VideoLikeService;
import top.kingdon.service.VideoStarService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class PrepareDataRunner implements ApplicationRunner {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    VideoStarService starService;
    @Resource
    VideoLikeService likeService;

    private class Counter{
        int value = 0;
        void increase(){
            this.value++;
        }
        void decrease(){
            this.value--;
        }
        @Override
        public String toString(){
            return String.valueOf(this.value);
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 准备数据的方法
        prepareFansData();
        prepareFollowData();
        prepareStarData();
        prepareLikeData();
        prepareViewData();
        prepareCommentCount();
        prepareVideoCount();
        System.gc();

    }

    private void prepareFansData() {
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select count(follower_address) as count,following_address from follow where canceled_at is null group by following_address");
        HashMap<Object, Object> followMap = new HashMap<>();
        maps.forEach(map -> {
            followMap.put(map.get("following_address"),map.get("count") );
        });
        redisTemplate.opsForHash().putAll(RedisKey.FAN_COUNT_KEY,followMap);
    }

    private void prepareFollowData() {
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select count(following_address) as count,follower_address from follow where canceled_at is null group by follower_address");
        HashMap<Object, Object> followMap = new HashMap<>();
        maps.forEach(map -> {
            followMap.put(map.get("follower_address"),map.get("count") );
        });
        redisTemplate.opsForHash().putAll(RedisKey.FOLLOW_COUNT_KEY,followMap);
    }

    private void prepareStarData() {
        HashMap<Integer, List<String>> starMap = new HashMap<>();
        List<VideoStar> list;
        int i=1;
        do {

            Page<VideoStar> page = starService.page(new Page<VideoStar>((i-1)*1000, 1000*i),new QueryWrapper<VideoStar>().isNull("canceled_at"));
            list = page.getRecords();
            list.forEach(star -> {
                List<String> address = starMap.putIfAbsent(star.getVideoId(), new LinkedList<String>());

                if(address == null){
                    address = starMap.get(star.getVideoId());
                }
                address.add(star.getUserAddress());
            });
            ++i;
        } while (list.size() ==1000);
        starMap.forEach((k,v)->redisTemplate.opsForSet().add(RedisKey.getStarKeyOfVideo(k),v.toArray()));

    }

    private void prepareLikeData(){
        HashMap<Integer, List<String>> likeMap = new HashMap<>();

        List<VideoLike> list;
        int i=1;
        do {

            Page<VideoLike> page = likeService.page(new Page<VideoLike>((i-1)*1000, 1000*i),new QueryWrapper<VideoLike>().isNull("canceled_at"));
            list = page.getRecords();
            list.forEach(like ->{
                List<String> address = likeMap.putIfAbsent(like.getVideoId(), new LinkedList<String>());

                if(address == null){
                    address = likeMap.get(like.getVideoId());
                }
                address.add(like.getUserAddress());
            });
            ++i;

        }while (list.size() ==1000);

        likeMap.forEach((k,v)->redisTemplate.opsForSet().add(RedisKey.getLikeKeyOfVideo(k),v.toArray()));
    }

   private void prepareViewData() {
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select video_id, count(1) as count from video_history where canceled_at is null group by video_id;");

        maps.forEach(map -> redisTemplate.opsForZSet().add( RedisKey.VIEW_KEY,map.get("video_id"),(Long)map.get("count")));

    }

    private void prepareCommentCount(){
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select video_id, count(1) as count from comment  where canceled_at is null  group by video_id;");
        maps.forEach(map -> redisTemplate.opsForHash().put(RedisKey.COMMENT_COUNT_KEY,map.get("video_id").toString(),map.get("count")));
    }

    private void prepareVideoCount(){
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select user_address as address,count(1) as count from videos group by user_address;");
        HashMap<String,Object> videoCountMap = new HashMap<>();
        maps.forEach(map -> videoCountMap.put(map.get("address").toString(),map.get("count")));
        redisTemplate.opsForHash().putAll(RedisKey.VIDEO_COUNT_KEY,videoCountMap);
    }


}

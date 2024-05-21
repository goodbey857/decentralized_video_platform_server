package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.VideoLike;
import top.kingdon.service.VideoLikeService;
import top.kingdon.mapper.VideoLikeMapper;
import org.springframework.stereotype.Service;
import top.kingdon.service.VideosService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 古德白
* @description 针对表【video_like】的数据库操作Service实现
* @createDate 2024-02-15 13:23:36
*/
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike>
    implements VideoLikeService{
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    VideosService videosService;


    @Override
    public Long likeCount(Integer videoId) {
        return redisTemplate.opsForSet().size(RedisKey.getLikeKeyOfVideo(videoId));
    }

    @Override
    public Boolean isLiked(Integer videoId, String address) {
        return redisTemplate.opsForSet().isMember(RedisKey.getLikeKeyOfVideo(videoId), address);
    }

    @Override
    public void doLike(Integer videoId, String address){
        redisTemplate.opsForSet().add(RedisKey.getLikeKeyOfVideo(videoId), address);
        VideoLike videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserAddress(address);
        videoLike.setCreatedAt(new Date());
        this.getBaseMapper().insert(videoLike);
    }

    @Override
    public void undoLike(Integer videoId, String address){
        redisTemplate.opsForSet().remove(RedisKey.getLikeKeyOfVideo(videoId), address);
        this.baseMapper.updateCanceledAt(videoId,address);
    }

    @Override
    public Long totalLikeContByAddress(String address){
        List<Integer> videoIDListByAddress = videosService.getVideoIDListByAddress(address);
        if(CollectionUtils.isEmpty(videoIDListByAddress)) return  0L;

        long sum = videoIDListByAddress.stream()
                .mapToLong(videoId -> redisTemplate.opsForSet().size(RedisKey.getLikeKeyOfVideo(videoId))).sum();

        return sum;
    }
}





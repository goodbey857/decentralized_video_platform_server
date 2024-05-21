package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.VideoStar;
import top.kingdon.service.VideoStarService;
import top.kingdon.mapper.VideoStarMapper;
import org.springframework.stereotype.Service;
import top.kingdon.service.VideosService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author 古德白
* @description 针对表【video_star】的数据库操作Service实现
* @createDate 2024-02-15 13:23:45
*/
@Service
public class VideoStarServiceImpl extends ServiceImpl<VideoStarMapper, VideoStar>
    implements VideoStarService{
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    VideosService videosService;
    @Override
    public boolean star(int videoID, String userAddress) {
        redisTemplate.opsForSet().add(RedisKey.getStarKeyOfVideo(videoID),userAddress);
        VideoStar videoStar = new VideoStar();
        videoStar.setVideoId(videoID);
        videoStar.setUserAddress(userAddress);
        videoStar.setCreatedAt(new Date());
        this.baseMapper.insert(videoStar);
        return true;
    }

    @Override
    public boolean unstar(int videoID, String userAddress) {
        redisTemplate.opsForSet().remove(RedisKey.getStarKeyOfVideo(videoID),userAddress);
        this.baseMapper.updateCanceledAt(videoID,userAddress);
        return true;
    }
    @Override
    public boolean isStar(int videoID, String userAddress) {
        return redisTemplate.opsForSet().isMember(RedisKey.getStarKeyOfVideo(videoID),userAddress);
    }

    @Override
    public long starCount(int videoID) {
        return redisTemplate.opsForSet().size(RedisKey.getStarKeyOfVideo(videoID));
    }

    @Override
    public Long totalStarContByAddress(String address) {
        List<Integer> videoIDListByAddress = videosService.getVideoIDListByAddress(address);
        if(CollectionUtils.isEmpty(videoIDListByAddress)) return  0L;

        long sum = videoIDListByAddress.stream()
                .mapToLong(videoId -> redisTemplate.opsForSet().size(RedisKey.getStarKeyOfVideo(videoId))).sum();
        return sum;
    }

}





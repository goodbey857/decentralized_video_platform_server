package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.VideoComment;
import top.kingdon.service.VideoCommentService;
import top.kingdon.mapper.VideoCommentMapper;
import org.springframework.stereotype.Service;
import top.kingdon.service.VideosService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 古德白
* @description 针对表【video_comment】的数据库操作Service实现
* @createDate 2024-02-15 13:23:18
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    VideosService videosService;

    @Override
    public Long commentCount(Integer id) {
        Integer count = (Integer) redisTemplate.opsForHash().get(RedisKey.COMMENT_COUNT_KEY,id.toString());
        return count == null? 0L :count.longValue();
    }

    @Override
    public Long totalCommentContByAddress(String address) {
            List<String> ids = videosService.getVideoIDListByAddress(address).stream().map(a->a.toString()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(ids)) return  0L;

        long sum = redisTemplate.opsForHash().multiGet(RedisKey.COMMENT_COUNT_KEY, ids).stream().mapToLong(a -> a==null?0L:((Integer) a).longValue()).sum();
        return sum;
    }
}





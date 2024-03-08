package top.kingdon.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.kingdon.mapper.VideoLikeMapper;
import top.kingdon.mapper.VideoStarMapper;
import top.kingdon.mapper.VideosMapper;


@Component
public class RedisCommandRunner implements CommandLineRunner {

    RedisTemplate <String, Object> redisTemplate;
    VideosMapper  videosMapper;
    VideoLikeMapper videoLikeMapper;
    VideoStarMapper  videoStarMapper;

   @Override
    public void run(String... args) throws Exception {



    }
}

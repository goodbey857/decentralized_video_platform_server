package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.protobuf.Api;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.dataobject.vo.VideoVO;
import top.kingdon.service.CommentService;
import top.kingdon.service.VideosService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    VideosService  videosService;
    @Resource
    CommentService  commentService;
    @Resource
    RedisTemplate redisTemplate;
    @GetMapping("/hot/{start}/{size}")
    public ApiResponse getHotVideoList(@PathVariable int start, @PathVariable int size) {
        List<VideoVO> videoVOList = videosService.hotVideoList(start, size);
        return  ApiResponse.ok().put("data",videoVOList);
    }

    @GetMapping("/new/{start}/{size}")
    public ApiResponse getNewVideoList(@PathVariable int start, @PathVariable int size) {
        List<VideoVO> videoVOList = videosService.newVideoList(start, size);
        return  ApiResponse.ok().put("data",videoVOList);
    }

    public ApiResponse getFollowVideoList() {
        return null;
    }

    public ApiResponse getStartedVideoList() {
        return null;
    }

    public ApiResponse getHistoryVideoList() {
        return null;
    }

    public ApiResponse getSomebodyVideoList() {
        return null;
    }

    public ApiResponse getVideoDetail() {
        return null;
    }

    @GetMapping("/like/{videoId}")
    public ApiResponse like(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.like(videoId,userAddress);
        return ApiResponse.ok();
    }

    @GetMapping("/unlike/{videoId}")
    public  ApiResponse unlike(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.unlike(videoId,userAddress);
        return ApiResponse.ok();
    }

    @GetMapping("/star/{videoId}")
    public ApiResponse star(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.star(videoId,userAddress);
        return ApiResponse.ok();
    }


    @GetMapping("/unstar/{videoId}")
    public ApiResponse unstar(@PathVariable Integer  videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.unstar(videoId,userAddress);
        return ApiResponse.ok();
    }


    @GetMapping("/detail/{videoId}")
    public ApiResponse videoDetail(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.view(videoId,userAddress);
        VideoVO videoMetadata = videosService.getVideoVOByID(videoId);
        Boolean likeStatus = redisTemplate.opsForSet().isMember(RedisKey.getLikeKeyOfVideo(videoId), userAddress);
        Boolean starStatus = redisTemplate.opsForSet().isMember(RedisKey.getStarKeyOfVideo(videoId),userAddress);

        return ApiResponse.ok().put("videoMetadata",videoMetadata)
                .put("likeStatus",likeStatus)
                .put("starStatus",starStatus);

    }

    @GetMapping("/series/{seriesId}")
    public ApiResponse getSeriesVideoList(@PathVariable Integer seriesId){
        List<Videos> seriesVideoList = Collections.emptyList();
        if(seriesId != null){
            seriesVideoList = videosService.list(new LambdaQueryWrapper<Videos>().eq(Videos::getSeries,seriesId).orderBy(true,true, Videos::getCreatedAt));
        }
        return ApiResponse.ok().put("seriesVideoList",seriesVideoList);
    }


}

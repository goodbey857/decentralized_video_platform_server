package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.protobuf.Api;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.po.Follow;
import top.kingdon.dataobject.po.Series;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.dataobject.vo.VideoVO;
import top.kingdon.service.*;
import top.kingdon.service.impl.VideoStarServiceImpl;
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
    FollowService followService;
    @Resource
    SeriesService seriesService;
    @Resource
    VideoLikeService videoLikeService;
    @Resource
    VideoStarService videoStarService;


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

    @GetMapping("/follow/{start}/{size}")
    public ApiResponse getFollowVideoList(@PathVariable int start , @PathVariable int size) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<String> followerAddress = followService.getFollowerAddress(userAddress);
        List<VideoVO> videoVOList = Collections.emptyList();
        if(!followerAddress.isEmpty()){

            videoVOList = videosService.getVideoVOListByAddress(followerAddress, start, size);

        }

        return ApiResponse.ok().put("data",videoVOList);
    }

    @GetMapping("/starred")
    public ApiResponse getStartedVideoList() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<VideoVO> videoVOList = videosService.starVideoList(userAddress);
        return ApiResponse.ok().put("data",videoVOList);
    }

    @GetMapping("/liked")
    public ApiResponse getLikedVideoList() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<VideoVO> videoVOList = videosService.likedVideoList(userAddress);
        return ApiResponse.ok().put("data",videoVOList);
    }



    @GetMapping("/somebody/{userAddress}/{page}/{size}")
    public ApiResponse getSomebodyVideoList(@PathVariable String userAddress,@PathVariable int page, @PathVariable int size) {
        if(userAddress == null){
            return  ApiResponse.error("用户地址不能为空");
        }
        if("@me".equals(userAddress)){
            userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        List<VideoVO> videoVOList = videosService.somebodyVideoList(userAddress, page, size);
        return ApiResponse.ok().put("data",videoVOList);
    }


    @GetMapping("/like/{videoId}")
    public ApiResponse like(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videoLikeService.doLike(videoId,userAddress);
        return ApiResponse.ok();
    }

    @GetMapping("/unlike/{videoId}")
    public  ApiResponse unlike(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videoLikeService.undoLike(videoId,userAddress);
        return ApiResponse.ok();
    }

    @GetMapping("/star/{videoId}")
    public ApiResponse star(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videoStarService.star(videoId,userAddress);
        return ApiResponse.ok();
    }


    @GetMapping("/unstar/{videoId}")
    public ApiResponse unstar(@PathVariable Integer  videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videoStarService.unstar(videoId,userAddress);
        return ApiResponse.ok();
    }


    @GetMapping("/detail/{videoId}")
    public ApiResponse videoDetail(@PathVariable Integer videoId){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videosService.view(videoId,userAddress);
        VideoVO videoMetadata = videosService.getVideoVOByID(videoId);
        if(videoMetadata==null){
            return ApiResponse.error("视频不存在");
        }

        Boolean likeStatus = videoLikeService.isLiked(videoId,userAddress);
        Boolean starStatus = videoStarService.isStar(videoId,userAddress);

        Follow one = followService.getOne(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowingAddress, videoMetadata.getAuthorAddress())
                .eq(Follow::getFollowerAddress, userAddress).isNull(Follow::getCanceledAt));
        boolean followStatus = one != null;
        return ApiResponse.ok().put("videoMetadata",videoMetadata)
                .put("likeStatus",likeStatus)
                .put("starStatus",starStatus)
                .put("followStatus",followStatus);

    }

    @GetMapping("/series/{seriesId}")
    public ApiResponse getSeriesVideoList(@PathVariable Integer seriesId){
        List<VideoVO> seriesVideoList = videosService.getSeriesVideoList(seriesId);
        return ApiResponse.ok().put("seriesVideoList",seriesVideoList);
    }

    @GetMapping("/series/list/{address}")
    public ApiResponse getSeriesList(@PathVariable String address){
        if(StringUtils.isEmpty(address) || "@me".equals(address)){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        List<Series> seriesList = seriesService.list(new LambdaQueryWrapper<Series>().eq(Series::getUserAddress,address));
        return ApiResponse.ok().put("seriesList",seriesList);
    }

    @GetMapping("/search")
    public ApiResponse search(@RequestParam String word, @RequestParam Integer page, @RequestParam Integer size){
        List<VideoVO> data = videosService.search(word, page, size);
        return ApiResponse.ok().put("data",data);
    }



}

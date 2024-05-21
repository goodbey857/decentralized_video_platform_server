package top.kingdon.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.bo.AnalyzeCount;
import top.kingdon.service.*;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/analyze")
public class PersonAnalytics {
    @Resource
    VideoHistoryService videoHistoryService;
    @Resource
    VideosService videosService;
    @Resource
    VideoLikeService videoLikeService;
    @Resource
    VideoStarService videoStarService;
    @Resource
    VideoCommentService videoCommentService;
    @Resource
    FollowService followService;

    @GetMapping("/view/{videoId}")
    public ApiResponse analyzeView(@PathVariable Integer videoId){
        Long count = videoHistoryService.viewCount(videoId);
        return  ApiResponse.ok().put("count",count);
    }

    @GetMapping("/analyzeView/{duration}")
    public ApiResponse analyzeTotalView(@PathVariable String duration){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<AnalyzeCount> analyzeView = videoHistoryService.analyzeView(userAddress, duration);
        return ApiResponse.ok().put("data",analyzeView).put("label",duration);
    }

    @GetMapping("/totalView/{address}")
    public ApiResponse totalView(@PathVariable String address){
        if(address == null || address.equals("@me")){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }

        Long viewCount = videosService.getUserViewCount(address);
        return ApiResponse.ok().put("count",viewCount);
    }

    @GetMapping("/like/{videoId}")
    public ApiResponse analyzeLike(@PathVariable Integer videoId){
        Long count = videoLikeService.likeCount(videoId);
        return  ApiResponse.ok().put("count",count);
    }

    @GetMapping("/totalLike/{address}")
    public ApiResponse totalLike(@PathVariable String address){
        if(address == null || address.equals("@me")){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long count = videoLikeService.totalLikeContByAddress(address);
        return ApiResponse.ok().put("count",count);
    }

    @GetMapping("/star/{videoId}")
    public ApiResponse  analyzeStar(@PathVariable Integer videoId){
        Long count = videoStarService.starCount(videoId);
        return ApiResponse.ok().put("count",count);
    }

    @GetMapping("/totalStar/{address}")
    public ApiResponse totalStar(@PathVariable String address){
        if(address == null || address.equals("@me")){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long count = videoStarService.totalStarContByAddress(address);
        return ApiResponse.ok().put("count",count);
    }

    @GetMapping("/comment/{videoId}")
    public ApiResponse analyzeComment(@PathVariable Integer videoId){
        Long count = videoCommentService.commentCount(videoId);
        return ApiResponse.ok().put("count",count);
    }

    @GetMapping("/totalComment/{address}")
    public ApiResponse totalComment(@PathVariable String address) {
        if(address == null || address.equals("@me")){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long count = videoCommentService.totalCommentContByAddress(address);
        return ApiResponse.ok().put("count", count);
    }


    @GetMapping("/fans/{address}")
    public ApiResponse analyzeFansCount(@PathVariable String address) {
        if(address == null || address.equals("@me")){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long fansCount = followService.getFansCount(address);
        return ApiResponse.ok().put("count", fansCount);
    }

    @GetMapping("/follower/{address}")
    public ApiResponse analyzeFollowerCount(@PathVariable String address) {
        if (address == null || address.equals("@me")) {
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long followCount = followService.getFollowerCount(address);
        return ApiResponse.ok().put("count", followCount);
    }

    @GetMapping("/videoCount/{address}")
    public ApiResponse videoCountByAddress(@PathVariable String address){
        if(address == null || "@me".equals(address)){
            address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        Long videoCount = videosService.getVideoCount(address);
        return ApiResponse.ok().put("count",videoCount);
    }









}

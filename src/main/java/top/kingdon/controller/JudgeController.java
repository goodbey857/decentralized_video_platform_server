package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.po.Review;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.dataobject.vo.ReviewVO;
import top.kingdon.service.ReviewService;
import top.kingdon.service.VideosService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/judge")
public class JudgeController {
    @Resource
    private ReviewService reviewService;
    @Resource
    private VideosService videosService;

    // 举报视频
    @GetMapping("/report")
    public ApiResponse report(@RequestParam("videoId") Integer videoId, @RequestParam("reason") String[] reason){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        Review review = new Review();
        review.setUserAddress(userAddress);
        review.setVideoId(videoId);
        review.setReason(String.join(",", reason));
        review.setStatus("待审批");
        review.setCreatedAt(new Date());
        reviewService.save(review);
        return ApiResponse.ok();
    }

    @GetMapping("/all")
    public ApiResponse all(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size){
        IPage<ReviewVO> reviewPage = reviewService.all(new Page<ReviewVO>(page, size));
        List<ReviewVO> records = reviewPage.getRecords();
        long total = reviewPage.getTotal();
        return ApiResponse.ok().put("data", records).put("total", total);
    }

    @GetMapping("/judge")
    public ApiResponse judge(@RequestParam("reviewId") Integer reviewId, @RequestParam("status") String status){
        Review review = reviewService.getById(reviewId);
        switch(status){
            case "待审批": break;
            case "已处理": videosService.update(new LambdaUpdateWrapper<Videos>().set(Videos::getCanceledAt,new Date()).eq(Videos::getId,review.getVideoId()));break;
            case "已退回": break;

            default: return ApiResponse.error("状态错误");
        }
        review.setStatus(status);
        review.setUpdatedAt(new Date());
        reviewService.updateById(review);
        return ApiResponse.ok();

    }

}

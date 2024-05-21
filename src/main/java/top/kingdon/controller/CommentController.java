package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.assertj.core.util.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.dto.CommentDTO;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.vo.CommentVO;
import top.kingdon.service.CommentService;
import top.kingdon.service.VideosService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private VideosService videosService;
    @Resource
    private RedisTemplate redisTemplate;


    @PostMapping
    public ApiResponse comment(@RequestBody CommentDTO commentDTO){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        Comment comment = new Comment();
        comment.setVideoId(commentDTO.getVideoId());
        comment.setContent(commentDTO.getContent());
        comment.setReplayTo(commentDTO.getReplayTo());
        comment.setUserAddress(userAddress);
        comment.setCreatedAt(new Date());
        commentService.save(comment);
        redisTemplate.opsForHash().increment(RedisKey.COMMENT_COUNT_KEY,commentDTO.getVideoId().toString(),1);
        return  ApiResponse.ok().put("comment",comment);

    }
    @GetMapping("/{videoId}/{page}/{size}")
    public ApiResponse getComment(@PathVariable("videoId") Integer videoId,
                                  @PathVariable("page") Integer page,
                                  @PathVariable("size") Integer size){
        List<CommentVO> commentList = commentService.getComment(page,size,videoId);
        return   ApiResponse.ok().put("commentList",commentList);
    }
    @GetMapping("/videoOwn/{userAddress}/{page}/{size}")
    public ApiResponse getCommentByUser(@PathVariable("userAddress") String userAddress,
                                       @PathVariable("page") Integer page,
                                       @PathVariable("size") Integer size){
        if(userAddress== null || "@me".equals(userAddress)){
            userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        }
        List<Integer> videoIDListByAddress = videosService.getVideoIDListByAddress(userAddress);
        if(CollectionUtils.isEmpty(videoIDListByAddress)){
            return  ApiResponse.ok().put("commentList", Lists.emptyList());
        }
        List<CommentVO> commentList = commentService.getComment(page,size,videoIDListByAddress.toArray(new Integer[videoIDListByAddress.size()]));

        return   ApiResponse.ok().put("commentList",commentList);
    }
}


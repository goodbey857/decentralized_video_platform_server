package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.dto.CommentDTO;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.vo.CommentVO;
import top.kingdon.service.CommentService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
@Resource
    private CommentService commentService;

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
        return  ApiResponse.ok().put("comment",comment);

    }
    @GetMapping("/{videoId}")
    public ApiResponse getComment(@PathVariable("videoId") Integer videoId){
        List<CommentVO> commentList = commentService.getComment(videoId);
        return   ApiResponse.ok().put("commentList",commentList);
    }
}

package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.VideoComment;
import top.kingdon.service.VideoCommentService;
import top.kingdon.mapper.VideoCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【video_comment】的数据库操作Service实现
* @createDate 2024-02-15 13:23:18
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{

}





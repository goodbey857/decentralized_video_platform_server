package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【video_comment】的数据库操作Mapper
* @createDate 2024-02-15 13:23:18
* @Entity top.kingdon.dataobject.po.VideoComment
*/
@Mapper
public interface VideoCommentMapper extends BaseMapper<VideoComment> {

}





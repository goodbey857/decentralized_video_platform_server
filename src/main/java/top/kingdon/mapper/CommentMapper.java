package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2024-02-19 18:59:53
* @Entity top.kingdon.dataobject.po.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}





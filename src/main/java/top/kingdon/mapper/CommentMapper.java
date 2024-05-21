package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kingdon.dataobject.vo.CommentVO;

import java.util.Map;

/**
* @author 古德白
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2024-02-19 18:59:53
* @Entity top.kingdon.dataobject.po.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    public IPage<CommentVO> getAllCommentData(IPage<CommentVO> page, @Param("address") String address, @Param("search") String search, @Param("orderMap") Map<String,Boolean> orderMap);

}





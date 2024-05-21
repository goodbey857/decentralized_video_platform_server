package top.kingdon.service;

import org.springframework.stereotype.Service;
import top.kingdon.dataobject.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kingdon.dataobject.vo.CommentVO;

import java.util.List;
import java.util.Map;

/**
* @author 古德白
* @description 针对表【comment】的数据库操作Service
* @createDate 2024-02-19 18:59:53
*/
@Service
public interface CommentService extends IService<Comment> {
    public List<CommentVO> getComment(Integer page, Integer size, Integer... videoId);
    public Map<String,Object> getAllComment(Integer page, Integer size,String search, Map<String,Boolean> orderMap);
}

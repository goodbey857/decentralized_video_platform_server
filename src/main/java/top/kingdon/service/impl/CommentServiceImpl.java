package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.po.Users;
import top.kingdon.dataobject.vo.CommentVO;
import top.kingdon.mapper.UsersMapper;
import top.kingdon.service.CommentService;
import top.kingdon.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 古德白
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2024-02-19 18:59:53
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private UsersMapper usersMapper;

    @Override
    public List<CommentVO> getComment(Integer videoId) {
        List<Comment> commentList = this.list(new LambdaQueryWrapper<Comment>().eq(Comment::getVideoId, videoId));
        Set<String> userAddressSet = commentList.stream().map(comment -> comment.getUserAddress()).collect(Collectors.toSet());
        if(userAddressSet.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        Map<String, Users> collect = usersMapper.selectBatchIds(userAddressSet).stream().collect(Collectors.toMap(Users::getAddress, users -> users));
        List<CommentVO> commentVOList = commentList.stream().map(comment -> new CommentVO(comment, collect.get(comment.getUserAddress()))).collect(Collectors.toList());

        return commentVOList;
    }
}





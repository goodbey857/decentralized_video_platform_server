package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Follow;
import top.kingdon.service.FollowService;
import top.kingdon.mapper.FollowMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【follow】的数据库操作Service实现
* @createDate 2024-02-15 14:11:03
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

}





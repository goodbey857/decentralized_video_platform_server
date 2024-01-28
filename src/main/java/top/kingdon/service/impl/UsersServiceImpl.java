package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dao.UsersMapper;
import top.kingdon.dataobject.po.Users;
import org.springframework.stereotype.Service;
import top.kingdon.service.UsersService;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-01-27 21:00:53
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

}





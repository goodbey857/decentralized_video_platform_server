package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import top.kingdon.mapper.UsersMapper;
import top.kingdon.dataobject.bo.AuthUserBO;
import top.kingdon.dataobject.po.Users;
import org.springframework.stereotype.Service;
import top.kingdon.service.UsersService;

import javax.annotation.Resource;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-01-27 21:00:53
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    @Resource
    UsersMapper usersMapper;

    @Override
    public AuthUserBO signIn(String address) {
        AuthUserBO authUserBO = new AuthUserBO();

        Users user = usersMapper.selectOne(new QueryWrapper<Users>().
                eq("address", address));

        if (user == null) {
            authUserBO.setAddress(address);
            authUserBO.setNewUser(true);
            usersMapper.insert(authUserBO);
        }else{
            BeanUtils.copyProperties(user, authUserBO);
            authUserBO.setNewUser(false);
        }

        return authUserBO;

    }
}





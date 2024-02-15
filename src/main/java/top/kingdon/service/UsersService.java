package top.kingdon.service;

import top.kingdon.dataobject.bo.AuthUserBO;
import top.kingdon.dataobject.dto.AuthUserDTO;
import top.kingdon.dataobject.po.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Service
* @createDate 2024-01-27 21:00:53
*/
public interface UsersService extends IService<Users> {
    public AuthUserBO signIn(String address);

}

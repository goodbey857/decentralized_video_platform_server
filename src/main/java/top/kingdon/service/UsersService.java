package top.kingdon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.kingdon.dataobject.bo.AuthUserBO;
import top.kingdon.dataobject.bo.UserData;
import top.kingdon.dataobject.dto.AuthUserDTO;
import top.kingdon.dataobject.po.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Service
* @createDate 2024-01-27 21:00:53
*/
public interface UsersService extends IService<Users> {
    public AuthUserBO signIn(String address);

    Map<String, Object> getUserData(Integer page, Integer size, String address, LinkedHashMap<String, Boolean> orderMap);
}

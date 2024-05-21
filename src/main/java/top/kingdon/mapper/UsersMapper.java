package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.bo.UserData;
import top.kingdon.dataobject.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Mapper
* @createDate 2024-01-27 21:00:53
* @Entity top.kingdon.dataobject.po.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    IPage<UserData> selectUserData(IPage<Users> page, @Param("userAddress")String userAddress, @Param("orderMap") LinkedHashMap<String, Boolean> orderMap);

}





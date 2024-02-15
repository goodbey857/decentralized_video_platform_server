package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Mapper
* @createDate 2024-01-27 21:00:53
* @Entity top.kingdon.dataobject.po.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}





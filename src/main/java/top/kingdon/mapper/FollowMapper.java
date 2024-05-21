package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 古德白
* @description 针对表【follow】的数据库操作Mapper
* @createDate 2024-02-15 14:11:03
* @Entity top.kingdon.dataobject.po.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
    List<String> selectFollowList(@Param("userId") String userId);
    List<String> selectFanList(@Param("userId") String userId);

}





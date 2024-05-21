package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.Videos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Mapper
* @createDate 2024-02-05 21:05:12
* @Entity top.kingdon.dataobject.po.Videos
*/
@Mapper
public interface VideosMapper extends BaseMapper<Videos> {
    List<Integer> getIdsByAddress(@Param("address")String address);


}





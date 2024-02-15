package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.Videos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Mapper
* @createDate 2024-02-05 21:05:12
* @Entity top.kingdon.dataobject.po.Videos
*/
@Mapper
public interface VideosMapper extends BaseMapper<Videos> {

}





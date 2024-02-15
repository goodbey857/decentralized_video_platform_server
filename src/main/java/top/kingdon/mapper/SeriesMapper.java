package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.Series;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【series】的数据库操作Mapper
* @createDate 2024-02-05 21:07:16
* @Entity top.kingdon.dataobject.po.Series
*/
@Mapper
public interface SeriesMapper extends BaseMapper<Series> {

}





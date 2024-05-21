package top.kingdon.mapper;

import jnr.ffi.annotations.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.SearchHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【search_history】的数据库操作Mapper
* @createDate 2024-03-17 17:09:47
* @Entity top.kingdon.dataobject.po.SearchHistory
*/
@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

}





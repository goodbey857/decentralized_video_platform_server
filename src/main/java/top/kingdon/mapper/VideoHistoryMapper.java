package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.bo.AnalyzeCount;
import top.kingdon.dataobject.po.VideoHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 古德白
* @description 针对表【video_history】的数据库操作Mapper
* @createDate 2024-02-15 13:23:31
* @Entity top.kingdon.dataobject.po.VideoHistory
*/
@Mapper
public interface VideoHistoryMapper extends BaseMapper<VideoHistory> {
    List<AnalyzeCount> analyzeViewDay(@Param("address") String address);
    List<AnalyzeCount> analyzeViewWeek(@Param("address") String address);
    List<AnalyzeCount> analyzeViewMonth(@Param("address") String address);

}





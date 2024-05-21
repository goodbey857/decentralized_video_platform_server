package top.kingdon.service;

import top.kingdon.dataobject.bo.AnalyzeCount;
import top.kingdon.dataobject.po.VideoHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 古德白
* @description 针对表【video_history】的数据库操作Service
* @createDate 2024-02-15 13:23:31
*/
public interface VideoHistoryService extends IService<VideoHistory> {

    Long viewCount(Integer videoId);

    List<AnalyzeCount> analyzeView(String address, String duration);
}

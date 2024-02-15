package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.VideoHistory;
import top.kingdon.service.VideoHistoryService;
import top.kingdon.mapper.VideoHistoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【video_history】的数据库操作Service实现
* @createDate 2024-02-15 13:23:31
*/
@Service
public class VideoHistoryServiceImpl extends ServiceImpl<VideoHistoryMapper, VideoHistory>
    implements VideoHistoryService{

}





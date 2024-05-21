package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.bo.AnalyzeCount;
import top.kingdon.dataobject.po.SearchHistory;
import top.kingdon.service.SearchHistoryService;
import top.kingdon.mapper.SearchHistoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 古德白
* @description 针对表【search_history】的数据库操作Service实现
* @createDate 2024-03-17 17:09:47
*/
@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory>
    implements SearchHistoryService{



}





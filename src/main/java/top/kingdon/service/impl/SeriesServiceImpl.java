package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Series;
import top.kingdon.service.SeriesService;
import top.kingdon.mapper.SeriesMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【series】的数据库操作Service实现
* @createDate 2024-02-05 21:07:16
*/
@Service
public class SeriesServiceImpl extends ServiceImpl<SeriesMapper, Series>
    implements SeriesService{

}





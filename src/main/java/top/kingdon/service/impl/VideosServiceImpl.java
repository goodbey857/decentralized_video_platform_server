package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.service.VideosService;
import top.kingdon.mapper.VideosMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Service实现
* @createDate 2024-02-05 21:05:12
*/
@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos>
    implements VideosService{

}





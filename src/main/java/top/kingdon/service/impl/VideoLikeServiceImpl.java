package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.VideoLike;
import top.kingdon.service.VideoLikeService;
import top.kingdon.mapper.VideoLikeMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【video_like】的数据库操作Service实现
* @createDate 2024-02-15 13:23:36
*/
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike>
    implements VideoLikeService{

}





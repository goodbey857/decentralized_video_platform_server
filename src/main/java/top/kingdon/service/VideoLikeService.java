package top.kingdon.service;

import top.kingdon.dataobject.po.VideoLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 古德白
* @description 针对表【video_like】的数据库操作Service
* @createDate 2024-02-15 13:23:36
*/
public interface VideoLikeService extends IService<VideoLike> {

    Long likeCount(Integer videoId);

    Boolean isLiked(Integer videoId, String address);

    void doLike(Integer videoId, String address);

    void undoLike(Integer videoId, String address);

    Long totalLikeContByAddress(String address);
}

package top.kingdon.service;

import top.kingdon.dataobject.po.VideoStar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 古德白
* @description 针对表【video_star】的数据库操作Service
* @createDate 2024-02-15 13:23:45
*/
public interface VideoStarService extends IService<VideoStar> {

    boolean star(int videoID, String userAddress);

    boolean unstar(int videoID, String userAddress);

    boolean isStar(int videoID, String userAddress);

    long starCount(int videoID);

    Long totalStarContByAddress(String address);
}

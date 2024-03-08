package top.kingdon.service;

import top.kingdon.dataobject.po.Videos;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kingdon.dataobject.vo.VideoVO;

import java.util.List;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Service
* @createDate 2024-02-05 21:05:12
*/
public interface VideosService extends IService<Videos> {
    public List<VideoVO> hotVideoList(int start, int size);
    public List<VideoVO> newVideoList(int start, int size);
    public List<VideoVO> followVideoList();
    public List<VideoVO> starVideoList();
    public List<VideoVO> historyVideoList();
    public List<VideoVO> somebodyVideoList(String userAddress);
    public List<Videos> getVideoListByID(List<Integer> ids);
    public boolean like(int videoID, String userAddress);
    public boolean unlike(int videoID, String userAddress);
    public boolean star(int videoID, String userAddress);
    public boolean unstar(int videoID, String userAddress);
    public boolean view(int videoID, String userAddress);
    public VideoVO getVideoVOByID(int videoID);
}

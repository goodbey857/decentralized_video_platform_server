package top.kingdon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jnr.ffi.annotations.In;
import top.kingdon.dataobject.po.Videos;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kingdon.dataobject.vo.VideoVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Service
* @createDate 2024-02-05 21:05:12
*/
public interface VideosService extends IService<Videos> {
    public List<VideoVO> hotVideoList(int start, int size);
    public List<VideoVO> newVideoList(int start, int size);
    public List<VideoVO> likedVideoList(String userAddress);
    public List<VideoVO> starVideoList(String userAddress);
    public List<VideoVO> somebodyVideoList(String userAddress,Integer page, Integer size);
    public List<Videos> getVideoListByID(Collection<Integer> ids);
    public List<Integer> getVideoIDListByAddress(String userAddress);

    Map<String,Object> getAllVideo(int page, int size, String search, Map<String, Boolean> orderMap);

    Long getUserViewCount(String userAddress);

    public boolean view(int videoID, String userAddress);
    public VideoVO getVideoVOByID(int videoID);
    public List<VideoVO> getVideoVOListByAddress(List<String> address,int start, int size);
    public List<VideoVO> getSeriesVideoList(Integer id);
    public List<VideoVO> search(String keyword,int start, int size);

    Long getVideoCount(String address);
}

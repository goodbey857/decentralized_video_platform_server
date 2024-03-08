package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.VideoLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【video_like】的数据库操作Mapper
* @createDate 2024-02-15 13:23:36
* @Entity top.kingdon.dataobject.po.VideoLike
*/
@Mapper
public interface VideoLikeMapper extends BaseMapper<VideoLike> {
    public void updateCanceledAt(@Param("videoId") Integer videoId, @Param("userAddress")String userAddress);

}





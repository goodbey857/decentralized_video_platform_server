package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.VideoStar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【video_star】的数据库操作Mapper
* @createDate 2024-02-15 13:23:45
* @Entity top.kingdon.dataobject.po.VideoStar
*/
@Mapper
public interface VideoStarMapper extends BaseMapper<VideoStar> {

    void updateCanceledAt(@Param("videoId") int videoID,@Param("userAddress") String userAddress);
}





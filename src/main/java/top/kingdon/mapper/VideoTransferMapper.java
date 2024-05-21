package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.VideoTransfer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【video_transfer】的数据库操作Mapper
* @createDate 2024-04-18 15:40:57
* @Entity top.kingdon.dataobject.po.VideoTransfer
*/
@Mapper
public interface VideoTransferMapper extends BaseMapper<VideoTransfer> {

}





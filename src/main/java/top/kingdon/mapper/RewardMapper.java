package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kingdon.dataobject.po.Reward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kingdon.dataobject.vo.RewardVO;

/**
* @author 古德白
* @description 针对表【reward】的数据库操作Mapper
* @createDate 2024-04-30 21:40:42
* @Entity top.kingdon.dataobject.po.Reward
*/
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {
    IPage<RewardVO> getAllReward(IPage<RewardVO> page,@Param("address") String address);
}





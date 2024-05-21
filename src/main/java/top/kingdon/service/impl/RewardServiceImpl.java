package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Reward;
import top.kingdon.dataobject.vo.RewardVO;
import top.kingdon.service.RewardService;
import top.kingdon.mapper.RewardMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author 古德白
* @description 针对表【reward】的数据库操作Service实现
* @createDate 2024-04-30 21:40:42
*/
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward>
    implements RewardService{

    @Override
    public Map<String, Object> getRewardList(Integer page, Integer size, String address) {

        IPage<RewardVO> allReward = this.baseMapper.getAllReward(new Page<>(page, size), address);
        List<RewardVO> records = allReward.getRecords();
        long total = allReward.getTotal();

        return Map.of("data", records, "total", total);
    }
}





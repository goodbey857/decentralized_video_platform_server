package top.kingdon.service;

import top.kingdon.dataobject.po.Reward;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 古德白
* @description 针对表【reward】的数据库操作Service
* @createDate 2024-04-30 21:40:42
*/
public interface RewardService extends IService<Reward> {
    Map<String, Object> getRewardList(Integer page, Integer size,String address);

}

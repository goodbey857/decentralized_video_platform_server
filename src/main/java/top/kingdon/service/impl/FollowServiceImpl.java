package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.assertj.core.util.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.Follow;
import top.kingdon.dataobject.po.Users;
import top.kingdon.mapper.UsersMapper;
import top.kingdon.service.FollowService;
import top.kingdon.mapper.FollowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 古德白
* @description 针对表【follow】的数据库操作Service实现
* @createDate 2024-02-15 14:11:03
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{
    @Resource
    UsersMapper usersMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public List<String> getFollowerAddress(String userId) {
        return this.baseMapper.selectFollowList(userId);
    }

    @Override
    public List<String> getFansAddress(String userId) {
        return this.baseMapper.selectFanList(userId);
    }

    @Override
    public List<Users> getFollowers(String userId) {
        List<String> followerAddress = this.getFollowerAddress(userId);
        if(CollectionUtils.isEmpty(followerAddress))
            return Lists.emptyList();
        List<Users> users = usersMapper.selectBatchIds(followerAddress);
        return users;
    }


    @Override
    public List<Users> getFans(String userId) {
        List<String> fansAddress = this.getFansAddress(userId);
        if(CollectionUtils.isEmpty(fansAddress))
            return Lists.emptyList();
        List<Users> users = usersMapper.selectBatchIds(fansAddress);
        return users;
    }

    @Override
    public Long getFollowerCount(String userId) {
        Integer count = (Integer) redisTemplate.opsForHash().get(RedisKey.FOLLOW_COUNT_KEY,userId);
        if(count ==null){
            return 0L;
        }
        return count.longValue();
    }

    @Override
    public Long getFansCount(String userId) {
        Integer count = (Integer) redisTemplate.opsForHash().get(RedisKey.FAN_COUNT_KEY,userId);
        if(count ==null){
            return 0L;
        }
        return count.longValue();
    }
}






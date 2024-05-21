package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.bo.UserData;
import top.kingdon.mapper.UsersMapper;
import top.kingdon.dataobject.bo.AuthUserBO;
import top.kingdon.dataobject.po.Users;
import org.springframework.stereotype.Service;
import top.kingdon.service.UsersService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 古德白
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-01-27 21:00:53
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    @Resource
    UsersMapper usersMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public AuthUserBO signIn(String address) {
        AuthUserBO authUserBO = new AuthUserBO();

        Users user = usersMapper.selectOne(new QueryWrapper<Users>().
                eq("address", address));

        if (user == null) {
            authUserBO.setAddress(address);
            authUserBO.setNewUser(true);
            authUserBO.setUsername("用户" + address.substring(2, 8));
            authUserBO.setProfileImageCid("QmbV61teX6ThxPUkogt4AVPnkp7spgerXuySGGMQw2rmNU");
            usersMapper.insert(authUserBO);
        }else{
            BeanUtils.copyProperties(user, authUserBO);
            authUserBO.setNewUser(false);
        }

        return authUserBO;

    }

    @Override
    public Map<String, Object> getUserData(Integer page, Integer size, String address, LinkedHashMap<String, Boolean> orderMap) {
        HashMap<String, Object> map = new HashMap<>();
        IPage<UserData> userDataPage = usersMapper.selectUserData(new Page<>(page,size), address,orderMap);
        List<UserData> userDatas= userDataPage.getRecords();
        if(CollectionUtils.isEmpty(userDatas)){
            map.put("total",0);
            return map;
        }
        Map<String, UserData> userDataMap = userDatas.stream().collect(Collectors.toMap(UserData::getUserAddress, (user) -> user, (k1, k2) -> k1));
        List<String> addresses = List.of(userDataMap.keySet().toArray(new String[userDataMap.size()]));
        List videoCountList = redisTemplate.opsForHash().multiGet(RedisKey.VIDEO_COUNT_KEY, addresses);
        List fanCountList = redisTemplate.opsForHash().multiGet(RedisKey.FAN_COUNT_KEY, addresses);
        Map<Object,Boolean> member = redisTemplate.opsForSet().isMember(RedisKey.BLOCK_LIST_KEY, addresses.toArray());
        long total = userDataPage.getTotal();
        for (int i = 0; i < addresses.size(); i++) {
            UserData userData = userDataMap.get(addresses.get(i));
            userData.setVideoCount(videoCountList.get(i) == null ? 0 : Integer.parseInt(videoCountList.get(i).toString()));
            userData.setFansCount(fanCountList.get(i) == null ? 0 : Integer.parseInt(fanCountList.get(i).toString()));
            userData.setInBlacklist(member.get(addresses.get(i)));
        }

        map.put("userData",userDatas);
        map.put("total",total);
        return map;

    }
}





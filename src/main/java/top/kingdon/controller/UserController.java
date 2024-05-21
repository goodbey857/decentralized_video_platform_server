package top.kingdon.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.po.Follow;
import top.kingdon.dataobject.po.Users;
import top.kingdon.dataobject.vo.UserVO;
import top.kingdon.service.FollowService;
import top.kingdon.service.UsersService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    FollowService followService;
    @Resource
    UsersService usersService;
    @Resource
    RedisTemplate redisTemplate;

    @PostMapping("/follow")
    public ApiResponse follow(@RequestBody String followingAddress) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        if(StringUtils.isEmpty(followingAddress) || "@me".equals(followingAddress)){
            followingAddress = userAddress;
        }
        redisTemplate.opsForHash().increment(RedisKey.FAN_COUNT_KEY,followingAddress, 1);
        redisTemplate.opsForHash().increment(RedisKey.FOLLOW_COUNT_KEY,userAddress, 1);
        Follow follow = new Follow();
        follow.setFollowingAddress(followingAddress);
        follow.setFollowerAddress(userAddress);
        follow.setCreatedAt(new Date());
        followService.save(follow);
        return ApiResponse.ok().put("follow", follow);
    }

    @PostMapping("/unfollow")
    public ApiResponse unfollow(@RequestBody String followingAddress) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        if(StringUtils.isEmpty(followingAddress) || "@me".equals(followingAddress)){
            followingAddress = userAddress;
        }
        redisTemplate.opsForHash().increment(RedisKey.FAN_COUNT_KEY,followingAddress, -1);
        redisTemplate.opsForHash().increment(RedisKey.FOLLOW_COUNT_KEY,userAddress, -1);
        Follow follow = new Follow();
        follow.setFollowingAddress(followingAddress);
        follow.setFollowerAddress(userAddress);
        follow.setCanceledAt(new Date());
        followService.update(new LambdaUpdateWrapper<Follow>()
                .set(Follow::getCanceledAt, follow.getCanceledAt())
                .eq(Follow::getFollowingAddress, follow.getFollowingAddress())
                .eq(Follow::getFollowerAddress, follow.getFollowerAddress()).isNull(Follow::getCanceledAt)
        );
        return ApiResponse.ok();
    }

    @GetMapping("/followers")
    public ApiResponse followers() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<Users> followers = followService.getFollowers(userAddress);
        return ApiResponse.ok().put("followers", followers);
    }

    @GetMapping("/fans")
    public ApiResponse fans() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<Users> fans = followService.getFans(userAddress);
        return ApiResponse.ok().put("fans", fans);
    }

    @GetMapping("/info/{address}")
    public ApiResponse info(@PathVariable String address) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        if("@me".equals(address)){
            address = userAddress;
        }
        Users user = usersService.getById(address);
        if(user == null){
            return ApiResponse.error("用户不存在");
        }
        UserVO userVO = new UserVO(user);
        Long followerCount = followService.getFollowerCount(address);
        Long fansCount = followService.getFansCount(address);
        Follow one = followService.getOne(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowingAddress, address)
                .eq(Follow::getFollowerAddress, userAddress).isNull(Follow::getCanceledAt));
        boolean followStatus = one != null;

        userVO.setFollowerNum(followerCount);
        userVO.setFanNum(fansCount);
        return ApiResponse.ok().put("user", userVO).put("followStatus", followStatus);
    }

    @GetMapping ("/search")
    public ApiResponse search(@RequestParam String keyword){
        LambdaQueryWrapper<Users> users = new LambdaQueryWrapper<Users>().eq(Users::getUsername, keyword);
        List<Users> usersList = usersService.list(users);
        return ApiResponse.ok().put("users", usersList);
    }

    @PostMapping("/changeUsername/{username}")
    public ApiResponse changeUsername(@PathVariable String username){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<Users>().set(Users::getUsername, username).eq(Users::getAddress, userAddress);
        boolean update = usersService.update(null, updateWrapper);
        return ApiResponse.ok().put("update", update);

    }

    @PostMapping("/changeAvatar/{cid}")
    public ApiResponse changeAvatar(@PathVariable String cid){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<Users>().set(Users::getProfileImageCid, cid).eq(Users::getAddress, userAddress);
        boolean update = usersService.update(null, updateWrapper);
        return ApiResponse.ok().put("update", update);
    }



}



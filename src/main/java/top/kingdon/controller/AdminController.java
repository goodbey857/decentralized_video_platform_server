package top.kingdon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bouncycastle.util.Arrays;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.bo.UserData;
import top.kingdon.dataobject.po.*;
import top.kingdon.dataobject.vo.VideoTransferVO;
import top.kingdon.dataobject.vo.VideoVO;
import top.kingdon.service.*;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;
import top.kingdon.utils.MetamaskUtil;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    UsersService usersService;
    @Resource
    VideosService videosService;
    @Resource
    CommentService commentService;
    @Resource
    VideoTransferService videoTransferService;
    @Resource
    RewardService rewardService;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    VisitRecordService visitRecordService;


    //获取用户数据，用户地址，首次登录时间，24小时访问次数，视频总数，粉丝数量，最后一次登录时间，加入黑名单，移除黑名单，
    @GetMapping("/userData")
    public ApiResponse userData(@RequestParam(value = "address",required = false) String address,
                               @RequestParam(value = "page") Integer page,
                               @RequestParam(value = "size") Integer size,
                                @RequestParam(value = "orderBy",required = false) String[] orderBy,
                                @RequestParam(value = "desc" ,required = false) Boolean[] desc
                                ){
        LinkedHashMap<String, Boolean> orderMap = new LinkedHashMap<>();
        if(!Arrays.isNullOrEmpty(orderBy) && !Arrays.isNullOrEmpty(desc)){
            for (int i = 0; i < orderBy.length; i++){
                orderMap.put(orderBy[i],desc[i]);
            }
        }
        address = MetamaskUtil.formatAddress(address);
        Map<String, Object> map = usersService.getUserData(page,size,address,orderMap);
        return ApiResponse.ok().putMap(map);
    }

    @PutMapping("/blackList/{address}")
    public ApiResponse blackList(@PathVariable("address") String address) {
        redisTemplate.opsForSet().add(RedisKey.BLOCK_LIST_KEY,address);
        return ApiResponse.ok();
    }

    @DeleteMapping("/blackList/{address}")
    public ApiResponse removeBlackList(@PathVariable("address") String address) {
        redisTemplate.opsForSet().remove(RedisKey.BLOCK_LIST_KEY, address);
        return ApiResponse.ok();

    }

    @GetMapping("/videoData")
    public ApiResponse videoData(
            @RequestParam  (value = "search") String search,

            @RequestParam  (value = "page" ) Integer page,
            @RequestParam  (value = "size") Integer size,
            @RequestParam  (value = "orderBy" ,required = false) String[] orderBy,
            @RequestParam  (value = "desc" ,required = false) Boolean[] desc){
        LinkedHashMap<String, Boolean> orderMap = new LinkedHashMap<>();
        if(!Arrays.isNullOrEmpty(orderBy) && !Arrays.isNullOrEmpty(desc)){
            for (int i = 0; i < orderBy.length; i++){
                orderMap.put(orderBy[i],desc[i]);
            }
        }
        Map<String, Object> allVideo = videosService.getAllVideo(page, size, search, orderMap);
        return ApiResponse.ok().putMap(allVideo);
    }

    @GetMapping("/commentData")
    public ApiResponse commentData(@RequestParam(value = "search") String search,
                                   @RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "size") Integer size,
                                   @RequestParam(value = "orderBy" ,required = false) String[] orderBy,
                                   @RequestParam(value = "desc" ,required = false) Boolean[] desc){

        LinkedHashMap<String, Boolean> orderMap = new LinkedHashMap<>();
        if(!Arrays.isNullOrEmpty(orderBy) && !Arrays.isNullOrEmpty(desc)){
            for (int i = 0; i < orderBy.length; i++){
                orderMap.put(orderBy[i],desc[i]);
            }
        }
        Map<String, Object> allVideo = commentService.getAllComment(page, size, search, orderMap);
        return ApiResponse.ok().putMap(allVideo);
    }

    @GetMapping("/videoTransferData")
    public ApiResponse videoTransferData(@RequestParam("address") String address,@RequestParam("page") Integer page, @RequestParam("size") Integer size){
        LambdaQueryWrapper<VideoTransfer> queryWrapper = new LambdaQueryWrapper<>();
        if(MetamaskUtil.isValidAddress(address)){
            queryWrapper.eq(VideoTransfer::getSource, address)
                    .or()
                    .eq(VideoTransfer::getDestination, address);
        }

        Page<VideoTransfer> record = videoTransferService.page(new Page<VideoTransfer>(page, size),queryWrapper);
        List<VideoTransfer> records = record.getRecords();
        Collection<Integer> ids = new HashSet<>(records.size());
        Collection<VideoTransferVO> videoTransferVOS = records.stream().map(e -> {
            ids.add(e.getVideoId());
            VideoTransferVO videoTransferVO = new VideoTransferVO(e);
            return videoTransferVO;
        }).collect(Collectors.toList());
        Map<Integer, String> videoIdToTitleMap = videosService.getBaseMapper()
                .selectBatchIds(ids).stream()
                .collect(Collectors.toMap(Videos::getId, Videos::getTitle,(k1, k2) -> k1));
        videoTransferVOS.forEach(e -> e.setTitle(videoIdToTitleMap.getOrDefault(e.getVideoId(),"")));

        long count = record.getTotal();


        return ApiResponse.ok().put("data", videoTransferVOS).put("total",count);

    }

    @GetMapping("/rewardData")
    public ApiResponse rewardData(@RequestParam("address") String address,@RequestParam("page") Integer page, @RequestParam("size") Integer size){
        if (!MetamaskUtil.isValidAddress(address)) {
            address = null;
        }
        Map<String, Object> rewardData = rewardService.getRewardList(page, size, address);
        return ApiResponse.ok().putMap(rewardData);
    }


    @GetMapping("/registedUser")
    public ApiResponse registedUser(){
        long totalUser = usersService.count();
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();

        long newUser = usersService.count(new LambdaQueryWrapper<Users>().ge(true, Users::getCreatedAt, localDateTime));
        return ApiResponse.ok().put("totalUser",totalUser).put("newUser",newUser);

    }

    @GetMapping("/dailyVisit")
    public ApiResponse dailyVisit(){
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        long totalVisit = visitRecordService.count(new QueryWrapper<VisitRecord>().select("distinct user_address").ge(true, "created_at", localDateTime));
        return ApiResponse.ok().put("totalVisit",totalVisit);
    }

    @GetMapping("/postedVideo")
    public ApiResponse postedVideo(){
        long totalVideo = videosService.count();
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();

        long count = videosService.count(new QueryWrapper<Videos>().select("distinct user_address").ge(true, "created_at", localDateTime));
        return ApiResponse.ok().put("totalVideo",totalVideo).put("newVideo",count);
    }

    @GetMapping("/comment")
    public ApiResponse comment() {
        long totalComment = commentService.count();
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        long newComment = commentService.count(new QueryWrapper<Comment>().ge(true, "created_at", localDateTime));
        return ApiResponse.ok().put("totalComment",totalComment).put("newComment",newComment);
    }

    @GetMapping("/reward")
    public ApiResponse reward() {
        return null;
    }






}




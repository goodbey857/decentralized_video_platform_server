package top.kingdon.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.kingdon.config.RedisKey;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.po.*;
import top.kingdon.dataobject.vo.VideoHistoryVO;
import top.kingdon.dataobject.vo.VideoTransferVO;
import top.kingdon.service.*;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    SearchHistoryService searchHistoryService;
    @Resource
    CommentService commentService;
    @Resource
    VideoHistoryService  videoHistoryService;
    @Resource
    VideosService videosService;
    @Resource
    VideoTransferService videoTransferService;
    @Resource
    TokenTransferService tokenTransferService;
    @Resource
    RedisTemplate redisTemplate;

    @PostMapping("/search")
    public ApiResponse recordSearch(@RequestBody String keyword){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setKeyword(keyword);
        searchHistory.setUserAddress(userAddress);
        searchHistory.setCreatedAt(new Date());
        searchHistoryService.save(searchHistory);
        return ApiResponse.ok();
    }

    @GetMapping("/search")
    public ApiResponse getSearchHistory(){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<SearchHistory> searchHistory = searchHistoryService.list(new LambdaQueryWrapper<SearchHistory>()
                .eq(SearchHistory::getUserAddress, userAddress)
                .isNull(SearchHistory::getCanceledAt)
                .orderByDesc(SearchHistory::getCreatedAt));
        searchHistory = searchHistory.stream().collect(Collectors.toSet())
                .stream().sorted((o1, o2) ->o2.getCreatedAt().compareTo(o1.getCreatedAt())).collect(Collectors.toList());

        return ApiResponse.ok().put("record", searchHistory);
    }

    @DeleteMapping("/search/{keyword}")
    public ApiResponse deleteSearchHistory(@PathVariable String keyword) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);

        searchHistoryService.update(new LambdaUpdateWrapper<SearchHistory>()
                .eq(SearchHistory::getKeyword, keyword)
                .eq(SearchHistory::getUserAddress, userAddress)
                .isNull(SearchHistory::getCanceledAt)
                .setSql(true,"canceled_at = now()"));
        return ApiResponse.ok();
    }

    @GetMapping("/comments")
    public ApiResponse getCommentHistory() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<Comment> comments = commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getUserAddress, userAddress).isNull(Comment::getCanceledAt));

        return ApiResponse.ok().put("record", comments);
    }

    @DeleteMapping("/comments/{id}")
    public ApiResponse deleteComments(@PathVariable Integer id) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        commentService.update(new LambdaUpdateWrapper<Comment>().eq(Comment::getId, id)
                .eq(Comment::getUserAddress, userAddress).setSql(true,"canceled_at = now()"));
        redisTemplate.opsForHash().increment(RedisKey.COMMENT_COUNT_KEY,userAddress, -1);
        return ApiResponse.ok();
    }

    @GetMapping("/video")
    public ApiResponse getWatchHistory() {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        List<VideoHistory> videoHistoryList = videoHistoryService.list(new LambdaQueryWrapper<VideoHistory>()
                .eq(VideoHistory::getUserAddress, userAddress).isNull(VideoHistory::getCanceledAt
        ).orderByDesc(VideoHistory::getCreatedAt));
        if(CollectionUtils.isEmpty(videoHistoryList)){
            return ApiResponse.ok().put("record", Collections.emptyList());
        }
        Collection<Integer> videoIdList = videoHistoryList.stream().map(VideoHistory::getVideoId)
                .collect(Collectors.toSet());
        List<Videos> videoList = videosService.listByIds(videoIdList);
        Map<Integer, VideoHistory> videoHistoryMap = videoHistoryList.stream()
                .collect(Collectors.toMap(VideoHistory::getVideoId, h -> h, (h1, h2) -> h1.getCreatedAt()
                        .compareTo(h2.getCreatedAt()) > 0 ? h1 : h2));
        List<VideoHistoryVO> collect = videoList.stream().map(video -> new VideoHistoryVO(videoHistoryMap.get(video.getId()), video)).sorted((h1, h2) -> h2.getCreatedAt().compareTo(h1.getCreatedAt()))
                .collect(Collectors.toList());
        return ApiResponse.ok().put("record", collect);
    }

    @DeleteMapping("/video/{id}")
    public ApiResponse deleteVideoHistory(@PathVariable Integer id) {
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        videoHistoryService.update(new LambdaUpdateWrapper<VideoHistory>().eq(VideoHistory::getVideoId, id)
                .eq(VideoHistory::getUserAddress, userAddress).isNull(VideoHistory::getCanceledAt).setSql("canceled_at = now()"));
        return ApiResponse.ok();
    }

    @GetMapping("/videoTransfer/{page}/{limit}")
    public ApiResponse videoTransferRecord(@PathVariable Integer page, @PathVariable Integer limit){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        Page<VideoTransfer> record = videoTransferService.page(new Page<VideoTransfer>(page, limit),
                new LambdaQueryWrapper<VideoTransfer>()
                        .eq(VideoTransfer::getSource, userAddress)
                        .or()
                        .eq(VideoTransfer::getDestination, userAddress));
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


        return ApiResponse.ok().put("record", videoTransferVOS).put("count",count);

    }

    @GetMapping("/tokenTransfer/{page}/{limit}")
    public ApiResponse tokenTransferRecord(@PathVariable Integer page, @PathVariable Integer limit){
        String userAddress = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        Page<TokenTransfer> record = tokenTransferService.page(new Page<TokenTransfer>(page, limit),
                new LambdaQueryWrapper<TokenTransfer>()
                        .eq(TokenTransfer::getSource, userAddress)
                        .or()
                        .eq(TokenTransfer::getDestination, userAddress));
        List<TokenTransfer> records = record.getRecords();
        long count = record.getTotal();
        return ApiResponse.ok().put("record", records).put("count",count);
    }










}

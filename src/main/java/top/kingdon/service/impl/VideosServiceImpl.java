package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.*;
import top.kingdon.dataobject.vo.VideoVO;
import top.kingdon.mapper.*;
import top.kingdon.service.VideosService;
import org.springframework.stereotype.Service;
import top.kingdon.utils.MetamaskUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 古德白
* @description 针对表【videos】的数据库操作Service实现
* @createDate 2024-02-05 21:05:12
*/
@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos>
    implements VideosService{

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private VideosMapper videosMapper;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private SeriesMapper seriesMapper;
    @Resource
    private VideoLikeMapper videoLikeMapper;
    @Resource
    private VideoStarMapper videoStarMapper;
    @Resource
    private VideoHistoryMapper videoHistoryMapper;


    @Override
    public List<VideoVO> hotVideoList(int start, int size) {
        Set range = redisTemplate.opsForZSet().reverseRange(RedisKey.VIEW_KEY, (start-1)*size, start*size-1);

        if  (range.isEmpty()) {
            return Collections.emptyList();
        }
        List<Videos> list = this.listByIds(range);

        return videosListToVideoVOList(list, true);
    }

    @Override
    public List<VideoVO> newVideoList(int start, int size) {
        LambdaQueryWrapper<Videos> videosLambdaQueryWrapper = new LambdaQueryWrapper<Videos>().orderByDesc(Videos::getCreatedAt);
        return getVideoVOS(start, size, videosLambdaQueryWrapper, true);
    }


    @Override
    public List<VideoVO> likedVideoList(String userAddress) {
        List<Videos> videos = videoLikeMapper.likedVideos(userAddress);
        return videosListToVideoVOList(videos,true);
    }

    @Override
    public List<VideoVO> starVideoList(String userAddress) {
        List<Videos> videos = videoStarMapper.startedVideos(userAddress);
        return videosListToVideoVOList(videos, true);
    }



    @Override
    public List<VideoVO> somebodyVideoList(String userAddress,Integer page, Integer size) {
        LambdaQueryWrapper<Videos> queryWrapper = new LambdaQueryWrapper<Videos>().eq(Videos::getUserAddress, userAddress);
        List<VideoVO> videoVOS = getVideoVOS(page, size, queryWrapper,true);
        return videoVOS;
    }

    @Override
    public List<Videos> getVideoListByID(Collection<Integer> ids) {

        return null;
    }

    @Override
    public List<Integer> getVideoIDListByAddress(String userAddress) {
        return this.baseMapper.getIdsByAddress(userAddress);
    }

    @Override
    public Map<String,Object> getAllVideo(int page, int size,String search, Map<String,Boolean> orderMap) {
        QueryWrapper<Videos> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(search)){
            if(MetamaskUtil.isValidAddress(search)){
                queryWrapper.eq("user_address",search);
            }else{
                queryWrapper.like("title",search);
            }
        }

        if(!CollectionUtils.isEmpty(orderMap)) {
            orderMap.forEach((key, value) -> {
                if (value) {
                    queryWrapper.orderByDesc(true,key);
                } else {
                    queryWrapper.orderByAsc(true,key);
                }
            });
        }


        Page<Videos> videosPage = this.page(new Page<>(page, size), queryWrapper);
        List<Videos> videosList = videosPage.getRecords();
        if(CollectionUtils.isEmpty(videosList)){
            return Collections.emptyMap();
        }
        List<VideoVO> videoVOList = videosListToVideoVOList(videosList, true);
        long total = videosPage.getTotal();
        return Map.of("data",videoVOList,"total",total);



    }



    @Override
    public Long getUserViewCount(String userAddress){
        List<Integer> ids = getVideoIDListByAddress(userAddress);
        if(CollectionUtils.isEmpty(ids)) return  0L;
        return getViewCount(ids);
    }

    public Long  getViewCount(List<Integer> ids){
        List<Double> score = redisTemplate.opsForZSet().score(RedisKey.VIEW_KEY, ids.toArray());
        if(CollectionUtils.isEmpty(score)){
            return 0L;
        }
        return score.stream().mapToLong((value -> {
            if(value == null) return 0L;
            return value.longValue();
        })).sum();
    }


    @Override
    public boolean view(int videoID,String userAddress) {
        redisTemplate.opsForZSet()
                .incrementScore(RedisKey.VIEW_KEY,videoID,1);
        VideoHistory videoHistory = new VideoHistory();
        videoHistory.setUserAddress(userAddress);
        videoHistory.setVideoId(videoID);
        videoHistory.setCreatedAt(new Date());
        videoHistoryMapper.insert(videoHistory);

        return true;
    }
    public VideoVO getVideoVOByID(int videoID){
        Videos videos = videosMapper.selectById(videoID);
        if(videos==null) return null;
        return videosToVideoVO(videos,false);

    }

    @Override
    public List<VideoVO> getVideoVOListByAddress(List<String> address, int start, int size) {
        LambdaQueryWrapper<Videos> videosLambdaQueryWrapper =
                new LambdaQueryWrapper<Videos>().in(Videos::getUserAddress, address)
                        .orderByDesc(Videos::getCreatedAt);
        return getVideoVOS(start, size, videosLambdaQueryWrapper,true);
    }

    @Override
    public List<VideoVO> getSeriesVideoList(Integer id) {
        List<VideoVO> seriesVideoList = Collections.emptyList();
        if(id != null){
            LambdaQueryWrapper<Videos> videosLambdaQueryWrapper = new LambdaQueryWrapper<Videos>().eq(Videos::getSeries, id).orderBy(true, true, Videos::getCreatedAt);
            seriesVideoList = getVideoVOS(1, 1000, videosLambdaQueryWrapper,false);
        }

        return seriesVideoList;
    }

    @Override
    public List<VideoVO> search(String keyword, int start, int size) {
        LambdaQueryWrapper<Videos> videosLambdaQueryWrapper = new LambdaQueryWrapper<Videos>().like(Videos::getTitle, keyword).isNull(Videos::getCanceledAt).orderByDesc(Videos::getCreatedAt);
        return getVideoVOS(start, size, videosLambdaQueryWrapper,true);
    }

    @Override
    public Long getVideoCount(String address) {

        Object o = redisTemplate.opsForHash().get(RedisKey.VIDEO_COUNT_KEY, address);
        return  o == null ? 0 : Long.parseLong(o.toString());

    }

    @NotNull
    private List<VideoVO> getVideoVOS(int start, int size, LambdaQueryWrapper<Videos> videosLambdaQueryWrapper, boolean withoutSeries) {
        Page<Videos> page = this.page(new Page<Videos>(start, size), videosLambdaQueryWrapper);
        List<Videos> videosList = page.getRecords();
        if  (videosList.isEmpty()) {
            return Collections.emptyList();
        }
        return videosListToVideoVOList(videosList, withoutSeries);
    }

    private VideoVO videosToVideoVO(Videos videos,boolean withoutSeries){
        // todo 以后可以重写Mapper.selectOne方法，增强一个将数据缓存到redis的功能。
        Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getAddress, videos.getUserAddress()));
        Series series = null;
        if(!withoutSeries){
            series = seriesMapper.selectOne(new LambdaQueryWrapper<Series>().eq(Series::getId, videos.getSeries()));
        }
        return mergeVideoAndUsersAndSeries(videos,users,series);
    }

    private List<VideoVO> videosListToVideoVOList(List<Videos> videosList,boolean withoutSeries){
        if(CollectionUtils.isEmpty(videosList)){
            return Collections.emptyList();
        }
        List<VideoVO>  videoVOList = new ArrayList<>();
        HashMap<String,Users> usersMap = new HashMap<>();
        HashMap<Integer,Series> seriesMap = new HashMap<>();
        Collection<String> userAddressList = videosList.stream().map(Videos::getUserAddress).collect(Collectors.toSet());
        Collection<Integer> seriesIdList = videosList.stream().map(Videos::getSeries).collect(Collectors.toSet());

        usersMapper.selectBatchIds(userAddressList).forEach(users -> usersMap.put(users.getAddress(),users));
        if(!withoutSeries){
            seriesMapper.selectBatchIds(seriesIdList).forEach(series -> seriesMap.put(series.getId(),series));
        }

        for (Videos videos : videosList) {
            VideoVO videoVO = mergeVideoAndUsersAndSeries(videos, usersMap.get(videos.getUserAddress()), seriesMap.get(videos.getSeries()));
            videoVOList.add(videoVO);
        }
        return videoVOList;
    }

    private VideoVO mergeVideoAndUsersAndSeries(Videos videos, Users users, Series series){
        VideoVO videoVO = new VideoVO();
        videoVO.setId(videos.getId());
        videoVO.setCid(videos.getCid());
        videoVO.setTitle(videos.getTitle());
        videoVO.setDescription(videos.getDescription());
        videoVO.setCoverCid(videos.getCoverCid());
        videoVO.setCreateAt(videos.getCreatedAt());
        if(users==null){
            users = new Users();
            users.setAddress(videos.getUserAddress());
            users.setUsername("unknown");
            users.setProfileImageCid("QmbvVVJXLBK4W4MzooVdd7PeyxpZHuYg2eLKKsFT2iJX1Z");
        }
        videoVO.setAuthorAddress(users.getAddress());
        videoVO.setAuthorName(users.getUsername());
        videoVO.setAuthorPhotoCid(users.getProfileImageCid());
        if(series != null){
            videoVO.setSeriesTitle(series.getTitle());
            videoVO.setSeriesCoverCid(series.getCoverCid());
            videoVO.setSeriesDescription(series.getDescription());
            videoVO.setSeriesId(series.getId());
        }
        Long like = redisTemplate.opsForSet().size(RedisKey.getLikeKeyOfVideo(videos.getId()));
        videoVO.setLikeNum(like);
        Long star = redisTemplate.opsForSet().size(RedisKey.getStarKeyOfVideo(videos.getId()));
        videoVO.setStarNum(star);
        Integer comment = (Integer) redisTemplate.opsForHash().get(RedisKey.COMMENT_COUNT_KEY,videos.getId().toString());
        videoVO.setCommentNum(comment==null ? 0L :comment.longValue());
        Double score = redisTemplate.opsForZSet().score(RedisKey.VIEW_KEY, videos.getId());
        if(score==null){
            videoVO.setViewNum(0L);
        }else{
            videoVO.setViewNum(score.longValue());
        }
        return videoVO;
    }
}







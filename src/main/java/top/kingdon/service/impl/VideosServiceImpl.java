package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.*;
import top.kingdon.dataobject.vo.VideoVO;
import top.kingdon.mapper.*;
import top.kingdon.service.VideosService;
import org.springframework.stereotype.Service;

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

        return videosListToVideoVOList(list);
    }

    @Override
    public List<VideoVO> newVideoList(int start, int size) {
        LambdaQueryWrapper<Videos> videosLambdaQueryWrapper = new LambdaQueryWrapper<Videos>().orderByDesc(Videos::getCreatedAt);
        Page<Videos> videosPage = this.page(new Page<Videos>(start, size), videosLambdaQueryWrapper);

//        Page<Videos> videosPage = videosMapper.selectPage(new Page<Videos>(start, size), videosLambdaQueryWrapper);
        List<Videos> videosList = videosPage.getRecords();
        if  (videosList.isEmpty()) {
            return Collections.emptyList();
        }
        return videosListToVideoVOList(videosList);
    }


    @Override
    public List<VideoVO> followVideoList() {
        return null;
    }

    @Override
    public List<VideoVO> starVideoList() {
        return null;
    }

    @Override
    public List<VideoVO> historyVideoList() {
        return null;
    }

    @Override
    public List<VideoVO> somebodyVideoList(String userAddress) {
        return null;
    }

    @Override
    public List<Videos> getVideoListByID(List<Integer> ids) {
        List<Videos> list = redisTemplate.opsForValue().multiGet(ids);
        if (list == null || list.size() != ids.size()) {
            if (list != null) {
                List<Integer> cacheVideos = list.stream().map(videos -> videos.getId()).collect(Collectors.toList());
                List<Integer> difference = ids.stream()
                        .filter(e -> !cacheVideos.contains(e))
                        .collect(Collectors.toList());
                if (difference.size() > 0) {
                    List<Videos> unCacheVideos = videosMapper.selectBatchIds(difference);
                    unCacheVideos.stream().forEach(videos -> redisTemplate.opsForValue().set(videos.getId(), videos));
                    list.addAll(unCacheVideos);
                }
            } else {
                List<Videos> unCacheVideos = videosMapper.selectBatchIds(ids);
                unCacheVideos.stream().forEach(videos -> redisTemplate.opsForValue().set(videos.getId(), videos));
                list.addAll(unCacheVideos);
            }
        }
        return list;
    }

    @Override
    public boolean like(int videoID, String userAddress) {
        redisTemplate.opsForSet().add(RedisKey.getLikeKeyOfVideo(videoID),userAddress);
        VideoLike videoLike = new VideoLike();
        videoLike.setVideoId(videoID);
        videoLike.setUserAddress(userAddress);
        videoLike.setCreatedAt(new Date());
        videoLikeMapper.insert(videoLike);
        return true;
    }

    @Override
    public boolean unlike(int videoID, String userAddress) {
        redisTemplate.opsForSet().remove(RedisKey.getLikeKeyOfVideo(videoID),userAddress);
        videoLikeMapper.updateCanceledAt(videoID,userAddress);
        return true;
    }

    @Override
    public boolean star(int videoID, String userAddress) {
        redisTemplate.opsForSet().add(RedisKey.getStarKeyOfVideo(videoID),userAddress);
        VideoStar videoStar = new VideoStar();
        videoStar.setVideoId(videoID);
        videoStar.setUserAddress(userAddress);
        videoStar.setCreatedAt(new Date());
        videoStarMapper.insert(videoStar);
        return true;
    }

    @Override
    public boolean unstar(int videoID, String userAddress) {
        redisTemplate.opsForSet().remove(RedisKey.getStarKeyOfVideo(videoID),userAddress);
        videoStarMapper.updateCanceledAt(videoID,userAddress);
        return true;
    }

    @Override
    public boolean view(int videoID,String userAddress) {
        redisTemplate.opsForZSet()
                .incrementScore(RedisKey.VIEW_KEY,videoID,1);


        return true;
    }
    public VideoVO getVideoVOByID(int videoID){
        Videos videos = videosMapper.selectById(videoID);
        return videosToVideoVO(videos);

    }

    private VideoVO videosToVideoVO(Videos videos){
        // todo 以后可以重写Mapper.selectOne方法，增强一个将数据缓存到reids的功能。
        Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getAddress, videos.getUserAddress()));
        Series series = seriesMapper.selectOne(new LambdaQueryWrapper<Series>().eq(Series::getId, videos.getSeries()));
        return mergeVideoAndUsersAndSeries(videos,users,series);
    }

    private List<VideoVO> videosListToVideoVOList(List<Videos> videosList){
        List<VideoVO>  videoVOList = new ArrayList<>();
        HashMap<String,Users> usersMap = new HashMap<>();
        HashMap<Integer,Series> seriesMap = new HashMap<>();
        Collection<String> userAddressList = videosList.stream().map(Videos::getUserAddress).collect(Collectors.toSet());
        Collection<Integer> seriesIdList = videosList.stream().map(Videos::getSeries).collect(Collectors.toSet());
        usersMapper.selectBatchIds(userAddressList).forEach(users -> usersMap.put(users.getAddress(),users));
        seriesMapper.selectBatchIds(seriesIdList).forEach(series -> seriesMap.put(series.getId(),series));
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
        videoVO.setCreateAt(videos.getCreatedAt().getTime());
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
        Long comment = redisTemplate.opsForSet().size(RedisKey.getCommentKeyOfVideo(videos.getId()));
        videoVO.setCommentNum(comment);
        Double score = redisTemplate.opsForZSet().score(RedisKey.VIEW_KEY, videos.getId());
        if(score==null){
            videoVO.setViewNum(0L);
        }else{
            videoVO.setViewNum(score.longValue());
        }
        return videoVO;
    }
}







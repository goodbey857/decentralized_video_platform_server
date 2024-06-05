package top.kingdon.Listener;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.kingdon.Listener.event.VideoPublishedEvent;
import top.kingdon.Listener.obj.VideoPublishedSource;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.po.Series;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.service.SeriesService;
import top.kingdon.service.VideosService;
import top.kingdon.utils.MetamaskUtil;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@Component
public class ContractEventToDBListener {

    @Autowired
    VideosService videosService;

    @Autowired
    SeriesService seriesService;

    @Autowired
    RedisTemplate redisTemplate;

    @Async
    @EventListener(VideoPublishedEvent.class)
    public void  onContractEvent(VideoPublishedEvent event){
        VideoPublishedSource source = (VideoPublishedSource) event.getSource();
        Videos videos = castVideoPublishedSourceToVideos(source);
        Series series = castVideoPublishedSourceToSeries(source);
        if(series!=null){
            Series one = seriesService.getOne(new LambdaQueryWrapper<Series>().eq(Series::getUserAddress, series.getUserAddress()).eq(Series::getTitle,series.getTitle()));
            if (one == null){
                seriesService.save(series);
            }else{
                series = one;
            }
            videos.setSeries(series.getId());
        }

        try{
            videosService.save(videos);
            redisTemplate.opsForHash().increment(RedisKey.VIDEO_COUNT_KEY,source.getTo(),1);
        }catch (Exception e){
            System.out.println("视频已存在");
        }

    }

    private Videos castVideoPublishedSourceToVideos(VideoPublishedSource source) {
        Videos videos = new Videos();
        BeanUtils.copyProperties(source, videos);
        videos.setCreatedAt(new Date(source.getBlockTimestamp().longValue()*1000));
        videos.setUserAddress(source.getTo());
        return  videos;

    }

    private Series castVideoPublishedSourceToSeries(VideoPublishedSource source) {
        if(!StringUtils.hasLength(source.getSeriesTitle())){
            return null;
        }
        Series series = new Series();

        series.setUserAddress(source.getTo());
        series.setCoverCid(source.getSeriesCoverCid());
        series.setDescription(source.getSeriesDescription());
        series.setTitle(source.getSeriesTitle());
        series.setCreatedAt(new Date());
        series.setUpdatedAt(new Date());

        return  series;
    }
}


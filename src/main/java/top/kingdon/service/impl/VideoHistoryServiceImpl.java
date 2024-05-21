package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import top.kingdon.config.RedisKey;
import top.kingdon.dataobject.bo.AnalyzeCount;
import top.kingdon.dataobject.po.VideoHistory;
import top.kingdon.service.VideoHistoryService;
import top.kingdon.mapper.VideoHistoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

/**
* @author 古德白
* @description 针对表【video_history】的数据库操作Service实现
* @createDate 2024-02-15 13:23:31
*/
@Service
public class VideoHistoryServiceImpl extends ServiceImpl<VideoHistoryMapper, VideoHistory>
    implements VideoHistoryService{

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Long viewCount(Integer videoId){
        return redisTemplate.opsForZSet().score(RedisKey.VIEW_KEY, videoId).longValue();
    }

    @Override
    public List<AnalyzeCount> analyzeView(String address, String duration){
        List<AnalyzeCount> data ;
        switch(duration){
            case "week":
                data = baseMapper.analyzeViewWeek(address); break;
            case "month":
                data =  baseMapper.analyzeViewMonth(address); break;
            case "day":
                data = baseMapper.analyzeViewDay(address); break;
            default:
                return null;
        }
        return adjustOrder(data,duration);
    }

    private List<AnalyzeCount> adjustOrder(List<AnalyzeCount> data,String label){
        LocalDate now = LocalDate.now();
        final int stand;
        switch(label){
            case "day": stand = now.getDayOfMonth();break;
            case "week":
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                stand = now.get(weekFields.weekOfMonth());
                break;
            case "month": stand = now.getMonth().getValue();break;
            default: stand = 0;
        }
        data.sort((o1, o2) -> {
            int x1 = Integer.parseInt(o1.getX());
            int x2 = Integer.parseInt(o2.getX());
            if(x1<= stand && x2<= stand){
                return x1-x2;
            }else if(x1> stand && x2> stand){
                return x1-x2;
            }else return stand-x1;
        });


        return data;

    }
}





package top.kingdon.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.kingdon.dataobject.po.VisitRecord;
import top.kingdon.service.VisitRecordService;
import top.kingdon.utils.HttpContextUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private VisitRecordService visitRecordService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
                             , Object handler) throws java.io.IOException {
        String address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        if(address==null){
            response.setStatus(401);
            return false;
        }
        if(redisTemplate.opsForSet().isMember(RedisKey.BLOCK_LIST_KEY, address)){
            response.setStatus(401);
            return false;
        }


        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                         @Nullable Exception ex) throws Exception {
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setUserAddress(HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS));
        visitRecord.setApi(request.getRequestURI());
        visitRecord.setCreatedAt(new Date());
        visitRecordService.save(visitRecord);
    }


}

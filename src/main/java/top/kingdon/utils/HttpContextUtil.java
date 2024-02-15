package top.kingdon.utils;



import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

public class HttpContextUtil {
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getRespponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    public static void setCookie(String name,String value){
        getRespponse().addCookie(new Cookie(name,value));
    }

    public static Cookie getCookie(String name) {

        Cookie[] cookies = getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public static HttpSession getSession(){
        return  getRequest().getSession(true);
    }

    public static String getSessionAttribute(String name){
        return (String) getSession().getAttribute(name);
    }

    public static void setSessionAttribute(String name,String value){
        getSession().setAttribute(name,value);
    }

    public static void setSessionAttributes(Object obj){
        HttpSession session = getSession();
        Map<String, Object> stringObjectMap = BeanUtils.beanToMap(obj);
        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
            session.setAttribute(entry.getKey(),entry.getValue().toString());
        }

    }

    public static void cleanSession(){
        getSession().invalidate();
    }


}


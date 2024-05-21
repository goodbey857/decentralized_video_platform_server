package top.kingdon.config;

import org.springframework.web.servlet.HandlerInterceptor;
import top.kingdon.utils.HttpContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminOnlyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String address = HttpContextUtil.getSessionAttribute(SessionKey.USER_ADDRESS);
        if("0x9425F590F455DEB079014133C04BB04A55AD0B54".equals(address) ){
            return true;
        }
//        response.sendRedirect("http://localhost:8080/");
        response.setStatus(302);
        return false;
    }
}

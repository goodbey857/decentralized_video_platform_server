package top.kingdon.config;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import javax.annotation.Resource;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {
    @Resource
    private WebSocketService wss;


    @ExceptionHandler(WebsocketNotConnectedException.class)
    public void reconnect(){
        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }

        log.info("reconnect");
    }
}

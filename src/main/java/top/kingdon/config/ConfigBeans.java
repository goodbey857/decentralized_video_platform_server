package top.kingdon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

@Component
public class ConfigBeans {
    @Value("${arb-rpc}")
    String ARB_RPC;
    @Bean
    public HttpSessionIdResolver httpSessionStrategy(){
        return new HeaderHttpSessionIdResolver("token");
    }

    @Bean
    public Web3j web3Client(){
        WebSocketService wss = new WebSocketService(ARB_RPC, false);

        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }

        // build web3j client
        Web3j client = Web3j.build(wss);
        return client;
    }
}



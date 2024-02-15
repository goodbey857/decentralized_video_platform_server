package top.kingdon.Listener.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;


public class VideoPublishedEvent  extends ApplicationEvent{


    public VideoPublishedEvent(Object source) {
        super(source);
    }

    public VideoPublishedEvent(Object source, Clock clock) {
        super(source, clock);
    }
}

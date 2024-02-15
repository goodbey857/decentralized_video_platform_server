package top.kingdon.Listener.obj;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.math.BigInteger;

@Data
public class VideoPublishedSource {
    String to;
    String cid;
    String title;
    String description;
    String seriesTitle;
    String coverCid;
    String seriesCoverCid;
    String seriesDescription;
    BigInteger blockTimestamp;
    BigInteger blockNumber;
    String txHash;
}

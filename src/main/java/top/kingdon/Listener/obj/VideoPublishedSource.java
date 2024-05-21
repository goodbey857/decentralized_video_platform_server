package top.kingdon.Listener.obj;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import top.kingdon.utils.MetamaskUtil;

import java.math.BigInteger;

@Data
public class VideoPublishedSource {
    Integer id;
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


    public void setTo(String to){
        this.to = MetamaskUtil.formatAddress(to);
    }

    public void setTxHash(String txHash){
        this.txHash = MetamaskUtil.formatAddress(txHash);
    }
}

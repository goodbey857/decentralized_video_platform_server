package top.kingdon.dataobject.vo;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kingdon.dataobject.po.VideoTransfer;


@Data
public class VideoTransferVO extends VideoTransfer {
    private String title;

    public VideoTransferVO(VideoTransfer videoTransfer){
        BeanUtils.copyProperties(videoTransfer,this);
    }
}

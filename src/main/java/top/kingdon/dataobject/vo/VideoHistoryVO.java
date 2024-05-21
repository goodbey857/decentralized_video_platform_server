package top.kingdon.dataobject.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.kingdon.dataobject.po.VideoHistory;
import top.kingdon.dataobject.po.Videos;

import java.util.Date;

@Data
@NoArgsConstructor
public class VideoHistoryVO {
    private Integer id;

    private String cid;

    private String title;

    private String description;

    private String coverCid;

    private String userAddress;

    private Date createdAt;

    private Integer videoId;

    public VideoHistoryVO(VideoHistory videoHistory, Videos videos) {
        this.id = videoHistory.getId();
        this.userAddress = videos.getUserAddress();
        this.createdAt = videoHistory.getCreatedAt();
        this.videoId = videos.getId();
        this.cid = videos.getCid();
        this.title = videos.getTitle();
        this.description = videos.getDescription();
        this.coverCid = videos.getCoverCid();

    }
        //this.userAddress = videoHistory.getUserAddress();
}

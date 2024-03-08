package top.kingdon.dataobject.vo;

import lombok.Data;

@Data
public class VideoVO {
    private int id;
    private String title;
    private String description;
    private String cid;
    private String coverCid;
    private String authorAddress;
    private String authorName;
    private String authorPhotoCid;
    private Integer seriesId;
    private String seriesTitle;
    private String seriesCoverCid;
    private String seriesDescription;
    private Long likeNum;
    private Long commentNum;
    private Long starNum;
    private Long viewNum;
    private Long createAt;

}

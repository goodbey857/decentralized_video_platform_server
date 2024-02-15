package top.kingdon.dataobject.bo;

import lombok.Data;

@Data
public class VideoMetadata {
    String owner;
    String cid;
    String title;
    String description;
    String coverCid;
    String series;
    String seriesDescription;
    String seriesCoverCid;
}

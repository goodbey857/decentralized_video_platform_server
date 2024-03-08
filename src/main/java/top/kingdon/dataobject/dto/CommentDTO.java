package top.kingdon.dataobject.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private  String content;
    private Integer videoId;
    private Integer replayTo;
}

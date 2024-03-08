package top.kingdon.dataobject.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.kingdon.dataobject.po.Comment;
import top.kingdon.dataobject.po.Users;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentVO {
    private Integer id;

    private Integer videoId;

    private String userAddress;

    private String content;

    private Integer replayTo;

    private Date createdAt;

    private String username;

    private String profilePhotoCid;

    public CommentVO(Comment comment, Users user){
        this.id = comment.getId();
        this.videoId = comment.getVideoId();
        this.userAddress = comment.getUserAddress();
        this.content = comment.getContent();
        this.replayTo = comment.getReplayTo();
        this.createdAt = comment.getCreatedAt();
        this.username = user.getUsername();
        this.profilePhotoCid = user.getProfileImageCid();
    }
}


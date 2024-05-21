package top.kingdon.dataobject.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RewardVO {
    private Integer id;

    private String address;

    private Integer videoId;

    private Double reward;

    private Date createdAt;

    private Integer score;

    private Integer totalScore;

    private String username;

    private String profileImageCid;

    private String title;

}

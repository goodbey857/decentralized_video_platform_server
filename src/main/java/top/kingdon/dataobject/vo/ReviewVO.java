package top.kingdon.dataobject.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewVO {
    private Integer id;

    private String userAddress;

    private String username;

    private String profileImageCid;

    private Integer videoId;

    private Object status;

    private String reason;

    private Date createdAt;

    private Date updatedAt;
}

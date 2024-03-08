package top.kingdon.dataobject.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName follow
 */
@TableName(value ="follow")
@Data
public class Follow implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 粉丝
     */
    private String followerAddress;

    /**
     * 被关注者
     */
    private String followingAddress;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date canceledAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
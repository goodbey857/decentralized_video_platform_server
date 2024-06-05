package top.kingdon.dataobject.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
@NoArgsConstructor
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer videoId;

    /**
     * 
     */
    private String userAddress;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer replayTo;

    /**
     * 
     */
    private Date createdAt;

    @TableLogic(value = "null")
    private Date canceledAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
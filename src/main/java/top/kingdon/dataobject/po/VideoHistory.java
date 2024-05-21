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
 * @TableName video_history
 */
@TableName(value ="video_history")
@Data
public class VideoHistory implements Serializable {
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
    private Date createdAt;

    private Date canceledAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
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
 * @TableName series
 */
@TableName(value ="series")
@Data
public class Series implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String coverCid;

    /**
     * 
     */
    private String userAddress;

    /**
     * 
     */
    private Date createdAt = new Date();

    /**
     * 
     */
    private Date updatedAt = new Date();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
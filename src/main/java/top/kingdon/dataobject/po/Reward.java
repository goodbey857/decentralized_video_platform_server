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
 * @TableName reward
 */
@TableName(value ="reward")
@Data
public class Reward implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private Integer videoId;

    /**
     * 
     */
    private Double reward;

    /**
     * 
     */
    private Date createAt;

    /**
     * 
     */
    private Integer score;

    /**
     * 
     */
    private Integer totalScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
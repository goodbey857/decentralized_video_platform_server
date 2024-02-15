package top.kingdon.dataobject.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName videos
 */
@TableName(value ="videos")
@Data
public class Videos implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String cid;

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
    private Integer series;



    private BigInteger blockNumber;


    private String txHash;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt = new Date();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
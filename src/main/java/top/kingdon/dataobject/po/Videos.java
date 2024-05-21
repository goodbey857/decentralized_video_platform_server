package top.kingdon.dataobject.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import top.kingdon.utils.MetamaskUtil;

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

    @TableLogic(value = "null")
    private Date canceledAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
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
 * @TableName search_history
 */
@TableName(value ="search_history")
@Data
public class SearchHistory implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String userAddress;

    /**
     * 
     */
    private String keyword;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchHistory)) return false;

        SearchHistory that = (SearchHistory) o;

        if (userAddress != null ? !userAddress.equals(that.userAddress) : that.userAddress != null) return false;
        return keyword != null ? keyword.equals(that.keyword) : that.keyword == null;
    }

    @Override
    public int hashCode() {
        int result = userAddress != null ? userAddress.hashCode() : 0;
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        return result;
    }
}
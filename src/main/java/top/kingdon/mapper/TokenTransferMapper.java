package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.TokenTransfer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
* @author 古德白
* @description 针对表【token_transfer】的数据库操作Mapper
* @createDate 2024-04-18 18:05:34
* @Entity top.kingdon.dataobject.po.TokenTransfer
*/
@Mapper
public interface TokenTransferMapper extends BaseMapper<TokenTransfer> {

}





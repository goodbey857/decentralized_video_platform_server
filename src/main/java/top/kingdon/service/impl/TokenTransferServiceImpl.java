package top.kingdon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.TokenTransfer;
import top.kingdon.service.TokenTransferService;
import top.kingdon.mapper.TokenTransferMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【token_transfer】的数据库操作Service实现
* @createDate 2024-04-18 18:05:34
*/
@Service
public class TokenTransferServiceImpl extends ServiceImpl<TokenTransferMapper, TokenTransfer>
    implements TokenTransferService{

}





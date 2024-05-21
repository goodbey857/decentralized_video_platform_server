package top.kingdon.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kingdon.dataobject.po.Review;
import top.kingdon.dataobject.vo.ReviewVO;
import top.kingdon.service.ReviewService;
import top.kingdon.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

/**
* @author 古德白
* @description 针对表【review】的数据库操作Service实现
* @createDate 2024-05-12 16:40:13
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{


    @Override
    public IPage<ReviewVO> all(IPage<ReviewVO> page) {
        return this.baseMapper.all(page);
    }
}





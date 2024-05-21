package top.kingdon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.kingdon.dataobject.po.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kingdon.dataobject.vo.ReviewVO;

/**
* @author 古德白
* @description 针对表【review】的数据库操作Service
* @createDate 2024-05-12 16:40:13
*/
public interface ReviewService extends IService<Review> {
    public IPage<ReviewVO> all(IPage<ReviewVO> page);

}

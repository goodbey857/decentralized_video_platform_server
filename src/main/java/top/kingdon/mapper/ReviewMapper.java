package top.kingdon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kingdon.dataobject.vo.ReviewVO;

/**
* @author 古德白
* @description 针对表【review】的数据库操作Mapper
* @createDate 2024-05-12 16:40:13
* @Entity top.kingdon.dataobject.po.Review
*/
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    IPage<ReviewVO> all(IPage<ReviewVO> page);

}





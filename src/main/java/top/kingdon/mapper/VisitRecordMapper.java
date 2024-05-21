package top.kingdon.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kingdon.dataobject.po.VisitRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 古德白
* @description 针对表【visit_record】的数据库操作Mapper
* @createDate 2024-04-24 15:08:59
* @Entity top.kingdon.dataobject.po.VisitRecord
*/
@Mapper
public interface VisitRecordMapper extends BaseMapper<VisitRecord> {

}





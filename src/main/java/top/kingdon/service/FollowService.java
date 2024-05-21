package top.kingdon.service;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import top.kingdon.dataobject.po.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kingdon.dataobject.po.Users;

import java.util.List;

/**
* @author 古德白
* @description 针对表【follow】的数据库操作Service
* @createDate 2024-02-15 14:11:03
*/
@Service
public interface FollowService extends IService<Follow> {
    public List<String>  getFollowerAddress(String userId);
    public List<String> getFansAddress(String userId);
    public List<Users>  getFollowers(String userId);

    public List<Users>  getFans(String userId);

    public Long   getFollowerCount(String userId);
    public Long   getFansCount(String userId);


}

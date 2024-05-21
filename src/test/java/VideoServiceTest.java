import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kingdon.Application;
import top.kingdon.dataobject.po.Users;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.mapper.UsersMapper;
import top.kingdon.mapper.VideosMapper;
import top.kingdon.service.UsersService;
import top.kingdon.service.VideoLikeService;
import top.kingdon.service.VideosService;

import javax.annotation.Resource;
import java.util.List;

import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest(classes = Application.class)
public class VideoServiceTest {
    @Resource
    VideosService videoService;
    @Resource
    UsersMapper usersMapper;
    @Resource
    VideosMapper videoMapper;
    @Resource
    UsersService usersService;
    @Resource
    VideosService videosService;
    @Resource
    VideoLikeService videoLikeService;


    @Test
    public void likeTest(){
        List<String> userAddressList = usersService.list().stream().map(Users::getAddress).collect(Collectors.toList());
        List<Integer> videoIdList = videoService.list().stream().map(Videos::getId).collect(Collectors.toList());
        int i=0;
        Random random = new Random();
        while(i<3000){
            i++;
            String userAddress = userAddressList.get(random.nextInt(userAddressList.size()));
            int videoId = videoIdList.get(random.nextInt(videoIdList.size()));
            videoLikeService.doLike(videoId,userAddress);
        }
    }
    @Test
    public void viewTest(){
        List<String> userAddressList = usersService.list().stream().map(Users::getAddress).collect(Collectors.toList());
        List<Integer> videoIdList = videoService.list().stream().map(Videos::getId).collect(Collectors.toList());
        int i=0;
        Random random = new Random();
        while(i<3000){
            i++;
            String userAddress = userAddressList.get(random.nextInt(userAddressList.size()));
            int videoId = videoIdList.get(random.nextInt(videoIdList.size()));
//            int videoId = 47;
            videoService.view(videoId,userAddress);

        }
    }
}

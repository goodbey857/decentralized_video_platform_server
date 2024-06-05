import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;
import top.kingdon.Application;
import top.kingdon.dataobject.po.Users;
import top.kingdon.dataobject.po.VideoHistory;
import top.kingdon.dataobject.po.VideoLike;
import top.kingdon.dataobject.po.Videos;
import top.kingdon.service.UsersService;
import top.kingdon.service.VideoHistoryService;
import top.kingdon.service.VideoLikeService;
import top.kingdon.service.VideosService;
import top.kingdon.utils.MetamaskUtil;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(classes = Application.class)
public class RandomDataGenerate {
    @Resource
    VideoHistoryService videoHistoryService;
    @Resource
    VideosService videosService;
    @Resource
    UsersService usersService;
    @Resource
    VideoLikeService  videoLikeService;
    @Test
    public void randomView() throws InterruptedException {
        ArrayList<VideoHistory> videoHistories = new ArrayList<>(1000);

        List<Integer> videoIds = videosService.list().stream().map(Videos::getId).collect(Collectors.toList());
        PrimitiveIterator.OfInt iterator = new Random().ints(0, videoIds.size()).iterator();
        List<Object> addresses = SqlRunner.db().selectObjs("select address from users");
        PrimitiveIterator.OfInt iterator1 = new Random().ints(0, addresses.size()).iterator();
        long D2023_1_1 = 1672556909000L;
        long today = new Date().getTime();
        PrimitiveIterator.OfLong iterator2 = new Random().longs(D2023_1_1, today).iterator();

        for(int i=0;i<1000;i++){
            VideoHistory videoHistory = new VideoHistory();
            videoHistories.add(videoHistory);
        }
        for(int m=0;m<20;m++){
            System.out.println(m);
            for (VideoHistory videoHistory : videoHistories){
                videoHistory.setId(null);
                videoHistory.setUserAddress(addresses.get(iterator1.nextInt()).toString());
                videoHistory.setVideoId(videoIds.get(iterator.nextInt()));
                videoHistory.setCreatedAt(new Date(iterator2.nextLong()));
//                videoHistory.setCreatedAt(new Date());
            }
            videoHistoryService.saveBatch(videoHistories);

        }




    }

    @Test
    public void randomLike(){
        List<Integer> videoIds = videosService.list().stream().map(Videos::getId).collect(Collectors.toList());
        List<Object> addresses = SqlRunner.db().selectObjs("select address from users");
        PrimitiveIterator.OfInt randomAddress = new Random().ints(0, addresses.size()).iterator();
        PrimitiveIterator.OfInt ints = new Random().ints(0, 20).iterator();
        ArrayList<VideoLike> videoLikes = new ArrayList<>();
        for (Integer videoId : videoIds) {

            for (int i = 0; i < ints.nextInt(); i++) {
                VideoLike videoLike = new VideoLike();
                videoLike.setVideoId(videoId);
                videoLike.setUserAddress(addresses.get(randomAddress.nextInt()).toString());
                videoLike.setCreatedAt(new Date());
                videoLikes.add(videoLike);
            }

        }
        videoLikeService.saveBatch(videoLikes);
    }

    @Test
    public void randomUser(){
        String cid = "QmSWYawyTFyXhJf8PsL4x9yEJFaZfBqyNjHhr33Qb328EE";
        ArrayList<Users> users = new ArrayList<>(500);
        for(int m=0;m<100;m++){
            System.out.println(m);
            for(int i=0;i<500;i++){
                Users user = new Users();
                user.setUsername("robot");
                user.setAddress(newWallet().address);
                user.setProfileImageCid(cid);
                user.setBio("测试机器人");
                user.setEmail("robot@gmail.com");
                users.add(user);
            }
            usersService.saveBatch(users);
            users.clear();
        }




    }

    public static wallet newWallet(){
        // 生成随机的私钥
        SecureRandom secureRandom = new SecureRandom();
        byte[] privateKeyBytes = new byte[32];
        secureRandom.nextBytes(privateKeyBytes);
        String privateKey = Numeric.toHexStringNoPrefix(privateKeyBytes);

        // 通过私钥生成公钥和地址
        ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        String publicKey = keyPair.getPublicKey().toString(16);
        String address = Keys.getAddress(keyPair.getPublicKey());

        wallet w = new wallet();
        w.privateKey = privateKey;
        w.publicKey = publicKey;
        w.address = MetamaskUtil.formatAddress(address);
        return w;
    }

    static class wallet{
        String privateKey;
        String publicKey;
        String address;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }


}

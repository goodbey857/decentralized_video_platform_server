import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kingdon.Application;
import top.kingdon.dataobject.po.Follow;
import top.kingdon.dataobject.po.Users;
import top.kingdon.service.FollowService;
import top.kingdon.service.UsersService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(classes = Application.class)
public class RandomFollowTest {
    @Resource
    UsersService usersService;

    @Resource
    FollowService followService;

    @Test
    void followTest() throws InterruptedException {
        List<Users> allUser = usersService.list();
        List<String> allUserAddress = allUser.stream().map(Users::getAddress).collect(Collectors.toList());
        Random random = new Random();
        PrimitiveIterator.OfInt iterator = random.ints(0, allUserAddress.size()).iterator();
        ArrayList<Follow> followArrayList = new ArrayList<Follow>(10000);

        for (int i = 0; i < 10000; i++) {
           String one = allUserAddress.get(iterator.nextInt());
           String two = allUserAddress.get(iterator.nextInt());
           Follow follow = new Follow();
           follow.setCreatedAt(new Date());
           follow.setFollowerAddress(one);
           follow.setFollowingAddress(two);
           followArrayList.add(follow);
        }
        followService.saveBatch(followArrayList);

    }

    @Test
    void test2(){
        List<Map<String, Object>> maps = SqlRunner.db().selectList("select count(follower_address) as count,following_address from follow where canceled_at is null group by following_address");
        maps.forEach(map -> {
            System.out.println(map.get("count") + "\t" + map.get("following_address"));
        });
    }
}

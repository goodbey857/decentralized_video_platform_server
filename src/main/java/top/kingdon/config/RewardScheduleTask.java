package top.kingdon.config;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;
import top.kingdon.service.ERC20ContractService;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
public class RewardScheduleTask {

    @Resource
    ERC20ContractService erc20ContractService;

    @Value("${ERC20_CONTRACT_ADDRESS}")
    String ERC20_CONTRACT_ADDRESS;
    @Value("${private_key}")
    String private_key;
    static final double dailyTotalReward = 200000;

    @Scheduled(cron = "1 0 0 * * *")
    public void calculateReward() throws ExecutionException, InterruptedException {
        List<Map<String, Object>> yesterdayViewData = SqlRunner.db().selectList("select video_id, count(1) as count, any_value(user_address) as address from video_history\n" +
                "where created_at\n" +
                "    between MAKEDATE(year(DATE_SUB(now(), INTERVAL 1 DAY)), dayofyear(DATE_SUB(now(), INTERVAL 1 DAY)))\n" +
                "    and MAKEDATE(year(now()), dayofyear(now()))\n" +
                "group by video_id;");
        List<Map<String, Object>> yesterdayLikeData = SqlRunner.db().selectList("select video_id, count(1) as count, any_value(user_address) as address from video_like\n" +
                "where created_at\n" +
                "          between MAKEDATE(year(DATE_SUB(now(), INTERVAL 1 DAY)), dayofyear(DATE_SUB(now(), INTERVAL 1 DAY)))\n" +
                "          and MAKEDATE(year(now()), dayofyear(now()))\n" +
                "group by video_id;");

        List<Map<String, Object>> videoIdMaps = SqlRunner.db().selectList("select id, user_address from videos");
        HashMap<Integer,String> videoIdMap = new HashMap<Integer,String>();
        for (Map<String, Object> map : videoIdMaps) {
            videoIdMap.put((Integer) map.get("id"),(String) map.get("user_address"));
        }

        Long totalScore = 0L;

        HashMap<Integer,Long> rewardMap = new HashMap<Integer,Long>();
        for (Map<String, Object> yesterdayViewDatum : yesterdayViewData) {
            Integer key = (Integer) yesterdayViewDatum.get("video_id");
            Long value= (Long) yesterdayViewDatum.get("count");
            rewardMap.put(key,value);
            totalScore+=value;
        }
        for (Map<String, Object> yesterdayLikeDatum : yesterdayLikeData) {
            Integer key = (Integer) yesterdayLikeDatum.get("video_id");
            Long value= (Long) yesterdayLikeDatum.get("count");
            rewardMap.put(key,rewardMap.getOrDefault(key,0L)+value*5);
            totalScore+=value*5;
        }
        final Double finalTotalScore = totalScore.doubleValue();

        rewardMap.forEach((k,v)->SqlRunner.db()
                .insert("insert into reward(address,video_id,reward,create_at,score,total_score) values({0},{1},{2},now(),{3},{4})",
                        videoIdMap.get(k),k,v/finalTotalScore*dailyTotalReward,v,finalTotalScore));

        ArrayList<String> addresses = new ArrayList<>();
        ArrayList<BigInteger> amounts = new ArrayList<>();
        for (Map.Entry<Integer, Long> integerLongEntry : rewardMap.entrySet()) {
            addresses.add(videoIdMap.get(integerLongEntry.getKey()));
            amounts.add(new BigInteger(integerLongEntry.getValue().toString()).multiply(BigInteger.valueOf(10).pow(18)));
        }
        String tx = erc20ContractService.allocateReward(addresses, amounts);
    }

}

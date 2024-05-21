package top.kingdon.Listener;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Filter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.websocket.WebSocketService;
import top.kingdon.Listener.event.VideoPublishedEvent;
import top.kingdon.Listener.obj.VideoPublishedSource;
import top.kingdon.config.RedisKey;
import top.kingdon.utils.MetamaskUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class ContractEventListener  {

    @Value("${contract-address}")
    String CONTRACT_ADDRESS;
    @Value("${ERC20_CONTRACT_ADDRESS}")
    String ERC20_CONTRACT_ADDRESS;

    @Autowired
    Web3j web3jClient;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    RedisTemplate redisTemplate;


    @Async
    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) throws ExecutionException, InterruptedException {
        // create filter for contract events
        listenVideoPublishedEvent();
        listenVideoTransferEvent();
        listenErc20TransferEvent();



    }

    private void listenVideoPublishedEvent() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, CONTRACT_ADDRESS);

        Event event = new Event("VideoPublished", Arrays.asList(
                new TypeReference<Uint256>(true) {
                },
                new TypeReference<Address>(true) {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                }
        ));
        filter.addSingleTopic(EventEncoder.encode(event));
        // subscribe to events
        web3jClient.ethLogFlowable(filter).subscribe(data -> {

            log.info("Event received: {}", data);
//            String indexedResults = FunctionReturnDecoder.decodeAddress(data.getTopics().get(1));
            Uint256 id = (Uint256) FunctionReturnDecoder.decodeIndexedValue(data.getTopics().get(1),new TypeReference<Uint256>(true) {});
            String address = FunctionReturnDecoder.decodeAddress(data.getTopics().get(2));
            address = MetamaskUtil.formatAddress(address);
            List<Type> results = FunctionReturnDecoder.decode(data.getData(),
                    event.getNonIndexedParameters());

            VideoPublishedSource videoPublishedSource = new VideoPublishedSource();
            videoPublishedSource.setId(id.getValue().intValue());
            videoPublishedSource.setTo(address);
            videoPublishedSource.setCid(((Utf8String) results.get(0)).getValue());
            videoPublishedSource.setTitle(((Utf8String) results.get(1)).getValue());
            videoPublishedSource.setDescription(((Utf8String) results.get(2)).getValue());
            videoPublishedSource.setCoverCid(((Utf8String) results.get(3)).getValue());
            videoPublishedSource.setSeriesTitle(((Utf8String) results.get(4)).getValue());

            videoPublishedSource.setSeriesCoverCid(((Utf8String) results.get(5)).getValue());
            videoPublishedSource.setSeriesDescription(((Utf8String) results.get(6)).getValue());
            videoPublishedSource.setBlockNumber(data.getBlockNumber());
            videoPublishedSource.setTxHash(data.getTransactionHash());
            EthBlock ethBlock = web3jClient.ethGetBlockByNumber(new DefaultBlockParameterNumber(data.getBlockNumber()), false).send();
            videoPublishedSource.setBlockTimestamp(ethBlock.getBlock().getTimestamp());

            VideoPublishedEvent videoPublishedEvent = new VideoPublishedEvent(videoPublishedSource);

            // 发布事件
            applicationEventPublisher.publishEvent(videoPublishedEvent);
            log.info("VideoPublishedSource: " + videoPublishedSource);
        }, error -> {
            System.out.println("Error: " + error);
        });
    }

    private void listenVideoTransferEvent() {
        EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, CONTRACT_ADDRESS);
        Event transferEvent = new Event("Transfer", Arrays.asList(
                new TypeReference<Address>() {
                },
                new TypeReference<Address>() {
                },
                new TypeReference<Uint256>() {
                }
        ));
        ethFilter.addSingleTopic(EventEncoder.encode(transferEvent));
        web3jClient.ethLogFlowable(ethFilter).subscribe(data -> {
            log.info("Received event: " + data);
            // 处理事件数据
            String from = FunctionReturnDecoder.decodeAddress(data.getTopics().get(1));
            String to = FunctionReturnDecoder.decodeAddress(data.getTopics().get(2));
            from = MetamaskUtil.formatAddress(from);
            to = MetamaskUtil.formatAddress(to);
            Uint256 type = (Uint256) FunctionReturnDecoder.decodeIndexedValue(data.getTopics().get(3), new TypeReference<Uint256>() {
            });
            BigInteger videoId = type.getValue();
            String transactionHash = data.getTransactionHash();
            BigInteger blockNumber = data.getBlockNumber();
            BigInteger timestamp = web3jClient.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send().getBlock().getTimestamp();

            log.info("VideoTransferEvent: from={}, to={}, videoId={}", from, to, videoId);
            SqlRunner.db().insert("insert into video_transfer(source,destination,video_id,tx, created_at) values({0},{1},{2},{3},CONVERT_TZ(FROM_UNIXTIME({4}), '+08:00','+00:00'))",from, to, videoId, transactionHash,timestamp.longValue());
            SqlRunner.db().update("update videos set user_address={0} where id={1} and user_address = {2}",to, videoId, from);
            if(!from.equals("0x0000000000000000000000000000000000000000")){
                redisTemplate.opsForHash().increment(RedisKey.VIDEO_COUNT_KEY, from, -1);
                redisTemplate.opsForHash().increment(RedisKey.VIDEO_COUNT_KEY, to, 1);
            }


        });
    }

    public void listenErc20TransferEvent() {
        EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, ERC20_CONTRACT_ADDRESS);
        Event transferEvent = new Event("Transfer", Arrays.asList(
                new TypeReference<Address>() {
                },
                new TypeReference<Address>() {
                },
                new TypeReference<Uint256>() {
                }
        ));
        ethFilter.addSingleTopic(EventEncoder.encode(transferEvent));
        web3jClient.ethLogFlowable(ethFilter).subscribe(data -> {
            log.info("Received event: " + data);
            // 处理事件数据
            String from = FunctionReturnDecoder.decodeAddress(data.getTopics().get(1));
            String to = FunctionReturnDecoder.decodeAddress(data.getTopics().get(2));
            from = MetamaskUtil.formatAddress(from);
            to = MetamaskUtil.formatAddress(to);
            Uint256 type = (Uint256) FunctionReturnDecoder.decodeIndexedValue(data.getData(), new TypeReference<Uint256>() {
            });
            BigInteger blockNumber = data.getBlockNumber();
            BigInteger timestamp = web3jClient.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send().getBlock().getTimestamp();
            BigInteger value = type.getValue();
            String transactionHash = data.getTransactionHash();
            SqlRunner.db().insert("insert into token_transfer(source,destination,value,tx, created_at) values({0},{1},{2},{3},CONVERT_TZ(FROM_UNIXTIME({4}), '+08:00','+00:00'))",from, to, value, transactionHash,timestamp.longValue());

        });
    }
}


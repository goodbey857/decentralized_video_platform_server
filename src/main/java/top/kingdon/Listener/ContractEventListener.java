package top.kingdon.Listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.websocket.WebSocketService;
import top.kingdon.Listener.event.VideoPublishedEvent;
import top.kingdon.Listener.obj.VideoPublishedSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class ContractEventListener  {

    @Value("${contract-address}")
    String CONTRACT_ADDRESS;
    @Value("${contract-abi}")
    String CONTRACT_ABI;

    @Autowired
    Web3j web3jClient;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @Async
    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) throws ExecutionException, InterruptedException {
        // create filter for contract events
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, CONTRACT_ADDRESS);

        Event event = new Event("VideoPublished", Arrays.asList(
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
            String indexedResults = FunctionReturnDecoder.decodeAddress(data.getTopics().get(1));
            List<Type> results = FunctionReturnDecoder.decode(data.getData(),
                    event.getNonIndexedParameters());

            VideoPublishedSource videoPublishedSource = new VideoPublishedSource();
            videoPublishedSource.setTo(indexedResults);
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
}

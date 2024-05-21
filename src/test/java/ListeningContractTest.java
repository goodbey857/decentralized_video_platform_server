import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.ContractUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListeningContractTest {
    public static String WSS_RPC = "wss://arb-sepolia.g.alchemy.com/v2/mzRwv-ctDBaqMDYVCxRX-3H-owXm3Z5S";
    public static String CONSTRACT_ADDRESS = "0x8165B15Ae68ABf97a4e0721fA4D90F0966DE7c07";
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketService wss = new WebSocketService(WSS_RPC, false);

        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }

        // build web3j client
        Web3j client = Web3j.build(wss);
//        EthBlock ethBlock = client.ethGetBlockByNumber(new DefaultBlockParameterNumber(1), false).sendAsync().get();
//        System.out.println(ethBlock.getBlock().getMiner());
        // create filter for contract events
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, CONSTRACT_ADDRESS);

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
        client.ethLogFlowable(filter).subscribe(log -> {
            System.out.println("Event received");
            Uint256 id = (Uint256) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1),new TypeReference<Uint256>(true) {});
            String address = FunctionReturnDecoder.decodeAddress(log.getTopics().get(2));
            List<Type> results = FunctionReturnDecoder.decode(log.getData(),
                    event.getNonIndexedParameters());

            System.out.println("id :" + id.getValue());
            System.out.println("indexedResult :" + address);
            for (Type result : results) {
                if (result instanceof Utf8String) {
                    String stringValue = ((Utf8String) result).getValue();
                    // 对解析出的字符串类型参数进行处理
                    System.out.println("String value: " + stringValue);
                }else{
                    System.out.println("Other value: " + result.getValue().toString());
                }
            }
        }, error -> {
            System.out.println("Error: " + error);
        });

    }
}

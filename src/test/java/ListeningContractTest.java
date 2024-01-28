import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.ContractUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListeningContractTest {
    public static String WSS_RPC = "wss://arb-sepolia.g.alchemy.com/v2/mzRwv-ctDBaqMDYVCxRX-3H-owXm3Z5S";
    public static String CONSTRACT_ADDRESS = "0xB269bb5824d68732188708eA51356fF758DABC5B";
    public static void main(String[] args) {
        WebSocketService wss = new WebSocketService(WSS_RPC, false);

        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }

        // build web3j client
        Web3j client = Web3j.build(wss);

        // create filter for contract events
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, CONSTRACT_ADDRESS);
        Event event = new Event("VideoPublished", Arrays.asList(
                new TypeReference<Address>(true) {
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

            List<Type> results = FunctionReturnDecoder.decode(log.getData(),
                    event.getNonIndexedParameters());
            String indexedResults = FunctionReturnDecoder.decodeAddress(log.getTopics().get(1));


            System.out.println("indexedResult :"+indexedResults);
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

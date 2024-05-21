
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class ContractReward {
    public static String WSS_RPC = "wss://arb-sepolia.g.alchemy.com/v2/mzRwv-ctDBaqMDYVCxRX-3H-owXm3Z5S";
    public static String CONSTRACT_ADDRESS = "0x93e519bf6b9a0d0fd239dd02ed8c678d26e3979e";
    static Credentials credentials = Credentials.create("4eeaec53e6e2525c5b956ce3af50e695a2ae29fcf13acd6378408e1124c88ef2");


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketService wss = new WebSocketService(WSS_RPC, false);

        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }
        Web3j client = Web3j.build(wss);
        BigInteger nonce = client.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get().getTransactionCount().subtract(BigInteger.valueOf(1)).add(BigInteger.valueOf(1));
        // Use the client to interact with the contract
        Function function = new Function("batchAllocateReward",
                Arrays.asList(
                        new DynamicArray(Address.class, new Address("0x9425F590F455deB079014133C04Bb04a55Ad0B54")),
                        new DynamicArray(Uint256.class, new Uint256[]{new Uint256(100000000)})
                ), Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger gasLimit = new BigInteger("30000000");
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, BigInteger.valueOf(2000000000), gasLimit, CONSTRACT_ADDRESS, encodedFunction);

        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                client.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                        .sendAsync()
                        .get();

        String transactionHash = response.getTransactionHash();
        System.out.println(transactionHash);


    }



}

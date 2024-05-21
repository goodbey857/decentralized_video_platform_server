package top.kingdon.service.impl;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ERC20ContractServiceImpl implements ERC20ContractService {
    @Resource
    Web3j client;

    @Value("ERC20_CONTRACT_ADDRESS")
    String ERC20_CONTRACT_ADDRESS;
    @Value("private_key")
    String private_key;


    @Override
    public String allocateReward(List<String> addresses, List<BigInteger> amounts) throws ExecutionException, InterruptedException {
        Credentials credentials = Credentials.create(private_key);

        BigInteger nonce = client.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get().getTransactionCount().subtract(BigInteger.valueOf(1)).add(BigInteger.valueOf(1));
        Address[] address = addresses.stream().map(Address::new).toArray(Address[]::new);
        Uint256[] amount = amounts.stream().map(Uint256::new).toArray(Uint256[]::new);
        DynamicArray<Address> addressDynamicArray = new DynamicArray<Address>(Address.class,address);
        DynamicArray<Uint256> amountDynamicArray = new DynamicArray<Uint256>(Uint256.class, amount);

        // Use the client to interact with the contract
        Function function = new Function("batchAllocateReward",
                Arrays.asList(addressDynamicArray, amountDynamicArray), Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger gasLimit = new BigInteger("30000000");
        BigInteger gasPrice = client.ethGasPrice().sendAsync().get().getGasPrice();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, ERC20_CONTRACT_ADDRESS, encodedFunction);

        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                client.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                        .sendAsync()
                        .get();

        String transactionHash = response.getTransactionHash();
        System.out.println(transactionHash);
        return transactionHash;
    }
}

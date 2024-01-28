import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.ens.contracts.generated.OffchainResolverContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.util.Arrays;
import java.util.List;

public class Test2 {

//    public static void main(String[] args) {
//        // Create a new instance of Web3j
//        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/your-project-id"));
//
//        // Load the contract
//        String contractAddress = "0x123456...";
//        String privateKey = "your-private-key";
//        Credentials credentials = Credentials.create(privateKey);
//        OffchainResolverContract contract = new OffchainResolverContract(contractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
//
//        // Create an Event object
//        Event event = new Event("Transfer",
//                Arrays.asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
//
//        // Parse the event logs
//        List<Contract.EventValuesWithLog> eventValuesWithLogs = contract.getLogs(event);
//        for (Contract.EventValuesWithLog eventValuesWithLog : eventValuesWithLogs) {
//            List<Type> indexedValues = eventValuesWithLog.getIndexedValues();
//            List<Type> nonIndexedValues = eventValuesWithLog.getNonIndexedValues();
//
//            // Print the parsed values
//            System.out.println("From: " + indexedValues.get(0));
//            System.out.println("To: " + indexedValues.get(1));
//            System.out.println("Value: " + nonIndexedValues.get(0));
//        }
//    }
}

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

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.IntStream;

public class Test2 {
    public static void main(String[] args) {
        System.out.println(System.getProperties());
    }

}

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
//        LocalDate now = LocalDate.now();
//        System.out.println(now.getDayOfMonth());
//        System.out.println(now.getMonth().getValue());
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        // 获取指定日期所在月份的第几周
//        int weekOfMonth = now.get(weekFields.weekOfMonth());
//        System.out.println(weekOfMonth);
//        List<Integer> data = Arrays.asList(1, 3, 6,   11,2, 7,10, 8, 5, 4, 9, 12);
        List<Integer> data  = Arrays.asList(12,11,10,9,8,7,6,5,4,3,2,1);
        int dayOfMonth = 5;
        data.sort((x1, x2) -> {
            if(x1<= dayOfMonth && x2<= dayOfMonth){
                return x1-x2;
            }else if(x1> dayOfMonth && x2> dayOfMonth){
                return x1-x2;
            }else return dayOfMonth-x1;
        });
        System.out.println(data);
    }

}

package top.kingdon.service;




import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ERC20ContractService {
    public String allocateReward(List<String> addresses, List<BigInteger> amounts) throws ExecutionException, InterruptedException;
}

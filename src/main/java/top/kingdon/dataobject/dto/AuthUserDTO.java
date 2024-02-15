package top.kingdon.dataobject.dto;

import lombok.Data;
import top.kingdon.utils.MetamaskUtil;

@Data
public class AuthUserDTO {
    private String signature;
    private String message ;
    private String address;

    public String setAddress(String address){

        return this.address = MetamaskUtil.formatAddress(address);
    }

}

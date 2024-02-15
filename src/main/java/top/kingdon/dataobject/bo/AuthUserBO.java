package top.kingdon.dataobject.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kingdon.dataobject.po.Users;


@Data
@EqualsAndHashCode(callSuper = true)
public class AuthUserBO extends Users {
    private boolean newUser;
}

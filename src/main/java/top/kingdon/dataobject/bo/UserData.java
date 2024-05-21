package top.kingdon.dataobject.bo;

import lombok.Data;

//获取用户数据，用户地址，首次登录时间，24小时访问次数，视频总数，粉丝数量，最后一次登录时间，加入黑名单，移除黑名单，
@Data
public class UserData {
    private String userAddress;
    private String username;
    private String firstLoginTime;
    private Integer visitTimes;
    private Integer videoCount;
    private Integer fansCount;
    private String lastLoginTime;
    private Boolean inBlacklist;
}

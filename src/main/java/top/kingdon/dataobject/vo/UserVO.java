package top.kingdon.dataobject.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import top.kingdon.dataobject.po.Users;

@Data
@NoArgsConstructor
public class UserVO {
    private String address;

    private String username;

    private String email;

    private String bio;

    private String profileImageCid;

    private Long followerNum;

    private Long fanNum;

    public UserVO(Users user){
        this.address = user.getAddress();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.profileImageCid = user.getProfileImageCid();
    }
}

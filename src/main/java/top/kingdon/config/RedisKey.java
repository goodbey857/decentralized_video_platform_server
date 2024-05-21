package top.kingdon.config;

public class RedisKey {

    static final String STAR_KEY_PREFIX="star:";
    static final String LIKE_KEY_PREFIX="like:";
    static final String COMMENT_KEY_PREFIX="comment:";
    public static final String VIEW_KEY="view";
    public static final String HOT_VIDEO_KEY="hot";
    public static final String FAN_COUNT_KEY = "fanCount";
    public static final String FOLLOW_COUNT_KEY = "followCount";
    public static final String STAR_COUNT_KEY = "starCount";
    public static final String LIKE_COUNT_KEY = "likeCount";
    public static final String COMMENT_COUNT_KEY = "commentCount";
    public static final String VIDEO_COUNT_KEY =  "videoCount";
    public static final String BLOCK_LIST_KEY = "blockList";

    public static String getStarKeyOfVideo(int videoId){
        return STAR_KEY_PREFIX+videoId;
    }

    public static String getLikeKeyOfVideo(int videoId){
        return LIKE_KEY_PREFIX+videoId;
    }

    public static String getCommentKeyOfVideo(int videoId){
        return COMMENT_KEY_PREFIX+videoId;
    }


}

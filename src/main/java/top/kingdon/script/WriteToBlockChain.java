package top.kingdon.script;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;
import top.kingdon.dataobject.bo.VideoMetadata;
import top.kingdon.dataobject.po.Videos;

import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.datatypes.*;

public class WriteToBlockChain {

    private static WebSocketService wss = new WebSocketService("wss://arb-sepolia.g.alchemy.com/v2/mzRwv-ctDBaqMDYVCxRX-3H-owXm3Z5S", false);
    static{
        try {
            wss.connect();
        } catch (Exception e) {
            System.out.println("Error while connecting to WSS service: " + e);
        }
    }


//     build web3j client
    static Web3j client = Web3j.build(wss);

    private static final String contractAddress = "0x597D7954139f7e1Ebbf6a31dF4086a45D366b588";
    static Credentials credentials = Credentials.create("4eeaec53e6e2525c5b956ce3af50e695a2ae29fcf13acd6378408e1124c88ef2");

    static BigInteger nonce;

    static {
        try {
            nonce = getNonce(credentials.getAddress()).subtract(BigInteger.valueOf(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 连接数据库
    public static Connection connnectToMysql() {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/decentralized_video_platform_db";
        String username = "root";
        String password = "2002";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立数据库连接
            connection = DriverManager.getConnection(url, username, password);

            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    public static HashMap<String,String> readPathToCidMapping() {
        HashMap<String, String> pathToCidMapping = new HashMap<>();
        Connection connection = connnectToMysql();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM temp_resources_cid_mapping");
            while (resultSet.next()) {
                String path = resultSet.getString("resources_path");
                String cid = resultSet.getString("cid");
                pathToCidMapping.put(path, cid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pathToCidMapping;
    }

    public static HashMap<String,String> readUsernameToAddressMapping() {
        HashMap<String, String> usernameToAddressMapping = new HashMap<>();
        Connection connection = connnectToMysql();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                String path = resultSet.getString("username");
                String cid = resultSet.getString("address");
                usernameToAddressMapping.put(path, cid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernameToAddressMapping;
    }

    public static ResultSet getVideoMetadata() {
        Connection connection = connnectToMysql();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM video_metadata where title not in (select title from videos)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private static BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount =
                client.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
        return ethGetTransactionCount.getTransactionCount();
    }


    public static void writeToBlockChain(VideoMetadata videos) throws Exception {
        nonce = nonce.add(BigInteger.valueOf(1));

        Function function = new Function(
                "mint",
                Arrays.asList(new Address(videos.getOwner()),
                        new Utf8String(videos.getCid()),
                        new Utf8String(videos.getTitle()==null?"":videos.getTitle()),
                        new Utf8String(videos.getDescription()==null?"":videos.getDescription()),
                        new Utf8String(videos.getCoverCid()),
                        new Utf8String(videos.getSeries()==null?"":videos.getSeries()),
                        new Utf8String(videos.getSeriesCoverCid()==null?"":videos.getSeriesCoverCid()),
                        new Utf8String(videos.getSeriesDescription()==null?"":videos.getSeriesDescription())
                        ),
                Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger gasLimit = new BigInteger("30000000");
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, BigInteger.valueOf(2000000000L),gasLimit, contractAddress, encodedFunction);

        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                client.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, credentials)))
                        .sendAsync()
                        .get();

        String transactionHash = response.getTransactionHash();
        System.out.println(transactionHash);

    }

    public static void insertIntoVideos(Videos videos){
        Connection connection = connnectToMysql();

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO videos(cid,title,description,cover_cid,user_address,series,created_at,updated_at,block_number,tx_hash) VALUES (?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, videos.getCid());
            statement.setString(2, videos.getTitle());
            statement.setString(3, videos.getDescription());
            statement.setString(4, videos.getCoverCid());
            statement.setString(5, videos.getUserAddress());
            statement.setInt(6, videos.getSeries());
            statement.setDate(7, (Date) videos.getCreatedAt());
            statement.setDate(8, (Date) videos.getUpdatedAt());
            statement.setLong(9, videos.getBlockNumber().longValue());
            statement.setString(10, videos.getTxHash());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        HashMap<String, String> pathToCidMapping = readPathToCidMapping();
        HashMap<String, String> usernameToAddressMapping = readUsernameToAddressMapping();
        ResultSet videoMetadatas = getVideoMetadata();
        int a = 0;
        List<String> titles = new ArrayList<>(1000);
        while (videoMetadatas.next()) {
//            if(a>100) break;
            a++;
            VideoMetadata videos = new VideoMetadata();
            videos.setCid(pathToCidMapping.get(videoMetadatas.getString("video_path")));
            videos.setTitle(videoMetadatas.getString("title"));
            videos.setDescription(videoMetadatas.getString("description"));
            videos.setOwner(usernameToAddressMapping.get(videoMetadatas.getString("author")));
            videos.setCoverCid(pathToCidMapping.get(videoMetadatas.getString("cover_path")));
            videos.setSeries(videoMetadatas.getString("series"));
            videos.setSeriesCoverCid(pathToCidMapping.get(videoMetadatas.getString("series_cover_path")));
            videos.setSeriesDescription(videoMetadatas.getString("series_description"));
//            titles.add(videoMetadatas.getString("title"));
            writeToBlockChain(videos);
        }
        System.out.println(titles);
    }




}

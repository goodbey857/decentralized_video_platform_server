package top.kingdon.utils;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class MetamaskUtil {

    public static boolean validate(String signature, String message, String address){
        byte[] messageHashBytes = message.getBytes();
        String r = signature.substring(0, 66);
        String s = "0x"+signature.substring(66, 130);
        String v = "0x"+signature.substring(130, 132);
        System.out.println();
        byte[] msgBytes = new byte[ messageHashBytes.length];
        System.arraycopy(messageHashBytes, 0, msgBytes,0, messageHashBytes.length);
        String pubkey = null;
        try {
            pubkey = Sign.signedPrefixedMessageToKey(msgBytes,
                            new Sign.SignatureData(Numeric.hexStringToByteArray(v)[0],
                                    Numeric.hexStringToByteArray(r),
                                    Numeric.hexStringToByteArray(s)))
                    .toString(16);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        String userAddress = formatAddress(Keys.getAddress(pubkey));
        return address.equals(userAddress);

    }

    public static String getMessage(String address) {
        address = formatAddress(address);
        Calendar now = Calendar.getInstance();
        // dateformat 2024-01-30T12:06:48.879Z
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
        String issuedDate = sdf.format(now.getTime());
        now.add(Calendar.MINUTE,5);
        // 5分钟后的时间
        String expirationDate = sdf.format(now.getTime());

        int nonce = new Random().nextInt();

        String message = String.format("localhost wants you to sign in with your Ethereum account:\n%s\n\nSign in to DVideo.\n\nURI: http://localhost\nVersion: 1\nChain ID: 1\nNonce: %d\nIssued At: %s\nExpiration Time: %s", address, nonce, issuedDate, expirationDate);
        return message;
    }

    public static String trimAddressPrefix(String address) {
        return address.startsWith("0x") ? address.substring(2).toUpperCase() : address.toUpperCase();
    }


    public static String ensureAddressPrefix(String address) {
        return "0x".concat(trimAddressPrefix(address));
    }

    public static String formatAddress(String address) {
        return ensureAddressPrefix(address);
    }

    public static void main(String[] args) {
        String message = getMessage("0x523fE72693c9B97EA03E3B6Fe09E47BB81b0B935");
        System.out.println(message);
    }
}

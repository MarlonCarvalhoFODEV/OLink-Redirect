package util;


import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CryptoUtil {

    public static String encryptMD5(byte[] bytes){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            BigInteger hash = new BigInteger(1, md.digest(bytes));
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static String encryptNEO(StringBuilder values){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            BigInteger hash = new BigInteger(1, md.digest(values.toString().getBytes()));

            return hash.toString(16)+(new Random().nextInt(999999999)+new Random().nextInt(777777777));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null ;
    }
}

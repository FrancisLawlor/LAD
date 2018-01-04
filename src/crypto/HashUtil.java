package crypto;


import java.io.DataInputStream;
import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

/*
    Using Daniel J. Bernsteins hash function dqjb
    extremely efficient
 */
public class HashUtil {

    public static String generateHash(byte bytestream[]){
        long hash = 5381;
        int a = 33;
        for(int i = 0; i < bytestream.length; ++i){
            hash = ((hash << 5) + hash) + bytestream[i];
        }
        return Long.toUnsignedString(hash);
    }

    //compares a hash with a files hash
    public static Boolean authenticateHash(byte bytestream[], String hash){
        return (generateHash(bytestream).equals(hash));
    }
    /*
        for testing
    */
    public static void main(String Args[]){
        long j = 2;
        while(j<(long)Math.pow(Math.pow(1048576,2),2)){
            byte bstream[] = new byte[(int)j-2];
            for(int i =0; i<j-2 ;i++) {
                bstream[i] = (byte) (Math.random() * 255);
            }
            System.out.print(j + "  ");
            System.out.println(generateHash(bstream));
            String hash = generateHash(bstream);
            System.out.println(authenticateHash(bstream,hash));
            j *= 2;
        }

    }
}

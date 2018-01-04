import java.io.File;
/*
    Using Daniel J. Bernsteins hash function dqjb
    extremely efficient
 */
public class HasherUtil {

    public static String generateHash(byte bytestream[]){
        long hash = 5381;
        int a = 33;
        for(int i = 0; i < bytestream.length; ++i){
            hash = ((hash << 5) + hash) + bytestream[i];
        }
        return Long.toUnsignedString(hash);
    }
    /*
        for testing
    */
    public static void main(String Args[]){
        byte bstream[] = new byte[(int)Math.pow(2,16)];
        for(int i =0;i<Math.pow(2,16);i++){
            for(int j = 0;j<(int)Math.pow(2,16);j++){
                bstream[j] = (byte)(Math.random() * 127);
            }
            System.out.println(generateHash(bstream));
        }
    }
}

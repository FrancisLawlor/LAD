package crypto;

public class EncryptionUtil {
    private static final int privatekey = 5183;
    /*
    simple assymetric key encryption
     */
    public static byte[] encryptFile(byte file[],int publickey) {
        byte newfile[] = new byte[file.length];
        for (int i = 0; i < file.length; i++) {
            newfile[i] = (byte) (file[i] - (privatekey*publickey));
        }
        return newfile;
    }

    public static byte[] decryptFile(byte file[],int publickey) {
        byte newfile[] = new byte[file.length];
        for (int i = 0; i < file.length; i++) {
            newfile[i] = (byte) (file[i] + (privatekey*publickey));
        }
        return newfile;
    }


    public static void main(String Args[]) {
        byte file[] = {1, 2, 3, 4, 5};

        for (int i = 0; i < 5; i++) {
            System.out.print(file[i]);
        }
        file = encryptFile(file,5387);
        System.out.println();
        for (int i = 0; i < 5; i++) {
            System.out.print(file[i]);
        }
        file = decryptFile(file,5387);
        System.out.println();
        for (int i = 0; i < 5; i++) {
            System.out.print(file[i]);
        }
    }
}

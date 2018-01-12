package tests.peer.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesTest {
    private static final String DATA_DIR = "./data/";
    private static final String FILENAME = "peerLinksTEST.properties";
    private static final String PEER_ONE = "localhost:10001";
    private static final String PEER_TWO = "localhost:10002";
    private static final String PEER_THREE = "localhost:10003";
    private static final String PEER_FOUR = "localhost:10004";
    
    public static void main(String[] args) {
        write();
        read();
        iterate();
        write2();
        read();
        iterate();
    }
    
    private static void write() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            String filename = DATA_DIR + FILENAME;
            output = new FileOutputStream(filename);
            prop.setProperty(PEER_ONE, "1.0");
            prop.setProperty(PEER_TWO, "2.0");
            prop.setProperty(PEER_THREE, "3.0");
            prop.setProperty(PEER_FOUR, "4.0");
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void write2() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            String filename = DATA_DIR + FILENAME;
            output = new FileOutputStream(filename, true);
            prop.setProperty(PEER_FOUR, "55.0");
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void read() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = DATA_DIR + FILENAME;
            input = new FileInputStream(filename);
            prop.load(input);
            System.out.println("Peer1: " + prop.getProperty(PEER_ONE));
            System.out.println("Peer2: " + prop.getProperty(PEER_TWO));
            System.out.println("Peer3: " + prop.getProperty(PEER_THREE));
            System.out.println("Peer4: " + prop.getProperty(PEER_FOUR));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void iterate() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = DATA_DIR + FILENAME;
            input = new FileInputStream(filename);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                System.out.println("Key: " + key + ", Value: " + value);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

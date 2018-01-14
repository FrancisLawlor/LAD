package tests.system;

public class SystemTestOtherPeers {
    private static Thread[] THREADS;
    
    public static void main(String[] args) throws Exception {
        THREADS = new Thread[9];
        THREADS[0] = new Thread(new PeerTwo());
        THREADS[1] = new Thread(new PeerThree());
        THREADS[2] = new Thread(new PeerFour());
        THREADS[3] = new Thread(new PeerFive());
        THREADS[4] = new Thread(new PeerSix());
        THREADS[5] = new Thread(new PeerSeven());
        THREADS[6] = new Thread(new PeerEight());
        THREADS[7] = new Thread(new PeerNine());
        THREADS[8] = new Thread(new PeerTen());
        
        for (Thread thread : THREADS) {
            thread.start();
        }
        
        while (true) {
            Thread.sleep(10);
        }
    }
}

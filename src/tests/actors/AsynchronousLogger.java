package tests.actors;

import java.util.ArrayList;
import java.util.Iterator;

public class AsynchronousLogger implements Iterable<String> {
    private ArrayList<String> loggedMessages;
    
    public AsynchronousLogger() {
        this.loggedMessages = new ArrayList<String>();
    }
    
    public void logMessage(String message) {
        this.loggedMessages.add(message);
    }
    
    public Iterator<String> iterator() {
        return this.loggedMessages.iterator();
    }
}

package tests.actors;

public class ConfirmMessageReceipt {
    private boolean confirmReceipt;
    
    public ConfirmMessageReceipt() {
        this.confirmReceipt = false;
    }
    
    public void setReceipt() {
        this.confirmReceipt = true;
    }
    
    public boolean hasReceipt() {
        return this.confirmReceipt;
    }
}

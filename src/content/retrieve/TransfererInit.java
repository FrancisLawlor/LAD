package content.retrieve;

/**
 * Initialises the Transferer with the info required for the transfer
 *
 */
public class TransfererInit {
    private TransferInfo transferInfo;
    
    public TransfererInit(TransferInfo transferInfo) {
        this.transferInfo = transferInfo;
    }
    
    public TransferInfo getTransferInfo() {
        return this.transferInfo;
    }
}

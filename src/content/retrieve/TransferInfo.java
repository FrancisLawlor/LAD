package content.retrieve;

import peer.core.UniversalId;
import peer.core.UniversalIdResolver;

/**
 * Info required for Transferer transfer
 *
 */
public class TransferInfo {
    private String transferHostIp;
    private int transferPort;
    
    public TransferInfo(UniversalId peerId, String transferPort) {
        this.transferHostIp = UniversalIdResolver.getIdIp(peerId);
        this.transferPort = Integer.getInteger(transferPort);
    }
    
    public String getTransferHostIp() {
        return this.transferHostIp;
    }
    
    public int getTransferPort() {
        return this.transferPort;
    }
}

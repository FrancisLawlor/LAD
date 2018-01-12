package content.retrieve.core;

import peer.frame.core.UniversalId;
import peer.frame.core.UniversalIdResolver;

/**
 * Info required for Transferer transfer
 *
 */
public class TransferInfo {
    private String transferHostIp;
    private int transferPort;
    
    public TransferInfo(UniversalId peerId, String transferPort) {
        this.transferHostIp = UniversalIdResolver.getIdIp(peerId);
        this.transferPort = Integer.parseInt(transferPort);
    }
    
    public String getTransferHostIp() {
        return this.transferHostIp;
    }
    
    public int getTransferPort() {
        return this.transferPort;
    }
}

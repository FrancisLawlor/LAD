package peer.frame.exceptions;

import peer.frame.core.UniversalId;

public class WrongPeerIdException extends RuntimeException {
    private static final long serialVersionUID = -6222170757936689434L;
    
    public WrongPeerIdException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("Request has been routed to wrong Peer Id: " + peerId + " rather than " + actualIdItShouldBe);
    }
}

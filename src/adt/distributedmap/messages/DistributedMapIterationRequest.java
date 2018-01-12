package adt.distributedmap.messages;

import peer.frame.core.ActorMessageType;

/**
 * Requests iteration through all current key-value pairs in the Distributed Map
 *
 */
public class DistributedMapIterationRequest extends DistributedMapRequest {
    public DistributedMapIterationRequest(int requestNum) {
        super(requestNum, null, ActorMessageType.DistributedMapIterationRequest);
    }
}

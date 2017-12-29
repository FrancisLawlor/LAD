package adt.impl;

/**
 * Requests a bucket to check if it contains a key
 *
 */
public class DistributedMapContainsRequest extends DistributedMapRequest {
    
    public DistributedMapContainsRequest(int index, Object k) {
        super(index, k);
    }
}

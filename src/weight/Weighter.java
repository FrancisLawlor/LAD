package weight;

import akka.actor.UntypedActor;

public class Weighter extends UntypedActor {
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WeightRequest) {
            WeightRequest weightRequest = 
                    (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    protected void processWeightRequest(WeightRequest weightRequest) {
        
    }
}

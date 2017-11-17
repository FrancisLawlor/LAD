package weight;

import akka.actor.UntypedActor;

public class WeightAssessor extends UntypedActor {
    private WeightAssessment assessment;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WeightAssessmentRequest) {
            WeightAssessmentRequest weightAssessmentRequest = 
        	    (WeightAssessmentRequest) message;
            this.processWeightAssessmentRequest(weightAssessmentRequest);
        }
        else if (message instanceof WeightResponse) {
            WeightResponse weightResponse = (WeightResponse) message;
            this.processWeightResponse(weightResponse);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    protected void processWeightAssessmentRequest(WeightAssessmentRequest request) {
        
    }
    
    protected void processWeightResponse(WeightResponse response) {
        
    }
}

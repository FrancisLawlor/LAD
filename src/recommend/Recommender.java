package recommend;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import weight.WeightAssessmentRequest;

public class Recommender extends UntypedActor {
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof RecommendationRequest) {
            RecommendationRequest recommendationRequest = 
        	    (RecommendationRequest) message;
            this.processRecommendationRequest(recommendationRequest);
        }
        else if (message instanceof Recommendation) {
            Recommendation recommendation = (Recommendation) message;
            this.processRecommendation(recommendation);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    protected void processRecommendationRequest(RecommendationRequest request) {
        WeightAssessmentRequest weightAssessmentRequest = new WeightAssessmentRequest();
        
        ActorSelection weightAssessor = getContext().actorSelection("user/weightAssessor");
        weightAssessor.tell(weightAssessmentRequest, getSelf());
    }
    
    protected void processRecommendation(Recommendation recommendation) {
        
    }
}

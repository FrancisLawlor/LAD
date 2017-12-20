package content.view;

import core.ActorMessage;
import core.ActorMessageType;
import statemachine.core.StateMachine;

/**
 * Actor Message that initialises viewer
 *
 */
public class ViewerInit extends ActorMessage {
    private StateMachine stateMachine;
    
    public ViewerInit(StateMachine stateMachine) {
        super(ActorMessageType.ViewerInit);
        this.stateMachine = stateMachine;
    }
    
    public StateMachine getStateMachine() {
        return this.stateMachine;
    }
}

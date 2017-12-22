package tests.actors;

import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorTest {

    @Test
    public void testActorCreation() {        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        
        actorSystem.actorOf(Props.create(TestActor.class), "TestActor");
        
        //ActorSelection testActor = 
    }

}

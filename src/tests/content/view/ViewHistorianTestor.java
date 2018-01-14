package tests.content.view;

import java.util.Iterator;

import akka.actor.ActorSelection;
import content.frame.core.Content;
import content.recommend.messages.PeerRecommendationRequest;
import content.view.core.ContentView;
import content.view.core.ViewingTime;
import content.view.messages.RecordContentView;
import content.view.messages.ViewHistoryRequest;
import content.view.messages.ViewHistoryResponse;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class ViewHistorianTestor extends DummyActor {
    private int testNum = 1;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof StartTest) {
            switch (testNum) {
            case 1:
                ContentView contentView = new ContentView(new Content("1", "1", "1", 12), super.peerId);
                contentView.recordView(new ViewingTime(6));
                RecordContentView recordContentView = new RecordContentView(contentView);
                ActorSelection viewHistorian = getContext().actorSelection(ActorPaths.getPathToViewHistorian());
                viewHistorian.tell(recordContentView, getSelf());
                viewHistorian.tell(new ViewHistoryRequest(new PeerRecommendationRequest(new UniversalId("PeerTwo"), new UniversalId("PeerOne"))), getSelf());
                break;
            case 2:
                contentView = new ContentView(new Content("2", "2", "2", 12), super.peerId);
                contentView.recordView(new ViewingTime(9));
                recordContentView = new RecordContentView(contentView);
                viewHistorian = getContext().actorSelection(ActorPaths.getPathToViewHistorian());
                viewHistorian.tell(recordContentView, getSelf());
                viewHistorian.tell(new ViewHistoryRequest(new PeerRecommendationRequest(new UniversalId("PeerTwo"), new UniversalId("PeerOne"))), getSelf());
                break;
            case 3:
                contentView = new ContentView(new Content("3", "3", "3", 12), super.peerId);
                contentView.recordView(new ViewingTime(3));
                recordContentView = new RecordContentView(contentView);
                viewHistorian = getContext().actorSelection(ActorPaths.getPathToViewHistorian());
                viewHistorian.tell(recordContentView, getSelf());
                viewHistorian.tell(new ViewHistoryRequest(new PeerRecommendationRequest(new UniversalId("PeerTwo"), new UniversalId("PeerOne"))), getSelf());
                break;
            case 4:
                contentView = new ContentView(new Content("2", "2", "2", 12), super.peerId);
                contentView.recordView(new ViewingTime(3));
                recordContentView = new RecordContentView(contentView);
                viewHistorian = getContext().actorSelection(ActorPaths.getPathToViewHistorian());
                viewHistorian.tell(recordContentView, getSelf());
                viewHistorian.tell(new ViewHistoryRequest(new PeerRecommendationRequest(new UniversalId("PeerTwo"), new UniversalId("PeerOne"))), getSelf());
                break;
            case 5:
                contentView = new ContentView(new Content("3", "3", "3", 12), super.peerId);
                contentView.recordView(new ViewingTime(9));
                recordContentView = new RecordContentView(contentView);
                viewHistorian = getContext().actorSelection(ActorPaths.getPathToViewHistorian());
                viewHistorian.tell(recordContentView, getSelf());
                viewHistorian.tell(new ViewHistoryRequest(new PeerRecommendationRequest(new UniversalId("PeerTwo"), new UniversalId("PeerOne"))), getSelf());
                break;
            }
            testNum++;
        }
        else if (message instanceof ViewHistoryResponse) {
            ViewHistoryResponse response = (ViewHistoryResponse) message;
            super.logger.logMessage("ViewHistoryResponse received in ViewHistorianTestor");
            Iterator<ContentView> iterator = response.getViewHistory().iterator();
            while (iterator.hasNext()) {
                ContentView contentView = iterator.next();
                super.logger.logMessage("ContentView content: " + contentView.getContent().getId());
                super.logger.logMessage("ContentView score: " + contentView.getScore());
                super.logger.logMessage("ContentView viewing peer: " + contentView.getViewingPeerId().toString());
            }
            super.logger.logMessage("");
        }
    }
}

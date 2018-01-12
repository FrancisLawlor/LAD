package tests.gui.core;

import com.google.gson.Gson;

import content.view.core.ContentView;
import content.view.messages.RecordContentView;
import filemanagement.fileretrieval.MediaFileSaver;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyViewHistorian extends DummyActor {
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof RecordContentView) {
            RecordContentView recordContentView = (RecordContentView) message;
            ContentView contentView = recordContentView.getContentView();
            Gson gson = new Gson();
            String json = gson.toJson(contentView);
            MediaFileSaver.writeMediaFile("TestRating", "txt", json.getBytes());
        }
    }
}

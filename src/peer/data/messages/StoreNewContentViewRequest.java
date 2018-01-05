package peer.data.messages;

import content.view.ContentView;

public class StoreNewContentViewRequest {

    private ContentView contentView;

    public StoreNewContentViewRequest(ContentView contentView) {
        this.contentView = contentView;
    }

    public ContentView getContentView() {
        return contentView;
    }

}

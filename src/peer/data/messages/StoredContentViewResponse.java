package peer.data.messages;

import content.view.ContentView;

public class StoredContentViewResponse {

    private ContentView contentView;

    public StoredContentViewResponse(ContentView contentView) {
        this.contentView = contentView;
    }

    public ContentView getContentView() {
        return contentView;
    }

}

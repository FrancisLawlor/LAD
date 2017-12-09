package content.content;

/**
 * Contains information about a piece of content
 * Content Id is a hash of the digital file to uniquely identify it
 *
 */
public class Content {
    private String uniqueId;
    
    public String getId() {
        return this.uniqueId;
    }
    
    public int getViewLength() {
        return 0;
    }
}

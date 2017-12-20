package content;

/**
 * Contains information about a piece of content
 * Content Id is a hash of the digital file to uniquely identify it
 *
 */
public class Content {
    private String uniqueId;
    private String fileName;
    private String fileFormat;
    
    public Content(String uniqueId, String fileName, String fileFormat) {
    		this.uniqueId = uniqueId;
    		this.fileName = fileName;
    		this.fileFormat = fileFormat;
    }
    
    public String getId() {
        return this.uniqueId;
    }
    
    public String getFileName() {
    		return this.fileName;
    }
    
    public String getFileFormat() {
    		return this.fileFormat;
    }
    
    public int getViewLength() {
        return 0;
    }
}

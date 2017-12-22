package content.impl;

/**
 * Contains information about a piece of content
 * Content Id is a hash of the digital file to uniquely identify it
 *
 */
public class Content {
    private String uniqueId;
    private String fileName;
    private String fileFormat;
    private String fileLocation;
    private int viewLength;
    
    public Content(String uniqueId, String fileName, String fileFormat, String fileLocation, int viewLength) {
	    this.uniqueId = uniqueId;
	    this.fileName = fileName;
	    this.fileFormat = fileFormat;
	    this.fileLocation = fileLocation;
	    this.viewLength = viewLength;
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
    
    public String getFileLocation() {
        return this.fileLocation;
    }
    
    public int getViewLength() {
        return this.viewLength;
    }
}

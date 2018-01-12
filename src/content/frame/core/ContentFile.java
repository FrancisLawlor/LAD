package content.frame.core;

/**
 * Actual file of bytes that this content represents
 *
 */
public class ContentFile {
    private Content content;
    private byte[] fileBytes;
    
    public ContentFile(Content content, byte[] fileBytes) {
        this.content = content;
        this.fileBytes = fileBytes;
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public byte[] getBytes() {
        return this.fileBytes;
    }
}

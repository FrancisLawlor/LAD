package peer.frame.exceptions;

import content.frame.core.Content;

/**
 * Content File has content ID mismatch in its header with the requested Content ID that should be stored at that filename
 *
 */
public class ImproperlyStoredContentFileException extends RuntimeException {
    private static final long serialVersionUID = 3670442410265329982L;
    
    public ImproperlyStoredContentFileException(Content shouldBe, Content actuallyIs) {
        super("Improperly stored Content File. Content should be " + shouldBe.getId() + " but is instead: " + actuallyIs.getId());
    }
}

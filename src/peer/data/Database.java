package peer.data;

import content.core.Content;
import content.core.ContentFile;

/**
 * Interface for database
 *
 */
public interface Database {

    void writeFile(ContentFile file);

    boolean checkIfFileExists(Content content);

    ContentFile getFile(Content content);
}

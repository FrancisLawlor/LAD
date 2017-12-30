package peer.data;

import content.core.Content;
import content.core.ContentFile;
import content.view.ContentViewAddition;

/**
 * Interface for database
 *
 */
public interface Database {

    void writeFile(ContentFile file);

    void appendToHeader(ContentViewAddition viewAddition);

    boolean checkIfFileExists(Content content);

    ContentFile getFile(Content content);
}

package peer.data;

import content.core.Content;
import content.core.ContentFile;
import content.view.ContentView;
import content.view.ContentViewAddition;
import peer.core.UniversalId;
import peer.data.wrappers.SimilarPeerContentWrapper;
import peer.graph.weight.Weight;

import java.util.Map;
import java.util.Set;

/**
 * Interface for database
 *
 */
public interface Database {

    void writeFile(ContentFile file);

    void appendToHeader(ContentViewAddition viewAddition);

    boolean checkIfFileExists(Content content);

    ContentFile getFile(Content content);

    void storePeerLink(UniversalId id);

    void addPeerLinkWeight(UniversalId id, Weight weight);

    Set <UniversalId> getAllPeerLinks();

    Map <UniversalId, Weight> getAllPeerLinkWeights();

    void storeSimilarPeerContent(Content content, Set <UniversalId> peers);

    SimilarPeerContentWrapper getAllStoredSimilarPeerContent();

    void storeNewContentViewInHistory(ContentView contentView);

    Set <ContentView> getAllContentViewsFromHistory();

}

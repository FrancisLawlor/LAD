package tests.peer.communicate;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import content.core.Content;
import content.recommend.PeerRecommendation;
import peer.core.UniversalId;

public class TestJson {
    private static Gson gson;
    
    @BeforeClass
    public static void executedOnceBeforeAll() {
        gson = new Gson();
    }
    
    private PeerRecommendation getTestPeerRecommendation() {
        PeerRecommendation peerRecommendation;
        
        UniversalId origin = new UniversalId("origin");
        UniversalId target = new UniversalId("target");
        List<Content> contentList = new LinkedList<Content>();

        contentList.add(new Content("abc", "abc", "file", 1));
        contentList.add(new Content("def", "def", "file", 2));
        contentList.add(new Content("ghi", "ghi", "file", 3));
        peerRecommendation = new PeerRecommendation(contentList, origin, target);
        
        return peerRecommendation;
    }

    @Test
    public void testPeerRecommendationToJson() {
        String expected = "{\"contentList\":[{\"uniqueId\":\"abc\",\"fileName\":\"abc\",\"fileFormat\":\"file\",\"viewLength\":1},{\"uniqueId\":\"def\",\"fileName\":\"def\",\"fileFormat\":\"file\",\"viewLength\":2},{\"uniqueId\":\"ghi\",\"fileName\":\"ghi\",\"fileFormat\":\"file\",\"viewLength\":3}],\"origin\":{\"ipAndPort\":\"origin\"},\"target\":{\"ipAndPort\":\"target\"},\"type\":\"PeerRecommendation\"}";
        PeerRecommendation peerRecommendation = getTestPeerRecommendation();
        
        String jsonString = gson.toJson(peerRecommendation);
        
        assertEquals(jsonString, expected);
    }
    
    @Test
    public void testJsonToPeerRecommendation() {
        PeerRecommendation a = getTestPeerRecommendation();
        
        String jsonString = gson.toJson(a);
        
        PeerRecommendation b = gson.fromJson(jsonString, PeerRecommendation.class);
        
        assertEquals(a.getOriginalRequester().toString(), b.getOriginalRequester().toString());
        assertEquals(a.getOriginalTarget().toString(), b.getOriginalTarget().toString());
        assertEquals(a.getRecommendationAtRank(0).getContentId(), b.getRecommendationAtRank(0).getContentId());
        assertEquals(a.getRecommendationAtRank(0).getContentName(), b.getRecommendationAtRank(0).getContentName());
        assertEquals(a.getRecommendationAtRank(0).getContentType(), b.getRecommendationAtRank(0).getContentType());
        assertEquals(a.getRecommendationAtRank(1).getContentId(), b.getRecommendationAtRank(1).getContentId());
        assertEquals(a.getRecommendationAtRank(1).getContentName(), b.getRecommendationAtRank(1).getContentName());
        assertEquals(a.getRecommendationAtRank(1).getContentType(), b.getRecommendationAtRank(1).getContentType());
        assertEquals(a.getRecommendationAtRank(2).getContentId(), b.getRecommendationAtRank(2).getContentId());
        assertEquals(a.getRecommendationAtRank(2).getContentName(), b.getRecommendationAtRank(2).getContentName());
        assertEquals(a.getRecommendationAtRank(2).getContentType(), b.getRecommendationAtRank(2).getContentType());
    }
    
    
}

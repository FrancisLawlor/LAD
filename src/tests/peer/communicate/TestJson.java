package tests.peer.communicate;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import content.impl.Content;
import content.recommend.PeerRecommendation;
import core.UniversalId;

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
        contentList.add(new Content("abc", "abc", "file"));
        contentList.add(new Content("def", "def", "file"));
        contentList.add(new Content("ghi", "ghi", "file"));
        peerRecommendation = new PeerRecommendation(contentList, origin, target);
        
        return peerRecommendation;
    }

    @Test
    public void testPeerRecommendationToJson() {
        String expected = "{\"contentList\":[{\"uniqueId\":\"abc\",\"fileName\":\"abc\",\"fileFormat\":\"file\"},{\"uniqueId\":\"def\",\"fileName\":\"def\",\"fileFormat\":\"file\"},{\"uniqueId\":\"ghi\",\"fileName\":\"ghi\",\"fileFormat\":\"file\"}],\"origin\":{\"ipAndPort\":\"origin\"},\"target\":{\"ipAndPort\":\"target\"},\"type\":\"PeerRecommendation\"}";
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
        assertEquals(a.getContentAtRank(0).getId(), b.getContentAtRank(0).getId());
        assertEquals(a.getContentAtRank(0).getFileName(), b.getContentAtRank(0).getFileName());
        assertEquals(a.getContentAtRank(0).getFileFormat(), b.getContentAtRank(0).getFileFormat());
        assertEquals(a.getContentAtRank(1).getId(), b.getContentAtRank(1).getId());
        assertEquals(a.getContentAtRank(1).getFileName(), b.getContentAtRank(1).getFileName());
        assertEquals(a.getContentAtRank(1).getFileFormat(), b.getContentAtRank(1).getFileFormat());
        assertEquals(a.getContentAtRank(2).getId(), b.getContentAtRank(2).getId());
        assertEquals(a.getContentAtRank(2).getFileName(), b.getContentAtRank(2).getFileName());
        assertEquals(a.getContentAtRank(2).getFileFormat(), b.getContentAtRank(2).getFileFormat());
    }
    
    
}

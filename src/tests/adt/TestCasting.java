package tests.adt;

import content.frame.core.Content;

public class TestCasting {
    private static Class<?> contentClass;
    private static Class<Content> contentClassExplicit;
    
    public static void main(String[] args) {
        contentClass = Content.class;
        contentClassExplicit = Content.class;
        
        Object contentA = new Content("UID_1", "1", "1", 1);
        Object contentB = new Content("UID_1", "1", "1", 1);
        Object contentC = new Content("UID_2", "2", "2", 2);
        
        System.out.println("A==B: " + equals(contentA, contentB));
        System.out.println("A==C: " + equals(contentA, contentC));
        
        System.out.println("A==B: " + equalsExplicit(contentA, contentB));
        System.out.println("A==C: " + equalsExplicit(contentA, contentC));
    }
    
    public static boolean equals(Object a, Object b) {
        return (contentClass.cast(a)).equals(contentClass.cast(b));
    }
    
    public static boolean equalsExplicit(Object a, Object b) {
        return (contentClassExplicit.cast(a)).equals(contentClassExplicit.cast(b));
    }
}

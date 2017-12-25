package peer.data;

/**
 * Initialises the Database in the Databaser actor's state
 *
 */
public class DatabaserInit {
    private Database db;
    
    public DatabaserInit(Database db) {
        this.db = db;
    }
    
    public Database getDatabase() {
        return this.db;
    }
}

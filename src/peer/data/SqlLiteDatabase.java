package peer.data;

import content.core.Content;
import content.core.ContentFile;
import content.view.ContentView;
import content.view.ContentViewAddition;
import content.view.ContentViews;
import filemanagement.filewrapper.FileUnwrapper;
import com.google.gson.Gson;
import org.apache.commons.lang.SerializationUtils;
import peer.core.UniversalId;
import peer.data.wrappers.SimilarPeerContentWrapper;
import peer.graph.weight.Weight;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SQLLite implementation of the Database
 *
 */
public class SqlLiteDatabase implements Database {

    Connection connection = null;
    static String dbLocation = "files.db";

    public SqlLiteDatabase() throws ClassNotFoundException {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " +
                            "content(" +
                            "contentId TEXT PRIMARY KEY, " +
                            "fileName TEXT," +
                            "fileFormat TEXT," +
                            "viewLength INTEGER" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " +
                            "files(" +
                            "contentId TEXT NOT NULL, " +
                            "data BLOB," +
                            "header BLOB" +
                            "FOREIGN KEY (contentId) REFERENCES content(contentId)" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " +
                            "viewHistory(" +
                            "contentId TEXT NOT NULL, " +
                            "contentView BLOB NOT NULL," +
                            "FOREIGN KEY (contentId) REFERENCES content(contentId)" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " +
                            "similarPeerContent(" +
                            "contentId TEXT NOT NULL, " +
                            "similarPeerId TEXT NOT NULL," +
                            "FOREIGN KEY (contentId) REFERENCES content(contentId)" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " +
                            "peerLinks(" +
                            "peerId TEXT PRIMARY KEY, " +
                            "linkWeight DECIMAL" +
                            ")");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeFile(ContentFile file) {
        Content metadata = file.getContent();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
            PreparedStatement statement = connection.prepareStatement(
                    "insert or ignore into files VALUES (?,?,?,?,?,?)");

            byte[] fileBytes = file.getBytes();
            statement.setString(1, metadata.getId());
            statement.setString(2, metadata.getFileName());
            statement.setString(3, metadata.getFileFormat());
            statement.setInt(4, metadata.getViewLength());
            statement.setBytes(5, FileUnwrapper.extractFileArray(fileBytes));
            statement.setBytes(6, FileUnwrapper.extractHeaderArray(fileBytes));

            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void appendToHeader(ContentViewAddition viewAddition) {
        ContentFile file = getFile(viewAddition.getContentView().getContent());

        byte[] headers = FileUnwrapper.extractHeaderArray(file.getBytes());
        String json = new String(headers);
        Gson gson = new Gson();
        ContentViews views = gson.fromJson(json, ContentViews.class);
        views.addContentView(viewAddition.getContentView());

        json = gson.toJson(views);

        try {

            Statement stmt = connection.createStatement();
            String sql = "UPDATE files SET header=" + json.getBytes() + " WHERE fileId=" + file.getContent().getId();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkIfFileExists(Content content) {
        int exists = 0;
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "SELECT EXISTS(SELECT * FROM files WHERE fileId=\"" + content.getId() + "\" LIMIT 1) AS entryExists";
            ResultSet rs = stmt.executeQuery(sql);

            exists = rs.getInt("entryExists");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return exists == 1;
    }

    @Override
    public ContentFile getFile(Content content) {
        ContentFile file = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "SELECT data FROM files WHERE fileId=\"" + content.getId() + "\" LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);

            file = new ContentFile(content, rs.getBytes("data"));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public void storePeerLink(UniversalId id) {
        String peerId = id.toString();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "insert or ignore into peerLinks(peerId) VALUES (" + peerId + ")";

            stmt.execute( sql );
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPeerLinkWeight(UniversalId id, Weight weight) {
        double weightValue = weight.getWeight();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE peerLinks SET linkWeight=? WHERE peerId=?");

            statement.setDouble(1, weightValue);
            statement.setString( 2, id.toString() );

            statement.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<UniversalId> getAllPeerLinks() {

        Set <UniversalId> peerLinks = new HashSet <>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "SELECT peerId FROM peerLinks";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString( "peerId");
                UniversalId uniId = new UniversalId( id );
                peerLinks.add( uniId );
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return peerLinks;
    }

    @Override
    public Map<UniversalId, Weight> getAllPeerLinkWeights() {
        Map <UniversalId, Weight> peerLinksWeights = new HashMap <>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM peerLinks";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString( "peerId");
                Double weightVal = rs.getDouble( "linkWeight" );

                UniversalId uniId = new UniversalId( id );
                Weight weight = new Weight( weightVal );
                peerLinksWeights.put( uniId, weight );
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return peerLinksWeights;
    }

    @Override
    public void storeSimilarPeerContent(Content content, Set <UniversalId> peers) {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            for (UniversalId id : peers) {
                PreparedStatement statement = connection.prepareStatement(
                        "insert or ignore into similarPeerContent VALUES (?,?)");
                statement.setString(1, content.getId());
                statement.setString( 2, id.toString() );

                statement.execute();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SimilarPeerContentWrapper getAllStoredSimilarPeerContent() {
        //TODO: get all content IDs from content table, then get all peers from similapeercontent
        // table
        return null;
    }

    @Override
    public void storeNewContentViewInHistory(ContentView contentView) {
        String id = contentView.getContent().getId();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            PreparedStatement statement = connection.prepareStatement(
                    "insert or replace into viewHistory VALUES (?,?)");

            statement.setString(1, id);
            statement.setBytes( 2, SerializationUtils.serialize( contentView ) );

            statement.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set <ContentView> getAllContentViewsFromHistory() {
        Set <ContentView> contentViews = new HashSet <>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);

            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM viewHistory";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                contentViews.add( (ContentView) SerializationUtils.deserialize( rs.getBytes(
                        "contentView" ) )  );
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contentViews;
    }

}

package peer.data;

import content.core.Content;
import content.core.ContentFile;
import java.sql.*;

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
                            "files(" +
                            "fileId TEXT PRIMARY KEY, " +
                            "fileName TEXT," +
                            "fileFormat TEXT," +
                            "viewLength INTEGER," +
                            "data BLOB" +
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
                    "insert or ignore into files VALUES (?,?,?,?,?)");

            statement.setString(1, metadata.getId());
            statement.setString(2, metadata.getFileName());
            statement.setString(3, metadata.getFileFormat());
            statement.setInt(4, metadata.getViewLength());
            statement.setBytes(5, file.getBytes());

            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
}

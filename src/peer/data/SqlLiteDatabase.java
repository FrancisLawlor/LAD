package peer.data;

import content.core.Content;
import content.core.ContentFile;
import content.view.ContentView;
import content.view.ContentViewAddition;
import content.view.ContentViews;
import filemanagement.filewrapper.FileUnwrapper;
import com.google.gson.Gson;

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
                            "data BLOB," +
                            "header BLOB" +
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
}

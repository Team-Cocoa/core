package kr.teamcocoa.core.mysql;

import kr.teamcocoa.core.mysql.pool.ConnectionPool;
import kr.teamcocoa.core.utils.AsyncDetector;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

@Getter
public class MySQL {

    private static String host = "localhost";
    private static String user = "root";
    private static String password = "root";
    private static String port = "3306";

    private static Connection getConnection(String dbName) throws SQLException {
        // 0 : host
        // 1 : port
        // 2 : db
        return DriverManager.getConnection(MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", host, port, dbName), user, password);
    }

    private String dbName;
    private Connection connection;

    @Getter
    private ConnectionPool relatedPool;

    private long lastTransactionStamp;

    public MySQL(String dbName) {
        this(dbName, null);
    }

    public MySQL(String dbName, ConnectionPool relatedPool) {
        this.dbName = dbName;
        this.relatedPool = relatedPool;
        this.lastTransactionStamp = System.currentTimeMillis();
    }

    public void connect() {
        if (!isConnected()) {
            try {
                this.connection = getConnection(dbName);
                System.out.println("[MySQL] Successfully connected to MySQL (Database : " + dbName + ")!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[MySQL] Failed to connect to MySQL (Database : " + dbName + ")!");
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                System.out.println("[MySQL] Successfully disconnected (Database : " + dbName + ")!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("[MySQL] Failed to disconnect (Database : " + dbName + ")!");
            }
        }
    }

    public boolean update(String query) {
        if(isConnected()) {
            try(PreparedStatement preparedStatement = getPreparedStatement(query)) {
                this.lastTransactionStamp = System.currentTimeMillis();
                return preparedStatement.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(String query, Object... placeholders) {
        if(isConnected()) {
            try(PreparedStatement preparedStatement = getPreparedStatement(query, placeholders)) {
                this.lastTransactionStamp = System.currentTimeMillis();
                return preparedStatement.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(String query, PlaceHolder placeholders) {
        if(isConnected()) {
            try(PreparedStatement preparedStatement = getPreparedStatement(query, placeholders)) {
                this.lastTransactionStamp = System.currentTimeMillis();
                return preparedStatement.execute();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public PreparedStatement getPreparedStatement(String query) {
        AsyncDetector.catchSynchronous();
        if(isConnected()) {
            try {
                this.lastTransactionStamp = System.currentTimeMillis();
                return connection.prepareStatement(query);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String query, Object... placeholders) {
        AsyncDetector.catchSynchronous();
        if(isConnected()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                for (int i = 0; i < placeholders.length; i++) {
                    preparedStatement.setObject(i + 1, placeholders[i]);
                }
                this.lastTransactionStamp = System.currentTimeMillis();
                return preparedStatement;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String query, PlaceHolder placeholders) {
        AsyncDetector.catchSynchronous();
        if(isConnected()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                List<Object> placeholderList = placeholders.getPlaceholders();
                for (int i = 0; i < placeholderList.size(); i++) {
                    preparedStatement.setObject(i + 1, placeholderList.get(i));
                }
                this.lastTransactionStamp = System.currentTimeMillis();
                return preparedStatement;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

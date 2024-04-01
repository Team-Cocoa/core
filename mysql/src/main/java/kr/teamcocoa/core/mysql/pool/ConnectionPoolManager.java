package kr.teamcocoa.core.mysql.pool;

import kr.teamcocoa.core.mysql.MySQL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionPoolManager {

    private static ScheduledExecutorService keepAliveTask = Executors.newSingleThreadScheduledExecutor();

    private static ConcurrentHashMap<String, ConnectionPool> connectionPoolMap = new ConcurrentHashMap<>();

    @Getter
    private static boolean keepAliveRunning = false;

    public static ConnectionPool createConnectionPool(String name, String dbName, int initSize, int maxSize) {
        ConnectionPool connectionPool = new ConnectionPool(dbName, initSize, maxSize);
        connectionPool.init();
        connectionPoolMap.put(name, connectionPool);
        return connectionPool;
    }

    public static void removePool(String name) {
        connectionPoolMap.remove(name);
    }

    public static boolean hasPool(String name) {
        return connectionPoolMap.containsKey(name);
    }

    public static ConnectionPool getPool(String name) {
        return connectionPoolMap.getOrDefault(name, null);
    }

    public static Collection<ConnectionPool> getAllPools() {
        return connectionPoolMap.values();
    }

    public static void disconnectAllConnections() {
        for (ConnectionPool connectionPool : connectionPoolMap.values()) {
            for (MySQL connection : connectionPool.getConnections()) {
                connection.disconnect();
            }
        }
    }

    public static void startKeepAlive() {
        if(keepAliveRunning) {
            return;
        }
        keepAliveRunning = true;
        keepAliveTask.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (ConnectionPool connectionPool : connectionPoolMap.values()) {
                for (MySQL connection : connectionPool.getConnections()) {
                    if(currentTime - connection.getLastTransactionStamp() > TimeUnit.MINUTES.toMillis(30)) {
                        try(PreparedStatement preparedStatement = connection.getPreparedStatement("select 1");
                            ResultSet rs = preparedStatement.executeQuery()) {
                            System.out.println("[MySQL] KeepAlive success for " + connection.getDbName() + ".");
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("[MySQL] KeepAlive failed for " + connection.getDbName() + ".");
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    public static void stopKeepAlive() {
        if(!keepAliveRunning) {
            return;
        }
        keepAliveTask.shutdown();
    }

}


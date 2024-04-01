package kr.teamcocoa.core.mysql;

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
public class MySQLManager {

    private static ScheduledExecutorService keepAliveTask = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private static boolean keepAliveRunning = false;

    // keepAliveTask 가 다른 스레드에서 돌아가서 raceCondition 방지하려면 concurrentHashMap 쓰는게 맞는거 같음
    private static ConcurrentHashMap<String, MySQL> connectionHashMap = new ConcurrentHashMap<>();

    public static MySQL createConnection(String dbName) {
        if(hasConnection(dbName)) {
            return null;
        }
        MySQL mySQL = new MySQL(dbName);
        connectionHashMap.put(dbName, mySQL);
        return mySQL;
    }

    public static boolean hasConnection(String dbName) {
        return connectionHashMap.containsKey(dbName);
    }

    public static MySQL getConnection(String dbName) {
        return connectionHashMap.getOrDefault(dbName, null);
    }

    public static void removeConnection(String dbName) {
        connectionHashMap.remove(dbName);
    }

    public static Collection<MySQL> getAllConnections() {
        return connectionHashMap.values();
    }

    public static void disconnectAllConnections() {
        for (MySQL mySQL : getAllConnections()) {
            mySQL.disconnect();
        }
    }

    public static void startKeepAlive() {
        if(keepAliveRunning) {
            return;
        }
        keepAliveRunning = true;
        keepAliveTask.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (MySQL mySQL : getAllConnections()) {
                if(currentTime - mySQL.getLastTransactionStamp() > TimeUnit.MINUTES.toMillis(30)) {
                    try(PreparedStatement preparedStatement = mySQL.getPreparedStatement("select 1");
                        ResultSet rs = preparedStatement.executeQuery()) {
                        System.out.println("[MySQL] KeepAlive success for " + mySQL.getDbName() + ".");
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("[MySQL] KeepAlive failed for " + mySQL.getDbName() + ".");
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


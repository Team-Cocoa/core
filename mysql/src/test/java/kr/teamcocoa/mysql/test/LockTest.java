package kr.teamcocoa.mysql.test;

import kr.teamcocoa.core.mysql.MySQL;
import kr.teamcocoa.core.mysql.pool.ConnectionPool;
import kr.teamcocoa.core.mysql.pool.ConnectionPoolManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("테이블 락 테스트")
public class LockTest {

    ConnectionPool connectionPool;
    String dbName = "freefight";
    ExecutorService service;

    AtomicBoolean failed = new AtomicBoolean(false);
    String message;

    @BeforeEach
    void 테스트_준비() {
        connectionPool = ConnectionPoolManager.createConnectionPool(
                "testPool",
                dbName,
                5,
                10
        );
        service = Executors.newFixedThreadPool(10);
        MySQL mySQL = connectionPool.getConnection();
        mySQL.update("delete from test_lock;");
        mySQL.update("ALTER TABLE test_lock AUTO_INCREMENT = 1");
        connectionPool.returnConnection(mySQL);
    }

    @AfterEach
    void 테스트_종료() {
        service.shutdown();
        ConnectionPoolManager.disconnectAllConnections();
        ConnectionPoolManager.removePool("testPool");
    }

    @Test
    @DisplayName("Primary Key 중복 처리 테스트")
    void 중복_처리_테스트() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                MySQL mySQL = connectionPool.getConnection();
                boolean locked = lockDatabase(mySQL);
                if(!locked) {
                    if(!failed.get()) {
                        failed.set(true);
                        message = "Lock Failed";
                    }
                }
                int preId = getId(mySQL);
                mySQL.update("insert into test_lock values();");
                int postId = getId(mySQL);
                boolean unlocked = unlockDatabase(mySQL);
                if(!unlocked) {
                    if(!failed.get()) {
                        failed.set(true);
                        message = "UnLock Failed";
                    }
                }
                connectionPool.returnConnection(mySQL);
                if(postId != preId + 1) {
                    if(!failed.get()) {
                        failed.set(true);
                        message = "postId and preId isn't match";
                    }
                }
                if(postId == 10) {
                    future.complete(null);
                }
            });
        }
        try {
            future.get(10, TimeUnit.SECONDS);
            if(failed.get()) {
                fail(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getId(MySQL mySQL) {
        try(PreparedStatement preparedStatement = mySQL.getPreparedStatement("SELECT IFNULL(MAX(id), 0) AS id FROM test_lock;");
            ResultSet resultSet = preparedStatement.executeQuery()) {
            if(resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean lockDatabase(MySQL mySQL) {
        try(    PreparedStatement preparedStatement = mySQL.getPreparedStatement("SELECT GET_LOCK(?, 30) as `lock`;", "simpleLock");
                ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("lock") == 1;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean unlockDatabase(MySQL mySQL) {
        try(    PreparedStatement preparedStatement = mySQL.getPreparedStatement("SELECT RELEASE_LOCK(?) as `lock`;", "simpleLock");
                ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("lock") == 1;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

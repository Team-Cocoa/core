package kr.teamcocoa.mysql.test;

import kr.teamcocoa.core.mysql.MySQL;
import kr.teamcocoa.core.mysql.MySQLManager;
import kr.teamcocoa.core.mysql.pool.ConnectionPool;
import kr.teamcocoa.core.mysql.pool.ConnectionPoolManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("커넥션 풀 테스트")
class ConnectionPoolTest {

    /*
     * org.junit 에 있는 어노테이션 들을 import 하면
     * 테스트 케이스가 작동하지 않는 현상이 JUnit5 에 있는거 같음...
     * org.junit.jupiter.api 에 있는 어노테이션 들을 사용하도록
     */

    private static ConnectionPool connectionPool;
    private static String dbName = "freefight";

    @BeforeEach
    void 테스트_준비() {
        connectionPool = ConnectionPoolManager.createConnectionPool(
                "testPool",
                dbName,
                5,
                10
        );
    }

    @AfterEach
    void 테스트_종료() {
        ConnectionPoolManager.disconnectAllConnections();
        ConnectionPoolManager.removePool("testPool");
    }

    @Test
    @DisplayName("큐 사이즈 테스트")
    void 큐_사이즈_테스트() {
        LinkedList<MySQL> list = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            list.add(connectionPool.getConnection());
        }

        assertEquals(6, connectionPool.getCurrentSize());

        for (int i = 0; i < 4; i++) {
            list.add(connectionPool.getConnection());
        }

        assertEquals(10, connectionPool.getCurrentSize());

        for (int i = 0; i < 6; i++) {
            MySQL mySQL = list.poll();
            connectionPool.returnConnection(mySQL);
        }

        assertEquals(9, connectionPool.getCurrentSize());

    }

    @Test
    @DisplayName("큐 대기시간 테스트")
    void 큐_대기시간_테스트() {
        LinkedList<MySQL> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(connectionPool.getConnection());
        }

        long timestamp = System.currentTimeMillis();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            MySQL mySQL = list.poll();
            connectionPool.returnConnection(mySQL);
        }, 1, TimeUnit.SECONDS);

        list.add(connectionPool.getConnection());

        assertTrue(System.currentTimeMillis() - timestamp >= 1000);
    }

    @Test
    @DisplayName("커넥션 연결 관계 테스트")
    void 커넥션_연결_관계_테스트() {
        MySQL newMySQL = MySQLManager.createConnection("freefight");

        MySQL mySQL = connectionPool.getConnection();
        mySQL.disconnect();

        assertThrows(IllegalArgumentException.class, () -> {
            connectionPool.returnConnection(newMySQL);
        });

        connectionPool.returnConnection(mySQL);

        assertEquals(5, connectionPool.getCurrentSize());

        newMySQL.disconnect();
    }

}

package kr.teamcocoa.core.mysql.pool;

import kr.teamcocoa.core.mysql.MySQL;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    private int initialSize;

    private int maximumSize;
    private String dbName;

    @Getter(AccessLevel.PROTECTED)
    private ArrayList<MySQL> connections;

    @Getter(AccessLevel.PROTECTED)
    private Deque<MySQL> queue;

    private Queue<CompletableFuture<MySQL>> waitingConnectionPool;

    protected ConnectionPool(String dbName, int initialSize, int maximumSize) {
        this.dbName = dbName;
        this.initialSize = initialSize;
        this.maximumSize = maximumSize;
        this.queue = new LinkedBlockingDeque<>(maximumSize);
        this.connections = new ArrayList<>(maximumSize);
        this.waitingConnectionPool = new LinkedBlockingQueue<>();
    }

    public void init() {
        for (int i = 0; i < initialSize; i++) {
            registerNewConnection();
        }
    }

    public int getCurrentSize() {
        return connections.size();
    }

    private MySQL registerNewConnection() {
        if(connections.size() >= maximumSize) {
            throw new IllegalStateException("Queue is full! We can't add more connection.");
        }
        MySQL mySQL = new MySQL(this.dbName, this);
        mySQL.connect();
        connections.add(mySQL);
        if(waitingConnectionPool.isEmpty()) {
            queue.add(mySQL);
        }
        else {
            checkWaitingConnection(mySQL);
        }
        return mySQL;
    }

    private void removeConnection(MySQL mySQL) {
        if(mySQL.isConnected() && connections.size() <= initialSize) {
            throw new IllegalStateException("We can't remove more connection.");
        }
        if(!connections.contains(mySQL)) {
            throw new IllegalStateException("This connection isn't part of this pool!");
        }
        connections.remove(mySQL);
        mySQL.disconnect();
    }

    private void checkWaitingConnection(MySQL mySQL) {
        if(!waitingConnectionPool.isEmpty()) {
            CompletableFuture<MySQL> completableFuture = waitingConnectionPool.poll();
            completableFuture.complete(mySQL);
        }
    }

    public MySQL getConnection() {
        // 큐가 비어있다면 새로운 커넥션이 반납 될때 까지 무한 대기
        // 이러면 CompletableFuture 를 쓰는 이유가 없는건 맞는데
        // 그냥 Future 인스턴스를 얻을 수 있는 방법을 모루겄어
        // 미안하지만 그냥 이거 쓸래
        if(queue.size() == 0) {
            try {
                CompletableFuture<MySQL> future = new CompletableFuture<>();
                waitingConnectionPool.add(future);
                if(connections.size() < maximumSize) {
                    registerNewConnection();
                }
                return future.get();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        // 일단 정석대로 구현하자
        return queue.pollLast();
    }

    public void returnConnection(MySQL mySQL) {
        if(mySQL.getRelatedPool() != this) {
            throw new IllegalArgumentException("This connection isn't related with this pool!");
        }
        if(!mySQL.isConnected()) {
            removeConnection(mySQL);
            if(queue.size() < this.initialSize) {
                registerNewConnection();
            }
            return;
        }
        if(queue.contains(mySQL)) {
            throw new IllegalStateException("This connection is already in pool!");
        }
        if(!waitingConnectionPool.isEmpty()) {
            CompletableFuture<MySQL> completableFuture = waitingConnectionPool.poll();
            completableFuture.complete(mySQL);
        }
        else {
            queue.add(mySQL);
            if(queue.size() > this.initialSize) {
                removeConnection(mySQL);
            }
        }
    }
}

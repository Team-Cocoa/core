package kr.teamcocoa.core.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoDB {

    private final static String ADDRESS = "localhost";
    private final static int PORT = 27017;

    private static List<MongoClient> connections = new LinkedList<>();

    public static void disconnectDatabases() {
        for (MongoClient connection : connections) {
            try {
                connection.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static MongoClient getConnection() {
        MongoClient connection = MongoClients.create("mongodb://" + ADDRESS + ":" + PORT);
        connections.add(connection);
        return connection;
    }
}

package kr.teamcocoa.core.mongo;

import com.mongodb.MongoClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoDB {

    private final static String ADDRESS = "localhost";
    private final static int PORT = 27017;

    @Getter
    private static MongoClient connection;

    public static void connectDatabase() {
        if(connection != null) {
            return;
        }
        connection = new MongoClient(ADDRESS, PORT);
    }

    public static void disconnectDatabase() {
        if(connection == null) {
            return;
        }
        try {
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package kr.teamcocoa.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String stringify(JsonObject object) {
        return gson.toJson(object);
    }

    public static JsonObject parse(String string) {
        return gson.fromJson(string, JsonObject.class);
    }

}

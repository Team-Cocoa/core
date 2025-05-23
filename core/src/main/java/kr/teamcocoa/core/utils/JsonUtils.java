package kr.teamcocoa.core.utils;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.leangen.geantyref.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static String stringify(JsonObject object) {
        return gson.toJson(object);
    }

    public static JsonObject parse(String string) {
        return gson.fromJson(string, JsonObject.class);
    }
    
    public static JsonArray listToJsonArray(List list) {
        return gson.toJsonTree(list).getAsJsonArray();
    }

    public static <T> List<T> jsonArrayToList(JsonArray jsonArray) {
        return gson.fromJson(jsonArray, new TypeToken<T>() {}.getType());
    }

}

package kr.teamcocoa.core.network.head;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class HeadSearchDto {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("properties")
    private JsonArray properties;

}

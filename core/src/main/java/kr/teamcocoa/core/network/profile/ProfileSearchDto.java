package kr.teamcocoa.core.network.profile;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileSearchDto {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("properties")
    private JsonArray properties;

}

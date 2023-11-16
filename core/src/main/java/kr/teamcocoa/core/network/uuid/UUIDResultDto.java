package kr.teamcocoa.core.network.uuid;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UUIDResultDto {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("errorMessage")
    private String errorMessage;

}

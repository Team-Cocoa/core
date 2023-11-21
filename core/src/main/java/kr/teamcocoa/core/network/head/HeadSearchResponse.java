package kr.teamcocoa.core.network.head;

import com.google.gson.JsonObject;
import kr.teamcocoa.core.network.controllers.mojang.SessionMojangController;
import kr.teamcocoa.retrofit.IResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public class HeadSearchResponse implements IResponse<HeadSearchDto> {

    private UUID uuid;
    private CompletableFuture<String> completableFuture;

    @Override
    public void onResponse(Call<HeadSearchDto> call, Response<HeadSearchDto> response) {
        if(response.isSuccessful()) {
            HeadSearchDto result = response.body();
            JsonObject property = result.getProperties().get(0).getAsJsonObject();
            String value = property.get("value").getAsString();

            if (SessionMojangController.getUuidToHeadValueCache().readData(uuid) == null) {
                SessionMojangController.getUuidToHeadValueCache().createData(uuid, value);
            }

            completableFuture.complete(value);
        }
        else {
            throw new IllegalStateException("Head API Failed! status : " + response.code());
        }
    }

}

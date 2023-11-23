package kr.teamcocoa.core.network.profile;

import com.google.gson.JsonObject;
import kr.teamcocoa.core.network.controllers.mojang.SessionMojangController;
import kr.teamcocoa.core.network.model.MojangProfile;
import kr.teamcocoa.retrofit.IResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public class ProfileSearchResponse implements IResponse<ProfileSearchDto> {

    private UUID uuid;
    private CompletableFuture<MojangProfile> completableFuture;

    @Override
    public void onResponse(Call<ProfileSearchDto> call, Response<ProfileSearchDto> response) {
        if(response.isSuccessful()) {
            ProfileSearchDto result = response.body();
            JsonObject property = result.getProperties().get(0).getAsJsonObject();
            String headValue = property.get("value").getAsString();

            MojangProfile profile = new MojangProfile(uuid, headValue, result.getName());

            if (SessionMojangController.getUuidToProfileCache().readData(uuid) == null) {
                SessionMojangController.getUuidToProfileCache().createData(uuid, profile);
            }

            completableFuture.complete(profile);
        }
        else {
            throw new IllegalStateException("Head API Failed! status : " + response.code());
        }
    }

}

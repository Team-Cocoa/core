package kr.teamcocoa.core.network.uuid;

import kr.teamcocoa.core.network.controllers.mojang.ApiMojangController;
import kr.teamcocoa.retrofit.IResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Response;

import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
public class UUIDSearchResponse implements IResponse<UUIDResultDto> {

    private String name;

    private CompletableFuture<String> completableFuture;

    @Override
    public void onResponse(Call<UUIDResultDto> call, Response<UUIDResultDto> response) {
        if(response.isSuccessful()) {
            UUIDResultDto result = response.body();
            String uuidString = result.getId().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");
            completableFuture.complete(uuidString);
            if(ApiMojangController.getNameToUUIDCache().readData(name) == null) {
                ApiMojangController.getNameToUUIDCache().createData(name, uuidString);
            }
        }
        else {
            completableFuture.complete(null);
            throw new IllegalStateException("Failed to search!");
        }
    }
}

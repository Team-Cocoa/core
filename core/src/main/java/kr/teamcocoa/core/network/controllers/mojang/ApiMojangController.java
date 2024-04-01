package kr.teamcocoa.core.network.controllers.mojang;

import kr.teamcocoa.core.network.cache.Cache;
import kr.teamcocoa.core.network.uuid.UUIDResultDto;
import kr.teamcocoa.core.network.uuid.UUIDSearchResponse;
import kr.teamcocoa.core.network.uuid.UUIDSearchService;
import kr.teamcocoa.core.retrofit.CallBackAdapter;
import kr.teamcocoa.core.retrofit.RetrofitFactory;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ApiMojangController {

    private static Retrofit retrofit = RetrofitFactory.getRetrofit("https://api.mojang.com/");

    private static UUIDSearchService uuidSearchService = retrofit.create(UUIDSearchService.class);

    @Getter
    private static Cache<String, String> nameToUUIDCache = new Cache<>(1, TimeUnit.HOURS);

    public static CompletableFuture<String> searchUUIDByName(String name) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String cachedUUIDString = nameToUUIDCache.readData(name);
        if(cachedUUIDString != null) {
            completableFuture.complete(cachedUUIDString);
            return completableFuture;
        }
        CallBackAdapter<UUIDResultDto> callBackAdapter = new CallBackAdapter<>();
        callBackAdapter.setIResponse(new UUIDSearchResponse(name, completableFuture));
        callBackAdapter.setIFailure((call, throwable) -> throwable.printStackTrace());
        Call<UUIDResultDto> call = uuidSearchService.getProfileInfo(name);
        call.enqueue(callBackAdapter);
        return completableFuture;
    }

}

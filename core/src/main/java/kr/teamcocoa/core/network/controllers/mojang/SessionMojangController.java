package kr.teamcocoa.core.network.controllers.mojang;

import kr.teamcocoa.core.network.cache.Cache;
import kr.teamcocoa.core.network.head.HeadSearchDto;
import kr.teamcocoa.core.network.head.HeadSearchResponse;
import kr.teamcocoa.core.network.head.HeadSearchService;
import kr.teamcocoa.retrofit.CallBackAdapter;
import kr.teamcocoa.retrofit.RetrofitFactory;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SessionMojangController {

    private static Retrofit retrofit = RetrofitFactory.getRetrofit("https://sessionserver.mojang.com/");

    private static HeadSearchService headSearchService = retrofit.create(HeadSearchService.class);

    @Getter
    private static Cache<UUID, String> uuidToHeadValueCache = new Cache<>(1, TimeUnit.DAYS);

    public static CompletableFuture<String> searchHeadByUUID(UUID uuid) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String cachedHeadValue = uuidToHeadValueCache.readData(uuid);
        if(cachedHeadValue != null) {
            completableFuture.complete(cachedHeadValue);
        }
        else {
            CallBackAdapter<HeadSearchDto> callBackAdapter = new CallBackAdapter<>();
            callBackAdapter.setIResponse(new HeadSearchResponse(uuid, completableFuture));
            callBackAdapter.setIFailure(((call, throwable) -> throwable.printStackTrace()));
            Call<HeadSearchDto> call = headSearchService.getHeadValue(uuid);
            call.enqueue(callBackAdapter);
        }
        return completableFuture;
    }

}

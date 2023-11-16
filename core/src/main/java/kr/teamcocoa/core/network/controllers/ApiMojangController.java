package kr.teamcocoa.core.network.controllers;

import kr.teamcocoa.core.network.uuid.UUIDResultDto;
import kr.teamcocoa.core.network.uuid.UUIDSearchResponse;
import kr.teamcocoa.core.network.uuid.UUIDSearchService;
import kr.teamcocoa.retrofit.CallBackAdapter;
import kr.teamcocoa.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.concurrent.CompletableFuture;

public class ApiMojangController {

    private static Retrofit retrofit = RetrofitFactory.getRetrofit("https://api.mojang.com/");

    private static UUIDSearchService uuidSearchService = retrofit.create(UUIDSearchService.class);

    public static CompletableFuture<String> searchUUIDByName(String name) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        CallBackAdapter<UUIDResultDto> callBackAdapter = new CallBackAdapter<>();
        callBackAdapter.setIResponse(new UUIDSearchResponse(completableFuture));
        callBackAdapter.setIFailure((call, throwable) -> throwable.printStackTrace());
        Call<UUIDResultDto> call = uuidSearchService.getProfileInfo(name);
        call.enqueue(callBackAdapter);
        return completableFuture;
    }

}

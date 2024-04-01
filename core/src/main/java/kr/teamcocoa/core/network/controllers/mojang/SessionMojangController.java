package kr.teamcocoa.core.network.controllers.mojang;

import kr.teamcocoa.core.network.cache.Cache;
import kr.teamcocoa.core.network.profile.ProfileSearchDto;
import kr.teamcocoa.core.network.profile.ProfileSearchResponse;
import kr.teamcocoa.core.network.profile.ProfileSearchService;
import kr.teamcocoa.core.network.model.MojangProfile;
import kr.teamcocoa.core.retrofit.CallBackAdapter;
import kr.teamcocoa.core.retrofit.RetrofitFactory;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SessionMojangController {

    private static Retrofit retrofit = RetrofitFactory.getRetrofit("https://sessionserver.mojang.com/");

    private static ProfileSearchService profileSearchService = retrofit.create(ProfileSearchService.class);

    @Getter
    private static Cache<UUID, MojangProfile> uuidToProfileCache = new Cache<>(6, TimeUnit.HOURS);

    public static CompletableFuture<MojangProfile> searchProfileByUUID(UUID uuid) {
        CompletableFuture<MojangProfile> completableFuture = new CompletableFuture<>();
        MojangProfile profile = uuidToProfileCache.readData(uuid);
        if(profile != null) {
            completableFuture.complete(profile);
        }
        else {
            CallBackAdapter<ProfileSearchDto> callBackAdapter = new CallBackAdapter<>();
            callBackAdapter.setIResponse(new ProfileSearchResponse(uuid, completableFuture));
            callBackAdapter.setIFailure(((call, throwable) -> throwable.printStackTrace()));
            Call<ProfileSearchDto> call = profileSearchService.getHeadValue(uuid);
            call.enqueue(callBackAdapter);
        }
        return completableFuture;
    }

}

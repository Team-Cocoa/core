package kr.teamcocoa.core.network.profile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.UUID;

public interface ProfileSearchService {

    @GET("session/minecraft/profile/{uuid}")
    Call<ProfileSearchDto> getHeadValue(@Path("uuid") UUID uuid);

}

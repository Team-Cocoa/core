package kr.teamcocoa.core.network.head;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.UUID;

public interface HeadSearchService {

    @GET("session/minecraft/profile/{uuid}")
    Call<HeadSearchDto> getHeadValue(@Path("uuid") UUID uuid);

}

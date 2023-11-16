package kr.teamcocoa.core.network.uuid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UUIDSearchService {

    @GET("/users/profiles/minecraft/{name}")
    Call<UUIDResultDto> getProfileInfo(@Path("name") String name);

}

package kr.teamcocoa.core.retrofit;

import retrofit2.Call;
import retrofit2.Response;

public interface IResponse<T> {

    void onResponse(Call<T> call, Response<T> response);

}

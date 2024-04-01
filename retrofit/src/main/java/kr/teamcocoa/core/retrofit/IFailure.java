package kr.teamcocoa.core.retrofit;

import retrofit2.Call;

public interface IFailure<T> {

    void onFailure(Call<T> call, Throwable t);

}

package kr.teamcocoa.core.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBackAdapter<T> implements Callback<T> {

    private IResponse<T> iResponse;
    private IFailure<T> iFailure;

    public void setIFailure(IFailure<T> iFailure) {
        this.iFailure = iFailure;
    }

    public void setIResponse(IResponse<T> iResponse) {
        this.iResponse = iResponse;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        iResponse.onResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        iFailure.onFailure(call, t);
    }
}

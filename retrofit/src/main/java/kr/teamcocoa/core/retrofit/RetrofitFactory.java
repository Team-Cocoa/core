package kr.teamcocoa.core.retrofit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrofitFactory {

    public static Retrofit getRetrofit(String baseURL) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit(RetrofitOptions options) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(options.getConnectTimeout())
                .readTimeout(options.getReadTimeout())
                .writeTimeout(options.getWriteTimeout());

        String token = options.getToken();
        if (token != null) {
            client.addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build()));
        }

        return new Retrofit.Builder()
                .baseUrl(options.getBaseUrl())
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <T> T create(RetrofitOptions options, Class<T> service) {
        return getRetrofit(options).create(service);
    }

}

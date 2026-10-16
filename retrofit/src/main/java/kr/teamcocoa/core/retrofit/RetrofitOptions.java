package kr.teamcocoa.core.retrofit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrofitOptions {

    private final String baseUrl;
    private final String token;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final Duration writeTimeout;

    public static Builder builder(@NonNull String baseUrl) {
        return new Builder(baseUrl);
    }

    public static class Builder {

        private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

        private final String baseUrl;
        private String token;
        private Duration connectTimeout = DEFAULT_TIMEOUT;
        private Duration readTimeout = DEFAULT_TIMEOUT;
        private Duration writeTimeout = DEFAULT_TIMEOUT;

        private Builder(String baseUrl) {
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        }

        public Builder token(String token) {
            this.token = token == null || token.isEmpty() ? null : token;
            return this;
        }

        public Builder timeout(@NonNull Duration timeout) {
            this.connectTimeout = timeout;
            this.readTimeout = timeout;
            this.writeTimeout = timeout;
            return this;
        }

        public Builder connectTimeout(@NonNull Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(@NonNull Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(@NonNull Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public RetrofitOptions build() {
            return new RetrofitOptions(baseUrl, token, connectTimeout, readTimeout, writeTimeout);
        }

    }

}

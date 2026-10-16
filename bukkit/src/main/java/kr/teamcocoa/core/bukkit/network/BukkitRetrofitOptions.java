package kr.teamcocoa.core.bukkit.network;

import kr.teamcocoa.core.retrofit.RetrofitOptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BukkitRetrofitOptions {

    /**
     * Reads {@code base-url}, {@code token} and {@code timeout-seconds} (default 5).
     */
    public static RetrofitOptions fromConfig(ConfigurationSection section) {
        String baseUrl = section.getString("base-url");
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("Missing " + section.getCurrentPath() + ".base-url");
        }
        return RetrofitOptions.builder(baseUrl)
                .token(section.getString("token"))
                .timeout(Duration.ofSeconds(section.getInt("timeout-seconds", 5)))
                .build();
    }

}

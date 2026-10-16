package kr.teamcocoa.core.bukkit.rank;

import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * Format placeholders: {tag}, {prefix}, {display}, {suffix}, {name} and {message}. The tag comes
 * from the tag provider, which is called from the async chat thread.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatFormatService {

    public static final String DEFAULT_FORMAT = "{tag}{prefix}{name} &8> &f{message}";

    public static final String DEFAULT_COLOR_PERMISSION = "teamcocoa.premium";

    @Getter
    @Setter
    private static volatile boolean enabled;

    @Getter
    @Setter
    @NonNull
    private static volatile String format = DEFAULT_FORMAT;

    @Getter
    @Setter
    @NonNull
    private static volatile String colorPermission = DEFAULT_COLOR_PERMISSION;

    private static volatile Function<Player, String> tagProvider = player -> "";

    public static void load(ConfigurationSection config) {
        if (config == null) {
            return;
        }
        enabled = config.getBoolean("enabled", enabled);
        format = config.getString("format", format);
        colorPermission = config.getString("color-permission", colorPermission);
    }

    public static void setTagProvider(@NonNull Function<Player, String> provider) {
        tagProvider = provider;
    }

    /**
     * @return a format for {@code AsyncPlayerChatEvent#setFormat}, with the message as {@code %2$s}
     */
    public static String format(Player player) {
        Rank rank = RankService.getRank(player);
        String tag = tagProvider.apply(player);
        return StringUtils.color(format
                        .replace("{tag}", escape(tag == null ? "" : tag))
                        .replace("{prefix}", escape(rank.getPrefix()))
                        .replace("{display}", escape(rank.getDisplay()))
                        .replace("{suffix}", escape(rank.getSuffix()))
                        .replace("{name}", escape(player.getName())))
                .replace("{message}", "%2$s");
    }

    public static String formatMessage(Player player, String message) {
        return player.hasPermission(colorPermission) ? StringUtils.color(message) : message;
    }

    private static String escape(String text) {
        return text.replace("%", "%%");
    }

}

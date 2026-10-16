package kr.teamcocoa.core.bukkit.rank;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RankService {

    public static final String PERMISSION_PREFIX = "teamcocoa.rank.";

    private static final Rank FALLBACK_RANK = new Rank("default", 999, null, "&7", "&7", "");

    private static volatile List<Rank> ranks = Collections.emptyList();

    private static volatile Rank defaultRank = FALLBACK_RANK;

    /**
     * Reads {@code ranks.<name>.sort-id/permission/prefix/display/suffix} and {@code default-rank}.
     * A rank without a permission uses {@code teamcocoa.rank.<name>}.
     */
    public static void load(ConfigurationSection config) {
        List<Rank> loaded = new ArrayList<>();
        Rank loadedDefault = FALLBACK_RANK;
        String defaultName = config.getString("default-rank", FALLBACK_RANK.getName());

        ConfigurationSection section = config.getConfigurationSection("ranks");
        if (section != null) {
            for (String name : section.getKeys(false)) {
                ConfigurationSection rankSection = section.getConfigurationSection(name);
                if (rankSection == null) {
                    continue;
                }
                Rank rank = new Rank(
                        name,
                        rankSection.getInt("sort-id", FALLBACK_RANK.getSortId()),
                        rankSection.getString("permission", PERMISSION_PREFIX + name),
                        rankSection.getString("prefix", ""),
                        rankSection.getString("display", ""),
                        rankSection.getString("suffix", ""));
                if (name.equalsIgnoreCase(defaultName)) {
                    loadedDefault = rank;
                } else {
                    loaded.add(rank);
                }
            }
        }

        loaded.sort(Comparator.comparingInt(Rank::getSortId));
        ranks = Collections.unmodifiableList(loaded);
        defaultRank = loadedDefault;
    }

    public static Rank getRank(Player player) {
        for (Rank rank : ranks) {
            if (player.hasPermission(rank.getPermission())) {
                return rank;
            }
        }
        return defaultRank;
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public static Rank getDefaultRank() {
        return defaultRank;
    }

}

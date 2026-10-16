package kr.teamcocoa.core.bukkit.rank;

import kr.teamcocoa.core.bukkit.scoreboard.PlayerScoreboards;
import kr.teamcocoa.core.bukkit.utils.TextLimit;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * One team per player on every viewer's own scoreboard. Teams are named by rank sort-id and then
 * player name, which is the order the tab list shows them in. Main thread only.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NameTagService {

    private static final int MAX_SORT_ID = 999;

    private static final Map<UUID, NameTag> nameTags = new HashMap<>();

    private static Function<Player, String> suffixProvider = player -> null;

    /**
     * Returns the suffix for a player, or null to use the rank suffix.
     */
    public static void setSuffixProvider(@NonNull Function<Player, String> provider) {
        suffixProvider = provider;
    }

    public static void update(Player target) {
        Rank rank = RankService.getRank(target);

        String suffix = suffixProvider.apply(target);
        NameTag nameTag = new NameTag(
                teamName(target, rank),
                TextLimit.TEAM_AFFIX.truncate(StringUtils.color(rank.getPrefix())),
                TextLimit.TEAM_AFFIX.truncate(StringUtils.color(suffix != null ? suffix : rank.getSuffix())));

        NameTag previous = nameTags.put(target.getUniqueId(), nameTag);
        if (previous != null && !previous.team.equals(nameTag.team)) {
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                unregisterTeam(viewer.getScoreboard(), previous.team);
            }
        }

        Scoreboard targetBoard = PlayerScoreboards.get(target);
        for (Player other : Bukkit.getOnlinePlayers()) {
            applyNameTag(PlayerScoreboards.get(other), nameTag, target.getName());

            NameTag otherTag = nameTags.get(other.getUniqueId());
            if (otherTag != null && other != target) {
                applyNameTag(targetBoard, otherTag, other.getName());
            }
        }

        target.setDisplayName(StringUtils.color(rank.getDisplay()) + target.getName());
    }

    public static void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            update(player);
        }
    }

    public static void remove(Player target) {
        NameTag nameTag = nameTags.remove(target.getUniqueId());
        if (nameTag == null) {
            return;
        }
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer != target) {
                unregisterTeam(viewer.getScoreboard(), nameTag.team);
            }
        }
    }

    private static void applyNameTag(Scoreboard scoreboard, NameTag nameTag, String entry) {
        Team team = scoreboard.getTeam(nameTag.team);
        if (team == null) {
            team = scoreboard.registerNewTeam(nameTag.team);
        }
        if (!nameTag.prefix.equals(team.getPrefix())) {
            team.setPrefix(nameTag.prefix);
        }
        if (!nameTag.suffix.equals(team.getSuffix())) {
            team.setSuffix(nameTag.suffix);
        }
        if (!team.hasEntry(entry)) {
            team.addEntry(entry);
        }
    }

    private static void unregisterTeam(Scoreboard scoreboard, String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
    }

    private static String teamName(Player target, Rank rank) {
        int maxLength = TextLimit.TEAM_NAME.getLegacyLength();
        int sortId = Math.max(0, Math.min(MAX_SORT_ID, rank.getSortId()));
        String base = String.format("%03d", sortId) + target.getName();
        if (base.length() > maxLength) {
            base = base.substring(0, maxLength);
        }

        String name = base;
        int counter = 0;
        while (isTakenByOther(name, target.getUniqueId())) {
            String suffix = Integer.toString(counter++, Character.MAX_RADIX);
            name = base.substring(0, Math.min(base.length(), maxLength - suffix.length())) + suffix;
        }
        return name;
    }

    private static boolean isTakenByOther(String teamName, UUID owner) {
        for (Map.Entry<UUID, NameTag> entry : nameTags.entrySet()) {
            if (!entry.getKey().equals(owner) && entry.getValue().team.equals(teamName)) {
                return true;
            }
        }
        return false;
    }

    private record NameTag(String team, String prefix, String suffix) {
    }

}

package kr.teamcocoa.core.bukkit.scoreboard;

import kr.teamcocoa.core.bukkit.utils.TextLimit;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Flicker-free sidebar: only scores that changed are rewritten.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sidebar {

    private static final String OBJECTIVE_NAME = "tc_sidebar";

    public static void render(Player player, String title, List<String> lines) {
        Scoreboard scoreboard = PlayerScoreboards.get(player);

        String coloredTitle = TextLimit.OBJECTIVE_TITLE.truncate(StringUtils.color(title));
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, "dummy");
        }
        if (!coloredTitle.equals(objective.getDisplayName())) {
            objective.setDisplayName(coloredTitle);
        }
        if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        Set<String> entries = new HashSet<>();
        int score = lines.size() - 1;
        for (String line : lines) {
            String entry = uniqueEntry(entries, TextLimit.SCOREBOARD_ENTRY.truncate(StringUtils.color(line)));
            entries.add(entry);

            Score current = objective.getScore(entry);
            if (!current.isScoreSet() || current.getScore() != score) {
                current.setScore(score);
            }
            score--;
        }

        for (String entry : new ArrayList<>(scoreboard.getEntries())) {
            if (!entries.contains(entry) && objective.getScore(entry).isScoreSet()) {
                scoreboard.resetScores(entry);
            }
        }
    }

    public static void clear(Player player) {
        Objective objective = player.getScoreboard().getObjective(OBJECTIVE_NAME);
        if (objective != null) {
            objective.unregister();
        }
    }

    // Each entry is a key on the scoreboard, so repeated lines (blank lines especially) need to
    // differ; a trailing reset code is invisible
    private static String uniqueEntry(Set<String> entries, String entry) {
        if (entry.isEmpty()) {
            entry = " ";
        }
        while (entries.contains(entry)) {
            entry = entry + "§r";
        }
        return entry;
    }

}

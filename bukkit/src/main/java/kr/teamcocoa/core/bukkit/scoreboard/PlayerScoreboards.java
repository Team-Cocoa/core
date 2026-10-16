package kr.teamcocoa.core.bukkit.scoreboard;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * The sidebar and the name tag teams both live on each player's own scoreboard, so neither
 * replaces the other.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerScoreboards {

    public static Scoreboard get(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }
        return scoreboard;
    }

}

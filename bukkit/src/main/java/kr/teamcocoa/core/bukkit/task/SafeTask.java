package kr.teamcocoa.core.bukkit.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SafeTask {

    public static BukkitTask timer(Plugin plugin, String name, Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimer(plugin, wrap(plugin, name, task), delayTicks, periodTicks);
    }

    public static BukkitTask timerAsync(Plugin plugin, String name, Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, wrap(plugin, name, task), delayTicks, periodTicks);
    }

    /**
     * Logs failures with the task name instead of letting them escape. A ScheduledExecutorService
     * cancels a repeating task for good once it throws.
     */
    public static Runnable wrap(Plugin plugin, String name, Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Throwable throwable) {
                plugin.getLogger().log(Level.SEVERE, "Task '" + name + "' failed", throwable);
            }
        };
    }

}

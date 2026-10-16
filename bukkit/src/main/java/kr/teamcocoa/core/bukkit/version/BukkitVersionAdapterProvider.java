package kr.teamcocoa.core.bukkit.version;

import com.github.retrooper.packetevents.manager.server.ServerVersion;

public interface BukkitVersionAdapterProvider {

    boolean isSupported(ServerVersion serverVersion);

    BukkitVersionAdapter create();

}

package kr.teamcocoa.core.bukkit.legacy;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapterProvider;

public class LegacyVersionAdapterProvider implements BukkitVersionAdapterProvider {

    @Override
    public boolean isSupported(ServerVersion serverVersion) {
        return serverVersion.isOlderThan(ServerVersion.V_1_13);
    }

    @Override
    public BukkitVersionAdapter create() {
        return new LegacyVersionAdapter();
    }

}

package kr.teamcocoa.core.bukkit.modern;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapterProvider;

public class ModernVersionAdapterProvider implements BukkitVersionAdapterProvider {

    @Override
    public boolean isSupported(ServerVersion serverVersion) {
        return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13);
    }

    @Override
    public BukkitVersionAdapter create() {
        return new ModernVersionAdapter();
    }

}

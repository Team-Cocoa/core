package kr.teamcocoa.core.bukkit.data;

public class PlayerDataLoadException extends RuntimeException {

    public PlayerDataLoadException(String kickMessage) {
        super(kickMessage);
    }

    public PlayerDataLoadException(String kickMessage, Throwable cause) {
        super(kickMessage, cause);
    }

}

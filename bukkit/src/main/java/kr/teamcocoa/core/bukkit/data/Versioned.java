package kr.teamcocoa.core.bukkit.data;

public interface Versioned {

    boolean isEdited();

    long getRevision();

    /**
     * Clears the edited flag only if nothing changed since the given revision was read.
     */
    void markSaved(long revision);

}

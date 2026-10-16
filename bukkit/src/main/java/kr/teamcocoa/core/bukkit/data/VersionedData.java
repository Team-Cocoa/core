package kr.teamcocoa.core.bukkit.data;

/**
 * Subclasses that are changed on the main thread and saved from other threads should make their
 * own accessors synchronized as well, so they share this monitor.
 */
public abstract class VersionedData implements Versioned {

    private boolean edited;

    private long revision;

    @Override
    public synchronized boolean isEdited() {
        return edited;
    }

    @Override
    public synchronized long getRevision() {
        return revision;
    }

    @Override
    public synchronized void markSaved(long revision) {
        if (this.revision == revision) {
            edited = false;
        }
    }

    protected synchronized void markEdited() {
        edited = true;
        revision++;
    }

}

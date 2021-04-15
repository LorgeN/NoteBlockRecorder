package org.tanberg.noteblockrecorder.playback;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Utility class for playing back a previously recorded song as a bukkit task.
 */
public class NoteBlockSongPlayback extends BukkitRunnable {

    private final Location location;
    private final NoteBlockSong song;
    private final int maxTick;
    private int currentTick;

    /**
     * Creates a new playback instance at the given location for the given song
     *
     * @param location The location. Can be null to play everywhere.
     * @param song     The song to play
     */
    public NoteBlockSongPlayback(Location location, NoteBlockSong song) {
        this.location = location;
        this.song = song;
        this.maxTick = this.song.getDuration();
    }

    /**
     * Starts a task to play back this song
     *
     * @param plugin The plugin to start the task from
     * @return The started task
     */
    public BukkitTask start(JavaPlugin plugin) {
        return this.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * @return If the song has been completed
     */
    public boolean isComplete() {
        return this.currentTick > this.maxTick;
    }

    /**
     * Plays the next note
     */
    public void nextNote() {
        this.song.getNotes(this.currentTick++).forEach(note -> note.playAt(this.location));
    }

    @Override
    public void run() {
        if (this.isComplete()) {
            this.cancel();
            return;
        }

        this.nextNote();
    }
}

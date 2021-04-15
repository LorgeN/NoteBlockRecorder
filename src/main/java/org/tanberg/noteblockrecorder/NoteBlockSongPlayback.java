package org.tanberg.noteblockrecorder;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NoteBlockSongPlayback extends BukkitRunnable {

    private final Location location;
    private final NoteBlockSong song;
    private final int maxTick;
    private int currentTick;

    public NoteBlockSongPlayback(Location location, NoteBlockSong song) {
        this.location = location;
        this.song = song;
        this.maxTick = this.song.getDuration();
    }

    public BukkitTask start(JavaPlugin plugin) {
        return this.runTaskTimer(plugin, 0L, 1L);
    }

    public boolean isComplete() {
        return this.currentTick > this.maxTick;
    }

    public void nextNote() {
        this.nextNote(this.location);
    }

    public void nextNote(Location location) {
        this.song.getNotes(this.currentTick++).forEach(note -> note.playAt(location));
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

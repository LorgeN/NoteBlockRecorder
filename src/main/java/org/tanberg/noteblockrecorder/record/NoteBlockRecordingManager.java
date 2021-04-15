package org.tanberg.noteblockrecorder.record;

import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.tanberg.noteblockrecorder.NoteBlockRecorder;
import org.tanberg.noteblockrecorder.playback.NoteBlockSong;

import java.io.File;

/**
 * Manager for recording new songs. Listens for {@link NotePlayEvent} and records.
 */
public class NoteBlockRecordingManager implements Listener {

    private final File folder;
    private int startTick;
    private NoteBlockSong currentSong;

    /**
     * Creates a new manager instance
     *
     * @param plugin The plugin instance
     */
    public NoteBlockRecordingManager(NoteBlockRecorder plugin) {
        this.folder = new File(plugin.getDataFolder(), "songs");
        this.folder.mkdirs();
    }

    /**
     * @return If a song is currently being recorded
     */
    public boolean isRecording() {
        return this.currentSong != null;
    }

    /**
     * Starts a new recording
     */
    public void startRecording() {
        this.startTick = -1;
        this.currentSong = new NoteBlockSong();
        System.out.println("Starting new note block recording!");
    }

    /**
     * Stops the current recording and saves it in the song folder under the given name
     *
     * @param name The name of the file to save to
     */
    public void stopRecording(String name) {
        this.currentSong.writeToFile(new File(this.folder, name + ".json"));
        this.currentSong = null;
        System.out.println("Recording stopped!");
    }

    @EventHandler
    public void onNoteblockPlay(NotePlayEvent event) {
        if (!this.isRecording()) {
            return;
        }

        if (this.startTick == -1) {
            this.startTick = MinecraftServer.currentTick;
        }

        int currentTick = MinecraftServer.currentTick - this.startTick;
        Note note = event.getNote();
        Instrument instrument = event.getInstrument();
        System.out.println(
            "Recording note @ " + currentTick + ": " + instrument.name() + " - " + note);
        this.currentSong.addNote(currentTick, instrument, note);
    }
}

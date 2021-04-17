package org.tanberg.noteblockrecorder.playback;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Instrument;
import org.bukkit.Note;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * A recorded song, or a song currently being recorded
 */
public class NoteBlockSong {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Multimap<Integer, NoteBlockNote> notes;

    /**
     * Creates a new empty song instance
     */
    public NoteBlockSong() {
        this.notes = HashMultimap.create();
    }

    /**
     * Creates a new song instance and reads the song from the given {@link InputStream input
     * stream}
     *
     * @param in The input stream to read from
     */
    public NoteBlockSong(InputStream in) {
        this();

        if (in == null) {
            throw new IllegalArgumentException("InputStream can't be null!");
        }

        NoteBlockNote[] notes = GSON.fromJson(
            new InputStreamReader(new BufferedInputStream(in)), NoteBlockNote[].class);

        for (NoteBlockNote note : notes) {
            this.notes.put(note.getTick(), note);
        }
    }

    /**
     * Writes the data of this song to the given file in JSON format
     *
     * @param file The file to write to
     */
    public void writeToFile(File file) {
        try (FileWriter out = new FileWriter(file)) {
            out.write(GSON.toJson(this.getNotes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all notes to be played at the specific tick. Useful for playback purposes.
     *
     * @param tick The tick
     * @return All notes to be played at the specific tick
     */
    public Collection<NoteBlockNote> getNotes(int tick) {
        return this.notes.get(tick);
    }

    /**
     * @return All notes in this song
     */
    public Collection<NoteBlockNote> getNotes() {
        return this.notes.values();
    }

    /**
     * @return The duration of this song in ticks
     */
    public int getDuration() {
        return this.getNotes().stream()
            .max(Comparator.comparing(NoteBlockNote::getTick))
            .orElseThrow(() -> new IllegalStateException("Empty song!")).getTick();
    }

    /**
     * Adds a new note to this song
     *
     * @param tick       The tick to play the note at
     * @param instrument The instrument to play the note with
     * @param note       The note
     */
    public void addNote(int tick, Instrument instrument, Note note) {
        this.notes.put(tick, new NoteBlockNote(tick, instrument, note));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof NoteBlockSong)) {
            return false;
        }

        NoteBlockSong that = (NoteBlockSong) o;
        return Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notes);
    }

    @Override
    public String toString() {
        // Using size instead of showing the actual notes because that could be absolutely massive.
        // I recorded a few songs and they all had several thousand notes.
        return "NoteBlockSong{" +
            "notes=" + notes.size() +
            '}';
    }
}

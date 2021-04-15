package org.tanberg.noteblockrecorder;

import com.google.common.collect.ArrayListMultimap;
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

public class NoteBlockSong {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Multimap<Integer, NoteBlockNote> notes;

    public NoteBlockSong() {
        this.notes = ArrayListMultimap.create();
    }

    public void fromResource(String path) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new NullPointerException("InputStream is null!");
            }

            NoteBlockNote[] notes = GSON.fromJson(
                new InputStreamReader(new BufferedInputStream(in)), NoteBlockNote[].class);

            for (NoteBlockNote note : notes) {
                this.notes.put(note.getTick(), note);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(File file) {
        try (FileWriter out = new FileWriter(file)) {
            out.write(GSON.toJson(this.getNotes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<NoteBlockNote> getNotes(int tick) {
        return this.notes.get(tick);
    }

    public Collection<NoteBlockNote> getNotes() {
        return this.notes.values();
    }

    public int getDuration() {
        return this.getNotes().stream()
            .min(Comparator.comparing(NoteBlockNote::getTick))
            .orElseThrow(() -> new IllegalStateException("Empty song!")).getTick();
    }

    public void addNote(int tick, Instrument instrument, Note note) {
        this.notes.put(tick, new NoteBlockNote(tick, instrument, note));
    }
}

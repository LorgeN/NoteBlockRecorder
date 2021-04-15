package org.tanberg.noteblockrecorder.playback;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * A single note of a given instrument at a specific tick of a song
 */
public class NoteBlockNote {

    private final int tick;
    private final Instrument instrument;
    private final Note note;

    /**
     * Creates a new note instance
     *
     * @param tick       The tick to play this note at. Starts at 0 for the first note of the song
     * @param instrument The instrument
     * @param note       The note
     */
    public NoteBlockNote(int tick, Instrument instrument, Note note) {
        this.tick = tick;
        this.instrument = instrument;
        this.note = note;
    }

    /**
     * @return The tick this note is to be played at in the song
     */
    public int getTick() {
        return tick;
    }

    /**
     * @return The instrument to play this note with
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * @return The note
     */
    public Note getNote() {
        return note;
    }

    /**
     * Plays this note for all players within 32 blocks of the given location. If no location is
     * given, it is played for all players.
     *
     * @param location The location
     */
    public void playAt(Location location) {
        if (location == null) {
            Bukkit.getOnlinePlayers().forEach(
                player -> player.playNote(player.getLocation(), this.instrument, this.note));
            return;
        }

        World world = location.getWorld();
        world.getNearbyEntities(location, 32.0, 32.0, 32.0).stream()
            .filter(Player.class::isInstance)
            .map(Player.class::cast)
            .forEach(player -> player.playNote(location, this.instrument, this.note));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof NoteBlockNote)) {
            return false;
        }

        NoteBlockNote that = (NoteBlockNote) o;
        return tick == that.tick && instrument == that.instrument && Objects.equals(note,
                                                                                    that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tick, instrument, note);
    }

    @Override
    public String toString() {
        return "NoteBlockNote{" +
            "tick=" + tick +
            ", instrument=" + instrument +
            ", note=" + note +
            '}';
    }
}

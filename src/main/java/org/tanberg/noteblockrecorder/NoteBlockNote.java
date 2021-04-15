package org.tanberg.noteblockrecorder;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NoteBlockNote {

    private final int tick;
    private final Instrument instrument;
    private final Note note;

    public NoteBlockNote(int tick, Instrument instrument, Note note) {
        this.tick = tick;
        this.instrument = instrument;
        this.note = note;
    }

    public int getTick() {
        return tick;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Note getNote() {
        return note;
    }

    public void playAt(Location location) {
        World world = location.getWorld();
        world.getNearbyEntities(location, 32.0, 32.0, 32.0).stream()
            .filter(Player.class::isInstance)
            .map(Player.class::cast)
            .forEach(player -> player.playNote(location, this.instrument, this.note));
    }
}

package org.tanberg.noteblockrecorder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.tanberg.noteblockrecorder.command.NoteBlockPlayCommand;
import org.tanberg.noteblockrecorder.command.NoteBlockRecordCommand;
import org.tanberg.noteblockrecorder.record.NoteBlockRecordingManager;

/**
 * Simple plugin for spigot servers to record note block playback to replay using the plugin instead
 * of actual note blocks.
 */
public class NoteBlockRecorder extends JavaPlugin {

    @Override
    public void onEnable() {
        NoteBlockRecordingManager manager = new NoteBlockRecordingManager(this);

        Bukkit.getPluginManager().registerEvents(manager, this);

        Bukkit.getPluginCommand("record").setExecutor(new NoteBlockRecordCommand(manager));
        Bukkit.getPluginCommand("play").setExecutor(new NoteBlockPlayCommand(this));
    }
}

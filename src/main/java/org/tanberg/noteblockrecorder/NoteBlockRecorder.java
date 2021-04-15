package org.tanberg.noteblockrecorder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NoteBlockRecorder extends JavaPlugin {

    @Override
    public void onEnable() {
        NoteBlockRecordingManager manager = new NoteBlockRecordingManager(this);
        Bukkit.getPluginManager().registerEvents(manager, this);
        Bukkit.getPluginCommand("record").setExecutor(new NoteBlockRecordCommand(manager));
    }
}

package org.tanberg.noteblockrecorder.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.tanberg.noteblockrecorder.NoteBlockRecorder;
import org.tanberg.noteblockrecorder.playback.NoteBlockSong;
import org.tanberg.noteblockrecorder.playback.NoteBlockSongPlayback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Command for playing back a saved recording from file.
 * <p>
 * Syntax:
 * <ul>
 *     <li>/record - Start a new recording
 *     <li>/record [name] - End the current recording and store under the given name
 * </ul>
 */
public class NoteBlockPlayCommand implements CommandExecutor {

    private final NoteBlockRecorder plugin;
    private final File folder;
    private NoteBlockSongPlayback currentPlayback;

    public NoteBlockPlayCommand(NoteBlockRecorder plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "songs");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please provide a song to play or \"stop\" to " +
                                   "stop the current playback!");
            return false;
        }

        if (this.currentPlayback != null) {
            if (!args[0].equalsIgnoreCase("stop")) {
                sender.sendMessage(ChatColor.RED + "Another song is already playing!");
                return false;
            }

            this.currentPlayback.cancel();
            this.currentPlayback = null;
            sender.sendMessage(ChatColor.GREEN + "Playback stopped!");
            return true;
        }

        File file = new File(this.folder, args[0] + ".json");
        if (!file.exists()) {
            sender.sendMessage(ChatColor.RED + "No recording by that name found!");
            return false;
        }

        try (InputStream in = new FileInputStream(file)) {
            NoteBlockSong song = new NoteBlockSong(in);
            this.currentPlayback = new NoteBlockSongPlayback(null, song);
            this.currentPlayback.start(this.plugin);
            sender.sendMessage(ChatColor.GREEN + "Playback started successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred!");
        }

        return false;
    }
}

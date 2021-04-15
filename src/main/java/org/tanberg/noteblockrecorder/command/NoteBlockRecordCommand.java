package org.tanberg.noteblockrecorder.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.tanberg.noteblockrecorder.record.NoteBlockRecordingManager;

/**
 * Command for starting and stopping a recording.
 * <p>
 * Syntax:
 * <ul>
 *     <li>/play [name] - Start playback of the given recording
 *     <li>/play stop - Stop the current playback
 * </ul>
 */
public class NoteBlockRecordCommand implements CommandExecutor {

    private final NoteBlockRecordingManager manager;

    public NoteBlockRecordCommand(NoteBlockRecordingManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.manager.isRecording()) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please provide a name!");
                return false;
            }

            this.manager.stopRecording(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Recording saved!");
            return true;
        }

        this.manager.startRecording();
        sender.sendMessage(ChatColor.GREEN + "Now recording!");
        return false;
    }
}

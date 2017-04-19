package com.myththewolf.MythBans.commands.discord;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import com.myththewolf.MythBans.lib.discord.MythCommandExecute;
import com.myththewolf.MythBans.tasks.LogWatcher;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class ReloadConsoleEngine implements MythCommandExecute{
	private int TaskId;
	private JavaPlugin thePlugin;
	public ReloadConsoleEngine(int task,JavaPlugin thePL) {
		TaskId = task;
		thePlugin = thePL;
	}

	@Override
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		theMessage.reply("Reloading ConsoleLogWatcher engine...");
		Bukkit.getScheduler().cancelTask(TaskId);
		Bukkit.getScheduler().runTaskAsynchronously(thePlugin, new LogWatcher());
		theMessage.reply("Engine reloaded.");
	}

	@Override
	public boolean requiresRoot() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean requiresLinked() {
		// TODO Auto-generated method stub
		return false;
	}

}

package com.myththewolf.MythBans.commands.discord;

import org.bukkit.OfflinePlayer;

import com.myththewolf.MythBans.lib.discord.MythCommandExecute;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Ping implements MythCommandExecute{

	public Ping() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void runCommand(User theDiscordUser, OfflinePlayer theBukkitUser, String[] args, Message theMessage) {
		theMessage.reply("Pong!");
	}

	@Override
	public boolean requiresRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresLinked() {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}

package com.myththewolf.MythBans.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;

public class CallUserName extends BukkitCommand{
	private String calledName;
	private String calledUUID;
	private DatabaseCommands dbc;
	
	public CallUserName(String name) {
		super(name);
		calledName = name;
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

}
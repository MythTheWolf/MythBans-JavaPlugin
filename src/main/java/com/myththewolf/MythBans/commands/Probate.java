package com.myththewolf.MythBans.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.feilds.DataCache;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class Probate implements CommandExecutor {
	private PlayerCache pCache = new PlayerCache(MythSQLConnect.getConnection());

	private MythPlayer playerClass;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		try {
			if (args.length < 1) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Usage: /probate <user> [reason]");
				return true;
			}
			if (pCache.getOfflinePlayerExact(args[0]) == null) {
				sender.sendMessage(ConfigProperties.PREFIX + ChatColor.RED + "Player has not been on this server.");
				return true;
			}
			if (!sender.hasPermission(ConfigProperties.PROBATION_PERMISSION)) {
				sender.sendMessage(
						ConfigProperties.PREFIX + ChatColor.RED + "You do not have permission for that command.");
				return true;
			}
			playerClass = DataCache.getPlayerInstance(pCache.getUUID(args[0]));

			if (playerClass.getProbate()) {
				if (sender instanceof ConsoleCommandSender) {
					playerClass.setProbate(false);
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Unprobated "
							+ pCache.getName(pCache.getUUID(args[0])));
					DataCache.rebuildUser(pCache.getUUID(args[0]));
					return true;
				} else {
					playerClass.setProbate(false);
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Unprobated "
							+ pCache.getName(pCache.getUUID(args[0])));
					DataCache.rebuildUser(pCache.getUUID(args[0]));
					return true;
				}
			} else {

				if (sender instanceof ConsoleCommandSender) {
					playerClass.setProbate(true);
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Set "
							+ pCache.getName(playerClass.getId()) + " on probation.");
					DataCache.rebuildUser(pCache.getUUID(args[0]));
					return true;
				} else {
					playerClass.setProbate(true);
					DataCache.rebuildUser(playerClass.getId());
					sender.sendMessage(ConfigProperties.PREFIX + ChatColor.GOLD + "Set "
							+ pCache.getName(pCache.getUUID(args[0])) + " on probation.");
					return true;
				}

			}

		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}

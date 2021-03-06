package com.myththewolf.MythBans.tasks;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.myththewolf.MythBans.lib.SQL.DatabaseCommands;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.player.MythPlayer;
import com.myththewolf.MythBans.lib.player.PlayerCache;

public class ImportJSON extends BukkitRunnable {

	private final CommandSender sender;

	private final DatabaseCommands dbc = new DatabaseCommands();
	private final PlayerCache PC = new PlayerCache(MythSQLConnect.getConnection());

	public ImportJSON(JavaPlugin plugin, CommandSender se) {
		this.sender = se;
	}

	@Override
	public void run() {
		JSONParser parser = new JSONParser();
		long count = 0;
		long bypass = 0;
		String eName = "undefined";
		try {

			Object obj = parser.parse(new FileReader("banned-players.json"));

			JSONArray ROOT = (JSONArray) obj;
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> i = ROOT.iterator();
			while (i.hasNext()) {
				JSONObject object = i.next();
				String UUID2 = object.get("uuid").toString();
				object.get("created").toString();
				String name = object.get("name").toString();
				String source = "CONSOLE";
				String expires = object.get("expires").toString();
				String reason = object.get("reason").toString();
				eName = name;
				ArrayList<String> checked = new ArrayList<String>();
				if (PC.getName(UUID2) == null) {
					if (!checked.contains(UUID2)) {
						MythPlayer.processNewUser(UUID2, name);

					}
				}

				MythPlayer MP = new MythPlayer(UUID2);
				if (expires.equals("forever") && !MP.getStatus().equals("banned")) {
					sender.sendMessage("UUID: " + UUID2);
					sender.sendMessage("NAME: " + name);
					sender.sendMessage("EXPIRES: " + expires);
					sender.sendMessage("REASON: " + reason);
					dbc.banUser(UUID2, source, reason);
					count++;
				} else {
					bypass++;
				}

			}
		} catch (Exception e) {
			sender.sendMessage("Error while reading user data! Username: " + eName);

		}
		sender.sendMessage("Imported " + count + " entries. (skipped " + bypass + ")");
	}

}
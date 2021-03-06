package com.myththewolf.MythBans.lib;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.myththewolf.MythBans.Startup;
import com.myththewolf.MythBans.commands.Ban;
import com.myththewolf.MythBans.commands.ClearChat;
import com.myththewolf.MythBans.commands.Dump;
import com.myththewolf.MythBans.commands.IPBan;
import com.myththewolf.MythBans.commands.IPKick;
import com.myththewolf.MythBans.commands.Kick;
import com.myththewolf.MythBans.commands.Mute;
import com.myththewolf.MythBans.commands.PardonIP;
import com.myththewolf.MythBans.commands.PardonUser;
import com.myththewolf.MythBans.commands.PlayerTime;
import com.myththewolf.MythBans.commands.Potato;
import com.myththewolf.MythBans.commands.Probate;
import com.myththewolf.MythBans.commands.ReloadMythBans;
import com.myththewolf.MythBans.commands.SocialSpy;
import com.myththewolf.MythBans.commands.TempBan;
import com.myththewolf.MythBans.commands.UpdateXenForo;
import com.myththewolf.MythBans.commands.UpgradeTables;
import com.myththewolf.MythBans.commands.createUI;
import com.myththewolf.MythBans.commands.getFam;
import com.myththewolf.MythBans.commands.importJSON;
import com.myththewolf.MythBans.commands.mbfix;
import com.myththewolf.MythBans.commands.mythapi;
import com.myththewolf.MythBans.commands.open;
import com.myththewolf.MythBans.commands.softmute;
import com.myththewolf.MythBans.commands.user;
import com.myththewolf.MythBans.commands.view;
import com.myththewolf.MythBans.lib.SQL.MythSQLConnect;
import com.myththewolf.MythBans.lib.feilds.ConfigProperties;
import com.myththewolf.MythBans.lib.player.events.ChunkLoad;
import com.myththewolf.MythBans.lib.player.events.CommandEvent;
import com.myththewolf.MythBans.lib.player.events.PlayerChat;
import com.myththewolf.MythBans.lib.player.events.PlayerDamageEvent;
import com.myththewolf.MythBans.lib.player.events.PlayerEatEvent;
import com.myththewolf.MythBans.lib.player.events.PlayerJoin;
import com.myththewolf.MythBans.lib.player.events.PlayerQuit;
import com.myththewolf.MythBans.lib.tool.LanguageGoverner;

public class MythBans {
	private JavaPlugin MythPlugin;

	private LanguageGoverner LG;
	private BukkitTask DISABLE_TASK;

	public MythBans(JavaPlugin inst) {
		this.MythPlugin = inst;
	}

	public JavaPlugin getJavaPlugin() {
		return this.MythPlugin;
	}

	public void setDisableTask(BukkitTask BT) {
		DISABLE_TASK = BT;
	}

	public BukkitTask getDisableTask() {
		return DISABLE_TASK;
	}

	public void loadConfig(Startup startup) {

		try {
			if (!MythPlugin.getDataFolder().exists()) {
				MythPlugin.getDataFolder().mkdirs();

			}
			File FF = new File(MythPlugin.getDataFolder() + File.separator + "lang");
			if (!FF.exists()) {
				FF.mkdirs();
			}
			for (String theLanguage : ConfigProperties.LANGS) {
				File specialf = new File(MythPlugin.getDataFolder() + File.separator + "lang", theLanguage + ".yml");
				if (!specialf.exists()) {
					specialf.getParentFile().mkdirs();
					MythPlugin.saveResource("lang" + File.separator + theLanguage + ".yml", false);
				}
				FileConfiguration daFonts = YamlConfiguration.loadConfiguration(specialf);
				ConfigProperties.langMap.put(theLanguage, daFonts);
			}
			File file = new File(MythPlugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				MythPlugin.getLogger().info("Config.yml not found, creating!");
				MythPlugin.saveDefaultConfig();
				ConfigProperties.dumpProperties(MythPlugin.getConfig());
			} else {
				MythPlugin.getLogger().info("--------->Config.yml found, loading!");
				ConfigProperties.dumpProperties(MythPlugin.getConfig());
			}
			
		} catch (Exception e) {
			

		}
		
		System.out.println("...Done..");
		startup.onConfigReady(this);
	}

	public void loadEvents() {
		MythPlugin.getServer().getPluginManager().registerEvents(new ChunkLoad(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerChat(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerJoin(MythPlugin), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerQuit(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerEatEvent(MythPlugin), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), MythPlugin);
		MythPlugin.getServer().getPluginManager().registerEvents(new CommandEvent(MythPlugin), MythPlugin);
		
	}

	public Connection loadMySQL() {
		MythSQLConnect msc = new MythSQLConnect();
		if (MythSQLConnect.getConnection() == null) {
			return null;
		}
		msc.makeTables();
		return MythSQLConnect.getConnection();
	}

	public void loadCommands() {
		MythPlugin.getCommand("mute").setExecutor(new Mute());
		MythPlugin.getCommand("ban").setExecutor(new Ban());
		MythPlugin.getCommand("tempban").setExecutor(new TempBan());
		MythPlugin.getCommand("kick").setExecutor(new Kick());
		MythPlugin.getCommand("probate").setExecutor(new Probate());
		MythPlugin.getCommand("banip").setExecutor(new IPBan(MythPlugin));
		MythPlugin.getCommand("import").setExecutor(new importJSON(MythPlugin));
		MythPlugin.getCommand("createUI").setExecutor(new createUI());
		MythPlugin.getCommand("pardon").setExecutor(new PardonUser());
		MythPlugin.getCommand("pardonIP").setExecutor(new PardonIP(MythPlugin));
		MythPlugin.getCommand("getFam").setExecutor(new getFam());
		MythPlugin.getCommand("kickip").setExecutor(new IPKick(MythPlugin));

		MythPlugin.getCommand("playertime").setExecutor(new PlayerTime());

		MythPlugin.getCommand("mythbans").setExecutor(new mythapi());

		MythPlugin.getCommand("player").setExecutor(new user());
		MythPlugin.getCommand("potato").setExecutor(new Potato(MythPlugin));
		MythPlugin.getCommand("softmute").setExecutor(new softmute());
		MythPlugin.getCommand("mbreload").setExecutor(new ReloadMythBans(MythPlugin));
		MythPlugin.getCommand("socialspy").setExecutor(new SocialSpy());
		MythPlugin.getCommand("clearchat").setExecutor(new ClearChat());
		MythPlugin.getCommand("mbfix").setExecutor(new mbfix(this));
		MythPlugin.getCommand("xenUpdate").setExecutor(new UpdateXenForo());
		MythPlugin.getCommand("upgradeTables").setExecutor(new UpgradeTables(MythPlugin, this));
		MythPlugin.getCommand("dump").setExecutor(new Dump());
		MythPlugin.getCommand("open").setExecutor(new open());
		MythPlugin.getCommand("view").setExecutor(new view());
	}

	public void buildCommandMap() {

	}

	public boolean runTests() throws IOException {
		File specialf = new File(
				MythPlugin.getDataFolder() + File.separator + "lang" + File.separator + "expected_codes.txt");

		specialf.getParentFile().mkdirs();
		MythPlugin.saveResource("lang" + File.separator + "expected_codes.txt", true);
		this.LG = new LanguageGoverner(specialf, MythPlugin);
		return LG.run();
	}

	public LanguageGoverner getLangGoverner() {
		return this.LG;
	}

	public void startDaemon() throws SQLException {

	}

	public void shutdown() {

		Bukkit.getScheduler().cancelAllTasks();

	}

	public void disableSelf() {
		Bukkit.getPluginManager().disablePlugin(MythPlugin);
	}
}
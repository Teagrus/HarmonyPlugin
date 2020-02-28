package cn.teagrus.harmonyplugin;


import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class HarmonyPlugin extends JavaPlugin {
	private BlackHouse bhouse;
	private LocationService lservice;
	//Override
	public void onEnable() {
		getLogger().info("Start to initial harmonyplugin");
		Bukkit.broadcastMessage("Plugin initial");
		//Check Directories
		this.dirMake();
		//initial BlackHouse::
		try {
			this.bhouse = new BlackHouse();
			this.bhouse.initialize();
		}catch (Exception e) {
			getLogger().info("!!!!!BlackHouse Initialization failed!!!!!");
			getLogger().info("-Check Whether the program are allowed to read file");
			getLogger().info("-Or the file harmonyPlugin/blackHouse/data.yml is correct(You may solve problem by deleting it)");
		}
		//initialize location service
		try {
			this.lservice = new LocationService();
			this.lservice.initialize();
		}catch (Exception e) {
			getLogger().info("!!!!!LocationService Initialization failed!!!!!");
			getLogger().info("-Check Whether the program are allowed to read file");
			getLogger().info("-Or the file harmonyPlugin/LocationService/publicLocations.yml is correct(You may solve problem by deleting it)");
		}
		
		//set BlackHouse Commands;
		this.getCommand("setblackhouseloc").setExecutor(this.bhouse.getExecutor());
		this.getCommand("senttoblackhouse").setExecutor(this.bhouse.getExecutor());
		this.getCommand("releasefromblackhouse").setExecutor(this.bhouse.getExecutor());
		
		//set locationServiceCommand
		this.getCommand("location").setExecutor(this.lservice.getExecutor());
		
		
		
		CommandExecutor excutor = new HarmonyCommandExe(this);
		this.getCommand("hangupmode").setExecutor(excutor);
		this.getCommand("closehangupmode").setExecutor(excutor);


		getLogger().info("End initialization for harmony plugin");
	}
	//Override
	
	public void onDisable() {
		getLogger().info("Disabling harmonyplugin");
		//close bh
		try {
			this.bhouse.saveData();
			getLogger().info("----BlackHouse Data saved----");
		}catch (Exception e) {
			getLogger().info("!!!!!BlackHouse Data save error!!!!!");	
		}
		
		try {
			this.bhouse.saveData();
			getLogger().info("----Location Service Data saved----");
		}catch (Exception e) {
			getLogger().info("!!!!!Location Service save error!!!!!");	
		}
		
	}
	
	
	public HarmonyPlugin() {
		// TODO Auto-generated constructor stub
	}
	
	
	public BlackHouse getBlackHouse() {
		return bhouse;
	};
	
	private void dirMake() {
		File pathMain = new File(Configs.PLUGIN_SAVE_PATH);
		if (!pathMain.exists()) {
			pathMain.mkdir();
		}
		File pathBlackHouse = new File(Configs.PLUGIN_SAVE_PATH + Configs.B_H_PATH);
		if (!pathBlackHouse.exists()) {
			pathBlackHouse.mkdir();
		}
		File pathLocationService = new File(Configs.PLUGIN_SAVE_PATH + Configs.L_S_PATH);
		if (!pathLocationService.exists()) {
			pathLocationService.mkdir();
		}
	}
	
}


class HarmonyCommandExe implements CommandExecutor  {
	final private HarmonyPlugin thePlugin;
	public HarmonyCommandExe(HarmonyPlugin plu){
		this.thePlugin = plu;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hangupmode")) {
			//make sure args length
			if (args.length == 0) {
				if (sender instanceof Player) {
					((Player) sender).setSleepingIgnored(true);
					return true;
				} else {
					sender.sendMessage("只有玩家才能使用这个指令\n");
				}
			} else {
				sender.sendMessage("指令格式错误\n");
			}
		} else if (cmd.getName().equalsIgnoreCase("closehangupmode")) {
			//make sure args length
			if (args.length == 0) {
				if (sender instanceof Player) {
					((Player) sender).setSleepingIgnored(false);
				} else {
					sender.sendMessage("只有玩家才能使用这个指令\n");
					return true;
				}
			} else {
				sender.sendMessage("指令格式错误\n");
			}
		}
		return false;
	}
}





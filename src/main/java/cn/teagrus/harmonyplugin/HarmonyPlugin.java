package cn.teagrus.harmonyplugin;

import java.awt.HeadlessException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.EndGateway;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class HarmonyPlugin extends JavaPlugin {
	private BlackHouse bhouse;
	
	//Override
	public void onEnable() {
		getLogger().info("Start to initial harmonyplugin");
		this.bhouse = new BlackHouse();
		
		CommandExecutor excutor = new HarmonyCommandExe(this);
		//SetCommandExecuters
		this.getCommand("setblackhouseloc").setExecutor(excutor);
		this.getCommand("senttoblackhouse").setExecutor(excutor);
		this.getCommand("releasefromblackhouse").setExecutor(excutor);
		this.getCommand("hangupmode").setExecutor(excutor);
		this.getCommand("closehangupmode").setExecutor(excutor);


		getLogger().info("End initialization for harmony plugin");
	}
	//Override
	
	public void onDisable() {
		getLogger().info("Disabling harmonyplugin");
	}
	
	
	public HarmonyPlugin() {
		// TODO Auto-generated constructor stub
	}
	
	
	public BlackHouse getBlackHouse() {
		return bhouse;
	};
	
}


class HarmonyCommandExe implements CommandExecutor  {
	final private HarmonyPlugin thePlugin;
	public HarmonyCommandExe(HarmonyPlugin plu){
		this.thePlugin = plu;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setblackhouseloc")) {
			//make sure args length
			if (args.length == 3) {
				int isSuccess = 1, xt = 0, yt = 255, zt = 0;
				//check the validity of loc
				try {
					xt = Integer.parseInt(args[0]);
					yt = Integer.parseInt(args[1]);
					zt = Integer.parseInt(args[2]);
					isSuccess = 1;
				}catch (Exception except) {
					// TODO: handle exception
					sender.sendMessage("错误的坐标格式");
					isSuccess = 0;
				}
				if (isSuccess == 1) {
					thePlugin.getBlackHouse().setloc(xt, yt, zt);
					return true;
				}
			} else {
				sender.sendMessage("指令格式错误\n");
			}
		} else if (cmd.getName().equalsIgnoreCase("senttoblackhouse")) {
			//make sure args length
			if (args.length == 1) {
				Player targetP = Bukkit.getPlayer(args[0]);
				if (targetP != null) {
					if (targetP.isOnline()) {
						targetP.setInvulnerable(true);
						targetP.setSleepingIgnored(true);
						targetP.getInventory().remove(Material.ENDER_PEARL);
						targetP.getInventory().remove(Material.CHORUS_FRUIT);
						targetP.sendMessage("您已被关入小黑屋");
						targetP.setGameMode(GameMode.ADVENTURE);
						World tarW = Bukkit.getWorld("world");
						Location loc = new Location(tarW, thePlugin.getBlackHouse().getx(), thePlugin.getBlackHouse().gety(), thePlugin.getBlackHouse().getz());
						targetP.teleport(loc);
						sender.sendMessage(targetP.getName() + "已入狱");
						return true;
					} else {
						sender.sendMessage("所选定的玩家不存在或不在线");
					}
				} else {
					sender.sendMessage("所选定的玩家不存在或不在线");
				}
				
			} else {
				sender.sendMessage("指令格式错误\n");
			}
			
		} else if (cmd.getName().equalsIgnoreCase("releasefromblackhouse")){
			//make sure args length
			if (args.length == 1) {
				Player targetP = Bukkit.getPlayer(args[0]);
				if (targetP != null) {
					if (targetP.isOnline()) {
						if (targetP.isInvulnerable()) {
							Location loc = Bukkit.getWorld("world").getSpawnLocation();
							targetP.teleport(loc);
							targetP.setInvulnerable(false);
							targetP.setSleepingIgnored(false);
							targetP.setGameMode(GameMode.SURVIVAL);							
							targetP.sendMessage("您已出狱");
							sender.sendMessage("已释放该玩家");
						} else {
							sender.sendMessage("这名玩家尚未被送进小黑屋");
						}
						return true;
					} else {
						sender.sendMessage("所选定的玩家不存在或不在线");
					}
				} else {
					sender.sendMessage("所选定的玩家不存在或不在线");
				}
				
			} else {
				sender.sendMessage("指令格式错误\n");
			}
			
		} else if (cmd.getName().equalsIgnoreCase("hangupmode")) {
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

class BlackHouse {
	
	private int xposi;
	private int yposi;
	private int zposi;
	
	public void setloc(int x, int y, int z) {
		this.xposi = x;
		this.yposi = y;
		this.zposi = z;
	}
	public int getx() {
		return xposi;
	}
	public int gety() {
		return yposi;
	}
	public int getz() {
		return zposi;
	}
	public BlackHouse() {
		this.xposi = 0;
		this.yposi = 255;
		this.zposi = 0;
	}
}



package cn.teagrus.harmonyplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import cn.teagrus.harmonyplugin.datayml.DataLabel;
import cn.teagrus.harmonyplugin.datayml.DataLabelGeneral;
import cn.teagrus.harmonyplugin.datayml.DataLabelReader;
import cn.teagrus.harmonyplugin.datayml.DataLabelWriter;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidFileFormatException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidRangeException;
import cn.teagrus.harmonyplugin.extended.DataLabelPosition;

public class BlackHouse {
	DataLabelPosition loc;
	BlackHouseExecutor executor;
	static final String B_H_PATH = "blackHouse/data.yml";
	public void setloc(int x, int y, int z) throws InvalidRangeException {
		loc.setXYZ(x, y, z);
	}
	public int getx() {
		return loc.getX();
	}
	public int gety() {
		return loc.getY();
	}
	public int getz() {
		return loc.getZ();
	}
	
	public boolean isValid() {
		if (loc == null)
			return false;
		return true;
	}
	
	public String getWorldName(){
		return loc.getWorldName();
	}
	
	public CommandExecutor getExecutor() {
		return executor;
	}
	
	public BlackHouse() throws Exception{
		this.loc = null;
		executor = new BlackHouseExecutor(this);
	}
	
	public void initialLocation(Location location) throws InvalidLabelException, InvalidRangeException {
		if (loc == null) {
			loc = new DataLabelPosition(location, "BlackHouseLocation");
		}
	};
	
	
	
	public void initialize() throws IOException, InvalidLabelException, InvalidFileFormatException, InvalidRangeException {
		File tartgetF = new File(Configs.PLUGIN_SAVE_PATH + B_H_PATH);

		if (!tartgetF.exists()) {
			loc = null;
		} else{
			DataLabel temp = DataLabelReader.readLabelsFrom(Configs.PLUGIN_SAVE_PATH + B_H_PATH);			
			loc = new DataLabelPosition(temp);
		}
	}	
	
	public void saveData() throws IOException, InvalidLabelException, InvalidFileFormatException {
		DataLabelWriter.writeLabelsTo(Configs.PLUGIN_SAVE_PATH + B_H_PATH, loc);
	}
}

class BlackHouseExecutor implements CommandExecutor {
	private BlackHouse bhClass;
	BlackHouseExecutor(BlackHouse bh){
		bhClass = bh;
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
					try {
						if (this.bhClass.isValid()) {
							this.bhClass.setloc(xt, yt, zt);
							this.bhClass.saveData();
						}else {
							this.bhClass.initialLocation(new Location(Bukkit.getWorld("world"), xt, yt, zt));
							this.bhClass.saveData();
						}
						
					}catch (Exception e) {
						sender.sendMessage("Some error happened When trying to set the location of blackhouse");
					}
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
						World tarW = Bukkit.getWorld(this.bhClass.getWorldName());
						Location loc = new Location(tarW, this.bhClass.getx(), this.bhClass.gety(), this.bhClass.getz());
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
		} 
		return false;
	}
}




package cn.teagrus.harmonyplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import cn.teagrus.harmonyplugin.datayml.DataLabel;
import cn.teagrus.harmonyplugin.datayml.DataLabelAttrib;
import cn.teagrus.harmonyplugin.datayml.DataLabelCluster;
import cn.teagrus.harmonyplugin.datayml.DataLabelGeneral;
import cn.teagrus.harmonyplugin.datayml.DataLabelReader;
import cn.teagrus.harmonyplugin.datayml.DataLabelWriter;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidFileFormatException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidRangeException;
import cn.teagrus.harmonyplugin.extended.DataLabelPosition;

public class LocationService {
	public final static String P_L_NAME = "publicLocations.yml"; //public location savefile name
	private DataLabelCluster publicLocations;
	private LocationExecutor commandExecutor;
	//Temporarily not use this buffers;
	//private DataLabelCluster PlayerLocationsBuffer;
	public LocationService() {
		publicLocations = null;
	}
	public void initialize() throws InvalidLabelException, FileNotFoundException, IOException, InvalidFileFormatException {
		//initialize Public location;
		String pth = Configs.PLUGIN_SAVE_PATH + Configs.L_S_PATH + P_L_NAME;
		File publicFile = new File(pth);
		if (!publicFile.exists()) {
			publicLocations = new DataLabelCluster("publicLocations");
			DataLabelWriter.writeLabelsTo(pth, publicLocations);
		}
		else {
			publicLocations =new DataLabelCluster(DataLabelReader.readLabelsFrom(pth));
		}
		commandExecutor = new LocationExecutor(this);
	}
	
	public void saveData() {
		return;
	}
	
	
	public CommandExecutor getExecutor() {
		return commandExecutor;
	}
	
	DataLabelCluster getCluster() {
		return publicLocations;
	}
}



class LocationExecutor implements CommandExecutor {
	private LocationService ls;
	LocationExecutor(LocationService ls) {
		this.ls = ls;
	}
	
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("location")) {
			if (args.length > 0) {
				try {
					if (args[0].equalsIgnoreCase("set")) {
						return setCommand(sender, args);
					} else if (args[0].equalsIgnoreCase("find")) {
						return findCommand(sender, args);
					} else if (args[0].equalsIgnoreCase("delete")) {
						return deleteCommand(sender, args);
					} else if (args[0].equalsIgnoreCase("tp")) {
						return tpCommand(sender, args);
					} else if (args[0].equalsIgnoreCase("setp")) {
						return setpCommand(sender, args);
					} else if (args[0].equalsIgnoreCase("deletep")) {
						return deletepCommand(sender, args);
					}
				} catch (Exception e) {
					sender.sendMessage("在执行指令是遇到一些错误");
				}
				
			}
		}
		return false;
	}

	
	
	
	
	private boolean setCommand(CommandSender sender, String[] args) throws InvalidLabelException, IOException, InvalidFileFormatException, NullPointerException, InvalidRangeException {
		if (args.length == 2) {
			if (sender instanceof Player) {
				if (((Player)sender).isOnline()) {
					//read and save, use player's location
					DataLabelCluster playerC = playerLoad(sender.getName());
					playerC.insertLabel(  new DataLabelPosition( ((Player)sender).getLocation(), args[1]) );
					playerWrite(playerC);
					sender.sendMessage("成功设置坐标点");
					return true;
				}
			}
			else {
				sender.sendMessage("Only player could use this commmand");
			}
			
		} else if (args.length == 5) {
			int[] loc = LocationCheck(args[2], args[3], args[4]);
			if (loc != null) {
				
				if (sender instanceof Player) {
					if (((Player)sender).isOnline()) {
						DataLabelCluster playerC = playerLoad(sender.getName());
						playerC.insertLabel(  new DataLabelPosition( new Location(((Player) sender).getLocation().getWorld(), loc[0], loc[1], loc[2]), args[1]) );
						playerWrite(playerC);
						sender.sendMessage("成功设置坐标点");
						return true;
					}
				}
				else {
					sender.sendMessage("Only player could use this commmand");
				}
				
			}else {
				sender.sendMessage("请检查输入的坐标格式是否正确");
			}
		} else if (args.length == 6) {
			int[] loc = LocationCheck(args[2], args[3], args[4]);
			if (loc != null) {
				if (Bukkit.getWorld(args[5]) != null) {
					Location location = new Location(Bukkit.getWorld(args[5]), loc[0], loc[1], loc[2]);
					DataLabelCluster playerC = playerLoad(sender.getName());
					playerC.insertLabel(  new DataLabelPosition(location, args[1]) );
					playerWrite(playerC);
					sender.sendMessage("成功设置坐标点");
					return true;
				}
				else {
					sender.sendMessage("所指定的目标世界不存在");
				}
			}else {
				sender.sendMessage("请检查输入的坐标格式是否正确");
			}
		} else {
			sender.sendMessage("指令参数错误, /location set使用方式:");
			sender.sendMessage(" 1. /location set <坐标名字> 设定当前所在坐标为<坐标名字>");
			sender.sendMessage(" 2. /location set <坐标名字> <x> <y> <z> 设定<x><y><z>坐标为<坐标名字>");
			sender.sendMessage(" 3. /location set <坐标名字> <x> <y> <z> <world> 设定<x><y><z><world>坐标为<坐标名字>");
		}
		return false;
	}
	
	private boolean findCommand(CommandSender sender, String[] args) throws InvalidLabelException, IOException, InvalidFileFormatException {
		if (args.length == 1) {
			DataLabelCluster playerCluster = playerLoad(sender.getName());
			DataLabel[] allLa = playerCluster.nameMatch("");
			if (allLa == null) {
				sender.sendMessage("未找到私人坐标点");
				return true;
			}
			sender.sendMessage("共找到" + allLa.length + "个坐标点");
			for (int i = 0; i < allLa.length; i++) {
				sender.sendMessage(getLocString(allLa[i]));
			}
			return true;
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("public")) {
				DataLabel[] allLa = this.ls.getCluster().nameMatch("");
				if (allLa == null) {
					sender.sendMessage("未找到公共坐标点");
					return true;
				}
				sender.sendMessage("共找到" + allLa.length + "个坐标点");
				for (int i = 0; i < allLa.length; i++) {
					sender.sendMessage(getLocString(allLa[i]));
				}
				return true;
			} else if (args[1].equalsIgnoreCase("all")) {
				DataLabel[] allLa = this.ls.getCluster().nameMatch("");
				if (allLa == null) {
					sender.sendMessage("未找到公共坐标点");
					return true;
				}
				sender.sendMessage("共找到" + allLa.length + "个公共坐标点");
				for (int i = 0; i < allLa.length; i++) {
					sender.sendMessage(getLocString(allLa[i]));
				}
				
				DataLabelCluster playerCluster = playerLoad(sender.getName());
				allLa = playerCluster.nameMatch("");
				if (allLa == null) {
					sender.sendMessage("未找到私人坐标点");
					return true;
				}
				sender.sendMessage("共找到" + allLa.length + "个私人坐标点");
				for (int i = 0; i < allLa.length; i++) {
					sender.sendMessage(getLocString(allLa[i]));
				}
				return true;
			} else {
				sender.sendMessage("坐标格式错误");
			}
		
		} else if (args.length == 3) {
			if (args[1].equalsIgnoreCase("contains")) {
				DataLabelCluster playerCluster = playerLoad(sender.getName());
				DataLabel[] allLa = playerCluster.nameMatch(args[2]);
				if (allLa == null) {
					sender.sendMessage("未找到坐标点，目前只支持查找私人坐标");
				}
				sender.sendMessage("共找到" + allLa.length + "个坐标点");
				for (int i = 0; i < allLa.length; i++) {
					sender.sendMessage(getLocString(allLa[i]));
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean deleteCommand(CommandSender sender, String[] args) throws InvalidLabelException, IOException, InvalidFileFormatException {
		if (args.length == 2) {
			DataLabelCluster playerCluster = playerLoad(sender.getName());
			playerCluster.deleteByName(args[1]);
			playerWrite(playerCluster);
			sender.sendMessage("成功删除目标坐标");
			return true;
		}
		return false;
	}

	private boolean tpCommand(CommandSender sender, String[] args) throws InvalidLabelException, IOException, InvalidFileFormatException, InvalidRangeException {
		if (!sender.hasPermission("harmonyPlugin.locationService.tp")) {
			sender.sendMessage("你没有使用这个命令的权限");
			return true;
		}
		//chec player
		if (args.length != 2) {
			return false;
		}
		if (sender instanceof Player && ((Player)sender).isOnline()) {
			Player ply = (Player)sender;
			DataLabelCluster playerCluster = playerLoad(sender.getName());
			if (playerCluster.nameMatchExact(args[1]) != null) {
				DataLabelPosition targetLabel = new DataLabelPosition(playerCluster.nameMatchExact(args[1]));
				if (Bukkit.getWorld(targetLabel.getWorldName()) != null) {
					Location tarL = new Location(Bukkit.getWorld(targetLabel.getWorldName()), targetLabel.getX(), targetLabel.getY(), targetLabel.getZ());
					ply.teleport(tarL);
					sender.sendMessage("已将您成功传送至" + targetLabel.getLabelName());
					return true;
				} 
			} else if(ls.getCluster().nameMatchExact(args[1]) != null ) {
				DataLabelPosition targetLabel = new DataLabelPosition(ls.getCluster().nameMatchExact(args[1]));
				if (Bukkit.getWorld(targetLabel.getWorldName()) != null) {
					Location tarL = new Location(Bukkit.getWorld(targetLabel.getWorldName()), targetLabel.getX(), targetLabel.getY(), targetLabel.getZ());
					ply.teleport(tarL);
					sender.sendMessage("已将您成功传送至" + targetLabel.getLabelName());
					return true;
				} 
			} else {
				sender.sendMessage("找不到指定的坐标，请检查名称是否有误");
			}
		}
		else {
			sender.sendMessage("这个指令只能由在线的玩家执行");
		}
		return false;
	}
	
	//overlapped code in this command. put together with codes in setCommnad;
	private boolean setpCommand(CommandSender sender, String[] args) throws NullPointerException, InvalidLabelException, InvalidRangeException, FileNotFoundException, IOException {
		if (!sender.hasPermission("harmonyPlugin.locationService.setp")) {
			sender.sendMessage("你没有使用这个命令的权限");
			return true;
		}
		
		if (args.length == 2) {
			if (sender instanceof Player) {
				if (((Player)sender).isOnline()) {
					//read and save, use player's location
					ls.getCluster().insertLabel(  new DataLabelPosition( ((Player)sender).getLocation(), args[1]) );
					playerWrite(ls.getCluster());
					sender.sendMessage("成功设置坐标点");
					return true;
				}
			}
			else {
				sender.sendMessage("Only player could use this commmand");
			}
			
		} else if (args.length == 5) {
			int[] loc = LocationCheck(args[2], args[3], args[4]);
			if (loc != null) {
				
				if (sender instanceof Player) {
					if (((Player)sender).isOnline()) {
						ls.getCluster().insertLabel(  new DataLabelPosition( new Location(((Player) sender).getLocation().getWorld(), loc[0], loc[1], loc[2]), args[1]) );
						playerWrite(ls.getCluster());
						sender.sendMessage("成功设置坐标点");
						return true;
					}
				}
				else {
					sender.sendMessage("Only player could use this commmand");
				}
				
			}else {
				sender.sendMessage("请检查输入的坐标格式是否正确");
			}
		} else if (args.length == 6) {
			int[] loc = LocationCheck(args[2], args[3], args[4]);
			if (loc != null) {
				if (Bukkit.getWorld(args[5]) != null) {
					Location location = new Location(Bukkit.getWorld(args[5]), loc[0], loc[1], loc[2]);
					ls.getCluster().insertLabel(  new DataLabelPosition(location, args[1]) );
					playerWrite(ls.getCluster());
					sender.sendMessage("成功设置坐标点");
					return true;
				}
				else {
					sender.sendMessage("所指定的目标世界不存在");
				}
			}else {
				sender.sendMessage("请检查输入的坐标格式是否正确");
			}
		} else {
			sender.sendMessage("指令参数错误, /location setp使用方式:");
			sender.sendMessage(" 1. /location setp <坐标名字> 设定当前所在坐标为<坐标名字>");
			sender.sendMessage(" 2. /location setp <坐标名字> <x> <y> <z> 设定<x><y><z>坐标为<坐标名字>");
			sender.sendMessage(" 3. /location setp <坐标名字> <x> <y> <z> <world> 设定<x><y><z><world>坐标为<坐标名字>");
		}
		return false;
	}
	
	private boolean deletepCommand(CommandSender sender, String[] args) throws FileNotFoundException, IOException {
		if (!sender.hasPermission("harmonyPlugin.locationService.deletep")) {
			sender.sendMessage("你没有使用这个命令的权限");
			return true;
		}
		if (args.length == 2) {
			ls.getCluster().deleteByName(args[1]);
			playerWrite(ls.getCluster());
			sender.sendMessage("成功删除目标坐标");
			return true;
		}
		return false;
	};
	
	private static String getLocString(DataLabel src) {
		String result = "";
		result += src.getLabelName() + ": ";
		DataLabel temp = src.getChild().getBrother().getBrother();
		String worldname = ((DataLabelAttrib)temp).getRange();
		temp = temp.getBrother();
		result += ((DataLabelAttrib)temp).getRange() + " ";
		temp = temp.getBrother();
		result += ((DataLabelAttrib)temp).getRange() + " ";
		temp = temp.getBrother();
		result += ((DataLabelAttrib)temp).getRange() + " ";
		result += worldname;
		return result;
	}
	
	private static DataLabelCluster playerLoad(String playerName) throws InvalidLabelException, IOException, InvalidFileFormatException {
		String pth = Configs.PLUGIN_SAVE_PATH + Configs.L_S_PATH + playerName + ".yml";
		File targetFile = new File(pth);
		if (!targetFile.exists()) {
			return new DataLabelCluster(playerName);
		}
		else {
			return new DataLabelCluster(DataLabelReader.readLabelsFrom(pth));
		}
	}
	
	private static void playerWrite(DataLabel PlayerCluster) throws FileNotFoundException, IOException {
		String pth = Configs.PLUGIN_SAVE_PATH + Configs.L_S_PATH + PlayerCluster.getLabelName() + ".yml";
		DataLabelWriter.writeLabelsTo(pth, (DataLabelGeneral) PlayerCluster);
	};
	
	private static int[] LocationCheck(String inputx, String inputy, String inputz) {

		int [] out = new int[3];
		try {
			out[0] = Integer.parseInt(inputx);
			out[1] = Integer.parseInt(inputy);
			out[2] = Integer.parseInt(inputz);
			return out;
		}catch (Exception except) {
			;
		}
		return null;
	}
}


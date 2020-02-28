package cn.teagrus.harmonyplugin.extended;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import cn.teagrus.harmonyplugin.datayml.DataLabel;
import cn.teagrus.harmonyplugin.datayml.DataLabelAttrib;
import cn.teagrus.harmonyplugin.datayml.DataLabelCluster;
import cn.teagrus.harmonyplugin.datayml.DataLabelGeneral;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidRangeException;

public class DataLabelPosition extends DataLabelCluster{
	DataLabelAttrib color;
	DataLabelAttrib worldId;
	DataLabelAttrib worldName;
	DataLabelAttrib x;
	DataLabelAttrib y;
	DataLabelAttrib z;
	
	public DataLabelPosition(Location loc, String name) throws InvalidLabelException, InvalidRangeException {
		super(name);
		color = new DataLabelAttrib("color", "WHITE");
		worldId = new DataLabelAttrib("worldId", loc.getWorld().getUID().toString());
		worldName = new DataLabelAttrib("worldName", loc.getWorld().getName());
		x = new DataLabelAttrib("x", String.valueOf(loc.getBlockX()));
		y = new DataLabelAttrib("y", String.valueOf(loc.getBlockY()));
		z = new DataLabelAttrib("z", String.valueOf(loc.getBlockZ()));
		this.setChild(color);
		color.setBrother(worldId);
		worldId.setBrother(worldName);
		worldName.setBrother(x);
		x.setBrother(y);
		y.setBrother(z);
	}
	
	public void setXYZ(int x, int y, int z) throws InvalidRangeException {
		this.x.reviseRange(String.valueOf(x));
		this.y.reviseRange(String.valueOf(y));
		this.z.reviseRange(String.valueOf(z));
	}
	
	public void setWorld(String worldName, UUID worldID) throws InvalidRangeException {
		this.worldId.reviseRange(worldID.toString());
		this.worldName.reviseRange(worldName);
	}
	
	public void setColor(String chatcolor)  throws InvalidRangeException {
			this.color.reviseRange(chatcolor);
	}
	
	public String getWorldName() {
		return this.worldName.getRange();
	}
	
	public UUID getWorldId() {
		return UUID.fromString(this.worldId.getRange());
	}
	
	public int getX() {
		return Integer.parseInt(this.x.getRange());
	}
	
	public int getY() {
		return Integer.parseInt(this.y.getRange());
	}
	
	public int getZ() {
		return Integer.parseInt(this.z.getRange());
	}
	
	//weak copy
	public DataLabelPosition(DataLabel generalDataLabel) throws InvalidLabelException, InvalidRangeException {
		super(generalDataLabel.getLabelName());
		//check
		int isType = 0;
		DataLabel temp = generalDataLabel.getChild();
		if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("color") == 0) {
			temp = temp.getBrother();
			if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("worldId") == 0) {
				temp = temp.getBrother();
				if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("worldName") == 0) {
					temp = temp.getBrother();
					if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("x") == 0) {
						temp = temp.getBrother();
						if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("y") == 0) {
							temp = temp.getBrother();
							if (temp != null && temp instanceof DataLabelAttrib && ((DataLabelAttrib)temp).getLabelName().compareTo("z") == 0) {
								isType = 1;
							} 
						} 
					} 
				} 
			} 
		}

				
		temp = generalDataLabel.getChild();
		if (isType == 1) {			
			//((DataLabelAttrib)temp).getRange()
			this.color = new DataLabelAttrib("color", ((DataLabelAttrib)temp).getRange());
			temp = temp.getBrother();
			this.worldId = new DataLabelAttrib("worldId", ((DataLabelAttrib)temp).getRange());
			temp = temp.getBrother();
			this.worldName = new DataLabelAttrib("worldName", ((DataLabelAttrib)temp).getRange());
			temp = temp.getBrother();
			this.x = new DataLabelAttrib("x", ((DataLabelAttrib)temp).getRange());
			temp = temp.getBrother();
			this.y = new DataLabelAttrib("y", ((DataLabelAttrib)temp).getRange());
			temp = temp.getBrother();
			this.z = new DataLabelAttrib("z", ((DataLabelAttrib)temp).getRange());
			this.setChild(color);
			color.setBrother(worldId);
			worldId.setBrother(worldName);
			worldName.setBrother(x);
			x.setBrother(y);
			y.setBrother(z);
			z.setBrother(null);
		}
		else {
			throw new NullPointerException();
		}
	} 
	
	
}

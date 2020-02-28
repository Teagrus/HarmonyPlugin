package cn.teagrus.harmonyplugin.datayml;


import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.*;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidRangeException;


public class DataLabelAttrib extends DataLabelGeneral {
/*
 * This is for those ranged labels
 * */
	private String range;
	
	public DataLabelAttrib(String name) throws InvalidLabelException {
		super(name);
		range = "";
	}
	
	public DataLabelAttrib(String name, String range) throws InvalidLabelException, InvalidRangeException {
		super(name);
		if (!ValidCheck.labelRange(range)) {
			throw new InvalidRangeException(range);
		}
		this.range = range;
	}
	
	//functions about range;
	public void setRange(String range) throws InvalidRangeException {
		if (!ValidCheck.labelRange(range)) {
			throw new InvalidRangeException(range);
		}
		this.range = range;
	}
	
	public String getRange() {
		return this.range;
	}
	
	public void reviseRange(String newrange) throws InvalidRangeException {
		if (!ValidCheck.labelRange(newrange)) {
			throw new InvalidRangeException(newrange);
		}
		range = newrange;
	}

	@Override
	public void getInfoString(StringBuilder targetS, int layer) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < layer; i++) {
			buffer.append("  ");
		}
		targetS.append(buffer + this.name + ": " + this.range + "\n");
		if (this.child != null) {
			((DataLabelGeneral)(this.child)).getInfoString(targetS, layer + 1);
		}
		if (this.brother != null) {
			((DataLabelGeneral)(this.brother)).getInfoString(targetS, layer);
		}
	}
}

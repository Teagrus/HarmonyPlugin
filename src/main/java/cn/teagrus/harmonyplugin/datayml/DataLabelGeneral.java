package cn.teagrus.harmonyplugin.datayml;

import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.*;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;

public class DataLabelGeneral implements DataLabel {
/*
 * This class is the general figure of Data Label, to provide a basic implement for DataLabel;
 * */
//Inner Member datas;
	protected String name;
	DataLabel brother;
	DataLabel child;
	
	//You must have A name to initial this class;
	public DataLabelGeneral(String name) throws InvalidLabelException {
		if (!ValidCheck.labelName(name)) {
			throw new InvalidLabelException(name);
		}
		this.name = name;
		this.brother = null;
		this.child = null;
	}
	
	
	public String getLabelName() {
		return name;
	}

	public void reviseLabelName(String name) throws InvalidLabelException {
		if (!ValidCheck.labelName(name)) {
			throw new InvalidLabelException(name);
		}
		this.name = name;
		this.brother = null;
		this.child = null;
	}

	public DataLabel getBrother() {
		return brother;
	}

	public DataLabel getChild() {
		return child;
	}
	
	public void setBrother(DataLabel labe) {
		brother = labe;
	}

	public void setChild(DataLabel labe) {
		child = labe;
	}

	
	public void getInfoString(StringBuilder targetS, int layer) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < layer; i++) {
			buffer.append("  ");
		}
		targetS.append(buffer + this.name + ":\n");
		if (this.child != null) {
			((DataLabelGeneral)(this.child)).getInfoString(targetS, layer + 1);
		}
		if (this.brother != null) {
			((DataLabelGeneral)(this.brother)).getInfoString(targetS, layer);
		}
	}
	


}

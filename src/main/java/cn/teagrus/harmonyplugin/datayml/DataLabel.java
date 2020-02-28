package cn.teagrus.harmonyplugin.datayml;

import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;

//Using linked list form to perform DataLabels;
public interface DataLabel {
	//About Label name;
	public String getLabelName();
	public void reviseLabelName(String name) throws InvalidLabelException;
	//deal with childs and brothers;
	public DataLabel getBrother();
	public DataLabel getChild();
	public void setBrother(DataLabel l);
	public void setChild(DataLabel l);
}

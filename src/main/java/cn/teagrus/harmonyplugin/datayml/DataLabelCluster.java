package cn.teagrus.harmonyplugin.datayml;

import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;

//Sort Labels using char range order;
public class DataLabelCluster extends DataLabelGeneral{
	public DataLabelCluster(String name) throws InvalidLabelException {
		super(name);
	}
	
	//WeakCopy
	public DataLabelCluster(DataLabel src) throws InvalidLabelException {
		super(src.getLabelName());
		this.child = src.getChild();
		this.brother = src.getBrother();
	}
	
	//use this function to find all labels containing targetString
	public DataLabel[] nameMatch(String targetString) {
		int sum = 0;
		DataLabel temp = this.getChild();
		while(temp !=null) {
			if (temp.getLabelName().contains(targetString)) {
				++sum;
			}
			temp = temp.getBrother();
		}
		
		DataLabel[] output;
		if (sum == 0) {
			output = null;
		}else {
			output = new  DataLabel[sum];
		}
		
		
		int posi = 0;
		temp = this.getChild();
		while(temp !=null) {
			if (temp.getLabelName().contains(targetString)) {
				output[posi++] = temp; 
			}
			temp = temp.getBrother();
		}
		return output;
	}
	
	public DataLabel nameMatchExact(String targetString) {
		if (this.getChild() == null) {
			return null;
		}
		if (this.getChild().getLabelName().compareTo(targetString) == 0) {
			return this.getChild();
		}
		DataLabel temp = this.getChild();
		while (temp.getBrother() != null) {
			if (temp.getBrother().getLabelName().compareTo(targetString) == 0) {
				return temp.getBrother();
			}
			temp = temp.getBrother();
		}
		return null;
	}
	
	public void deleteByName(String name) {
		if (this.getChild() == null) {
			return;
		}
		if (this.getChild().getLabelName().compareTo(name) == 0) {
			this.setChild(this.getChild().getBrother());
			return;
		}
		DataLabel temp = this.getChild();
		while (temp.getBrother() != null) {
			if (temp.getBrother().getLabelName().compareTo(name) == 0) {
				temp.setBrother(temp.getBrother().getBrother());
				return;
			}
			temp = temp.getBrother();
		}
	}
	
	
	
	public DataLabel findLabel(String targetName) {
		DataLabel temp = this.getChild();
		while(temp !=null && temp.getLabelName().compareTo(targetName) < 0) {
			temp = temp.getBrother();
		}
		if (temp == null ||  temp.getLabelName().compareTo(targetName) > 0) {
			return null;
		} else {
			return temp;
		}
	}
	
	public void insertLabel(DataLabel label) throws NullPointerException{
		if (label == null) {
			throw new NullPointerException();
		}
		DataLabel target = this.findInsertPlace(label.getLabelName());
		if (target == this) {
			DataLabel t = this.getChild();
			this.setChild(label);
			label.setBrother(t);
		} else {
			DataLabel t = target.getBrother();
			target.setBrother(label);
			label.setBrother(t);
		}
	}
	
	private DataLabel findInsertPlace(String str) {
		if (this.child == null || str.compareTo(this.child.getLabelName()) < 0){
			return this;
		}
		DataLabel temp = this.child;
		while (!(temp.getBrother() == null || str.compareTo(temp.getBrother().getLabelName()) < 0)) {
			temp = temp.getBrother();
		}
		return temp;
	}
	

}

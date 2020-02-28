package cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel;

public class InvalidLabelException extends Exception {
	private static final long serialVersionUID = 10001L;
	private String ErrName;
	public InvalidLabelException(String errn) {
		this.ErrName = errn; 
	}

	public String getInvalidName() {
		return this.ErrName;
	}
}


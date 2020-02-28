package cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel;

public class InvalidRangeException extends Exception {
	/**
	 *  This exception is used for err range in labels;
	 */
	private static final long serialVersionUID = 10002L;
	String range;
	public InvalidRangeException(String str) {
		this.range = str;
	}
	
	public String getErrRange() {
		return range;
	}
}

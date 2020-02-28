package cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel;

public class InvalidFileFormatException extends Exception {
	private static final long serialVersionUID = 10003L;
	int line;
	int positionInLine;
	public InvalidFileFormatException(int x, int y) {
		this.line = x;
		this.positionInLine = y;
	}
	
	public int[] getErrPosition() {
		int[] arr = new int[2];
		arr[0] = this.line;
		arr[1] = this.positionInLine;
		return arr;
	}
}

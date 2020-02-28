package cn.teagrus.harmonyplugin.datayml.ExceptionAPI;

//for all methods in this class
//true - valid
//false = invalid
public class ValidCheck {
	public static boolean labelName(String lab) {
		if (lab.length() == 0) {
			return false;
		}
		if (!Character.isJavaIdentifierStart(lab.charAt(0))) {
			return false;
		}
		for (int i = 1; i < lab.length(); i++) {
			if (!Character.isJavaIdentifierPart(lab.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean labelRange(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				return false;
			}
		}
		return true;
	}
	
}



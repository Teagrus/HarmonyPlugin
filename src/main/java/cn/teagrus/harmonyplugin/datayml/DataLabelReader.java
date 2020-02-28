package cn.teagrus.harmonyplugin.datayml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.bukkit.Bukkit;

import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidFileFormatException;
import cn.teagrus.harmonyplugin.datayml.ExceptionAPI.DataLabel.InvalidLabelException;

public class DataLabelReader {
	public static DataLabel readLabelsFrom(String path) throws IOException, InvalidLabelException, InvalidFileFormatException {
		File target = new File(path);
		FileReader source = new FileReader(target);
		StringBuilder contentBuffer = new StringBuilder();
		int temp = source.read();
		while (temp != -1) {
			contentBuffer.append((char)temp);
			temp = source.read();
		}	
		source.close();
		
	    return fromString(contentBuffer.toString());
	}
	
	private static DataLabel fromString(String data) throws InvalidLabelException, InvalidFileFormatException{
		int linecount = 0;
		int positionInString = 0;
		int condition = 0;
		int spaceNum = 0;
		StringBuilder labelName = new StringBuilder();
		StringBuilder labelRange = new StringBuilder();
		/*
		 * Condition:
		 * 0 - ready to read spaces
		 * 1 - ready to read label name
		 * 2 - ready to read range
		 * 3 - the end
		 * */
		
		Stack<DataLabel> labelS = new Stack<DataLabel>();
		Stack<Integer> spaceS = new Stack<Integer>();
		while (positionInString < data.length()) {
			switch (condition) {
			case 0:
					while (positionInString < data.length() && data.charAt(positionInString) == '\n') {
						positionInString++;
						linecount++;
					}
					if (positionInString >= data.length()) {
						condition = 3;
					}
					else {
						while (data.charAt(positionInString) == ' ') {
							++spaceNum;
							positionInString++;
						}
						condition = 1;
					}
				break;
			case 1:
					while (positionInString < data.length() && data.charAt(positionInString) != ':') {
						labelName.append(data.charAt(positionInString));
						++positionInString;
					}
					++positionInString;
					while(positionInString < data.length() && data.charAt(positionInString) == ' ') {
						++positionInString;
					}
					if (positionInString >= data.length()) {
						condition = 3;
					}else {
						condition = 2;
					}
				break;
			case 2:
					while ( positionInString != data.length() && data.charAt(positionInString) != '\n') {
						labelRange.append(data.charAt(positionInString));
						positionInString++;
					}
					if (labelName.length() != 0) {
						if (labelS.empty()) {
							try {
								labelS.push(deriveDataLabel(labelName, labelRange));
							}catch (Exception e) {
								throw new InvalidFileFormatException(linecount, positionInString);
							}
							spaceS.push(new Integer(spaceNum));
						} else {
							if (spaceNum > spaceS.peek()) {
								try {
									DataLabel temp = deriveDataLabel(labelName, labelRange);
									labelS.peek().setChild(temp);
									labelS.push(temp);
								}catch (Exception e) {
									throw new InvalidFileFormatException(linecount, positionInString);
								}
								spaceS.push(new Integer(spaceNum));
							} else if (spaceNum == spaceS.peek()) {
								try {
									DataLabel temp = deriveDataLabel(labelName, labelRange);
									labelS.peek().setBrother(temp);
									labelS.push(temp);
								}catch (Exception e) {
									throw new InvalidFileFormatException(linecount, positionInString);
								}
								spaceS.push(new Integer(spaceNum));
							} else {
								while (spaceS.peek() > spaceNum) {
									labelS.pop();
									spaceS.pop();
								}
								if (spaceS.peek() == spaceNum) {
									try {
										DataLabel temp = deriveDataLabel(labelName, labelRange);
										labelS.peek().setBrother(temp);
										labelS.push(temp);
									}catch (Exception e) {
										throw new InvalidFileFormatException(linecount, positionInString);
									}
									spaceS.push(spaceNum);
								}else {
									throw new InvalidFileFormatException(linecount, positionInString);
								}
							
							}
						}
					}
					spaceNum = 0;
					labelName = new StringBuilder();
					labelRange = new StringBuilder();
					++linecount;
					//++positionInString;
					condition = 0;
				break;
			case 3: 
			default:
				break;
			}
		}
		DataLabel result = null;
		while (!labelS.empty()) {
			result = labelS.pop();
		}
		return result;
	}
	
	private static DataLabel deriveDataLabel(StringBuilder name, StringBuilder range) throws Exception{
		if (range.length() != 0) {
			return new DataLabelAttrib(name.toString(), range.toString());	
		} else {
			return new DataLabelGeneral(name.toString());
		}
	}

}

package cn.teagrus.harmonyplugin.datayml;

import java.io.*;

public class DataLabelWriter {
	public static void writeLabelsTo(String path, DataLabelGeneral data) throws IOException, FileNotFoundException {
		if (data == null)
			throw new NullPointerException();
		File target = new File(path);
		FileWriter out = new FileWriter(target);
		StringBuilder range = new StringBuilder();
		data.getInfoString(range, 0);
		out.write(range.toString());
		out.close();
	}
}

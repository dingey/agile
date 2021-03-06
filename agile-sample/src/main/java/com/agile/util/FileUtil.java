package com.agile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author d
 */
public class FileUtil {
	public static void save(byte[] bytes, String path) {
		try {
			File f = new File(path);
			if(!f.exists()){
				f.createNewFile();
			}
			OutputStream out = new FileOutputStream(f);
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

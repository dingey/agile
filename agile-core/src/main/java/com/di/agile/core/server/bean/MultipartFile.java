package com.di.agile.core.server.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author d
 */
public class MultipartFile {
	public String getName() {
		return null;
	}

	public String getOriginalFilename() {
		return null;
	}


	public String getContentType() {
		return null;
	}


	public boolean isEmpty() {
		return false;
	}

	public long getSize() {
		return 0;
	}

	public byte[] getBytes() throws IOException {
		return null;
	}

	public InputStream getInputStream() throws IOException {
		return null;
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
	}
}

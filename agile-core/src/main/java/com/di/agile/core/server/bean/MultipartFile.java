package com.di.agile.core.server.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author d
 */
public class MultipartFile {
	private String name;
	private String originalFilename;
	private String contentType;
	private long size;
	private InputStream inputStream;
	private byte[] bytes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
	}
}

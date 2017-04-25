package com.di.agile.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author di
 */
public class ByteUtil {
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public static List<byte[]> splitByRN(byte[] bytes) {
		List<Integer> indexs = new ArrayList<>();
		if (bytes.length > 0) {
			indexs.add(0);
			for (int i = 1; i < bytes.length - 1; i++) {
				if (bytes[i] == '\r' && bytes[i + 1] == '\n') {
					indexs.add(i);
				}
			}
		}
		if (indexs.get(indexs.size() - 1) < bytes.length) {
			indexs.add(bytes.length);
		}
		List<byte[]> tmps = new ArrayList<>();
		for (int i = 0; i < indexs.size() - 1; i++) {
			int from = indexs.get(i);
			int to = indexs.get(i + 1);
			if (from != 0 && from < (to + 2)) {
				from += 2;
			}
			byte[] bs = Arrays.copyOfRange(bytes, from, to);
			tmps.add(bs);
		}
		return tmps;
	}

	public static List<byte[]> splitByRNRN(byte[] bytes) {
		int off = 0;
		if (bytes.length > 0) {
			for (int i = 1; i < bytes.length - 1; i++) {
				if (bytes[i] == '\r' && bytes[i + 1] == '\n' && bytes[i + 2] == '\r' && bytes[i + 3] == '\n') {
					off = i;
					break;
				}
			}
		}
		List<byte[]> tmp = new ArrayList<>();
		tmp.add(Arrays.copyOfRange(bytes, 0, off));
		if (off + 4 < bytes.length) {
			tmp.add(Arrays.copyOfRange(bytes, off + 4, bytes.length));
		}
		return tmp;
	}

	public static List<byte[]> splitByBoundary(byte[] bytes, String boundary) {
		List<Integer> indexs = new ArrayList<>();
		String b0 = "--" + boundary;
		if (bytes.length > 0 && bytes.length > b0.getBytes().length) {
			indexs.add(0);
			for (int i = 0; i < bytes.length - 1; i++) {
				boolean b = true;
				for (int j = 0; j < b0.getBytes().length; j++) {
					if ((i + j) < bytes.length && bytes[i + j] != b0.getBytes()[j]) {
						b = false;
						break;
					}
				}
				if (b) {
					indexs.add(i);
				}
			}
		}
		if (indexs.get(indexs.size() - 1) < bytes.length) {
			indexs.add(bytes.length);
		}
		List<byte[]> tmps = new ArrayList<>();
		for (int i = 0; i < indexs.size() - 1; i++) {
			int from = indexs.get(i) + b0.getBytes().length + 2;
			int to = indexs.get(i + 1);
			if (from < to) {
				byte[] bs = Arrays.copyOfRange(bytes, from, to);
				tmps.add(bs);
			}
		}
		return tmps;
	}

	public static byte[] getBodyByBoundary(byte[] bytes, String boundary) {
		String b0 = "--" + boundary;
		int index = 0;
		if (bytes.length > 0 && bytes.length > b0.getBytes().length) {
			for (int i = 0; i < bytes.length - 1; i++) {
				boolean b = true;
				for (int j = 0; j < b0.getBytes().length; j++) {
					if ((i + j) < bytes.length && bytes[i + j] != b0.getBytes()[j]) {
						b = false;
						break;
					}
				}
				if (b) {
					index = i;
					break;
				}
			}
		}
		byte[] tmp = new byte[bytes.length - index];
		for (int i = index; i < bytes.length; i++) {
			tmp[i - index] = bytes[i];
		}
		return tmp;
	}

	public static byte[] getHeader(byte[] bytes) {
		int off = 0;
		for (int i = 0; i < bytes.length; i++) {
			if ((i + 3) < bytes.length && bytes[i] == '\r' && bytes[i + 1] == '\n' && bytes[i + 2] == '\r'
					&& bytes[i + 3] == '\n') {
				off = i;
			}
		}
		return Arrays.copyOfRange(bytes, 0, off);
	}

	public static void main(String[] args) {
		byte[] bs = { '5', 'a', '\r', '\n', '\r', '\n', '6', '-', '-', 'a', '7', '6', '6', '\r', '\n', '\r', '\n' };
		List<byte[]> list = splitByRNRN(bs);
		String string0 = new String(list.get(0));
		String string1 = new String(list.get(1));
		System.out.println(string0);
		System.out.println(string1);
	}
}

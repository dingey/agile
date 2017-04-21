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

	public static void main(String[] args) {
		byte[] bs = { '5', 'a', '\r', '\n', '6', '\r', '\n' , '\r', '\n'};
		List<byte[]> split = splitByRN(bs);
		for (byte[] b : split) {
			String string = new String(b);
			System.out.println(string);
		}
	}
}

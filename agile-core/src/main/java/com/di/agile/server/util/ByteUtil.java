package com.di.agile.server.util;

/**
 * @author di
 * @date 2016年12月2日 下午12:32:29
 * @since 1.0.0
 */
public class ByteUtil {
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
}

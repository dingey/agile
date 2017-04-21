package com.di.agile.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author di
 */
public class UrlParamUtil {
	public static Map<String, Object[]> getParamByGet(String url) {
		int from = url.indexOf("?");
		int to = url.indexOf("#");
		if (from == -1) {
			from = 0;
		}
		if (to == -1) {
			from = url.length();
		}
		String str = url.substring(from, to);
		Map<String, Object[]> reqs = new HashMap<>();
		for (String s : str.split("&")) {
			if (s.split("=").length > 1) {
				put(reqs, s.split("=")[0], s.split("=")[1]);
			} else if (s.split("=").length == 1) {
				put(reqs, s.split("=")[0], null);
			}
		}
		return null;
	}
	
	public static Map<String, Object[]> getParamByMultipart(String url,String boundary) {
		
		return null;
	}

	public static void put(Map<String, Object[]> map, String key, Object val) {
		if (map.get(key) == null) {
			Object[] os = new Object[1];
			os[0] = val;
			map.put(key, os);
		} else {
			Object[] os = map.get(key);
			Object[] oss = new Object[os.length + 1];
			for (int i = 0; i < os.length; i++) {
				oss[i] = os[i];
			}
			oss[oss.length - 1] = val;
			map.put(key, oss);
		}
	}

	public static void main(String[] args) {
		String s = "/hi.htm?id=5&name=?sa&age=";
		System.out.println(s);
	}
}

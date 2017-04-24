package com.di.agile.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.di.agile.core.server.bean.MultipartFile;

/**
 * @author di
 */
public class UrlParamUtil {
	public static Map<String, Object[]> getParamByGet(String url) {
		Map<String, Object[]> reqs = new HashMap<>();
		int from = url.indexOf("?") + 1;
		int to = url.indexOf("#");
		if (from == -1) {
			from = 0;
		}
		if (to == -1) {
			to = url.length();
		}
		if (from > to || to > url.length()) {
			return reqs;
		}
		String str = url.substring(from, to);
		for (String s : str.split("&")) {
			if (s.split("=").length > 1) {
				put(reqs, s.split("=")[0], s.split("=")[1]);
			} else if (s.split("=").length == 1) {
				put(reqs, s.split("=")[0], null);
			}
		}
		return reqs;
	}

	public static Map<String, Object[]> getParamByMultipart(byte[] bytes, String boundary) {
		Map<String, Object[]> m = new HashMap<>();
		List<byte[]> bounds = ByteUtil.splitByBoundary(bytes, boundary);
		for (byte[] bs : bounds) {
			List<byte[]> lines = ByteUtil.splitByRN(bs);
			String s = new String(lines.get(0));
			if (s.equals("\r\n")) {
				continue;
			} else if (s.indexOf("filename") == -1) {
				String s0 = s.split(";")[1];
				String name = s0.substring(s0.indexOf("name=")+5).replace("\"", "").trim();
				String val = lines.size() > 1 ? new String(lines.get(2)) : "";
				put(m, name, val);
			} else {
				String s0 = s.split(";")[1];
				String name = s0.substring(s0.indexOf("name=")+5).replace("\"", "").trim();
				String filename = s.substring(s.indexOf("filename=")+9).replace("\"", "").trim();
				MultipartFile f = new MultipartFile();
				f.setOriginalFilename(filename);
				f.setContentType(new String(lines.get(1)).split(":")[1]);
				List<Byte> tmps = new ArrayList<>();
				for (int i = 3; i < lines.size(); i++) {
					for (byte b : lines.get(i)) {
						tmps.add(b);
					}
				}
				byte[] tps = new byte[tmps.size()];
				for (int i = 0; i < tmps.size(); i++) {
					tps[i] = tmps.get(i);
				}
				f.setBytes(tps);
				put(m, name, f);
			}
		}
		return m;
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

package com.di.agile.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author di
 * @date 2016年12月1日 下午1:37:41
 * @since 1.0.0
 */
public class SessionUtil {
	static final char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	static int length = chars.length;
	static ConcurrentMap<String, Map<String, Object>> sessions;
	static Random random = new Random();
	static {
		sessions = new ConcurrentHashMap<>();
	}

	public static String generate() {
		StringBuilder s = new StringBuilder();
		while (s.length() < 8) {
			s.append(chars[random.nextInt(length)]);
		}
		return s.toString();
	}

	public static ConcurrentMap<String, Map<String, Object>> get() {
		if (sessions == null) {
			sessions = new ConcurrentHashMap<>();
		}
		return sessions;
	}

	public static String generateSessionId() {
		String id = generate();
		while (sessions.containsKey(id)) {
			id = generate();
		}
		Map<String, Object> m = new HashMap<String, Object>();
		sessions.put(id, m);
		return id;
	}

	public static boolean contains(String sessionId) {
		if (sessionId == null || sessions.get(sessionId) == null) {
			return false;
		}
		return sessions.containsKey(sessionId);
	}

	public static Map<String, Object> getSession(String sessionId) {
		return sessions.get(sessionId);
	}

	public static void remove(String sessionId) {
		sessions.remove(sessionId);
	}

	public static void putValue(String sessionId, String key, Object value) {
		sessions.get(sessionId).put(key, value);
	}

	public static void removeValue(String sessionId, String key) {
		sessions.get(sessionId).remove(key);
	}

	public static void clear(String sessionId) {
		sessions.put(sessionId, new HashMap<>());
	}

	public static void main(String[] args) {
		String id = generateSessionId();
		System.out.println(id);
		putValue(id, "a", new Date().getTime());
		Map<String, Object> map = getSession(id);
		for (String s : map.keySet()) {
			System.out.println(map.get(s));
		}
	}
}

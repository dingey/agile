package com.di.agile.core.server.bean;

import java.util.HashMap;

/**
 * @author di
 * @date 2016年12月1日 下午2:25:03
 * @since 1.0.0
 */
public class Model {
	HashMap<String, Object> m = new HashMap<>();

	public void setAttribute(Object o) {
		m.put(o.toString(), o);
	}

	public void setAttribute(String key, Object value) {
		m.put(key, value);
	}

	public void put(Object o) {
		m.put(o.toString(), o);
	}

	public void put(String key, Object value) {
		m.put(key, value);
	}

	public void addAttribute(Object o) {
		m.put(o.toString(), o);
	}

	public void addAttribute(String key, Object value) {
		m.put(key, value);
	}

	public HashMap<String, Object> get() {
		return m;
	}
}

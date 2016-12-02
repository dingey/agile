package com.di.agile.core.server.bean;

import com.di.agile.server.util.SessionUtil;

/**
 * @author di
 * @date 2016年12月1日 下午9:45:55
 * @since 1.0.0
 */
public class HttpSession {
	private String sessionId;

	public HttpSession(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setAttribute(String key, Object value) {
		SessionUtil.putValue(getSessionId(), key, value);
	}

	public Object getAttribute(String key) {
		return SessionUtil.getSession(getSessionId()).get(key);
	}

	public void remove(Object o) {
		SessionUtil.removeValue(getSessionId(), o.toString());
	}

	public void clear() {
		SessionUtil.clear(getSessionId());
	}

	public void isValid() {
		SessionUtil.contains(getSessionId());
	}

	public void invalid() {
		SessionUtil.remove(getSessionId());
	}

	public void put(String key, Object value) {
		SessionUtil.putValue(getSessionId(), key, value);
	}

}

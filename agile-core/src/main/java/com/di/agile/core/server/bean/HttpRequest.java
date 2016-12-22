package com.di.agile.core.server.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author di
 */
public class HttpRequest {
	private String path;
	private Map<String, String> reqParams;
	private String method;
	private String host;
	private String connection;
	private String userAgent;
	private String accept;
	private String referer;
	private String acceptEncoding;
	private String acceptLanguage;
	private String cookie;
	private Map<String, String> cookies;
	private String sessionId;

	public HttpRequest(String requestString) {
		String[] ss = requestString.split("\r\n");
		setMethod(ss[0].split(" ")[0]);
		String split = "";
		for (int i = 0; i < ss.length; i++) {
			String s = ss[i];
			if (s.contains("Accept")) {
				setAccept(spit(s));
			} else if (s.indexOf("Accept-Encoding") != -1) {
				setAcceptEncoding(spit(s));
			} else if (s.indexOf("Accept-Language") != -1) {
				setAcceptLanguage(spit(s));
			} else if (s.indexOf("Cookie") != -1) {
				setCookie(spit(s));
			} else if (s.indexOf("Connection") != -1) {
				setConnection(spit(s));
			} else if (s.indexOf("Host") != -1) {
				setHost(spit(s));
			} else if (s.indexOf("Referer") != -1) {
				setReferer(s.substring(s.indexOf(":") + 1, s.indexOf("?") > 0 ? s.indexOf("?") : s.length()).trim());
			}
			if (s.indexOf("sessionId") != -1) {
				String s0 = s.substring(s.indexOf("sessionId="));
				s0 = s0.substring(s0.indexOf("=") + 1, s0.indexOf(";") > 0 ? s0.indexOf(";") : s0.length());
				setSessionId(s0);
			}
			if (s.indexOf("Content-Type") != -1 && s.indexOf("multipart/form-data") != -1) {
				split = s.substring(s.indexOf("boundary=") + 1).trim();
			}
			if (s.startsWith(split)&&!split.isEmpty()) {
				break;
			}
		}
		setPath(ss[0].split(" ")[1]);
	}

	private String spit(String str) {
		String s = "";
		try {
			s = str.split(":")[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (path.indexOf("?") > 0) {
			this.path = path.substring(0, path.indexOf("?"));
			String rq = path.substring(path.indexOf("?") + 1);
			reqParams = new HashMap<>();
			for (String s : rq.split("&")) {
				reqParams.put(s.split("=")[0], s.split("=")[1]);
			}
		} else {
			this.path = path;
		}
	}

	public Map<String, String> getReqParams() {
		return reqParams;
	}

	public void setReqParams(Map<String, String> reqParams) {
		this.reqParams = reqParams;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
		cookies = new HashMap<>();
		for (String s : cookie.split(";")) {
			cookies.put(s.split("=")[0], s.split("=")[1]);
		}
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public static void main(String[] args) {
		HttpRequest hr = new HttpRequest(" /hi.html?p=1");
		hr.setPath("/hi.html?p=1");
		System.out.println(hr.getPath());
		for (String s : hr.getReqParams().keySet()) {
			System.out.println(s);
		}
	}
}

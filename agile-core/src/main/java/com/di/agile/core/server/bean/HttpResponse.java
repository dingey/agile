package com.di.agile.core.server.bean;

import java.util.Date;
import java.util.Map;

import com.di.agile.server.util.ByteUtil;
import com.di.agile.server.util.Str;

/**
 * @author di
 */
public class HttpResponse {
	private String connection;
	private String contentEncoding;
	private String contentType;
	private String contentLength;
	private Map<String, String> cookies;
	private String body;
	private byte[] bodys;
	private String domain;
	private String sessionId;

	public byte[] getBodys() {
		return bodys;
	}

	public void setBodys(byte[] bodys) {
		this.bodys = bodys;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public String getBody() {
		return body == null ? "body is null" : body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String writer() {
		Str s = new Str();
		s.line("HTTP/1.1 200 OK");
		s.add("Content-Type:")
				.line((getContentType() == null || getContentType().isEmpty()) ? "text/plain" : getContentType());
		s.add("Content-Length:").line(String.valueOf(getBody().length()));
		s.add("Date:").line(new Date().toString());
		s.add("Set-Cookie:sessionId=").add(getSessionId()).add(";Max-Age=1800000;").add(" Path=/;").add(" Domain=")
				.add(domain).add("; HttpOnly").newLine();
		if (cookies != null) {
			for (String c : getCookies().keySet()) {
				s.add("Set-Cookie:").add(c).add("=").add(getCookies().get(c)).newLine();
			}
		}
		s.newLine();
		s.add(getBody());
		return s.toString();
	}

	public byte[] writerFile() {
		Str s = new Str();
		s.line("HTTP/1.1 200 OK");
		s.add("Content-Type:").line(getContentType());
		s.add("Content-Length:").line(String.valueOf(getBodys().length));
		s.add("Date:").line(new Date().toString());
		s.newLine();
		byte[] bytes = s.toString().getBytes();
		return ByteUtil.byteMerger(bytes, getBodys());
	}
}

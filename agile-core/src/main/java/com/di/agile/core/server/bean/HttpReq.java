package com.di.agile.core.server.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.di.agile.server.util.ByteUtil;
import com.di.agile.server.util.UrlParamUtil;

/**
 * @author d
 */
public class HttpReq {

	private String[] headers;
	private HttpMethod method;
	private String path;
	private String cacheControl;
	private HttpContentType contentType;
	private String userAgent;
	private String accept;
	private String host;
	private Map<String, String> cookies;
	private String acceptEncoding;
	private String connection;
	private String contentLength;
	private String boundary;
	private Map<String, Object[]> reqs;
	private String sessionId;
	private byte[] body;

	public HttpReq() {
		super();
	}

	public HttpReq(byte[] bytes) {
		List<byte[]> list = ByteUtil.splitByRN(bytes);
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			String s = new String(list.get(i));
			if (s.indexOf("GET") != -1) {
				this.method = HttpMethod.getEnumByName(s);
				this.path = s.split(" ")[1];
				this.reqs = UrlParamUtil.getParamByGet(this.path);
			} else if (s.indexOf("POST") != -1) {
				this.method = HttpMethod.getEnumByName(s);
				this.path = s.split(" ")[1];
			} else if (s.indexOf("cache-control") != -1) {
				this.cacheControl = s.split(":")[1];
			} else if (s.indexOf("Content-Type") != -1) {
				this.contentType = HttpContentType.getEnumByName(s);
				if (this.contentType == HttpContentType.MULTIPART) {
					this.boundary = s.split("=")[1];
				}
			} else if (s.indexOf("User-Agent") != -1) {
				this.userAgent = s.split(":")[1];
			} else if (s.indexOf("Accept") != -1) {
				this.accept = s.split(":")[1];
			} else if (s.indexOf("Host") != -1) {
				this.host = s.split(":")[1];
			} else if (s.indexOf("accept-encoding") != -1) {
				this.acceptEncoding = s.split(":")[1];
			} else if (s.indexOf("Connection") != -1) {
				this.connection = s.split(":")[1];
			} else if (s.isEmpty() && this.method == HttpMethod.GET) {
				return;
			} else if (s.equals("\r\n")) {
				index = i;
				break;
			}
		}
		List<byte[]> subList = list.subList(index, list.size());
		List<Byte> bs = new ArrayList<>();
		for (byte[] bb : subList) {
			for (byte b : bb) {
				bs.add(b);
			}
		}
		byte[] bs0 = new byte[bs.size()];
		for (int i = 0; i < bs0.length; i++) {
			bs0[i] = bs.get(i);
		}
		setBody(bs0);
		if(this.contentType==HttpContentType.FORM_URLENCODED){
			reqs=UrlParamUtil.getParamByGet(new String(getBody()));
		}else if (this.contentType==HttpContentType.MULTIPART) {
			reqs=UrlParamUtil.getParamByMultipart(getBody(),this.boundary);
		}
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object[]> getReqs() {
		return reqs;
	}

	public void setReqs(Map<String, Object[]> reqs) {
		this.reqs = reqs;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	public HttpContentType getContentType() {
		return contentType;
	}

	public void setContentType(HttpContentType contentType) {
		this.contentType = contentType;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public static enum HttpContentType {
		FORM_URLENCODED("application/x-www-form-urlencoded"), MULTIPART("multipart/form-data");

		private String name;

		HttpContentType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static HttpContentType getEnumByName(String name) {
			for (HttpContentType ct : HttpContentType.values()) {
				if (name.indexOf(ct.getName()) != -1) {
					return ct;
				}
			}
			return FORM_URLENCODED;
		}
	}

	public static enum HttpMethod {
		GET("get"), POST("post");

		private String name;

		HttpMethod(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static HttpMethod getEnumByName(String name) {
			for (HttpMethod ct : HttpMethod.values()) {
				if (name.indexOf(ct.getName()) != -1) {
					return ct;
				}
			}
			return GET;
		}
	}
}

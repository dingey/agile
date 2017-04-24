package com.di.agile.server.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.di.agile.annotation.Autowired;
import com.di.agile.annotation.Resource;
import com.di.agile.annotation.ResponseBody;
import com.di.agile.core.server.bean.HttpReq;
import com.di.agile.core.server.bean.HttpResponse;
import com.di.agile.core.server.bean.HttpSession;
import com.di.agile.core.server.bean.Model;
import com.di.agile.server.util.ComponentUtil;
import com.di.agile.server.util.HttpReqUtil;
import com.di.agile.server.util.LogUtil;
import com.di.agile.server.util.SessionUtil;

/**
 * @author di
 */
public class HttpHandler implements Runnable {
	// 就绪的I/O键
	private SelectionKey key;
	private Selector selector;
	private ByteBuffer buffer;
	private SocketChannel channel;
	private HttpReq request;
	private HttpResponse response;
	private String sessionId;

	public HttpHandler(byte[] requestHeader, SelectionKey key) {
		this.key = key;
		request = new HttpReq(requestHeader);
		response = new HttpResponse();
		response.setDomain(request.getHost() == null ? "" : request.getHost().trim());
		if (!SessionUtil.contains(request.getSessionId())) {
			sessionId = SessionUtil.generateSessionId();
		} else {
			sessionId = request.getSessionId();
		}
		response.setSessionId(sessionId);
	}

	@Override
	public void run() {
		handler();
	}

	public void handler() {
		// 从context中得到相应的参数
		buffer = ByteBuffer.allocate(1024);
		selector = key.selector();
		channel = (SocketChannel) key.channel();
		process(sessionId, request, response);
		LogUtil.info("path : " + request.getPath());
		buffer = ByteBuffer.allocate(20240);
		LogUtil.info(response.writer());
		if (response.getBodys() != null) {
			buffer.put(response.writerFile());
		} else {
			buffer.put(response.writer().getBytes());
		}
		// 从写模式，切换到读模式
		buffer.flip();
		try {
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void process(String sessionId, HttpReq request, HttpResponse resp) {
		LogUtil.info("process : " + request.getPath());
		RequestMapper mapper = RequestHandler.maps.get(request.getPath());
		if (mapper == null) {
			InputStream in = null;
			try {
				String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
				LogUtil.info("file path:" + path);
				LogUtil.info("load file path:" + path + request.getPath().replaceFirst("/", ""));
				in = new FileInputStream(path + request.getPath().replaceFirst("/", ""));
				resp.setBody(null);
				byte[] b = new byte[in.available()];
				in.read(b);
				resp.setBodys(b);
			} catch (IOException | URISyntaxException e) {
				resp.setBody("not found.");
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		Class<?> handlerClass = mapper.getHandlerClass();
		Method method = mapper.getInvokeMethod();
		Parameter[] parameters = mapper.getInvokeMethod().getParameters();
		Field[] fs = handlerClass.getDeclaredFields();
		Model m = new Model();
		Object result = null;
		try {
			Object o = handlerClass.newInstance();
			for (Field f : fs) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(Autowired.class)) {
					f.set(o, ComponentUtil.set(f.getName()));
				} else if (f.isAnnotationPresent(Resource.class)) {
					String n = f.getAnnotation(Resource.class).name();
					if (n.equals("")) {
						n = f.getName();
					}
					f.set(o, ComponentUtil.set(n));
				}
			}
			method.setAccessible(true);
			Object[] args = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				Parameter p = parameters[i];
				if (p.getType() == HttpSession.class) {
					args[i] = new HttpSession(sessionId);
				} else if (p.getType() == Model.class) {
					args[i] = m;
				} else {
					args[i] = HttpReqUtil.getVal(p, request.getReqs());
				}
			}
			result = method.invoke(o, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			LogUtil.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		if (method.isAnnotationPresent(ResponseBody.class)) {
			resp.setBody(result.toString());
		} else {
			resp.setBody(FreeMarkerUtil.render((String) result, m));
		}
	}
}

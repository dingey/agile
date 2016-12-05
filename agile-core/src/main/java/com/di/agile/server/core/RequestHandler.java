package com.di.agile.server.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.di.agile.annotation.Autowired;
import com.di.agile.annotation.Component;
import com.di.agile.annotation.Controller;
import com.di.agile.annotation.Repository;
import com.di.agile.annotation.RequestMapping;
import com.di.agile.annotation.RequestMethod;
import com.di.agile.annotation.RequestParam;
import com.di.agile.annotation.Resource;
import com.di.agile.annotation.ResponseBody;
import com.di.agile.annotation.Service;
import com.di.agile.core.server.bean.HttpRequest;
import com.di.agile.core.server.bean.HttpResponse;
import com.di.agile.core.server.bean.HttpSession;
import com.di.agile.core.server.bean.Model;
import com.di.agile.server.util.ClassesUtil;
import com.di.agile.server.util.ComponentUtil;
import com.di.agile.server.util.FieldUtil;
import com.di.agile.server.util.LogUtil;

/**
 * @author di
 * @since 1.0.0
 */
public class RequestHandler {
	static List<RequestMapper> mappers = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Class> components = new HashMap<>();
	static ConcurrentMap<String, RequestMapper> maps = new ConcurrentHashMap<>();

	@SuppressWarnings("rawtypes")
	public static void init(String packagePath) {
		synchronized (maps) {
			ArrayList<Class> list = ClassesUtil.getAllClass(packagePath);
			for (Class<?> c : list) {
				if (c.isAnnotationPresent(Service.class)) {
					String n = c.getAnnotation(Service.class).value();
					if (n.equals("")) {
						n = firstCharLower(c.getSimpleName());
					}
					components.put(n, c);
				} else if (c.isAnnotationPresent(Component.class)) {
					String n = c.getAnnotation(Component.class).value();
					if (n.equals("")) {
						n = firstCharLower(c.getSimpleName());
					}
					components.put(n, c);
				} else if (c.isAnnotationPresent(Repository.class)) {
					String n = c.getAnnotation(Repository.class).value();
					if (n.equals("")) {
						n = firstCharLower(c.getSimpleName());
					}
					components.put(n, c);
				} else if (c.isAnnotationPresent(Controller.class)) {
					Method[] methods = c.getDeclaredMethods();
					for (Method m : methods) {
						m.setAccessible(true);
						if (m.isAnnotationPresent(RequestMapping.class)) {
							RequestMapper mapper = new RequestMapper();
							mapper.setHandlerClass(c);
							mapper.setInvokeMethod(m);
							mapper.setPath(m.getAnnotation(RequestMapping.class).path());
							RequestMethod[] rms = m.getAnnotation(RequestMapping.class).method();
							String[] ls = new String[rms.length];
							for (int i = 0; i < rms.length; i++) {
								ls[i] = rms[i].name();
							}
							mapper.setReqMethod(ls);
							for (String p : mapper.getPath()) {
								maps.put(p, mapper);
								LogUtil.info("path:" + p + " [" + mapper.getClass().getName() + "]");
							}
							mappers.add(mapper);
						}
					}
				}
			}
		}
	}

	public static void process(String sessionId, HttpRequest request, HttpResponse resp) {
		LogUtil.info("process : " + request.getPath());
		RequestMapper mapper = maps.get(request.getPath());
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
		Map<String, String> params = request.getReqParams();
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
				} else {
					String n = f.getName();
					String val = params.get(f.getName());
					if (f.isAnnotationPresent(RequestParam.class)) {
						n = f.getAnnotation(RequestParam.class).name();
						if (n.equals("")) {
							n = f.getName();
						}
						if (val.equals("")) {
							val = f.getAnnotation(RequestParam.class).defaultValue();
						}
					}
					f.set(o, FieldUtil.getVal(f, val));
				}
			}
			method.setAccessible(true);
			if (parameters.length == 0) {
				result = method.invoke(o, null);
			} else if (parameters.length == 1 && parameters[0].getType() == Model.class) {
				result = method.invoke(o, m);
			} else if (parameters.length == 1 && parameters[0].getType() == HttpSession.class) {
				result = method.invoke(o, new HttpSession(sessionId));
			} else if (parameters.length == 2) {
				if (parameters[0].getType() == Model.class && parameters[1].getType() == HttpSession.class) {
					result = method.invoke(o, m, new HttpSession(sessionId));
				} else if (parameters[1].getType() == Model.class && parameters[0].getType() == HttpSession.class) {
					result = method.invoke(o, new HttpSession(sessionId), m);
				}
			}
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

	private static String firstCharLower(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
}

package com.di.agile.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.di.agile.annotation.RequestParam;

/**
 * @author d
 */
public class ReqSetUtil {
	public static Object getVal(Parameter p, Map<String, String[]> reqs) {
		Class<?> type = p.getType();
		String n = p.getName();
		String[] vs = reqs.get(n);
		String val = (vs == null || vs.length == 0) ? "" : vs[0];
		if (p.isAnnotationPresent(RequestParam.class)) {
			n = p.getAnnotation(RequestParam.class).name();
			if (val.isEmpty() || val == null) {
				val = p.getAnnotation(RequestParam.class).defaultValue();
			}
		}
		if (type == byte.class || type == Byte.class) {
			return Byte.valueOf(val);
		} else if (type == short.class || type == Short.class) {
			return Short.valueOf(val);
		} else if (type == int.class || type == Integer.class) {
			return Integer.valueOf(val);
		} else if (type == long.class || type == Long.class) {
			return Long.valueOf(val);
		} else if (type == float.class || type == Float.class) {
			return Float.valueOf(val);
		} else if (type == double.class || type == Double.class) {
			return Double.valueOf(val);
		} else if (type == boolean.class || type == Boolean.class) {
			return Boolean.valueOf(val);
		} else if (type == java.util.Date.class) {
			return RequestUtil.get(val);
		} else if (type == char.class || type == Character.class) {
			if (val != null && !val.isEmpty()) {
				return val.charAt(0);
			}
		} else if (type == String.class) {
			return val;
		} else if (type.isArray()) {
			Type type2 = p.getParameterizedType();
			if (vs.length > 0) {
				Object[] os = new Object[vs.length];
				for (int i = 0; i < os.length; i++) {
					try {
						Object newInstance = Class.forName(type2.getTypeName()).newInstance();
						setVal(newInstance, p.getName(), i, reqs);
						os[i] = newInstance;
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return os;
			}
		} else if (type == java.util.ArrayList.class || type == java.util.List.class) {
			Type type2 = p.getParameterizedType();
			if (vs.length > 0) {
				List<Object> os = new ArrayList<>();
				for (int i = 0; i < vs.length; i++) {
					try {
						Object newInstance = Class.forName(type2.getTypeName()).newInstance();
						setVal(newInstance, p.getName(), i, reqs);
						os.add(newInstance);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return os;
			}
		}else{
			Class<?> type2 = p.getType();
			try {
				Object newInstance = type2.newInstance();
				setVal(newInstance, n, 0, reqs);
				return newInstance;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void setVal(Object o, String prefix, int index, Map<String, String[]> reqs) {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			String n = f.getName();
			if (f.isAnnotationPresent(RequestParam.class) && !f.getAnnotation(RequestParam.class).name().isEmpty()) {
				n = f.getAnnotation(RequestParam.class).name();
			} else {
				if (prefix != null && !prefix.isEmpty()) {
					n = prefix + "." + n;
				}
			}
			String[] vs = reqs.get(n);
			Class<?> type = f.getType();
			String val = (vs == null || vs.length == 0) ? "" : vs[index];
			try {
				if (type == byte.class || type == Byte.class) {
					f.set(o, Byte.valueOf(val.isEmpty()?"0":val));
				} else if (type == short.class || type == Short.class) {
					f.set(o, Short.valueOf(val.isEmpty()?"0":val));
				} else if (type == int.class || type == Integer.class) {
					f.set(o, Integer.valueOf(val.isEmpty()?"0":val));
				} else if (type == long.class || type == Long.class) {
					f.set(o, Long.valueOf(val.isEmpty()?"0":val));
				} else if (type == float.class || type == Float.class) {
					f.set(o, Float.valueOf(val.isEmpty()?"0":val));
				} else if (type == double.class || type == Double.class) {
					f.set(o, Double.valueOf(val.isEmpty()?"0":val));
				} else if (type == boolean.class || type == Boolean.class) {
					f.set(o, Boolean.valueOf(val));
				} else if (type == java.util.Date.class) {
					f.set(o, RequestUtil.get(val));
				} else if (type == char.class || type == Character.class) {
					if (val != null && !val.isEmpty()) {
						f.set(o, val.charAt(0));
					}
				} else if (type == String.class) {
					f.set(o, val);
				} else if (type.isArray()) {
					Type genericType = f.getGenericType();
					Object[] os = new Object[vs.length];
					for (int i = 0; i < vs.length; i++) {
						Object newInstance = Class.forName(genericType.getTypeName()).newInstance();
						setVal(newInstance, n, i, reqs);
						os[i] = newInstance;
					}
					setVal(os, prefix, 0, reqs);
					f.set(o, os);
				} else if (type == java.util.List.class || type == java.util.ArrayList.class) {
					Type genericType = f.getGenericType();
					List<Object> os = new ArrayList<>();
					for (int i = 0; i < vs.length; i++) {
						Object newInstance = Class.forName(genericType.getTypeName()).newInstance();
						setVal(newInstance, n, i, reqs);
						os.add(newInstance);
					}
					setVal(os, prefix, 0, reqs);
					f.set(o, os);
				}
			} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException
					| InstantiationException e) {
				e.printStackTrace();
			}
		}
	}
}

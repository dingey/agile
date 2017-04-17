package com.di.agile.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author d
 */
public class RequestUtil {
	public static Date get(String date) {
		Date d = null;
		if (date != null && !date.isEmpty()) {
			try {
				d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
			} catch (ParseException e) {
				try {
					d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				} catch (ParseException e1) {
					try {
						d = new SimpleDateFormat("hh:mm:ss").parse(date);
					} catch (ParseException e2) {
					}
				}
			}
		}
		return d;
	}

	public static Object getParameterByRequest(Class<?> type, String name, Map<String, String> reqs) {
		String value = reqs.get(name);
		if (type == byte.class || type == Byte.class) {
			return Byte.valueOf(value);
		} else if (type == short.class || type == Short.class) {
			return Short.valueOf(value);
		} else if (type == int.class || type == Integer.class) {
			return Integer.valueOf(value);
		} else if (type == long.class || type == Long.class) {
			return Long.valueOf(value);
		} else if (type == float.class || type == Float.class) {
			return Float.valueOf(value);
		} else if (type == double.class || type == Double.class) {
			return Double.valueOf(value);
		} else if (type == boolean.class || type == Boolean.class) {
			return Boolean.valueOf(value);
		} else if (type == java.util.Date.class) {
			return RequestUtil.get(value);
		} else if (type == char.class || type == Character.class) {
			if (value != null && !value.isEmpty()) {
				return value.charAt(0);
			}
		} else if (type == String.class) {
			return value;
		} else if (type == Object.class && name.indexOf(".") != -1) {
			return set(name, type, reqs);
		}
		return value;
	}

	public static Object set(String n, Class<?> t, Map<String, String> reqs) {
		Field[] fields = t.getDeclaredFields();
		for (Field f : fields) {
			Class<?> type = f.getType();
			String value=reqs.get(n+"."+f.getName());
			if(value==null){
				value=reqs.get(n.substring(n.indexOf(".")+1)+"."+f.getName());
			}
			if (type == byte.class || type == Byte.class) {
				return Byte.valueOf(value);
			} else if (type == short.class || type == Short.class) {
				return Short.valueOf(value);
			} else if (type == int.class || type == Integer.class) {
				return Integer.valueOf(value);
			} else if (type == long.class || type == Long.class) {
				return Long.valueOf(value);
			} else if (type == float.class || type == Float.class) {
				return Float.valueOf(value);
			} else if (type == double.class || type == Double.class) {
				return Double.valueOf(value);
			} else if (type == boolean.class || type == Boolean.class) {
				return Boolean.valueOf(value);
			} else if (type == java.util.Date.class) {
				return RequestUtil.get(value);
			} else if (type == char.class || type == Character.class) {
				if (value != null && !value.isEmpty()) {
					return value.charAt(0);
				}
			} else if (type == String.class) {
				return value;
			} else if (type.isArray()) {
				Type type1 = f.getGenericType();
				ParameterizedType pt = (ParameterizedType) type1;
				Type type2 = pt.getActualTypeArguments()[0];
				String typeName = type2.getTypeName();
				List<Object> os_ = new ArrayList<>();
				for (@SuppressWarnings("unused") String v : value.split("")) {
					Object o0;
					try {
						o0 = Class.forName(typeName).newInstance();
						set(n+"."+f.getName()+"[",type2.getClass(),reqs);
						os_.add(o0);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return set(n+"."+f.getName(), type, reqs);
			} else if (type == Object.class) {
				return set(n+"."+f.getName(), type, reqs);
			}
		}
		return reqs;
	}
	public static void main(String[] args) {
		String s="bod.id";
		System.out.println(s.substring(s.indexOf(".")));
	}
}

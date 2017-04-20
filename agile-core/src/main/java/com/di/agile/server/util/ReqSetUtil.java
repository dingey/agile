package com.di.agile.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
				if (type2 == java.lang.String.class) {
					os = vs;
				} else if (type2 == short.class || type2 == Short.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = Short.valueOf(vs[i]);
					}
				} else if (type2 == int.class || type2 == Integer.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = Integer.valueOf(vs[i]);
					}
				} else if (type2 == long.class || type2 == Long.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = Long.valueOf(vs[i]);
					}
				} else if (type2 == float.class || type2 == Float.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = Float.valueOf(vs[i]);
					}
				} else if (type2 == double.class || type2 == Double.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = Double.valueOf(vs[i]);
					}
				} else if (type2 == java.util.Date.class) {
					for (int i = 0; i < vs.length; i++) {
						os[i] = getDate(vs[i]);
					}
				} else {
					int count = 0;
					Map<String,Integer> ss=new HashMap<>();
					for (String s : reqs.keySet()) {
						if (s.indexOf(n) != -1) {
							Integer in = ss.get(s);
							if(in==null){
								ss.put(s, reqs.get(s).length);
							}else{
								ss.put(s, reqs.get(s).length+in);
							}
						}
					}
					for(String s1:ss.keySet()){
						if(ss.get(s1)>count){
							count=ss.get(s1);
						}
					}
					for (int i = 0; i < count; i++) {
						try {
							Object newInstance = Class.forName(type2.getTypeName()).newInstance();
							setVal(newInstance, p.getName(), i, reqs);
							os[i] = newInstance;
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				return os;
			}
		} else if (type == java.util.ArrayList.class || type == java.util.List.class) {
			Type type2 = p.getParameterizedType();
			if (vs.length > 0) {
				List<Object> os = new ArrayList<>();
				if (type2 == java.lang.String.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(vs[i]);
					}
				} else if (type2 == short.class || type2 == Short.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(Short.valueOf(vs[i]));
					}
				} else if (type2 == int.class || type2 == Integer.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(Integer.valueOf(vs[i]));
					}
				} else if (type2 == long.class || type2 == Long.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(Long.valueOf(vs[i]));
					}
				} else if (type2 == float.class || type2 == Float.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(Float.valueOf(vs[i]));
					}
				} else if (type2 == double.class || type2 == Double.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(Double.valueOf(vs[i]));
					}
				} else if (type2 == java.util.Date.class) {
					for (int i = 0; i < vs.length; i++) {
						os.add(getDate(vs[i]));
					}
				} else {
					int count = 0;
					Map<String,Integer> ss=new HashMap<>();
					for (String s : reqs.keySet()) {
						if (s.indexOf(n) != -1) {
							Integer in = ss.get(s);
							if(in==null){
								ss.put(s, reqs.get(s).length);
							}else{
								ss.put(s, reqs.get(s).length+in);
							}
						}
					}
					for(String s1:ss.keySet()){
						if(ss.get(s1)>count){
							count=ss.get(s1);
						}
					}
					for (int i = 0; i < count; i++) {
						try {
							Object newInstance = Class.forName(type2.getTypeName()).newInstance();
							setVal(newInstance, p.getName(), i, reqs);
							os.add(newInstance);
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				return os;
			}
		} else {
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
			String[] vs = reqs.get(n) == null ? new String[0] : reqs.get(n);
			Class<?> type = f.getType();
			String val = (vs == null || vs.length == 0) ? "" : vs[index];
			try {
				if (type == byte.class || type == Byte.class) {
					f.set(o, Byte.valueOf(val.isEmpty() ? "0" : val));
				} else if (type == short.class || type == Short.class) {
					f.set(o, Short.valueOf(val.isEmpty() ? "0" : val));
				} else if (type == int.class || type == Integer.class) {
					f.set(o, Integer.valueOf(val.isEmpty() ? "0" : val));
				} else if (type == long.class || type == Long.class) {
					f.set(o, Long.valueOf(val.isEmpty() ? "0" : val));
				} else if (type == float.class || type == Float.class) {
					f.set(o, Float.valueOf(val.isEmpty() ? "0" : val));
				} else if (type == double.class || type == Double.class) {
					f.set(o, Double.valueOf(val.isEmpty() ? "0" : val));
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
					if (genericType == java.lang.String.class) {
						os = vs;
					} else if (genericType == short.class || genericType == Short.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = Short.valueOf(vs[i]);
						}
					} else if (genericType == int.class || genericType == Integer.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = Integer.valueOf(vs[i]);
						}
					} else if (genericType == long.class || genericType == Long.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = Long.valueOf(vs[i]);
						}
					} else if (genericType == float.class || genericType == Float.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = Float.valueOf(vs[i]);
						}
					} else if (genericType == double.class || genericType == Double.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = Double.valueOf(vs[i]);
						}
					} else if (genericType == java.util.Date.class) {
						for (int i = 0; i < vs.length; i++) {
							os[i] = getDate(vs[i]);
						}
					} else {
						int count = 0;
						Map<String,Integer> ss=new HashMap<>();
						for (String s : reqs.keySet()) {
							if (s.indexOf(n) != -1) {
								Integer in = ss.get(s);
								if(in==null){
									ss.put(s, reqs.get(s).length);
								}else{
									ss.put(s, reqs.get(s).length+in);
								}
							}
						}
						for(String s1:ss.keySet()){
							if(ss.get(s1)>count){
								count=ss.get(s1);
							}
						}
						for (int i = 0; i < count; i++) {
							Object newInstance = Class.forName(genericType.getTypeName()).newInstance();
							setVal(newInstance, n, i, reqs);
							os[i] = newInstance;
						}
						setVal(os, prefix, 0, reqs);
					}
					f.set(o, os);
				} else if ((type == java.util.List.class || type == java.util.ArrayList.class) && vs != null) {
					Type genericType = f.getGenericType();
					List<Object> os = new ArrayList<>();
					if (genericType == java.lang.String.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(vs[i]);
						}
					} else if (genericType == short.class || genericType == Short.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(Short.valueOf(vs[i]));
						}
					} else if (genericType == int.class || genericType == Integer.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(Integer.valueOf(vs[i]));
						}
					} else if (genericType == long.class || genericType == Long.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(Long.valueOf(vs[i]));
						}
					} else if (genericType == float.class || genericType == Float.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(Float.valueOf(vs[i]));
						}
					} else if (genericType == double.class || genericType == Double.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(Double.valueOf(vs[i]));
						}
					} else if (genericType == java.util.Date.class) {
						for (int i = 0; i < vs.length; i++) {
							os.add(getDate(vs[i]));
						}
					} else {
						int count = 0;
						Map<String,Integer> ss=new HashMap<>();
						for (String s : reqs.keySet()) {
							if (s.indexOf(n) != -1) {
								Integer in = ss.get(s);
								if(in==null){
									ss.put(s, reqs.get(s).length);
								}else{
									ss.put(s, reqs.get(s).length+in);
								}
							}
						}
						for(String s1:ss.keySet()){
							if(ss.get(s1)>count){
								count=ss.get(s1);
							}
						}
						for (int i = 0; i < count; i++) {
							ParameterizedType pt=(ParameterizedType)genericType;
							Type type2 = pt.getActualTypeArguments()[0];
							Object newInstance = Class.forName(type2.getTypeName()).newInstance();
							setVal(newInstance, n, i, reqs);
							os.add(newInstance);
						}
					}
					f.set(o, os);
				}
			} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException
					| InstantiationException e) {
				System.err.println(f.getName());
				e.printStackTrace();
			}
		}
	}

	static Date getDate(String s) {
		Date d = null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(s);
		} catch (ParseException e) {
			try {
				d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			} catch (ParseException e1) {
				try {
					d = new SimpleDateFormat("hh:mm:ss").parse(s);
				} catch (ParseException e2) {
					e2.printStackTrace();
				}
			}
		}
		return d;
	}
}

package com.di.agile.server.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author di
 */
public class FieldUtil {
	public static Object getVal(Field p, String obj) {
		if (p.getType() == byte.class || p.getType() == java.lang.Byte.class) {
			return Byte.valueOf(obj);
		} else if (p.getType() == short.class || p.getType() == java.lang.Short.class) {
			return Short.valueOf(obj);
		} else if (p.getType() == int.class || p.getType() == java.lang.Integer.class) {
			return Integer.valueOf(obj);
		} else if (p.getType() == long.class || p.getType() == java.lang.Long.class) {
			return Long.valueOf(obj);
		} else if (p.getType() == double.class || p.getType() == java.lang.Double.class) {
			return Double.valueOf(obj);
		} else if (p.getType() == float.class || p.getType() == java.lang.Float.class) {
			return Float.valueOf(obj);
		} else if (p.getType() == String.class || p.getType() == java.lang.String.class) {
			return obj;
		} else if (p.getType() == boolean.class || p.getType() == java.lang.Boolean.class) {
			if (obj.equals("1") || obj.equals("true")) {
				return true;
			} else {
				return false;
			}
		} else if (p.getType() == Date.class) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(obj);
			} catch (ParseException e) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(obj);
				} catch (ParseException e1) {
					return new Date(Long.valueOf(obj));
				}
			}
		}
		return null;
	}
}

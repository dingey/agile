package com.di.agile.server.util;

import java.lang.reflect.Field;

import com.di.agile.annotation.Autowired;
import com.di.agile.annotation.Component;
import com.di.agile.annotation.Repository;
import com.di.agile.annotation.Service;
import com.di.agile.server.core.RequestHandler;

/**
 * @author di
 */
public class ComponentUtil {
	public static Object set(String name) {
		@SuppressWarnings("rawtypes")
		Class c1 = RequestHandler.components.get(name);
		Object o = null;
		try {
			o = c1.newInstance();
			Field[] fs = o.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				if (f.isAnnotationPresent(Component.class) || f.isAnnotationPresent(Autowired.class)
						|| f.isAnnotationPresent(Service.class) || f.isAnnotationPresent(Repository.class)) {
					f.set(o, set(f.getName()));
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}
}

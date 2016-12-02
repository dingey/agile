package com.di.agile.server.core;

import java.lang.reflect.Method;

/**
 * @author di
 * @date 2016年12月1日 下午2:20:12
 * @since 1.0.0
 */
public class RequestMapper {
	private String[] path;
	private String[] reqMethod;
	private Class<?> handlerClass;
	private Method invokeMethod;

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}

	public String[] getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String[] reqMethod) {
		this.reqMethod = reqMethod;
	}

	public Class<?> getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class<?> handlerClass) {
		this.handlerClass = handlerClass;
	}

	public Method getInvokeMethod() {
		return invokeMethod;
	}

	public void setInvokeMethod(Method invokeMethod) {
		this.invokeMethod = invokeMethod;
	}

}

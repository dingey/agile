package com.di.agile.server;

import java.util.Date;
import com.di.agile.server.core.FreeMarkerUtil;
import com.di.agile.server.core.RequestHandler;
import com.di.agile.server.core.Server;

/**
 * @author di
 */
public class AgileServer {
	public static void start(int port, String pagePath, String actionPath) {
		long start = new Date().getTime();
		FreeMarkerUtil.init(pagePath);
		RequestHandler.init(actionPath);
		new Thread(new Server(port)).start();
		long end = new Date().getTime();
		System.out.println("server start " + (end - start) + " ms.");
	}

	public static void main(String[] args) {
		start(8081, "template", "com.controller");
	}
}

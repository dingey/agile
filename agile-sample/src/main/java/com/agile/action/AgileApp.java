package com.agile.action;

import com.di.agile.server.AgileServer;

/**
 * @author di
 * @date 2016年12月2日 下午5:03:02
 * @since 1.0.0
 */
public class AgileApp {
	public static void main(String[] args) {
		AgileServer.start(8090, "template", "com.agile");
	}
}

package com.agile.action;

import com.di.agile.server.AgileServer;

/**
 * @author di
 */
public class AgileApp {
	public static void main(String[] args) {
		AgileServer.start(8090, "template", "com.agile");
	}
}

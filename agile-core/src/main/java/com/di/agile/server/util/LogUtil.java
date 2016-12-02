package com.di.agile.server.util;

/**
 * @author di
 * @date 2016年12月1日 下午3:35:41
 * @since 1.0.0
 */
public class LogUtil {
	private static LogLevel level = LogLevel.info;
	private static LogMode mode = LogMode.console;

	public static enum LogLevel {
		info, error, debug, close;
	}

	public static enum LogMode {
		console, file, none;
	}

	public static void setLevel(LogLevel level) {
		LogUtil.level = level;
	}

	public static void setMode(LogMode mode) {
		LogUtil.mode = mode;
	}

	public static void println(String s) {
		if (mode == null || mode.equals(LogMode.console) || mode.equals(LogMode.file)) {
			System.out.println(s);
		}
	}

	public static void println0(String s) {
		if (mode == null || mode.equals(LogMode.console) || mode.equals(LogMode.file)) {
			System.err.println(s);
		}
	}

	public static void info(String s) {
		if (level == null || level.equals(LogLevel.info)) {
			println(s);
		}
	}

	public static void error(String s) {
		if (level == null || level.equals(LogLevel.error)) {
			println0(s);
		}
	}

	public static void debug(String s) {
		if (level == null || level.equals(LogLevel.debug)) {
			println(s);
		}
	}
}

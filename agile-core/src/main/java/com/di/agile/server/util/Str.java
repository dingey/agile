package com.di.agile.server.util;

/**
 * @author di
 */
public class Str {
	StringBuilder s = new StringBuilder();

	public Str add(String str) {
		s.append(str);
		return this;
	}

	public Str line(String str) {
		return add(str).newLine();
	}

	public Str newLine() {
		s.append(System.getProperty("line.separator", "\n"));
		return this;
	}

	public Str empty() {
		s = new StringBuilder();
		return this;
	}

	public Str empty(String str) {
		s = new StringBuilder(str);
		return this;
	}

	@Override
	public String toString() {
		return s.toString();
	}

}

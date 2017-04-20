package com.agile.form;

import com.di.agile.annotation.RequestParam;

/**
 * @author d
 */
public class Man {
	private int id;
	private String name;
	@RequestParam(name = "birth_date")
	private String birthDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

}

package com.agile.form;

import java.util.Date;
import java.util.List;

import com.di.agile.annotation.RequestParam;
import com.di.toolkit.data.annotation.DateFormat;

/**
 * @author d
 */
public class Man {
	private int id;
	private String name;
	@DateFormat(pattern="yyyy年MM月dd日")
	@RequestParam(name = "birth_date")
	private Date birthDate;
	private List<Child> strs;

	public List<Child> getStrs() {
		return strs;
	}

	public void setStrs(List<Child> strs) {
		this.strs = strs;
	}

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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

}

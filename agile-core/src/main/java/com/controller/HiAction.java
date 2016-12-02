package com.controller;

import com.di.agile.annotation.Controller;
import com.di.agile.annotation.RequestMapping;
import com.di.agile.annotation.RequestParam;
import com.di.agile.core.server.bean.HttpSession;
import com.di.agile.core.server.bean.Model;

/**
 * @author di
 * @date 2016年12月1日 下午4:51:00
 * @since 1.0.0
 */
@Controller
public class HiAction {
	@RequestParam(defaultValue = "s")
	private String name;

	@RequestMapping(path = "/hi.htm")
	public String hi(Model m, HttpSession session) {
		session.setAttribute("name", name);
		m.addAttribute("name", name);
		return "hi.ftl";
	}

	@RequestMapping(path = "/hi2.htm")
	public String hi2(Model m, HttpSession session) {
		String n = (String) session.getAttribute("name");
		m.addAttribute("name", n);
		return "hi.ftl";
	}
}

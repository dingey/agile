package com.agile.action;

import com.agile.form.Man;
import com.agile.service.HiService;
import com.di.agile.annotation.Autowired;
import com.di.agile.annotation.Controller;
import com.di.agile.annotation.RequestMapping;
import com.di.agile.annotation.ResponseBody;
import com.di.agile.core.server.bean.HttpSession;
import com.di.agile.core.server.bean.Model;
import com.di.toolkit.JsonUtil;

/**
 * @author di
 * @date 2016年12月1日 下午4:51:00
 * @since 1.0.0
 */
@Controller
public class HiAction {

	@Autowired
	private HiService hiService;

	@RequestMapping(path = "/hi.htm")
	public String hi(String name, Model m, HttpSession session) {
		String n0 = hiService.hi();
		session.setAttribute("name", name);
		m.addAttribute("name", name);
		m.addAttribute("n0", n0);
		return "hi.ftl";
	}

	@RequestMapping(path = "/hi2.htm")
	public String hi2(Model m, HttpSession session) {
		String n = (String) session.getAttribute("name");
		String n0 = hiService.hi();
		m.addAttribute("name", n);
		m.addAttribute("n0", n0);
		return "hi.ftl";
	}

	@ResponseBody
	@RequestMapping(path = "/say.htm")
	public String say(int age, String name) {
		return "success";
	}

	@ResponseBody
	@RequestMapping(path = "/m.htm")
	public String m(Man m) {
		return JsonUtil.toJson(m);
	}
}

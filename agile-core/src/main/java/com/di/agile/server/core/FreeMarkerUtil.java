package com.di.agile.server.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import com.di.agile.core.server.bean.Model;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author di
 */
public class FreeMarkerUtil {
	@SuppressWarnings("deprecation")
	static Configuration cfg = new Configuration();
	static {
		init("template");
	}

	public static void init(String path) {
		cfg.setTemplateLoader(new ClassTemplateLoader(FreeMarkerUtil.class, "/" + path));
	}

	@SuppressWarnings("deprecation")
	public static String render(String file, Model model) {
		Writer out = new OutputStreamWriter(System.out);
		String s = "";
		try {
			Template template = cfg.getTemplate(file == null ? "hi.ftl" : file);
			StringWriter stringWriter = new StringWriter();
			BufferedWriter writer = new BufferedWriter(stringWriter);
			template.setEncoding("UTF-8");
			template.process(model.get(), writer);
			s = stringWriter.toString();
		} catch (TemplateException | IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
}

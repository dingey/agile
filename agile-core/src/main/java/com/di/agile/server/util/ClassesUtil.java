package com.di.agile.server.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author di
 */
public class ClassesUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Class> getAllClassByInterface(Class clazz) {
		ArrayList<Class> list = new ArrayList<>();
		// 判断是否是一个接口
		if (clazz.isInterface()) {
			try {
				ArrayList<Class> allClass = getAllClass(clazz.getPackage().getName());
				for (int i = 0; i < allClass.size(); i++) {
					if (clazz.isAssignableFrom(allClass.get(i))) {
						if (!clazz.equals(allClass.get(i))) {// 自身并不加进去
							list.add(allClass.get(i));
						} else {

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
		return list;
	}

	/**
	 * 从一个指定路径下查找所有的类
	 * 
	 * @param packagename 包路径
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<Class> getAllClass(String packagename) {
		ArrayList<Class> list = new ArrayList<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packagename.replace('.', '/');
		try {
			ArrayList<File> fileList = new ArrayList<>();
			/**
			 * 这里面的路径使用的是相对路径 如果大家在测试的时候获取不到，请理清目前工程所在的路径 使用相对路径更加稳定！
			 * 另外，路径中切不可包含空格、特殊字符等
			 */
			Enumeration<URL> enumeration = classLoader.getResources("" + path);
			while (enumeration.hasMoreElements()) {
				URL url = enumeration.nextElement();
				fileList.add(new File(url.getFile()));
			}
			for (int i = 0; i < fileList.size(); i++) {
				list.addAll(findClass(fileList.get(i), packagename));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类 如果file本身是类文件，则加入list中进行保存，并返回
	 * 
	 * @param file
	 * @param packagename
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static ArrayList<Class> findClass(File file, String packagename) {
		ArrayList<Class> list = new ArrayList<>();
		if (!file.exists()) {
			return list;
		}
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.isDirectory()) {
				assert !file2.getName().contains(".");// 添加断言用于判断
				ArrayList<Class> arrayList = findClass(file2, packagename + "." + file2.getName());
				list.addAll(arrayList);
			} else if (file2.getName().endsWith(".class")) {
				try {
					// 保存的类文件不需要后缀.class
					list.add(Class
							.forName(packagename + '.' + file2.getName().substring(0, file2.getName().length() - 6)));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		System.out.println(path);
		path = ClassesUtil.class.getClassLoader().getResource("").getPath();
		System.out.println(path);
		path = path.replaceFirst("test-classes", "classes");
		System.out.println(path);
	}
}

package com.agile.service;

import com.agile.dao.HiDao;
import com.di.agile.annotation.Autowired;
import com.di.agile.annotation.Service;

/**
 * @author di
 * @date 2016年12月2日 下午10:09:24
 * @since 1.0.0
 */
@Service
public class HiService {
	@Autowired
	private HiDao dao;

	public String hi() {
		return dao.hi();
	}
}

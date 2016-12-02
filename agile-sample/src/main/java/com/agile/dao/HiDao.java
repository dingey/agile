package com.agile.dao;

import com.di.agile.annotation.Repository;

/**
 * @author di
 * @date 2016年12月2日 下午10:05:33
 * @since 1.0.0
 */
@Repository("dao")
public class HiDao {
	public String hi() {
		return "Alice";
	}
}

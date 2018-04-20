package com.yuyenews.aop.traction;

import com.yuyenews.aop.base.BaseAop;

/**
 * 事务管理aop
 * 
 * @author yuye
 *
 */
public class TractionAop implements BaseAop {

	@Override
	public void startMethod(Object[] args) {
		/* TODO(待mybatis兼容包开发后，完善此处代码) */
		System.out.println("开启事务");
	}

	@Override
	public void endMethod(Object[] args) {
		/* TODO(待mybatis兼容包开发后，完善此处代码) */
		System.out.println("事务提交");
	}

	@Override
	public void exp() {
		/* TODO(待mybatis兼容包开发后，完善此处代码) */
		System.out.println("事务回滚");
	}
}

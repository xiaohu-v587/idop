package com.goodcol.exception;

/**
 * 角色互斥异常类
 */
public class RoleRejectException extends Exception {

	private static final long serialVersionUID = 1414706976411450222L;

	public RoleRejectException() {
		super();
	}

	public RoleRejectException(String msg) {
		super(msg);
	}
}

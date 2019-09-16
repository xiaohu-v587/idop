package com.goodcol.util.ext.anatation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogBind {
	//操作的菜单
	String menuname() default "";
	//操作类型
	String type() default "";
	//备注信息
	String remark() default "";
}

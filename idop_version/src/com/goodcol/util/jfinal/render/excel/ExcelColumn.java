package com.goodcol.util.jfinal.render.excel;

import java.util.List;

public class ExcelColumn {
	//列名
	private String title;
	//对应字段名
	private String field;
	//列宽
	private int width=0;
	//子列
	private List<ExcelColumn> children;
	
	public ExcelColumn(String title, String field, int width,List<ExcelColumn> children) {
		this.title = title;
		this.field = field;
		this.width = width;
		this.children = children;
	}
	public ExcelColumn() {
	}
	public ExcelColumn(String title, String field, int width) {
		this.title = title;
		this.field = field;
		this.width = width;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public List<ExcelColumn> getChildren() {
		return children;
	}
	public void setChildren(List<ExcelColumn> children) {
		this.children = children;
	}
	
}

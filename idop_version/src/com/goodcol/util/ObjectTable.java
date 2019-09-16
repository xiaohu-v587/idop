package com.goodcol.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.goodcol.core.plugin.activerecord.Record;




public class ObjectTable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1615860527268877533L;
	/**
	 * 日志记录器
	 */
	
	protected String tableName;//声明表名
	protected String comment;//注释
	protected List<Record> fileds = new ArrayList<Record>();//声明字段集合
	
	public String createSql(){
		StringBuffer table = new StringBuffer();
//		StringBuffer comment = new StringBuffer();
		table.append("create table "+getTableName()+"(");
//		comment.append("comment on table "+getTableName()+" is "+"'"+getComment()+"'"+"; \n");
		Iterator<Record> it = getFileds().iterator();
		while (it.hasNext()) {
			Record  column = it.next();
			if("ID".equals(column.getStr("name"))){
				table.append(" "+column.getStr("name")+"   "+column.getStr("type")+"("+column.get("length")+")  NOT NULL  PRIMARY KEY\n");
			}else{
				if("number".equals(column.getStr("type"))){
					table.append(" "+column.getStr("name")+"   "+column.getStr("type")+"("+column.get("length")+",2) "+(column.get("null")?" ":" NOT NULL \n"));

				}else{
					table.append(" "+column.getStr("name")+"   "+column.getStr("type")+"("+column.get("length")+") "+(column.get("null")?" ":" NOT NULL \n"));

				}
			}
//			if(!column.getComment().isEmpty()){
//				comment.append("comment on column "+getTableName()+"."+column.getName()+" is "+ "'" +column.getComment()+ "'" +"; \n");
//			}
			if(it.hasNext()){
				table.append(",");
			}
		}
		table.append(") \n");
		return table.toString().toUpperCase();
	}
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Record> getFileds() {
		return fileds;
	}
	public ObjectTable setFiled(Record filed) {
		this.fileds.add(filed);
		return this;
	}
	public ObjectTable setFileds(List<Record> objectColumnVOs) {
		this.fileds.addAll(objectColumnVOs);
		return this;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}


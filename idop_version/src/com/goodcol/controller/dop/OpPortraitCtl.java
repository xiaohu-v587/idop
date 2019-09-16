package com.goodcol.controller.dop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.anatation.RouteBind;


@RouteBind(path = "/opPortrait")
@Before({ManagerPowerInterceptor.class})
public class OpPortraitCtl extends BaseCtl{

	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		String key = getPara("key");
		String style = getPara("style");
		//和数据权限有关的  加最大权限机构条件 
		String  orgid =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		 
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}
		}
		String orgnum = getPara("orgnum");
		if(AppUtils.StringUtil(orgnum) != null){
			orgid = orgnum;
		}
		setAttr("org", orgid);
		render("main.jsp");
	}
	
	/**
	 * 加载机构图谱主页面
	 */
	//@Before(PermissionInterceptor.class)
	public void indexOrg() {
		String  orgid = getPara("orgnum");
		
		setAttr("org", orgid);
		render("index_org.jsp");
	}
	
	
	/**
	 * 加载人员图谱主页面
	 */
	//@Before(PermissionInterceptor.class)
	public void indexPerson() {
		String  orgid = getPara("orgnum");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month = sdf.format(ci.getTime()); 
		setAttr("month", month);
		setAttr("org", orgid);
		render("index_rw.jsp");
	}
	
	
	public void getTab(){
		String orgid = getPara("orgid");
//		String orgid="013001012";
		List<Record> tabList = new ArrayList<>();
		Record r=new Record(); 
		String sql="select indicator_code from idop_graph_val_list l,sys_org_info i " +
				"where l.field01=i.bancsid and i.id=? and field03 is null " +
				"group by indicator_code ";
		List<Record> records=Db.use("gbase").find(sql,orgid);
		for (Record record : records) {
			String field1=record.getStr("indicator_code");
			r=Db.findFirst("select indicator_code, indicator_name from dop_graph where indicator_code=? ",field1);
			String field2=r.getStr("indicator_name");
			tabList.add(new Record().set("indicator_code", field1).set("indicator_name", field2));
		}
		List<Record> headers = new ArrayList<>();
		headers.add(new Record().set("text","标签内容" ).set("field","indicator_code"));
		headers.add(new Record().set("text","标签描述" ).set("field","indicator_name"));
		
		setAttr("headers",headers);
		setAttr("data", tabList);
		renderJson();
	}
	
	public void getBaseData(){
		List<Record> seriesList = new ArrayList<>();
		Record r=new Record(); 
		String orgid = getPara("orgid");
		String sql="select field02,indicator_code,indicator_value from idop_graph_val_list l,sys_org_info i " +
				"where l.field01=i.bancsid and i.id=? and field03 is null " +
				"group by field02,indicator_code ,indicator_value";
		List<Record> records=Db.use("gbase").find(sql,orgid);
		for (Record record : records) {
			String field1=record.getStr("indicator_code");
			String field2=record.getStr("field02");
			r=Db.findFirst("select indicator_name from dop_graph where indicator_code=? ",field1);
			String field3=r.getStr("indicator_name");
			String value=record.getStr("indicator_value");
			seriesList.add(new Record().set("name", field3+field2).set("value", value));
		}
		setAttr("series", seriesList);
		renderJson();
	}
	
	/**
	 * 人员基本信息
	 */	
	public void getEmpBasicInfo() {
		String empId = getPara("followed_teller");
		Record record = Db.findFirst("select name, phone, (select orgname from " +
				"sys_org_info soi where soi.id = sui.org_id) org, " +
				"(case gender when '1' then '男' when '2' then '女' end) gender, " +
				"substr(WORKTIME, 0, 10) worktime, substr(BIRTHDAY, 0, 10) birthday " +
				"from sys_user_info sui where user_no = ?", empId);
		setAttr("data", record);
		renderJson();
	}
	
	public void getWords(){
		List<Record> seriesList = new ArrayList<>();
		Record r=new Record(); 
		String orgid = getPara("orgid");
		String empId = getPara("followed_teller");
		List<Record> records = null;
		if (AppUtils.StringUtil(empId) == null) {
			String sql = "select distinct field03, indicator_code, indicator_value from idop_graph_val_list l,sys_org_info i " +
					"where l.field01=i.bancsid and i.id=? and field03 is not null";
			records=Db.use("gbase").find(sql,orgid);
		} else {
			String sql="select field03,indicator_code,indicator_value from idop_graph_val_list l where l.field02 = ? ";
			records=Db.use("gbase").find(sql,empId);
		}
		for (Record record : records) {
			String field1=record.getStr("indicator_code");
			String field3=record.getStr("field03");
			r=Db.findFirst("select indicator_name from dop_graph where indicator_code=? ",field1);
			String tabName=r.getStr("indicator_name");
			String value=record.getStr("indicator_value");
			seriesList.add(new Record().set("name", tabName+field3).set("value", value));
		}
		setAttr("series", seriesList);
		renderJson();
	}
	
	public void getEmpTab(){
		String orgid = getPara("orgid");
		String empId = getPara("followed_teller");
		List<Record> tabList = new ArrayList<>();
		Record r=new Record(); 
		List<Record> records = null;
		if (AppUtils.StringUtil(empId) == null) {
			String sql = "select distinct indicator_code from idop_graph_val_list l,sys_org_info i " +
					"where l.field01=i.bancsid and i.id=? and field03 is not null ";
			records=Db.use("gbase").find(sql,orgid);
		} else {
			String sql="select indicator_code from idop_graph_val_list l where field02 = ?";
			records=Db.use("gbase").find(sql,empId);
		}
		for (Record record : records) {
			String field1=record.getStr("indicator_code");
			r=Db.findFirst("select indicator_code, indicator_name from dop_graph where indicator_code=? ",field1);
			String field2=r.getStr("indicator_name");
			tabList.add(new Record().set("indicator_code", field1).set("indicator_name", field2));
		}
		setAttr("data", tabList);
		renderJson();
	}
}

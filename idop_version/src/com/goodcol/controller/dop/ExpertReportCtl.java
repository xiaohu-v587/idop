package com.goodcol.controller.dop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * BI报表导入
 * @author 陆磊磊
 * @date 2018-11-26
 */
@RouteBind(path = "/expertReport")
@Before({ManagerPowerInterceptor.class})
public class ExpertReportCtl extends BaseCtl{

	/**
	 * 加载BI导入主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		// TODO Auto-generated method stub
		render("index.jsp");
	}
	
	public void getTicket(){
		URL url;
		HttpURLConnection connect = null;
		String userID ="041376";//当前userid只支持041376  032067
		//String userID =getCurrentUser().getStr("USER_NO");
		String address = ParamContainer.getDictName("ticketaddress", "ticketaddress");
		String BIaddress = ParamContainer.getDictName("BIaddress", "BIaddress");
		String openresource = "openresource.jsp";
//		String resid = "I40280e81016368f168f13b8801636972c31f0501";//后续数据库进行查询
		String page=getPara("page");
		
		String sql="select resid from dop_expert_config where id=?";
		Record record=Db.findFirst(sql,page);
		
		String  resid = record.getStr("resid");//后续数据库进行查询
		String biaddress = BIaddress+openresource+"?";
		//String ticket="Iu1ete6fQxkTF9z8qG1jk6knpkhDyi+Ly/cmJRNtGBo=|t6S7/awr8yU=|JuFq5jDTaRM=|Y0hZ/JGXpr+ciUR4ZWIzf75gKixqgFVA|Y0hZ/JGXpr+WoDhTIznPy1T4bShMZ3/1|y/cmJRNtGBo=";
		String ticket="";
		try {
			url = new URL(address+userID);
			try {
				connect = (HttpURLConnection) url.openConnection();
				connect.setRequestMethod("GET");
				connect.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
				String line = "";
				StringBuffer buffer = new StringBuffer();
				while((line =reader.readLine())!=null){
					buffer.append(line);
				}
				reader.close();
				connect.disconnect();
				String datas = buffer.toString();
				if(datas.contains(">")&&datas.contains("<")){
					String[] str1 = datas.split(">");
					String[] str2 = str1[2].split("<");
					ticket=str2[0];
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        

		setAttr("biaddress",biaddress+"resid="+resid+"&ticket=");
		setAttr("ticket",ticket);
		renderJson();
	}        
	
	
	/**
	 * 获取页面名称
	 */
	public void getPageName(){
		String val = getPara("val");
		String sql="";
			sql = "select id,page from dop_expert_config where module=? and dict ='1' order by createtime asc";
		
		List<Record> list = Db.find(sql,val);
		setAttr("data",list);
		renderJson();
	}
}

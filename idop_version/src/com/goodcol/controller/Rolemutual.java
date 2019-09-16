package com.goodcol.controller;

import java.util.ArrayList;
import java.util.List;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.Constant;
import com.goodcol.util.ext.anatation.RouteBind;
/**
 * 印章申请
 *
 * @author tu
 */
@RouteBind(path = "/rolemutual")
@Before({ ManagerPowerInterceptor.class })
public class Rolemutual extends BaseCtl {

	protected Logger log = Logger.getLogger(Rolemutual.class);

	@Override
	public void index() {
		render("index.jsp");
	}

	public void query(){
		setAttr("yzlx", getPara("yzlx"));
		render("query.jsp");
	}
	/**
	 * 获得首页列表数据
	 */
	 public void getList(){
		String reject=getPara("reject");
		String role=getPara("role");
		
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		StringBuffer sb=new StringBuffer();
		if(AppUtils.StringUtil(reject)!=null){
		 sb.append(" and c.id=?");
		 listStr.add(reject);
		}
		if(AppUtils.StringUtil(role)!=null){
			 sb.append(" and b.id=?");
			 listStr.add(role);
		}
		
			String sql = "select a.id,a.roleid,a.rejectid,b.name as js,c.name as hcjs";
			String extrasql = " FROM `sys_rolereject_info` a INNER JOIN `sys_role_info` b ON a.roleid=b.id  "
					+ " INNER JOIN sys_role_info c ON a.rejectid=c.id WHERE a.stat='0' "+sb.toString();
	
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	 }
	 
	
	public void form(){
		render("form.jsp");
	}
	
	/**
	 * 保存或更新
	 */
	@Before(Tx.class)
	public void save() {
		String id = getPara("id");
		String role=getPara("role");
		String reject=getPara("reject");
		List<Record> reclist=Db.find("select roleid,rejectid from sys_rolereject_info ");
		int flag = 0;
		if(reclist.size()>0||reclist!=null){
			for (int i = 0; i < reclist.size(); i++) {
				String a=reclist.get(i).getStr("roleid");
				String b=reclist.get(i).getStr("rejectid");
				if(a.equals(role)&&b.equals(reject)||a.equals(reject)&&b.equals(role)){
					flag=-1;
				}
			}
		}
		
		if(flag==-1){
			
		}else{
			if(role.equals(reject)){
				flag=0;
			}else{
				// 根据id是否为空来判断是新增还是修改
				if (AppUtils.StringUtil(id)!=null) {
					flag = Db.update("update sys_rolereject_info set rejectid=? where id=?",new Object[]{reject,id});
				} else {
					id=AppUtils.getStringSeq();
					flag = Db.update("insert into sys_rolereject_info (roleid,rejectid) values (?,?)",new Object[]{role,reject});
				}
			}
		}
		
		setAttr("flag",flag);
		renderJson();
		
	}
	
	
	/**
	 * 删除
	 */
	public void del() {
		String ids=getPara("ids");
		int flag = 0;
		if(ids.contains(",")){
			String []array=ids.split(",");
			for(int i=0;i<array.length;i++){
				String id=array[i];
				flag = Db.update("update sys_rolereject_info set stat='1' where id=?",new Object[]{id});
			}
		}else{
			flag = Db.update("update sys_rolereject_info set stat='1'  where id=?",new Object[]{ids});	
		}
		setAttr("result",flag);
		renderJson();
	}
	
	public void getCombobox(){
		List<Record> records=Db.find("select id,name from sys_role_info ");
		setAttr("records", records);
		renderJson();
	}
	
	/**
	 * 获得填充表单的详情
	 */
	public void getDetail(){
		String id=getPara("id");
		String sql=" SELECT a.id,b.id as role,c.id as reject  "
				+ " FROM `sys_rolereject_info` a INNER JOIN `sys_role_info` b ON a.roleid=b.id "
				+ " INNER JOIN sys_role_info c ON a.rejectid=c.id where 1=1 ";
		StringBuffer sb=new StringBuffer();
		List<String> listStr=new ArrayList<String>();
		if(AppUtils.StringUtil(id)!=null){
			sb.append(" and a.ID= ?");
			listStr.add(id.trim());
		}
		List<Record> list=Db.find(sql+sb.toString(),listStr.toArray());
		setAttr("record",list.get(0));
		renderJson();
	}

}

package com.goodcol.controller;


import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * 权限管理
 * @author first blush
 */
@RouteBind(path = "/permission")
@Before({ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class RolePerMissionCtl extends BaseCtl {
	public static Logger log=Logger.getLogger(RolePerMissionCtl.class);

	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 角色信息列表
	 * */
	@LogBind(menuname = "权限管理", type = "3", remark = "权限管理-角色信息查询")
	public void getRole(){
		String name = getPara("name");
		int pageNumber = getParaToInt("pageIndex")+1;
		int pageSize = getParaToInt("pageSize",10);
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if(AppUtils.StringUtil(name)!=null){
			sb.append(" and name like ? ");
			listStr.add("%"+name.trim()+"%");
		}
		String sql = "select * ";
		String extrasql = " from SYS_ROLE_INFO where 1=1 and ROLE_DELE_FLAG='1' "+sb.toString()+" order by id";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql,listStr.toArray());
		setAttr("data" ,r.getList());
		setAttr("total" , r.getTotalRow());
		renderJson();
	}
	
	/**
	 *角色对应的菜单树 
	 * **/
	public void getRoleTree(){
		String roleid = getPara("id");
		//String sql = "select c.id,c.name,c.url,c.type,c.icon,c.ordernum,c.pid from SYS_ROLE_INFO a,SYS_POWER_INFO b,SYS_MENU_INFO c where 1=1 and a.id=b.objid and c.id=b.menuid and a.id=? order by c.ORDERNUM";
		String sql ="select d.id,d.name,d.pid  from (select c.id,c.name,c.url,c.type,c.icon,c.ordernum,c.pid "
                      +" from SYS_ROLE_INFO a,SYS_POWER_INFO b,SYS_MENU_INFO c "
                      +" where 1=1 and a.id=b.objid and c.id=b.menuid and c.enable='0' and a.id=? " 
                      +"    order by c.ORDERNUM) d union all "
                      +"    select e.id,e.objname,e.menu_id  from"
                      +"   (select  a.role_id,b.id,b.menu_id,b.objname "
                      +" from SYS_ROLE_POWER a,SYS_USERPOWER_INFO b "
                      +" where a.power_id=b.id and a.role_id=? order by b.id ) e ";
		List<Record> list = Db.find(sql,roleid,roleid);
		setAttr("datas",list);
		renderJson();
	}
	
	/**
	 *菜单树页面
	 * **/
	public void getChoiceTree(){
		render("treeList.jsp");
	}
	
	/**
	 *角色菜单树checkbox
	 * **/
	public void getCheckTree(){
		String sql = "select  id,name,pid from SYS_MENU_INFO where (type='0' or type='2') union all select a.id,objname,menu_id "
				+ "from  SYS_USERPOWER_INFO a ,SYS_MENU_INFO b where b.id=a.menu_id and  (b.type='0' or b.type='2')";
		List<Record> list = Db.find(sql);
		setAttr("datas",list);
		renderJson();
	}
	
	/**
	 *查询已有的菜单，已有的角色权限
	 **/
	public void getHaveMenu(){
		String roleid = getPara("key");
		String sql ="select d.menuid from  " +
				" (select menuid from sys_power_info where objid=? ) d "+
                "  union all "+
                "  select e.id from " +
                "  (select b.id from SYS_ROLE_POWER a,SYS_USERPOWER_INFO b where a.role_id=? and a.power_id= b.id order by b.id)"+
                "   e";
		List<Record> list = Db.find(sql,roleid,roleid);
		setAttr("record", list);
		renderJson();
	}
	
	/**
	 *角色对应的菜单保存(菜单下的权限保存)
	 * **/
	@LogBind(menuname = "权限管理", type = "5", remark = "权限管理-更新")
	public void savePower(){
		String user_no = getCurrentUser().getStr("USER_NO");
		JSONObject json = getJsonObj("data");
		String menuid = json.getString("menuid");//权限id
		String roleid = json.getString("roleid");//角色id
		String functionid = json.getString("functionid");//菜单id
		String mainpage = json.getString("mainpage");
		if(mainpage==null||("\"null\"").equals(mainpage)||("null").equals(mainpage)){
			mainpage = "";
		}
		System.out.println("mainpage:  "+mainpage);
		
		if(roleid!=null&&!roleid.equals("")){
		
			String mainSql = "update SYS_ROLE_INFO set main_page = ? where id = '"+roleid+"'";//修改角色的首页
		
			Db.update(mainSql,mainpage);
			
			if(menuid!=null&&!menuid.equals("")){
				String delsql = "delete from SYS_ROLE_POWER where role_id=?";//删除该角色对应的权限
				String insql  = "insert into SYS_ROLE_POWER(ID,ROLE_ID,POWER_ID)values(?,?,?)";//插入该角色对应的权限
				Db.update(delsql,roleid);
				/**
				 *更新权限
				 * */
				if(menuid.contains(",")){
					String[] array = menuid.split(",");
					for(int i=0;i<array.length;i++){
						String menu = array[i];
						Db.update(insql,new Object[]{AppUtils.getStringSeq(),roleid,menu});
					}
				}else{
					Db.update(insql,new Object[]{AppUtils.getStringSeq(),roleid,menuid});
				}
			}
			else{
				String delsql = "delete from SYS_ROLE_POWER where role_id=?";//删除该角色对应的权限
				Db.update(delsql,roleid);
			}
			if(functionid!=null&&!functionid.equals("")){
				String delsql = "delete from SYS_POWER_INFO where objid=?";//删除该角色对应的菜单
				String insql  = "insert into SYS_POWER_INFO(MENUID,OBJID,TYPE)values(?,?,?)";//插入该角色对应的菜单
				Db.update(delsql,roleid);
				/**
				 *更新菜单 
				 * */
				if(functionid.contains(",")){
					String[] array = functionid.split(",");
					for(int i=0;i<array.length;i++){
						String function = array[i];
						Db.update(insql,new Object[]{function,roleid,"1"});
					}
				}else{
					Db.update(insql,new Object[]{functionid,roleid,"1"});
				}
			}
			else{
				String delsql = "delete from SYS_POWER_INFO where objid=?";//删除该角色对应的菜单
				Db.update(delsql,roleid);
			}
		}
		renderJson();
		
	}
	
	
}

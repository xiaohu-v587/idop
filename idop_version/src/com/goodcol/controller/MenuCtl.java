package com.goodcol.controller;

import java.util.ArrayList;
import java.util.List;

import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.OrgUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.zxgldbutil.CommonUtil;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;

@RouteBind(path = "/menu")
@Before({ManagerPowerInterceptor.class})
public class MenuCtl extends BaseCtl{
	public static Logger log=Logger.getLogger(MenuCtl.class);
	
	public void index() {
		render("index.jsp");
		
	}
	
	public void getList(){
		//String key = getPara("key");
		String pid = getPara("pid");
		String name = getPara("name");
		String url = getPara("url");
		String enbale = getPara("enbale");
		
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		
		if (AppUtils.StringUtil(pid) != null) {
			sb.append(" and (pid = ? or id=?)");
			listStr.add(pid.trim());
			listStr.add(pid.trim());
		}
		if (AppUtils.StringUtil(name) != null){
			sb.append(" and name like ?");
			listStr.add("%"+name+"%");
		}
		if (AppUtils.StringUtil(url) != null) {
			sb.append(" and url = ? ");
			listStr.add(url.trim());
		}
		if (AppUtils.StringUtil(enbale) != null) {
			sb.append(" and enbale = ? ");
			listStr.add(enbale.trim());
		}
		
		String sql = "select id,name,url,pid,enable,actikey,flag,STYLE1,ADRESS ";
		String extrasql = " from sys_menu_info where 1=1 " + sb.toString()
				+ " order by PID,ORDERNUM";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql,
				listStr.toArray());
		List<Record> list = r.getList();
		setAttr("datas", list);
		setAttr("total", r.getTotalRow());
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"),
				"菜单管理", "3",
				"菜单信息-查询");
		renderJson();
	}
	
	//菜单树，只查出启用的数据，enable = ‘0’
	public void getMenuList(){
		String sql = "select id,name,url,enable,pid,flag,STYLE1,ADRESS from sys_menu_info where enable = '0' order by ORDERNUM";
		List<Record> list = Db.find(sql);
		setAttr("datas",list);
		renderJson();
	}
	
	/*根据id获取机构的详细信息*/
	public void getListMenu(){
		String id = getPara("id");
		String sql= "select NAME,URL,TYPE,ORDERNUM,ENABLE,flag,STYLE1,ADRESS from sys_menu_info where id=? ";
		List<Record> list = Db.find(sql,id);
		setAttr("datamenu",list);
		renderJson();
	}
	
	/*根据id获取机构的详细信息*/
	public void getDetail(){
		String id = getPara("id");
		String sql = "";
		List<Record> entity = Db.find("select pid from sys_menu_info where id = ?",id);
		if(entity.size() == 1){
			if(entity.get(0).get("pid").equals("0")){
				sql = "select id,name,url,enable,type,ordernum,remark,pid,flag,style1,adress,0 as tid from sys_menu_info where id = ?";
			}else{
				sql = "  select a.id,a.name,a.url,a.enable,a.actikey,a.type,a.ordernum,a.remark,a.flag,a.style1,a.adress,b.name as pid,a.pid as tid from sys_menu_info a, sys_menu_info b where a.pid = b.id and a.id = ? ";
			}
		}
		List<Record> list = Db.find(sql,id);
		if(list.size() > 0){
			setAttr("record", list.get(0));
		}else{
			setAttr("record", null);
		}
		
		renderJson();
	}
	
	/*增加*/
	public void add(){
		render("form.jsp");
	}
	
	/*保存数据*/
	public void save(){
		
        //JSONObject json =  getJsonObj("data");
        String id = getPara("PID");//上级机构id
        String name = getPara("NAME");
        String url = getPara("URL");
        String ordernum = getPara("ORDERNUM");
        String remark = getPara("REMARK");
        String flag = getPara("flag");
        String style1 = getPara("style1");
        String adress = getPara("adress");
        String bz = getPara("bz");
        String flag1 = "0";
		if(AppUtils.StringUtil(bz)==null){
			String type="";
			if(AppUtils.StringUtil(bz)==null){
        	type="0";
            	}
            else{
            	type="1";
            }
			//判断菜单名称是否有重复
			List<Record> list = Db.find("select name from SYS_MENU_INFO ");
			for(int i = 0;i < list.size();i++){
				if(name.equals(list.get(i).getStr("name"))){
					flag1 = "1";
					break;
				}
			}
			if(flag1.equals("1")){
				String msg = "菜单名称重复！更新失败！";
				renderText(msg);
			}else{
    	    Db.update("insert into sys_menu_info(ID,NAME,URL,TYPE,ORDERNUM,PID,ENABLE,REMARK,flag,STYLE1,ADRESS) "
    		    		+ "values(?,?,?,?,?,?,?,?,?,?,?)"
    		    		,new Object[]{AppUtils.getStringSeq(),name,url,type,ordernum,id,0,remark,flag,style1,adress});
    	 // 记录操作日志
    		LoggerUtil.getIntanceof().saveLogger(
    				getCurrentUser().getStr("USER_NO"),
    				"菜单管理", "4",
    				"菜单信息-新增");
			}
    		}
        else{
        	//判断菜单名称是否有重复
    		List<Record> list = Db.find("select name from SYS_MENU_INFO where id <> '"+id+"' ");
    		//判断菜单URL是否被修改
    		List<Record> urlList = Db.find("select url from SYS_MENU_INFO where id = '"+id+"' ");
    		for(int i = 0;i < list.size();i++){
    			if(name.equals(list.get(i).getStr("name"))){
    				flag = "1";
    				break;
    			}
    		}
    		if (!(urlList.get(0).getStr("url").equals(url))) {
				flag1 = "2";
			}
    		if (flag1.equals("1")) {
    			String msg = "菜单名称重复！更新失败！";
    			renderText(msg);
    		} else if (flag1.equals("2")) {
    			String msg = "URL不可修改！";
    			renderText(msg);
    		} else {
    		Db.update("update sys_menu_info set NAME=?,URL=?,ORDERNUM=?,REMARK=?,flag=?,STYLE1=?,ADRESS=? where id= ? "
			   ,new Object[]{name,url,ordernum,remark,id,flag,style1,adress});
    		// 记录操作日志
    		LoggerUtil.getIntanceof().saveLogger(
    				getCurrentUser().getStr("USER_NO"),
    				"菜单管理", "5",
    				"菜单信息-更新");
    		}
        }
	}
	
	//保存数据
	public void savee(){
		String pid = getPara("uid");
        String name = getPara("name");
        String url = getPara("url");
        String ordernum = getPara("ordernum");
        String remark = getPara("remark");
        String type = getPara("type");
        String enable = getPara("enable");
        String actikey = getPara("actikey");
        String flag = getPara("flag");
        String style = getPara("style1");
        String adress = getPara("adress");
        Db.update("insert into sys_menu_info(ID,NAME,URL,TYPE,ORDERNUM,PID,ENABLE,REMARK,ICON,ACTIKEY,FLAG,STYLE1,ADRESS) "
	    		+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)"
	    		,new Object[]{AppUtils.getStringSeq(),name,url,type,ordernum,pid,enable,remark,"icon-node",actikey,flag,style,adress});
        renderJson();
	}
	
	/*删除机构*/
//	public void del(){
//		String id = getPara("id");
//		String msg = "";
//		//判断是否是权限管理
//		List<Record> list = Db.find("select name from SYS_MENU_INFO where id=?",id);
//		if (list.get(0).getStr("name").equals("权限管理")) {
//			msg = "权限管理菜单不允许刪除！";
//		} else {
//			Db.update("delete from sys_menu_info where id = ? ",id);
//			// 记录操作日志
//			LoggerUtil.getIntanceof().saveLogger(
//					getCurrentUser().getStr("USER_NO"),
//					"菜单管理", "6",
//					"菜单信息-删除");
//			msg = "删除成功！";
//		}
//		renderText(msg);
//	}
	
	//删除机构
	public void del(){
		String pid = getPara("id");
	    //先查询要删除的节点下面有没有子节点
		String sql = "select id from SYS_MENU_INFO where pid = ?";
		List<Record> list = Db.find(sql,pid);
		//顶一个返回的值，0--节点下面没有值，可以删除；1--有值，不可以删除
		String flag = "0";
		if(list.size() > 0){
			flag = "1";
		}else{
			Db.update("delete from sys_menu_info where id = ? ",pid);
		}
		setAttr("record", flag);
		renderJson();
	}
	
	/*更新*/
	public void update(){
        String id = getPara("id");
        String name = getPara("name");
        String url = getPara("url");
        String ordernum = getPara("ordernum");
        String remark = getPara("remark");
        String type = getPara("type");
        String enable = getPara("enable");
        String pid = getPara("uid");
        String actikey = getPara("actikey");
        String style = getPara("style1");
        String flag = getPara("flag");
        String adress = getPara("adress");
		Db.update("update sys_menu_info set name=? ,url= ?,ordernum=?,remark=?,type=?,enable=?,pid=?,actikey=?,flag=?,style1=?,adress=?  where id = ? ",name,url,ordernum,remark,type,enable,pid,actikey,flag,style,adress,id);
		//setAttr("datas", id);   
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"),
				"菜单管理", "5",
				"菜单信息-更新(禁用/激活菜单)");
		renderJson();
	}
	
	//根据柜员号或者机构号来查询
	public void menuSearch(){
		//获取前台传过来的参数
		String key = getPara("key");
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		// 获取用户角色等级和机构号
		List<Record> lr = Db.find("select ri.name,ri.role_level,ui.org_id from sys_role_info ri, sys_user_info ui where ui.role_id=ri.id and ui.user_no='" + userNo + "'");
		String roleName = lr.get(0).getStr("NAME");
		String org_id = lr.get(0).getStr("org_id");
		
		String role_level = AppUtils.getLevelByRoleName(roleName);
		
		//this.pageSize;
		/**
		 *  20181205 14:13  常显阳 修改
		 *  修改
		 *  	1.每次最大 10笔 
		 *      2.机构号 由 orgnum 改为 bancsid
		 *  
		 */
		StringBuffer select = new StringBuffer();
		select.append(" select key,describe,adress,style1,menuname ");
		
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(" from ( ");
		//人员数据组合
		sqlExceptSelect.append(" select t.user_no as key,t.user_no || '  ' || t.name || '  ' || p.name as describe,t.user_no as keyone,t.name,p.name as menuname,p.adress,'1' as style1 from sys_user_info t, sys_menu_info p where p.flag = '1'and p.style1 like '%1%' ");
		//组装查询条件
		
		//限制人员层级
		if (AppUtils.StringUtil(role_level) == null || role_level.equals("4")) {
			// 柜员只允许查询本人
			sqlExceptSelect.append(" and t.user_no = '" + userNo + "' ");
		} else if (role_level.equals("3")) {
			// 支行查询机构号相等或下级
			sqlExceptSelect.append(" and t.org_id in (select id from sys_org_info k where stat = '1' start with k.orgnum = '"+org_id+"'  connect by prior orgnum = upid) ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			sqlExceptSelect.append(" and t.org_id in (select id from sys_org_info k where stat = '1' start with k.orgnum in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " )  connect by prior orgnum = upid) ");
		} else if (role_level.equals("1")) {
			// 总行和系统管理员，查询所有
		}
		if(AppUtils.StringUtil(key)!=null)
			sqlExceptSelect.append(" and t.user_no || '  ' || t.name || '  ' || p.name like '%"+key+"%' ");
		
		sqlExceptSelect.append(" union ");
		sqlExceptSelect.append(" select a.orgnum as key,a.bancsid || '  ' || a.orgname || '  ' || p.name as describe,a.bancsid as keyone,a.orgname as name,p.name as menuname,p.adress,'0' as style1 from sys_org_info a, sys_menu_info p where  a.bancsid not in ('Z0001') and p.flag = '1' and p.style1 like '%0%' ");
		
		if ( AppUtils.StringUtil(role_level) == null || role_level.equals("4") || role_level.equals("3")) {
			// 支行查询机构号相等或下级
			sqlExceptSelect.append(" and a.orgnum in (select id from sys_org_info k where stat = '1' start with k.orgnum = '"+org_id+"'  connect by prior orgnum = upid) ");
		} else if (role_level.equals("2")) {
			// 分行查询机构号相等或下级
			sqlExceptSelect.append(" and a.orgnum in (select id from sys_org_info k where stat = '1' start with k.orgnum in ( " + OrgUtil.getIntanceof().getSubOrgId(org_id) + " )  connect by prior orgnum = upid) ");
		} else if (role_level.equals("1")) {
			// 总行和系统管理员，查询所有
		}
		if(AppUtils.StringUtil(key)!=null)
			sqlExceptSelect.append(" and a.bancsid || '  ' || a.orgname || '  ' || p.name like '%"+key+"%' ");
		
		sqlExceptSelect.append(CommonUtil.orgNameSqlNoBenBu());
		sqlExceptSelect.append(" order by key ");
		
		//结果包围
		sqlExceptSelect.append(" )  ");
		Page<Record> page = Db.paginate(1, pageSize, select.toString(), sqlExceptSelect.toString());
		setAttr("data", page.getList());
		renderJson(page.getList());
	}
	
}

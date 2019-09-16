package com.goodcol.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 风险问责统计口径，客户经理扣分记录
 * 
 * @author first blush
 */
@RouteBind(path = "/custPuni")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class CustPuniCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(CustPuniCtl.class);

	/**
	 * 初始化
	 */
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 增加和修改都使用
	 */
	public void form() {
		setAttr("bigFina", AppUtils.findDict("kpl_big_comp_para", "经济处理"));//大公司经济处理扣分
		setAttr("bigPuni", AppUtils.findDict("kpl_big_comp_para", "纪律处分"));//大公司纪律处分扣分
		setAttr("midFina", AppUtils.findDict("kpl_mid_sma_para", "经济处理"));//中小公司经济处理扣分
		setAttr("midPuni", AppUtils.findDict("kpl_mid_sma_para", "纪律处分"));//中小公司纪律处分扣分
		render("form.jsp");
	}

	public void listCustPuni(){
	
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Map<String, Object> map = busiSql();
		// 从数据库查询指定条数的用户记录
		@SuppressWarnings("unchecked")
		Page<Record> r = Db.use("default").paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("listBusi--r.getList():" + r.getList());
		log.info("listBusi--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}

	/**
	 * 获取页面输入的条件，拼接sql，组织参数
	 * 
	 * @return map
	 */
	public Map<String, Object> busiSql() {
		// 获取当前用户信息
//		String userNo = getCurrentUser().getStr("USER_NO");
//		String orgNo = getCurrentUser().getStr("ORG_ID");

		String selectSql = " select rownum rn,c.id,o.orgname,c.cust_name, p1.name punish_type, c.deduction, c.remark, "
				+" uc.name creater_name, to_char(c.create_time, 'yyyy-MM-dd') create_time ";
		String extrasql = " from pccm_cust_puni c "
				+" left join sys_user_info u on c.cust_id = u.id "
				+" left join sys_user_info uc on c.creater = uc.id "
				+" left join sys_org_info o on u.org_id = o.id "
				+" left join gcms_param_info p1 on c.punish_type = p1.val and p1.key = 'punish_type' ";
		StringBuffer whereSql = new StringBuffer(" where 1=1 and c.del_stat = '0' ");
		List<String> sqlStr = new ArrayList<String>();

		// 获取页面输入查询条件
		String org_id = getPara("org_id");
		String punish_type = getPara("punish_type");	
		String cust_name = getPara("cust_name");	
		String creater	= getPara("creater");
		String create_time	= getPara("create_time");
		if (AppUtils.StringUtil(org_id) != null) {
			 whereSql.append(" and o.id=? ");
			 sqlStr.add(org_id.trim());
		}
		if (AppUtils.StringUtil(punish_type) != null) {
			whereSql.append(" and c.punish_type= ? ");
			sqlStr.add(punish_type.trim());
		}
		if (AppUtils.StringUtil(cust_name) != null) {
			whereSql.append(" and c.cust_name like ? ");
			sqlStr.add("%" + cust_name.trim() + "%");
		}
		if (AppUtils.StringUtil(creater) != null) {
			whereSql.append(" and uc.name like ? ");
			sqlStr.add("%" + creater.trim() + "%");
		}
		if (AppUtils.StringUtil(create_time) != null) {
			whereSql.append(" and to_char(create_time,'yyyy-MM-dd') = ? ");
			sqlStr.add(create_time.trim().substring(0, 10));
		}

		extrasql += whereSql.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
	
	/**
	 * 下拉框的json数据 ;只展示大企业客户经理和中小企业客户经理
	 */
	public void getListByOrgId() {
		List<Record> list = null;
		String org_id = getPara("org_id");
		//String user_id = getCurrentUser().getStr("id");
		if (AppUtils.StringUtil(org_id) != null && !"000000000".equals(org_id)) {
			list = Db.use("default").find(" select u.id,u.name from sys_user_info u "
						+" left join gcms_role_apply a on u.user_no = a.user_id "
						+" left join sys_role_info p on p.id = a.role_id "
						+" where 1=1 and (dele_flag='0' or stat!='1')  and p.kpi_flag in ('1', '2') and u.org_id in "+AppUtils.getOrgStr(org_id,"2"));
		}else{
			list = Db.use("default").find(" select u.id,u.name from sys_user_info u "
					+" left join gcms_role_apply a on u.user_no = a.user_id "
					+" left join sys_role_info p on p.id = a.role_id "
					+" where 1=1 and (dele_flag='0' or stat!='1') and p.kpi_flag in ('1','2')  ");
		}
		JSONArray array = JSONArray.fromObject(list);
		String jsonStr = "";
		for (int i = 0; i < array.size(); i++) {
			String columns = array.getJSONObject(i).getString("columns");
			if (i == 0) {
				jsonStr = columns;
			} else if (i > 0) {
				jsonStr = columns + "," + jsonStr;
			}
		}
		jsonStr = "[ " + jsonStr + " ]";
		renderJson(jsonStr);
	}
	/**
	 * 获取客户经理类型
	 */
	public void getMgrType() {
		String type = null;
		String user_id = getPara("id");
		if (AppUtils.StringUtil(user_id) != null) {
			type = Db.use("default").queryStr(" select p.kpi_flag from sys_user_info u "
					+" left join gcms_role_apply a on u.user_no = a.user_id "
					+" left join sys_role_info p on p.id = a.role_id "
					+" where 1=1 and u.id =? ",new Object[]{user_id});
		}
		setAttr("type", type);
		renderJson();
	}
	
	public void getDetail() {
		String id = getPara("id");		
		String sql = " select c.id,o.id org_id,c.cust_id,c.cust_name, c.punish_type, c.deduction, c.remark "
				//+" uc.name creater_name, to_char(c.create_time, 'yyyy-MM-dd') create_time "
				+ " from pccm_cust_puni c "
				+" left join sys_user_info u on c.cust_id = u.id "
				+" left join sys_user_info uc on c.creater = uc.id "
				+" left join sys_org_info o on u.org_id = o.id "
				+" left join gcms_param_info p1 on c.punish_type = p1.val and p1.key = 'punish_type' "
				+" where c.del_stat = '0' ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and c.id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(), listStr.toArray());
		
		log.info("sql=" + sql);
		setAttr("record", list.get(0));
		//System.out.println(list.get(0).toString());
		renderJson();
	}
	
	public void save() {
		//获取当前用户信息
		String user_id = getCurrentUser().getStr("id");
		
		String id = getPara("id");
		String cust_id = getPara("cust_id");
		String cust_name = getPara("cust_name");
		String punish_type = getPara("punish_type");
		String deduction = getPara("deduction");
		String remark = getPara("remark");
		
		// 根据用户pkuuid是否存在来判断是新增
		if (AppUtils.StringUtil(id) == null) {
			Db.use("default").update(" insert into pccm_cust_puni(id,cust_id,cust_name,punish_type,deduction,creater,remark,create_time,del_stat) " 
					+ " values(?,?,?,?,?,?,?,sysdate,'0') ", new Object[] { AppUtils.getStringSeq(), cust_id,cust_name,punish_type,deduction,user_id,remark});
		}else{
			Db.use("default").update(" update pccm_cust_puni set  punish_type=?,deduction=?,remark=?,updater=?,update_time=sysdate where id=?", 
					new Object[] { punish_type, deduction,remark,user_id,id});
		}
		renderNull();
	}
	
	public void del() {
		//获取当前用户信息
		String user_id = getCurrentUser().getStr("id");
		
		String ids = getPara("ids");
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				Db.use("default").update("update pccm_cust_puni set del_stat='1',del_time=sysdate,deleter=? where id = ? ", new Object[] {user_id,array[i]});
			}
		} else {
			Db.use("default").update("update pccm_cust_puni set del_stat='1',del_time=sysdate,deleter=? where id = ? ", new Object[] {user_id,ids});
		}
		renderNull();
	}

}

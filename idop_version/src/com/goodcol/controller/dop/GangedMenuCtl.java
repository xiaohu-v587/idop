package com.goodcol.controller.dop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;

/**
 * 联动数据管理
 * 
 * @author miao_j
 */
@RouteBind(path = "/gangedmenu")
@Before({ ManagerPowerInterceptor.class })
public class GangedMenuCtl extends BaseCtl {

	public static Logger log = Logger.getLogger(GangedMenuCtl.class);

	/**
	 * 控制器默认访问方法
	 */
	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
    
	/**
	 * 显示左边的树
	 */
	public void getZdwhList() {
		String sql = "SELECT ID,TYPE,UPID,REMARK,VAL,COLLATE FROM DOP_GANGEDMENU_INFO ORDER BY to_number(COLLATE) ASC";
		//String sql = "SELECT ID,TYPE,UPID,REMARK,VAL FROM DOP_GANGEDMENU_INFO ";
		List<Record> list = Db.find(sql);
		setAttr("datas", list);
		log.info("getZdwhList--list:" + list);
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "3", "联动数据管理-查询所有结点");
 		renderJson();
	}

	/**
	 * 根据id获取字段的详细信息 （点击左边树，右边显示详细信息）
	 */
	public void getListZdwh() {
		
		// 获取查询参数
		
		
		Map<String, Object> map = organSql();
		
		// 从数据库查询指定条数的用户记录
		@SuppressWarnings("unchecked")
		List<Record> lr = Db.find(
				(String) map.get("selectSql") + (String) map.get("extrasql"),
				((List<String>) map.get("sqlStr")).toArray());
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("datajg", lr);
		
		// 打印日志
		log.info("getListZdwh--lr:" + lr);
		
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "3", "联动数据管理-查询");
		renderJson();
	}

	/**
	 * 根据id获取详细信息 （修改时使用）
	 */
	public void getDetail() {
		String id = getPara("id");
		String sql = "SELECT ID,VAL,TYPE,UPID,REMARK,COLLATE FROM DOP_GANGEDMENU_INFO  WHERE id = ?  ";
		List<Record> list = Db.find(sql, id);
		setAttr("datas", list.get(0));
		// 打印日志
		log.info("getDetail--datas:" + list.get(0));
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "3", "联动数据管理-详情查询");
		renderJson();
	}

	/**
	 * 增加 
	 */
	@Before(PermissionInterceptor.class)
	public void add() {
		render("form.jsp");
	}
	/**
	 * 编辑
	 */
	@Before(PermissionInterceptor.class)
	public void edit() {
		render("form.jsp");
	}

	/**
	 * 判断键值是否重复 （新增时判断，判断本级）
	 */
	public void checkZdwhNum() {
		
		// 获取参数
		String val = getPara("val");
		String upid = getPara("upid");
		String edit = getPara("edit");
		List<Record> lr = null;
		if (edit == null) {
			
			// 增加前的判断是否重复,先判断层级是否大于等于三级
			String grandpa = Db.findFirst("select * from dop_gangedmenu_info where id=?",upid).getStr("upid");
			if(AppUtils.StringUtil(grandpa)!=null){
				lr = Db.find("SELECT * FROM DOP_GANGEDMENU_INFO WHERE VAL = ? ",val);
			}else{
				lr = Db.find("SELECT ID,VAL,TYPE,UPID,REMARK FROM DOP_GANGEDMENU_INFO WHERE VAL = ? and upid = ?",val,upid);
			}
		} 
		if (lr.size()>=1)
			setAttr("msg", "1");
		else
			setAttr("msg", "0");
		// 打印日志
		log.info("checkOrgNum--msg:" + lr.size());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "3", "联动数据管理-字段键值验重");
		renderJson();
	}

	/**
	 * 保存数据（增加）
	 */
	public void save() {
		
		// 获取前台数据
		JSONObject json = getJsonObj("data");
		
		// 上级机构id
		String upid = json.getString("upid");
		String remark = json.getString("remark");
		String val = json.getString("val").trim();
		String type = json.getString("type");
		String collate=json.getString("collate");
		int flag = 0;
		try {
			flag = Db.update("INSERT INTO DOP_GANGEDMENU_INFO(ID,VAL,REMARK,UPID,TYPE,COLLATE) "
							+ "VALUES(?,?,?,?,?,?)",
							new Object[] { AppUtils.getStringSeq(), val, remark, upid, type,collate});
	/*		flag = Db.update("INSERT INTO DOP_GANGEDMENU_INFO(ID,VAL,REMARK,UPID,TYPE) "
					+ "VALUES(?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), val, remark, upid, type});*/
		} catch (Exception e) {
			log.error("OrgCtl-save", e);
		}
		// 打印日志
		log.info("save--flag:" + flag);
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "4", "联动数据管理-新增");
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 修改数据（修改更新）
	 */
	public void update() {
		
		// 获取前台数据
		JSONObject json = getJsonObj("data");
		
	
		String remark = json.getString("remark");
		String val = json.getString("val").trim();
		String collate=json.getString("collate").trim();
		String id = json.getString("id").trim();
		// 更新
		int flag = 0;
		try {
			flag = Db.update("UPDATE DOP_GANGEDMENU_INFO SET VAL = ?,REMARK = ? ,COLLATE =? WHERE id = ? ",
							new Object[] {val,remark,collate,  id});
			/*flag = Db.update("UPDATE DOP_GANGEDMENU_INFO SET VAL = ?,REMARK = ? WHERE id = ? ",
					new Object[] {val,remark,  id});*/
		} catch (Exception e) {
			log.error("OrgCtl-save", e);
		}
		setAttr("flag", flag);
		
		// 打印日志
		log.info("update--flag:" + flag);
		
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "5", "联动管理-更新");
		renderJson();
	}

	/**
	 * 删除节点
	 */
	@Before(PermissionInterceptor.class)
	public void del() {
		String id = getPara("id");
			int flag = Db.update("DELETE FROM DOP_GANGEDMENU_INFO WHERE ID = ? ", id);
			setAttr("flag", flag);
			
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "联动数据管理", "5", "联动数据管理-删除");
		renderJson();
	}


	/**
	 * 获取页面输入的条件，拼接sql，组织参数 （查询和下载使用）
	 * 
	 * @return map
	 */
	public Map<String, Object> organSql() {
		
		// 获取页面输入查询条件
		String remark = getPara("remark");
		String id = getPara("id");

		String selectSql = " select a.id id ,a.upid upid ,a.remark remark,a.val val, a.collate ,"
				+ " b.id sjid, b.remark sjremark, b.val sjval ";
		String extrasql = " from dop_gangedmenu_info a left join  dop_gangedmenu_info b "
				+ " on a.upid = b.id    ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(remark) != null) {
			whereSql.append(" and a.remark like ? ");
			sqlStr.add("%" + remark.trim() + "%");
		}
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" and a.id = ? ");
			sqlStr.add(id);
		}
		//whereSql.append("  order by a.val ");
		
		extrasql += whereSql.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
}

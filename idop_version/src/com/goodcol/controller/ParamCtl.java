package com.goodcol.controller;

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
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;

/**
 * 参数管理
 * 
 * @author first blush
 */
@RouteBind(path = "/param")
@Before({ ManagerPowerInterceptor.class })
public class ParamCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(ParamCtl.class);

	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取字典的所有信息
	 */
	public void getList() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String key = getPara("key");
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(key) != null) {
			sb.append(" and key = ? ");
			listStr.add(key);
			// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "3",
			// "字典管理-查询");
		}
		
		/*和数据权限有关的  加机构条件 
		用户角色
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		获取当前机构号
		String orgId = getCurrentUser().getStr("ORG_ID");
		获取当前用户最高等级权限
		String roleLevel = AppUtils.getLevByRole(roleNames);
		最大权限的机构号	
		String orgNum = AppUtils.getUpOrg(orgId, roleLevel);
		//id代表9位机构号
		sb.append(" and id in (select id from sys_org_info where id = '" + orgNum + "' or by5 like '%" + orgNum + "%')");
		*/
		String sql = "select * ";
		String extrasql = " from sys_param_info where 1=1 " + sb.toString() + " order by key,sortnum";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extrasql, listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("listUser--r.getList():" + r.getList());
		log.info("listUser--r.getTotalRow():" + r.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-查询");
		renderJson();
	}

	/**
	 * 字典添加页面
	 */
	@Before(PermissionInterceptor.class)
	public void add() {
		render("form.jsp");
	}

	/**
	 * 获取编辑时的回显数据
	 */
	public void getDetail() {
		String id = getPara("key");
		String sql = "select * from SYS_PARAM_INFO where 1 = 1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("record", list.get(0));
		log.info("参数管理-查询详情");
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "参数管理", "3",
				"参数管理-查询详情");
		renderJson();

	}

	/**
	 * 保存新增的字典信息
	 */
	@Before(Tx.class)
	public void save() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String key = getPara("key");
		String name = getPara("name");
		String val = getPara("val");
		String remark = getPara("remark");
		String sortNum = getPara("sortNum");
		String isyypzdjb = getPara("isyypzdjb");
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db.find("select val,remark from SYS_PARAM_INFO where key = '" + key + "'");
		// 判断
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val")) || remark.equals(list.get(i).getStr("remark"))) {
				flag = "1";
				break;
			}
		}
		if (!flag.equals("1")) {
			Db.update("insert into SYS_PARAM_INFO(id,key,name,val,remark,sortnum,status,isyypzdjb) values(?,?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), key, name, val, remark, sortNum, "1" ,isyypzdjb});
			flag = "2";
		}
		log.info("字典管理-新增");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "4", "字典管理-新增");
		renderText(flag);
	}

	/**
	 * 更新字典信息
	 **/
	@Before(Tx.class)
	public void update() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String id = getPara("id");
		String key = getPara("key").trim();
		String name = getPara("name").trim();
		String val = getPara("val").trim();
		String remark = getPara("remark").trim();
		String sortNum = getPara("sortNum").trim();
		String isyypzdjb = getPara("isyypzdjb").trim();
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db
				.find("select val,remark from SYS_PARAM_INFO where key = '" + key + "' and id <> '" + id + "' ");
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val")) || remark.equals(list.get(i).getStr("remark"))) {
				flag = "1";
				break;
			}
		}
//		if (!flag.equals("1")) {
			Db.update("update SYS_PARAM_INFO set key=?,name=?,val=?,remark=?,sortnum=? ,isyypzdjb=? where id= ? ",
					new Object[] { key, name, val, remark, sortNum,isyypzdjb, id });
			flag = "2";
//		}
		log.info("字典管理-更新");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-更新");
		renderJson(flag);
	}

	/**
	 * 删除数据字典
	 */
	@Before(Tx.class)
	public void del() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		setAttr("record", flag);
		log.info("字典管理-删除");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "6", "字典管理-删除");
		renderJson();
	}

	/**
	 * 修改状态启动、停止
	 */
	@Before(Tx.class)
	public void changeStatus1() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		log.info("字典管理-停用状态");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-停用状态");
		setAttr("record", flag);
		renderJson();
	}

	@Before(Tx.class)
	public void changeStatus0() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "1", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "1", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		log.info("字典管理-启动状态");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-启动状态");
		setAttr("record", flag);
		renderJson();
	}

	/**
	 * 字典下拉框
	 **/
	public void getCombox() {
		String sql = "select distinct(key),name from sys_param_info where status=? order by key";
		List<Record> list = Db.find(sql, "1");
		renderJson(list);
	}

	/**
	 * 根据字典编码查询字典val remark
	 */
	public void getKeyList() {
		String key = getPara("key");
		/*
		 * List<Record> list = Db .find(
		 * "select val,remark from SYS_PARAM_INFO where 1=1 and status='1' and key = ?"
		 * , key);
		 */
		List<Map<String, String>> list = ParamContainer.getDictList(key);
		renderJson(list);
	}
	
	public void getParam() {
		String key = getPara("key");
		List<Record> list = Db.find("SELECT VAL,REMARK FROM SYS_PARAM_INFO WHERE STATUS='1' AND KEY = ? ORDER BY SORTNUM",key);
		renderJson(list);
    }
	
	public void getTemp() {
		List<Record> list = Db.find("SELECT id, template_title FROM yygl_message_info");
		renderJson(list);
    }

	/**
	 * 字典映射
	 **/
	public void getDict() {
		String key = getPara("key");
		List<Record> list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? order by sortnum", key);
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
    * 我的条线字典表	
    */
	public void gettxDict(){
		String userno=getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		String key = getPara("key");
		List<Record> list=new ArrayList<Record>();
		if("dop_ywtype".equals(key)){
			list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? and val = ? order by sortnum", key,model);
		}else{
			list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? order by sortnum", key);
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
	 * 根据type(设置查询条件、设置列表字段、设置增、改字段)
	 */
	public void getDictByType() {
		String key = getPara("key");
		String type = getPara("type");
		String jsonStr = "";
		List<Record> list = null;
		JSONArray array = null;
		if (type.equals(PropertiesContent.get("query"))) {
			list = Db.find(
					"select val,remark from SYS_PARAM_INFO where key = ? and val!=5 and val!=6 and val!=7 and val!=3 and val!=0",
					key);
			array = JSONArray.fromObject(list);
		} else if (type.equals(PropertiesContent.get("list"))) {
			list = Db.find(
					"select val,remark from SYS_PARAM_INFO where key = ? and val!=5 and val!=6 and val!=7 and val!=2 and val!=0 order by sortnum",
					key);
			array = JSONArray.fromObject(list);
		} else {
			list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? order by sortnum", key);
			array = JSONArray.fromObject(list);
		}
		for (int i = 0; i < array.size(); i++) {
			String columns = array.getJSONObject(i).getString("columns");
			if (i == 0) {
				jsonStr = columns;
			} else if (i > 0) {
				jsonStr = jsonStr + "," + columns;
			}
		}
		jsonStr = "[ " + jsonStr + " ]";
		log.info("字典管理-查询字典数据");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-查询字典数据");
		renderJson(jsonStr);
	}

	/**
	 * 查询关联字段的类型
	 */
	public void getGlFieldType() {
		String key = getPara("key");
		List<Record> list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? and val!=5 and val!=6 and val!=7 and val!=3 and val!=0 and val!=2", key);
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
		log.info("字典管理-查询关联字段的类型");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-查询关联字段的类型");
		renderJson(jsonStr);
	}
	
	/**
	 * 获取字典值
	 */
	public void getDictText() {
		String key = getPara("key");
		String val = getPara("val");
		String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		if (AppUtils.StringUtil(remark) == null) {
			remark = "null";
		}
		log.info("字典管理-获取字典值");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-获取字典值");
		renderText(remark);
	}

	/**
	 * 获取字典列表
	 */
	public void loadDict() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		String dictTypeId = getPara("dictTypeId");
		if (StringUtils.isEmpty(dictTypeId)) {
			renderNull();
			return;
		}
		/*
		 * String sqlStr =
		 * "select val as dictID ,remark as dictName from sys_param_info where key=?"
		 * ; List<Record> list = Db.find(sqlStr, dictTypeId); Map<String,
		 * Object> map = null; for (Record record : list) { map = new
		 * HashMap<String, Object>(); map.put("dictID",
		 * record.getStr("dictid")); map.put("dictName",
		 * record.getStr("dictname")); result.add(map); }
		 */
		result = ParamContainer.getDictList(dictTypeId);
		setAttr("dictList", result);
		log.info("字典管理-获取字典列表");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-获取字典列表");
		renderJson();
	}

	/**
	 * 刷新缓存
	 */
	public void init() {
		ParamContainer container = new ParamContainer();
		container.init();
		log.info("字典管理-刷新缓存");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-刷新缓存");
		renderNull();
	}

	/**
	 * 删除数据字典
	 */
	@Before(Tx.class)
	public void delete() {
		String id = getPara("id");
		Db.update("delete from sys_param_info where id =?", id);
		log.info("字典管理-删除数据字典");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "6", "字典管理-删除数据字典");
		renderNull();
	}

	/**
	 * 关联表字段
	 */
	public void getGl() {
		List<Record> list = Db.find("select distinct key,name from SYS_PARAM_INFO ");
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Record record = list.get(i);
				String name = record.getStr("key") + "【" + record.getStr("name") + "】";
				String key = record.getStr("key");
				Map<String, Object> subMap = new HashMap<String, Object>();
				subMap.put("key", key);
				subMap.put("name", name);
				map.add(subMap);
			}
		}
		log.info("字典管理-关联表字段");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "6", "字典管理-关联表字段");
		renderJson(map);
	}

	/**
	 * 获取字典列表
	 */
	public void getDictByKey(){
		String key = getPara("key");
		List<Record> list =null;
		if(AppUtils.StringUtil(key)!=null){
			list=Db.find("select id,remark from SYS_PARAM_INFO where key = ? order by sortnum desc", key);
		}else{
			list=Db.find("select id,remark from SYS_PARAM_INFO order by sortnum desc");
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
		log.info("字典管理-获取字典列表");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "6", "字典管理-获取字典列表");
		renderJson(jsonStr);
	}
	
	public void getActiKey(){
		RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
        List<ProcessDefinition> processDefinitionList = null;
        Record r;
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        processDefinitionQuery.orderByProcessDefinitionVersion().asc();
   	 	processDefinitionList = processDefinitionQuery.list();
   	 	Map<String,ProcessDefinition> map=new LinkedHashMap<String,ProcessDefinition>();
		 if(processDefinitionList!=null&&processDefinitionList.size()>0){
			for(ProcessDefinition pd:processDefinitionList){
				map.put(pd.getKey(), pd);
			}
		 }
		 processDefinitionList=new ArrayList<ProcessDefinition>(map.values());
        for (ProcessDefinition processDefinition : processDefinitionList) {
        	Map<String,String> mapActi=new HashMap<String,String>();
        	r = bru.toRecord(processDefinition);
        	mapActi.put("name", r.getStr("name"));
        	mapActi.put("key", r.getStr("key"));
        	list.add(mapActi);
        }
        renderJson(list);
	}
	
	 public void getKey() {
	    	List<Record> list
	    	= Db.find(
					"select DISTINCT KEY,NAME from SYS_PARAM_INFO where 1=1 and status='1'  ORDER BY KEY");
			renderJson(list);
	    	
	    }
	 //判断是否为配置登记簿
	 public void getDjbKey(){
			List<Record> list = Db.find(
					"select DISTINCT KEY,NAME from SYS_PARAM_INFO where 1=1 and status='1' and isyypzdjb='1' ORDER BY KEY");
			renderJson(list);
	 }
}

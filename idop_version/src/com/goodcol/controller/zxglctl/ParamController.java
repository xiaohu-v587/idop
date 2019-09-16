package com.goodcol.controller.zxglctl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 质效系统字典表相关操作
 * 2018年5月4日15:53:09
 * @author liutao
 *
 */
@RouteBind(path = "/zxparam")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class ParamController extends BaseCtl {
	public static Logger log = Logger.getLogger(ParamCtl.class);

	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取字典的所有信息
	 */
	public void getList() {
		String key = getPara("key");
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(key) != null) {
			sb.append(" and (key = ?  or name like '%" + key + "%' or remark like '%" + key + "%') ");
			listStr.add(key);
		}
		String sql = "select * ";
		String extrasql = " from gcms_param_info where 1=1 " + sb.toString() + " order by key,sortnum";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extrasql, listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-查询");
		renderJson();
	}

	/**
	 * 字典添加页面
	 */
	public void add() {
		render("form.jsp");
	}

	/**
	 * 获取编辑时的回显数据
	 */
	public void getDetail() {
		String id = getPara("key");
		String sql = "select * from gcms_param_info where 1 = 1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("record", list.get(0));
		renderJson();

	}

	/**
	 * 保存新增的字典信息
	 */
	public void save() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String key = getPara("key");
		String name = getPara("name");
		String val = getPara("val");
		String remark = getPara("remark");
		String sortNum = getPara("sortNum");
		String pid = getPara("pid");
		if(StringUtils.isBlank(pid)){
			pid = null;
		}
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db.find("select val,name from gcms_param_info where key = '" + key + "'");
		// 判断
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val")) || name.equals(list.get(i).getStr("name"))) {
				flag = "1";
				break;
			}
		}
		if (!flag.equals("1")) {
			Db.update("insert into gcms_param_info(id,key,name,val, pid, remark,sortnum,status) values(?,?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), key, name, val, pid, remark, sortNum, "1" });
			flag = "2";
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "4", "字典管理-新增");
		renderText(flag);
	}

	/**
	 * 更新字典信息
	 **/
	public void update() {
		String id = getPara("id");
		String key = getPara("key").trim();
		String name = getPara("name").trim();
		String val = getPara("val").trim();
		String remark = getPara("remark").trim();
		String sortNum = getPara("sortNum").trim();
		String pid = getPara("pid");
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db
				.find("select val,name from gcms_param_info where key = '" + key + "' and id <> '" + id + "' ");
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val")) || name.equals(list.get(i).getStr("name"))) {
				flag = "1";
				break;
			}
		}
		if (!flag.equals("1")) {
			Db.update("update gcms_param_info set key=?,name=?,val=?,pid=?,remark=?,sortnum=? where id= ? ",
					new Object[] { key, name, val, pid, remark, sortNum, id });
			flag = "2";
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-更新");
		renderJson(flag);
	}

	/**
	 * 删除数据字典
	 */
	public void del() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update gcms_param_info set status=? where id = ? ", "0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update gcms_param_info set status=? where id = ? ", "0", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		setAttr("record", flag);
		renderJson();
	}

	/**
	 * 修改状态启动、停止
	 */
	public void changeStatus1() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update gcms_param_info set status=? where id = ? ", "0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update gcms_param_info set status=? where id = ? ", "0", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-停用状态");
		setAttr("record", flag);
		renderJson();
	}

	public void changeStatus0() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update gcms_param_info set status=? where id = ? ", "1", uuid);
			}
			flag = "1";
		} else {
			Db.update("update gcms_param_info set status=? where id = ? ", "1", uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-启动状态");
		setAttr("record", flag);
		renderJson();
	}

	/**
	 * 字典下拉框
	 **/
	public void getCombox() {
		String key = getPara("key");
		String val = getPara("val");
		String flag = getPara("flag");
		if(StringUtils.isNotBlank(key)){
			String sql = "select id,name,val from gcms_param_info where status=? and key = ?";
			if (StringUtils.isNotBlank(val) && "0".equals(flag)) { // 不包含
				sql += " and val <> '" + val + "'";
			}
			List<Record> list = Db.find(sql, "1", key);
			renderJson(list);
		}else{
			String sql = "select id,name,val from gcms_param_info where status=? order by key";
			List<Record> list = Db.find(sql, "1");
			renderJson(list);
		}
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

	/**
	 * 字典映射
	 **/
	public void getDict() {
		String key = getPara("key");
		List<Record> list = Db.find("select val,name,remark from gcms_param_info where key = ? order by to_number(sortnum) desc", key);
		
		String jsonStr = "";
//		JSONArray array = JSONArray.fromObject(list);
//		for (int i = 0; i < array.size(); i++) {
//			String columns = array.getJSONObject(i).getString("columns");
//			if (i == 0) {
//				jsonStr = columns;
//			} else if (i > 0) {
//				jsonStr = columns + "," + jsonStr;
//			}
//		}
		
		//将下拉列表改成排序的
		if(null!=list&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				String columns = JSONObject.fromObject(list.get(i)).getString("columns");
				if (i == 0) {
					jsonStr = columns;
				} else if (i > 0) {
					jsonStr = columns + "," + jsonStr;
				}
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
					"select val,name from gcms_param_info where key = ? and val!=5 and val!=6 and val!=7 and val!=3 and val!=0",
					key);
			array = JSONArray.fromObject(list);
		} else if (type.equals(PropertiesContent.get("list"))) {
			list = Db.find(
					"select val,name from gcms_param_info where key = ? and val!=5 and val!=6 and val!=7 and val!=2 and val!=0 order by sortnum",
					key);
			array = JSONArray.fromObject(list);
		} else {
			list = Db.find("select val,name from gcms_param_info where key = ? order by sortnum", key);
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
		renderJson(jsonStr);
	}

	/**
	 * 查询关联字段的类型
	 */
	public void getGlFieldType() {
		String key = getPara("key");
		List<Record> list = Db.find("select val,name from gcms_param_info where key = ? and val!=5 and val!=6 and val!=7 and val!=3 and val!=0 and val!=2", key);
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

	public void getDictText() {
		String key = getPara("key");
		String val = getPara("val");
		String remark = Db.queryStr("select name from gcms_param_info where key = ? and val = ? ",
				new Object[] { key, val });
		if (AppUtils.StringUtil(remark) == null) {
			remark = "null";
		}
		renderText(remark);
	}
	public void getZxDictText(){
		String key = getPara("key");
		String val = getPara("val");
		String remark = Db.queryStr("select name from gcms_param_info where key = ? and val = ? ",
				new Object[] { key, val });
		if (AppUtils.StringUtil(remark) == null) {
			remark = "";
		}
		renderJson(remark);
	}
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
		renderJson();
	}

	/**
	 * 刷新缓存
	 */
	public void init() {
		ParamContainer container = new ParamContainer();
		container.init();
		renderNull();
	}

	/**
	 * 删除数据字典
	 */
	public void delete() {
		String id = getPara("id");
		Db.update("delete from gcms_param_info where id =?", id);

		renderNull();
	}

	/**
	 * 关联表字段
	 */
	public void getGl() {
		List<Record> list = Db.find("select distinct key,name from gcms_param_info ");
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
		renderJson(map);
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
	public void getMonth(){
		List<Record> records=new ArrayList<Record>();
		for(int n=1978;n<=2018;n++){
			for(int i=1;i<=12;i++){
				StringBuffer sb=new StringBuffer();
				if(String.valueOf(i).length()<2){
					sb.append(n+"0"+i);
				}else{
					sb.append(n+""+i);
				}
				Record record=new Record();
				record.set("run_time",sb.toString());
				records.add(record);
			}
		}
		for(int n=2018;n<2048;n++){
			for(int i=1;i<=12;i++){
				StringBuffer sb=new StringBuffer();
				if(String.valueOf(i).length()<2){
					sb.append(n+"0"+i);
				}else{
					sb.append(n+""+i);
				}
				Record record=new Record();
				record.set("run_time",sb.toString());
				records.add(record);
			}
		}
		renderJson(records);
	}
	public void getDictByCode(){
		String key = getPara("key");
		String code=getPara("code");
		String parentKey=getPara("parentKey");
		String pid=Db.use("default").queryStr("select id from gcms_param_info where key=? and name=?",new Object[]{parentKey,code});
		List<Record> list = Db.use("default").find("select val,name,remark from gcms_param_info where key = ? and pid=? order by sortnum desc",new Object[]{key,pid});
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
}

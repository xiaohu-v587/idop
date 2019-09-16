package com.goodcol.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * 参数管理
 * 
 * @author first blush
 */
@RouteBind(path = "/test")
@Before({ ManagerPowerInterceptor.class })
public class TestCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(TestCtl.class);

	public void index() {
		render("index.jsp");
	}

	public void test(){
		System.out.println("11111111111111111111111111");
		ActivitiUtil.deploy("resources/activiti/diagrams/MyProcess.bpmn");
		/*ProcessInstance processInstance=ActivitiUtil.startInstanceByKey("HelloWorld");
		System.out.println("流程实例ID："+processInstance.getId());//流程实例ID
		System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//流程定义ID
*/		renderNull();
	}

	/**
	 * 获取字典的所有信息
	 * */
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
		String sql = "select * ";
		String extrasql = " from sys_param_info where 1=1 " + sb.toString()
				+ " order by key,sortnum";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql,
				listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extrasql,
					listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("listUser--r.getList():" + r.getList());
		log.info("listUser--r.getTotalRow():" + r.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "字典管理", "3", "字典管理-查询");
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
		String sql = "select * from SYS_PARAM_INFO where 1 = 1 ";
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
	 * */
	public void save() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String key = getPara("key");
		String name = getPara("name");
		String val = getPara("val");
		String remark = getPara("remark");
		String sortNum = getPara("sortNum");
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db
				.find("select val,remark from SYS_PARAM_INFO where key = '"
						+ key + "'");
		// 判断
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val"))
					|| remark.equals(list.get(i).getStr("remark"))) {
				flag = "1";
				break;
			}
		}
		if (!flag.equals("1")) {
			Db.update(
					"insert into SYS_PARAM_INFO(id,key,name,val,remark,sortnum,status) values(?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), key, name, val,
							remark, sortNum, "1" });
			flag = "2";
		}
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "字典管理", "4", "字典管理-新增");
		renderText(flag);
	}

	/**
	 * 更新字典信息
	 **/
	public void update() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String id = getPara("id");
		String key = getPara("key").trim();
		String name = getPara("name").trim();
		String val = getPara("val").trim();
		String remark = getPara("remark").trim();
		String sortNum = getPara("sortNum").trim();
		String flag = "0";
		// 判断字典键值是否有重复
		List<Record> list = Db
				.find("select val,remark from SYS_PARAM_INFO where key = '"
						+ key + "' and id <> '" + id + "' ");
		for (int i = 0; i < list.size(); i++) {
			if (val.equals(list.get(i).getStr("val"))
					|| remark.equals(list.get(i).getStr("remark"))) {
				flag = "1";
				break;
			}
		}
		if (!flag.equals("1")) {
			Db.update(
					"update SYS_PARAM_INFO set key=?,name=?,val=?,remark=?,sortnum=? where id= ? ",
					new Object[] { key, name, val, remark, sortNum, id });
			flag = "2";
		}
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-更新");
		renderJson(flag);
	}

	/**
	 * 删除数据字典
	 * */
	public void del() {
		// String user_no = getCurrentUser().getStr("USER_NO");
		String flag = "0";
		String uuids = getPara("uuid");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ",
						"0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0",
					uuids);
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
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ",
						"0", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "0",
					uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-停用状态");
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
				Db.update("update SYS_PARAM_INFO set status=? where id = ? ",
						"1", uuid);
			}
			flag = "1";
		} else {
			Db.update("update SYS_PARAM_INFO set status=? where id = ? ", "1",
					uuids);
			flag = "1";
		}
		// LoggerUtil.getIntanceof().saveLogger(user_no, menuname, "6",
		// "字典管理-删除");
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "字典管理", "5", "字典管理-启动状态");
		setAttr("record", flag);
		renderJson();
	}

	/**
	 * 字典下拉框
	 * **/
	public void getCombox() {
		String sql = "select distinct(key),name from sys_param_info where status=? order by key";
		List<Record> list = Db.find(sql, "1");
		renderJson(list);
	}

	/**
	 * 根据字典编码查询字典val remark
	 * */
	public void getKeyList() {
		String key = getPara("key");
		List<Record> list = Db
				.find("select val,remark from SYS_PARAM_INFO where 1=1 and status='1' and key = ?",
						key);
		renderJson(list);
	}

	/**
	 * 字典映射
	 * **/
	public void getDict() {
		String key = getPara("key");
		List<Record> list = Db.find(
				"select val,remark from SYS_PARAM_INFO where key = ?", key);
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
		String remark = Db.queryStr(
				"select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		if (AppUtils.StringUtil(remark) == null) {
			remark = "null";
		}
		renderText(remark);
	}

	public void loadDict() {
		List<Object> result = new ArrayList<Object>();
		String dictTypeId = getPara("dictTypeId");
		if (StringUtils.isEmpty(dictTypeId)) {
			renderNull();
			return;
		}
		String sqlStr = "select val as dictID ,remark as dictName from sys_param_info where key=?";
		List<Record> list = Db.find(sqlStr, dictTypeId);
		Map<String, Object> map = null;
		for (Record record : list) {
			map = new HashMap<String, Object>();
			map.put("dictID", record.getStr("dictid"));
			map.put("dictName", record.getStr("dictname"));
			result.add(map);
		}
		setAttr("dictList", result);
		renderJson();
	}

}

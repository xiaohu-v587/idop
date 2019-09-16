/**
 * 
 */
package com.goodcol.controller;

import java.util.ArrayList;
import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.anatation.RouteBind;

import net.sf.json.JSONArray;

/**
 * @author Administrator
 *
 */
@RouteBind(path = "/module_index")
public class ModuleCtl extends BaseCtl {

	/**
	 * 模块首页
	 */
	public void index() {
		String menu_id = getPara("menu_id");
		List<Record> records = Db.find("select * from sys_crud_auto where menu_id=?", menu_id);
		Record record = records.get(0);
		String table_name = record.getStr("table_name");
		String sql_content = record.getStr("sql_content");
		String extra_sql_content = record.getStr("extra_sql_content");
		String where_sql_content = record.getStr("where_sql_content");
		String listfields = record.getStr("fields");
		String list_field_names = record.getStr("list_field_names");
		String fieldTypes = record.getStr("field_types");
		String pkField = getpkField(table_name);
		List<Record> menus = Db.find("select name from sys_menu_info where id=?", menu_id);
		String menu_name = menus.get(0).getStr("name");
		String query_criteria = record.getStr("query_criteria");
		String list_field = record.getStr("list_field");
		String query_criteria_txt = record.getStr("query_criteria_txt");
		String list_field_txt = record.getStr("list_field_txt");
		String query_criteria_type = record.getStr("query_criteria_type");
		String list_field_type = record.getStr("list_field_type");
		String dialog_width = record.getStr("dialog_width");
		String dialog_height = record.getStr("dialog_height");
		// 查询条件
		String[] querys = query_criteria.split(",");
		String[] queryTxts = query_criteria_txt.split(",");
		String[] queryTypes = query_criteria_type.split(",");
		List<Object[]> queryList = new ArrayList<Object[]>();
		for (int i = 0; i < querys.length; i++) {
			Object[] obj = new Object[3];
			obj[0] = querys[i].toLowerCase();
			obj[1] = queryTxts[i];
			obj[2] = queryTypes[i];
			queryList.add(obj);
		}
		List<Object[]> fieldList = new ArrayList<Object[]>();
		if (AppUtils.StringUtil(sql_content) != null && AppUtils.StringUtil(extra_sql_content) != null
				&& AppUtils.StringUtil(where_sql_content) != null && AppUtils.StringUtil(listfields) != null
				&& AppUtils.StringUtil(list_field_names) != null && AppUtils.StringUtil(fieldTypes) != null) {
			String[] fields = listfields.split(",");
			String[] field_txts = list_field_names.split(",");
			String[] field_types = fieldTypes.split(",");
			for (int i = 0; i < fields.length; i++) {
				Object[] obj = new Object[3];
				obj[0] = fields[i].toLowerCase();
				obj[1] = field_txts[i];
				obj[2] = field_types[i];
				fieldList.add(obj);
			}
		} else {
			// 列表字段
			String[] fields = list_field.split(",");
			String[] field_txts = list_field_txt.split(",");
			String[] field_types = list_field_type.split(",");

			for (int i = 0; i < fields.length; i++) {
				Object[] obj = new Object[3];
				obj[0] = fields[i].toLowerCase();
				obj[1] = field_txts[i];
				obj[2] = field_types[i];
				fieldList.add(obj);
			}
		}

		setAttr("dialog_width", dialog_width);
		setAttr("dialog_height", dialog_height);
		setAttr("queryList", queryList);
		setAttr("fieldList", fieldList);
		setAttr("querys", JSONArray.fromObject(queryList).toString());
		setAttr("menu_id", menu_id);
		setAttr("menu_name", menu_name);
		setAttr("table_name", table_name);
		setAttr("pkField", pkField.toLowerCase());
		renderJsp("module_index.jsp");
	}

	/**
	 * 获取模块首页数据
	 */
	public void getList() {
		String menu_id = getPara("menu_id");
		List<Record> records = Db.find("select * from sys_crud_auto where menu_id=?", menu_id);
		Record record = records.get(0);
		String table_name = record.getStr("table_name");
		String sql_content = record.getStr("sql_content");
		String extra_sql_content = record.getStr("extra_sql_content");
		String where_sql_content = record.getStr("where_sql_content");
		String order_by_sql_content = record.getStr("order_by_sql_content");
		String list_field_names = record.getStr("list_field_names");
		String fieldTypes = record.getStr("field_types");
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		String query_criteria = records.get(0).getStr("query_criteria");
		String list_field = records.get(0).getStr("list_field");
		String[] querys = query_criteria.split(",");
		for (int i = 0; i < querys.length; i++) {
			String str = getPara(querys[i].toLowerCase());
			if (AppUtils.StringUtil(str) != null) {
				sb.append(" and " + querys[i].toLowerCase() + "=?");
				listStr.add(str);
			}
		}
		Page<Record> r = null;
		if (AppUtils.StringUtil(sql_content) != null && AppUtils.StringUtil(extra_sql_content) != null
				&& AppUtils.StringUtil(where_sql_content) != null && AppUtils.StringUtil(list_field_names) != null
				&& AppUtils.StringUtil(fieldTypes) != null) {
			String sql = sql_content.trim();
			String extrasql = " " + extra_sql_content.trim() + " " + where_sql_content.trim() + " " + sb.toString()
					+ " " + (AppUtils.StringUtil(order_by_sql_content) == null ? "" : order_by_sql_content);
			r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		} else {
			String sql = "select " + list_field.toLowerCase();
			String extrasql = " from " + table_name.toLowerCase() + " where 1=1" + sb.toString();
			r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		}

		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	/**
	 * 模块增加或修改页面
	 */
	public void module_form() {
		String menu_id = getPara("menu_id");
		String uuid = getPara("uuid");
		List<Record> records = Db.find("select * from sys_crud_auto where menu_id=?", menu_id);
		Record record = records.get(0);
		String table_name = record.getStr("table_name");
		String pkField = getpkField(table_name);
		String save_update_field = record.getStr("save_update_field");
		String save_update_field_txt = record.getStr("save_update_field_txt");
		String save_update_field_type = record.getStr("save_update_field_type");
		String field_length = record.getStr("field_length");
		String field_edit = record.getStr("field_edit");
		String input_content = record.getStr("input_content");
		// 增加、修改字段
		String[] sufields = save_update_field.split(",");
		String[] sufield_txts = save_update_field_txt.split(",");
		String[] sufield_types = save_update_field_type.split(",");
		String[] inputs = input_content.split(",");
		String[] field_len = null;
		String[] edits = null;
		if (AppUtils.StringUtil(field_length) != null) {
			field_len = field_length.split(",");
		}
		if (AppUtils.StringUtil(field_edit) != null) {
			edits = field_edit.split(",");
		}
		List<Object[]> sufieldList = new ArrayList<Object[]>();
		for (int i = 0; i < sufields.length; i++) {
			Object[] obj = new Object[6];
			obj[0] = sufields[i].toLowerCase();
			obj[1] = sufield_txts[i];
			obj[2] = sufield_types[i];
			obj[3] = field_len == null ? 0 : field_len[i];
			obj[4] = edits == null ? "" : edits[i];
			obj[5] = inputs[i];
			sufieldList.add(obj);
		}
		setAttr("pkField", pkField);
		setAttr("sufieldList", sufieldList);
		setAttr("table_name", table_name);
		setAttr("uuid", uuid);
		renderJsp("module_form.jsp");
	}

	/**
	 * 模块保存或更新
	 */
	public void module_save() {
		String table_name = getPara("table_name");
		String pkField = getpkField(table_name);
		String pk = getPara(pkField);
		List<Record> records = Db.find("select * from sys_crud_auto where table_name=?", table_name);
		String save_update_field = records.get(0).getStr("save_update_field");
		String save_update_field_type = records.get(0).getStr("save_update_field_type");
		String[] fieldTypes = save_update_field_type.split(",");
		StringBuffer sb = new StringBuffer();
		String[] sufields = save_update_field.split(",");
		String field_type = "";
		StringBuffer setSql = new StringBuffer();
		for (int i = 0; i < sufields.length; i++) {
			sb.append("?,");
			if (!sufields[i].equals(pkField)) {
				setSql.append(sufields[i].toLowerCase() + "=?,");
			} else {
				field_type = fieldTypes[i];
			}

		}
		String sbContent = sb.toString();
		if (sbContent.endsWith(",")) {
			sbContent = sbContent.substring(0, sbContent.length() - 1);
		}
		String setSqlContent = setSql.toString();
		if (setSqlContent.endsWith(",")) {
			setSqlContent = setSqlContent.substring(0, setSqlContent.length() - 1);
		}
		int flag = 0;
		// 根据id是否为空来判断是新增还是修改
		if (AppUtils.StringUtil(pk) != null) {
			List<String> strList = new ArrayList<String>();
			for (int i = 0; i < sufields.length; i++) {
				if (!sufields[i].equals(pkField)) {
					String str = getPara(sufields[i].toLowerCase());
					strList.add(str);
				}
			}
			strList.add(pk);
			flag = Db.update("update " + table_name + " set " + setSqlContent + " where " + pkField + "=?",
					strList.toArray());
		} else {
			Object[] obj = new Object[sufields.length];
			for (int i = 0; i < sufields.length; i++) {
				if (!sufields[i].equals(pkField)) {
					if (fieldTypes[i].equals("5")) {
						obj[i] = DateTimeUtil.getTime();
					} else if (fieldTypes[i].equals("6")) {
						obj[i] = getCurrentUser().getStr("org_id");
					} else if (fieldTypes[i].equals("7")) {
						obj[i] = getCurrentUser().getStr("USER_NO");
					} else {
						obj[i] = getPara(sufields[i].toLowerCase());
					}
				} else {
					if (field_type.equals("3")) {
						obj[i] = AppUtils.getStringSeq();
					} else {
						obj[i] = pk;
					}
				}
			}
			flag = Db.update("insert into " + table_name + "(" + save_update_field.toLowerCase() + ") values ("
					+ sbContent + ")", obj);
		}
		setAttr("flag", flag);
		renderJson();
	}

	private String getpkField(String table_name) {
		String pkField = "";
		List<Record> rs = Db.find("SELECT A .column_name FROM user_cons_columns A,user_constraints b "
				+ "WHERE A .constraint_name = b.constraint_name " + "AND b.constraint_type = 'P' AND A .table_name = ?",
				table_name);
		if (rs != null && !rs.isEmpty()) {
			Record record = rs.get(0);
			pkField = record.getStr("column_name");
		}
		return pkField;
	}

	/**
	 * 模块-删除
	 */
	public void module_del() {
		String menu_id = getPara("menu_id");
		List<Record> records = Db.find("select * from sys_crud_auto where menu_id=?", menu_id);
		Record record = records.get(0);
		String table_name = record.getStr("table_name");
		String pkField = getpkField(table_name);
		String ids = getPara("ids");
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String id = array[i];
				Db.update("delete from " + table_name + " where " + pkField + "=?", new Object[] { id });
			}
		} else {
			Db.update("delete from " + table_name + " where " + pkField + "=?", new Object[] { ids });
		}
		setAttr("result", "success");
		renderJson();
	}

	/**
	 * 模块-获得填充表单的详情
	 * 
	 */
	public void module_getDetail() {
		String id = getPara("id");
		String table_name = getPara("table_name");
		String pkField = getpkField(table_name);
		String sql = "select * from " + table_name + " where 1=1";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and " + pkField + "= ?");
			listStr.add(id.trim());
		}
		List<Record> list = Db.find(sql + sb.toString(), listStr.toArray());
		setAttr("datas", list.get(0));
		renderJson();
	}

	/**
	 * 获得下拉框的name:value
	 */
	public void getCombobox() {
		String table_name = getPara("table_name");
		String field_name = getPara("field_name").toUpperCase();
		List<Record> records = Db.find("select * from sys_gl_crud_auto where table_name=? and field_name=?",
				new Object[] { table_name, field_name });
		List<Record> name_values = new ArrayList<Record>();
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			String item_field = record.getStr("item_field");
			if (AppUtils.StringUtil(item_field) != null) {
				name_values = Db.find("select remark as name,val as value from sys_param_info where key=?", item_field);
			} else {
				String gl_table_name = record.getStr("gl_table_name");
				String selct_field = record.getStr("selct_field");
				String[] selcts = selct_field.split(",");
				name_values = Db
						.find("select " + selcts[1] + " as name," + selcts[0] + " as value from " + gl_table_name);
			}

		}
		renderJson(name_values);
	}
}

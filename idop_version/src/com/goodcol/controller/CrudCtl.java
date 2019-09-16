/**
 * 
 */
package com.goodcol.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.Dict;
import com.goodcol.util.FreeMarkerUtil;
import com.goodcol.util.PropertiesContent;
import com.goodcol.util.ext.anatation.RouteBind;

import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 增删改查配置化
 * 
 * @author Administrator
 *
 */
@RouteBind(path = "/crud_auto")
@Before({ ManagerPowerInterceptor.class })
public class CrudCtl extends BaseCtl {

	/**
	 * 首页
	 */
	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获得增删改查配置化数据
	 */
	public void getList() {
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		String sql = "select c.*,s.name";
		String extrasql = " from  sys_crud_auto c left join sys_menu_info s on c.menu_id=s.id order by modify_time desc";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	/**
	 * 配置属性页面
	 */
	public void form() {
		String table_name = getPara("table_name");
		List<Record> records = null;
		if (AppUtils.StringUtil(table_name) == null) {
			records = Db.find("select t.table_name from user_tables t");
		} else {
			records = Db.find("select t.table_name from user_tables t order by case when table_name='" + table_name
					+ "' then 1 else 2 end asc");
		}

		List<Record> rs = null;
		if (records != null && !records.isEmpty()) {
			if (AppUtils.StringUtil(table_name) == null) {
				table_name = records.get(0).getStr("table_name");
			}
			rs = Db.find(
					"select table_name,column_name,(case when comments is null then '无' else comments end) as comments from user_col_comments where Table_Name=? "
							+ "order by column_name",
					table_name);
		}
		//查询(查询条件、列表字段、增改字段)
		List<Record>slist=Db.find("select query_criteria,list_field,save_update_field from sys_crud_auto where table_name=?",table_name);
		Record record=new Record();
		if(slist!=null&&!slist.isEmpty()){
			record=slist.get(0);
		}
		Map<String, Object> map = getInfos(table_name);
		// 按照表中列的顺序排序
		List<Record> list = Db.find("select column_name from user_tab_columns where table_name=? order by column_id",
				table_name);
		setAttr("listRecords", list);
		setAttr("infoMap", map);
		setAttr("table_name", table_name);
		setAttr("rs", rs);
		setAttr("records", records);
		setAttr("query", Dict.QUERY);
		setAttr("list", Dict.LIST);
		setAttr("save_update", Dict.SAVE_UPDATE);
		setAttr("record",record);
		render("form.jsp");
	}

	/**
	 * 根据table_name查出查询条件、列表字段、增加修改字段,判断是否为空
	 * 
	 * @param table_name
	 * @return
	 */
	public Map<String, Object> getInfos(String table_name) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> rd = Db.find("select * from sys_crud_auto where table_name=?", table_name);
		String query_criteria = "";
		String list_field = "";
		String save_update_field = "";
		if (rd != null && !rd.isEmpty()) {
			query_criteria = rd.get(0).getStr("query_criteria");
			list_field = rd.get(0).getStr("list_field");
			save_update_field = rd.get(0).getStr("save_update_field");
		}
		map.put("query_criteria", query_criteria);
		map.put("list_field", list_field);
		map.put("save_update_field", save_update_field);
		return map;

	}

	/**
	 * 获取数据库中所有表的名称
	 */
	public void getTableNames() {
		List<Record> records = Db.find("select t.table_name from user_tables t");
		renderJson(records);
	}

	/**
	 * 根据表名查出表字段、注释
	 */
	public void getFieldDetails() {
		String table_name = getPara("table_name");
		List<Record> records = Db
				.find("select table_name,column_name,(case when comments is null then '无' else comments end) as comments from user_col_comments where Table_Name=? "
						+ "order by column_name", table_name);
		// 按照表中列的顺序排序
		List<Record> list = Db.find("select column_name from user_tab_columns where table_name=? order by column_id",
				table_name);
		//查询(查询条件、列表字段、增改字段)
		List<Record>slist=Db.find("select query_criteria,list_field,save_update_field from sys_crud_auto where table_name=?",table_name);
		Record record=new Record();
		if(slist!=null&&!slist.isEmpty()){
			record=slist.get(0);
		}
		setAttr("listRecords", list);
		setAttr("records", records);
		setAttr("record",record);
		Map<String, Object> map = getInfos(table_name);
		setAttr("infoMap", map);
		renderJson();
	}

	/**
	 * 设置查询条件、设置列表字段、设置增删改查字段
	 */
	public void saveField() {
		String table_name = getPara("table_name");
		String infos = getPara("infos");
		String type = getPara("type");
		StringBuffer params = new StringBuffer();
		StringBuffer types = new StringBuffer();
		StringBuffer inputs = new StringBuffer();
		String[] info = infos.split(",");
		StringBuffer lenParams = new StringBuffer();
		StringBuffer edits = new StringBuffer();
		for (int i = 0; i < info.length; i++) {
			String str = getPara("info_" + info[i]);
			String typeStr = getPara("type_" + info[i]);
			String inputStr = getPara("input_" + info[i]);
			String lenStr = getPara("len_" + info[i]);
			String editStr = getPara("edit_" + info[i]);
			edits.append(editStr + ",");
			lenParams.append(AppUtils.StringUtil(lenStr) == null ? 0 + "," : lenStr + ",");
			types.append(typeStr + ",");
			params.append(str + ",");
			inputs.append(inputStr + ",");
		}
		String editContent = getStrParam(edits);
		String lenParamsContent = getStrParam(lenParams);
		String typeContent = getStrParam(types);
		String paramsContent = getStrParam(params);
		String inputContent = getStrParam(inputs);
		List<Record> records = getCurdAutoRecords(table_name);
		int flag = getSaveFieldFlag(records, type, infos, DateTimeUtil.getTime(), paramsContent, typeContent,
				table_name, inputContent, lenParamsContent, editContent);
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 获得截取的参数
	 * 
	 * @param types
	 * @return
	 */
	private String getStrParam(StringBuffer strParam) {
		String params = strParam.toString();
		if (AppUtils.StringUtil(params) != null) {
			if (params.endsWith(",")) {
				params = params.substring(0, params.length() - 1);
			}
		}
		return params;
	}

	/**
	 * 保存或者更新sys_crud_auto
	 * 
	 * @param records
	 * @param type
	 * @param infos
	 * @param time
	 * @param paramsContent
	 * @param typeContent
	 * @param table_name
	 * @param inputContent
	 * @param editContent
	 * @param lenParamsContent
	 * @param pkContent
	 * @return
	 */
	private int getSaveFieldFlag(List<Record> records, String type, String infos, String time, String paramsContent,
			String typeContent, String table_name, String inputContent, String lenParamsContent, String editContent) {
		int flag = 0;
		if (records != null && !records.isEmpty()) {
			if (type.equals(Dict.QUERY)) {
				flag = Db.update(
						"update sys_crud_auto set query_criteria=?,modify_time=?,query_criteria_txt=?,query_criteria_type=? where table_name=?",
						new Object[] { infos, DateTimeUtil.getTime(), paramsContent, typeContent, table_name });
			} else if (type.equals(Dict.LIST)) {
				flag = Db.update(
						"update sys_crud_auto set list_field=?,modify_time=?,list_field_txt=?,list_field_type=? where table_name=?",
						new Object[] { infos, DateTimeUtil.getTime(), paramsContent, typeContent, table_name });
			} else if (type.equals(Dict.SAVE_UPDATE)) {
				flag = Db.update(
						"update sys_crud_auto set save_update_field=?,modify_time=?,save_update_field_txt=?,save_update_field_type=?,input_content=?,field_length=?,field_edit=? where table_name=?",
						new Object[] { infos, DateTimeUtil.getTime(), paramsContent, typeContent, inputContent,
								lenParamsContent, editContent, table_name });
			}
		} else {
			if (type.equals(Dict.QUERY)) {
				flag = Db.update(
						"insert into sys_crud_auto (id,table_name,query_criteria,modify_time,query_criteria_txt,query_criteria_type) values(?,?,?,?,?,?)",
						new Object[] { AppUtils.getStringSeq(), table_name, infos, DateTimeUtil.getTime(),
								paramsContent, typeContent });
			} else if (type.equals(Dict.LIST)) {
				flag = Db.update(
						"insert into sys_crud_auto (id,table_name,list_field,modify_time,list_field_txt,list_field_type) values(?,?,?,?,?,?)",
						new Object[] { AppUtils.getStringSeq(), table_name, infos, DateTimeUtil.getTime(),
								paramsContent, typeContent });
			} else if (type.equals(Dict.SAVE_UPDATE)) {
				flag = Db.update(
						"insert into sys_crud_auto (id,table_name,save_update_field,modify_time,save_update_field_txt,save_update_field_type,input_content,field_length,field_edit) values(?,?,?,?,?,?,?,?,?)",
						new Object[] { AppUtils.getStringSeq(), table_name, infos, DateTimeUtil.getTime(),
								paramsContent, typeContent, inputContent, lenParamsContent, editContent });
			}

		}
		return flag;
	}

	/**
	 * 根据表的名称获取一条记录，判断是否存在
	 * 
	 * @param table_name
	 * @return
	 */
	public List<Record> getCurdAutoRecords(String table_name) {
		List<Record> records = Db.find("select * from sys_crud_auto where table_name=?", table_name);
		return records;
	}

	/**
	 * 自动生成代码
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void generateCode() throws IOException, TemplateException {
		String table_name = getPara("table_name");
		// 包名
		String packageName = getPara("packageName");
		// 作者
		String author = getPara("author");
		// 控制器类名
		String className = getPara("classN");
		// 请求路径
		String url_path = getPara("url_path");
		// 模板路径
		String template_path = getPara("template_path");
		// 生成文件路径
		String dest_path = getPara("dest_path");
		// 增改对话框宽度
		String dialog_width = getPara("dialog_width");
		// 增改对话框高度
		String dialog_height = getPara("dialog_height");
		// 获得生成后缀名为.java文件的信息
		Map<String, Object> root = getRootInfo(table_name, packageName, author, className, url_path, template_path,
				dest_path);
		// 获得生成后缀名为.jsp文件的信息
		Map<String, Object> map = getJspInfo(table_name, url_path);
		String result = FreeMarkerUtil.gen(root, map, table_name, className, template_path, dest_path);
		// 设置已生成代码状态
		int status = 1;
		Db.update("update sys_crud_auto set dialog_width=?,dialog_height=?,status=? where table_name=?",
				new Object[] { dialog_width, dialog_height, status, table_name });
		setAttr("result", result);
		renderJson();

	}

	/**
	 * 赋予jsp文件的freemarker{key:valaue},获得value值
	 * 
	 * @param table_name
	 * @param url_path
	 * @return
	 */
	private Map<String, Object> getJspInfo(String table_name, String url_path) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> records = getCurdAutoRecords(table_name);
		Record record = records.get(0);
		String sql_content = record.getStr("sql_content");
		String extra_sql_content = record.getStr("extra_sql_content");
		String where_sql_content = record.getStr("where_sql_content");
		String listfields = record.getStr("fields");
		String list_field_names = record.getStr("list_field_names");
		String fieldTypes = record.getStr("field_types");
		String list_field_txt = "";
		String list_field = "";
		String list_field_type = "";
		if (AppUtils.StringUtil(sql_content) != null && AppUtils.StringUtil(extra_sql_content) != null
				&& AppUtils.StringUtil(where_sql_content) != null && AppUtils.StringUtil(listfields) != null
				&& AppUtils.StringUtil(list_field_names) != null && AppUtils.StringUtil(fieldTypes) != null) {
			list_field = record.getStr("fields");
			list_field_txt = record.getStr("list_field_names");
			list_field_type = record.getStr("field_types");
		} else {
			list_field = record.getStr("list_field");
			list_field_txt = record.getStr("list_field_txt");
			list_field_type = record.getStr("list_field_type");
		}
		String query_criteria = record.getStr("query_criteria");
		String save_update_field = record.getStr("save_update_field");
		String query_criteria_txt = record.getStr("query_criteria_txt");
		String save_update_field_txt = record.getStr("save_update_field_txt");
		String query_criteria_type = record.getStr("query_criteria_type");
		String save_update_field_type = record.getStr("save_update_field_type");
		String field_length = record.getStr("field_length");
		String field_edit = record.getStr("field_edit");
		String dialog_width = record.getStr("dialog_width");
		String dialog_height = record.getStr("dialog_height");
		String input_content = record.getStr("input_content");
		String[] querys = query_criteria.split(",");
		String[] querytxts = query_criteria_txt.split(",");
		String[] querytypes = query_criteria_type.split(",");
		List<Object[]> queryCriterias = new ArrayList<Object[]>();
		for (int i = 0; i < querys.length; i++) {
			Object[] obj = new Object[3];
			obj[0] = querys[i];
			obj[1] = querytxts[i];
			obj[2] = querytypes[i];
			queryCriterias.add(obj);
		}
		String[] fields = list_field.split(",");
		String[] fieldtxts = list_field_txt.split(",");
		String[] fieldtypes = list_field_type.split(",");
		List<Object[]> listFields = new ArrayList<Object[]>();
		for (int i = 0; i < fields.length; i++) {
			Object[] obj = new Object[3];
			obj[0] = fields[i];
			obj[1] = fieldtxts[i];
			obj[2] = fieldtypes[i];
			listFields.add(obj);
		}
		String[] field_len = field_length.split(",");
		String[] edits = field_edit.split(",");
		String[] saveUpdates = save_update_field.split(",");
		String[] saveUpdatetxts = save_update_field_txt.split(",");
		String[] saveUpdatetypes = save_update_field_type.split(",");
		String[] inputs = input_content.split(",");
		List<Object[]> saveUpdateFields = new ArrayList<Object[]>();
		for (int i = 0; i < saveUpdates.length; i++) {
			Object[] obj = new Object[6];
			obj[0] = saveUpdates[i];
			obj[1] = saveUpdatetxts[i];
			obj[2] = saveUpdatetypes[i];
			obj[3] = field_len[i];
			obj[4] = edits[i];
			obj[5] = inputs[i];
			saveUpdateFields.add(obj);
		}
		String pkField = getpkField(table_name);
		map.put("queryCriterias", queryCriterias);
		map.put("listFields", listFields);
		map.put("saveUpdateFields", saveUpdateFields);
		map.put("url_path", url_path);
		map.put("table_name", table_name);
		map.put("pkField", pkField);
		map.put("dialog_width", dialog_width);
		map.put("dialog_height", dialog_height);
		return map;
	}

	/**
	 * 赋予java文件的freemarker{key:valaue},获得value值
	 * 
	 * @param table_name
	 * @param dest_path
	 * @param template_path
	 * @param url_path
	 * @param className
	 * @param author
	 * @param packageName
	 * @return
	 */
	private Map<String, Object> getRootInfo(String table_name, String packageName, String author, String className,
			String url_path, String template_path, String dest_path) {
		Map<String, Object> root = new HashMap<String, Object>();
		List<Record> records = getCurdAutoRecords(table_name);
		String pkField = getpkField(table_name);
		// 表的主键
		root.put("pkField", pkField);
		// 包名
		root.put("packageName", packageName);
		// 路径名
		root.put("url_path", url_path);
		// 表名
		root.put("table_name", table_name);
		if (table_name.indexOf("_") != -1) {
			table_name = table_name.replaceAll("_", "");
		}
		// 类名
		root.put("className", className);
		// 作者
		root.put("author", author);
		Record record = records.get(0);
		String query_criteria = record.getStr("query_criteria");
		String list_field = record.getStr("list_field");
		String save_update_field = record.getStr("save_update_field");
		String sql_content = record.getStr("sql_content");
		String extra_sql_content = record.getStr("extra_sql_content");
		String where_sql_content = record.getStr("where_sql_content");
		String order_by_sql_content = record.getStr("order_by_sql_content");
		root.put("sql_content", sql_content);
		root.put("extra_sql_content", extra_sql_content);
		root.put("where_sql_content", where_sql_content);
		root.put("order_by_sql_content", order_by_sql_content);
		// 查询条件
		root.put("query_criteria", query_criteria);
		// 传入参数
		String[] querys = query_criteria.split(",");
		StringBuffer params = new StringBuffer();
		StringBuffer paramContents = new StringBuffer();
		for (int i = 0; i < querys.length; i++) {
			params.append(
					"String " + querys[i].toLowerCase() + "=getPara(\"" + querys[i].toLowerCase() + "\");\r\n\t\t");
			paramContents.append("if(AppUtils.StringUtil(" + querys[i].toLowerCase()
					+ ")!=null){\r\n\t\t sb.append(\" and " + querys[i].toLowerCase() + "=?\");\r\n\t\t listStr.add("
					+ querys[i].toLowerCase() + ");\r\n\t\t}");
		}
		root.put("params", params.toString());
		root.put("paramContents", paramContents.toString());
		// 列表字段
		root.put("list_field", list_field);
		// 增加、修改字段
		root.put("save_update_field", save_update_field);
		String[] sufields = save_update_field.split(",");
		StringBuffer sb = new StringBuffer();
		StringBuffer setSql = new StringBuffer();
		StringBuffer gParam = new StringBuffer();
		StringBuffer saveOrUpdateParams = new StringBuffer();
		for (int i = 0; i < sufields.length; i++) {
			sb.append("?,");
			if (!sufields[i].equals(pkField)) {
				setSql.append(sufields[i] + "=?,");
				saveOrUpdateParams.append(sufields[i].toLowerCase() + ",");
			}
			if (!sufields[i].equals(pkField)) {
				gParam.append("String " + sufields[i].toLowerCase() + "=getPara(\"" + sufields[i].toLowerCase()
						+ "\");\r\n\t\t");
			}

		}
		String setSqlContent = getStrParam(setSql);
		String sbContent = getStrParam(sb);
		String saveOrUpdateContent = getStrParam(saveOrUpdateParams);
		root.put("saveOrUpdateContent", saveOrUpdateContent);
		// 插入的问号
		root.put("sbContent", sbContent);
		// update中的set语句
		root.put("setSqlContent", setSqlContent);
		// 接收的参数
		root.put("gParam", gParam.toString());
		return root;
	}

	/**
	 * 根据表名称获取表的主键
	 * 
	 * @param table_name
	 * @return
	 */
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
	 * 进入设置查询条件页面或设置列表字段或设置增、改字段页面
	 */
	public void addForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		String table_name = getPara("table_name");
		String infos = getPara("infos");
		String type = getPara("type");
		String[] info = infos.split(",");
		List<String> list = new ArrayList<String>();
		String pkField = getpkField(table_name);
		if (!type.equals(Dict.QUERY)) {
			list.add(pkField);
		}
		for (int i = 0; i < info.length; i++) {
			if (!info[i].equals(pkField)) {
				list.add(info[i]);
			}
		}
		List<Record> records = getCurdAutoRecords(table_name);
		String txtStr = "";
		String typeStr = "";
		String fields = "";
		String inputStr = "";
		String lenStr = "";
		String editStr = "";
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			if (type.equals(Dict.QUERY)) {
				fields = record.getStr("query_criteria");
				txtStr = record.getStr("query_criteria_txt");
				typeStr = record.getStr("query_criteria_type");
			} else if (type.equals(Dict.LIST)) {
				fields = record.getStr("list_field");
				txtStr = record.getStr("list_field_txt");
				typeStr = record.getStr("list_field_type");
			} else if (type.equals(Dict.SAVE_UPDATE)) {
				fields = record.getStr("save_update_field");
				txtStr = record.getStr("save_update_field_txt");
				typeStr = record.getStr("save_update_field_type");
				inputStr = record.getStr("input_content");
				lenStr = record.getStr("field_length");
				editStr = record.getStr("field_edit");
			}
		}
		List<Object[]> txtList = null;
		List<Object[]> typeList = null;
		List<Object[]> inputList = null;
		List<Object[]> lenList = null;
		List<Object[]> editList = null;
		if (AppUtils.StringUtil(fields) != null) {
			String[] field = fields.split(",");
			txtList = getObjectList(field, txtStr);
			typeList = getObjectList(field, typeStr);
			inputList = getObjectList(field, inputStr);
			lenList = getObjectList(field, lenStr);
			editList = getObjectList(field, editStr);
		}
		setAttr("table_name", table_name);
		setAttr("list", list);
		setAttr("type", type);
		setAttr("infos", infos);
		setAttr("txtList", txtList);
		setAttr("typeList", typeList);
		setAttr("pkField", pkField);
		setAttr("lenList", lenList);
		map.put("list", list);
		map.put("typeList", typeList);
		map.put("inputList", inputList);
		map.put("editList", editList);
		JSONObject jsonObject = JSONObject.fromObject(map);
		setAttr("data", jsonObject.toString());
		renderJsp("add_form.jsp");
	}

	/**
	 * 
	 * @param field
	 * @param str
	 * @return
	 */
	private List<Object[]> getObjectList(String[] field, String str) {
		List<Object[]> objList = new ArrayList<Object[]>();
		if (AppUtils.StringUtil(str) != null) {
			String[] txt = str.split(",");
			for (int i = 0; i < txt.length; i++) {
				Object[] obj = new Object[2];
				obj[0] = field[i];
				obj[1] = txt[i];
				objList.add(obj);
			}
		}
		return objList;
	}

	/**
	 * 进入配置生成代码页面
	 */
	public void addGenerateCode() {
		String path = this.getRequest().getSession().getServletContext().getRealPath("/");
		String template_path = path+Dict.TEMPLATE_PATH;
		String dest_path = PropertiesContent.get("dest_path");
		String table_name = getPara("table_name");
		List<Record> records = getCurdAutoRecords(table_name);
		setAttr("record", records.get(0));
		setAttr("template_path", template_path);
		setAttr("dest_path", dest_path);
		setAttr("table_name", table_name);
		renderJsp("add_generate.jsp");
	}

	/**
	 * 设置增、改字段其它属性
	 */
	public void addProperty() {
		String infos = getPara("infos");
		String[] info = infos.split(",");
		List<String> infolist = new ArrayList<String>();
		for (int i = 0; i < info.length; i++) {
			infolist.add(info[i]);
		}
		String table_name = getPara("table_name");
		List<Record> records = getCurdAutoRecords(table_name);
		List<Object[]> list = new ArrayList<Object[]>();
		String save_update_field = "";
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			save_update_field = record.getStr("save_update_field");
			String field_length = record.getStr("field_length");
			String field_edit = record.getStr("field_edit");
			String[] field_len = null;
			String[] edits = null;
			if (AppUtils.StringUtil(field_length) != null) {
				field_len = field_length.split(",");
			}
			if (AppUtils.StringUtil(field_edit) != null) {
				edits = field_edit.split(",");
			}
			String[] fields = save_update_field.split(",");
			for (int i = 0; i < fields.length; i++) {
				Object[] obj = new Object[3];
				obj[0] = fields[i];
				obj[1] = field_len == null ? "" : field_len[i];
				obj[2] = edits == null ? "" : edits[i];
				list.add(obj);
			}
		}
		setAttr("infos", save_update_field);
		setAttr("table_name", table_name);
		setAttr("list", list);
		setAttr("infolist", infolist);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("infolist", infolist);
		map.put("list", list);
		JSONObject jsonObject = JSONObject.fromObject(map);
		setAttr("data", jsonObject.toString());
		renderJsp("add_property.jsp");
	}

	/**
	 * 设置增、改字段其它属性
	 */
	public void saveProperty() {
		String table_name = getPara("table_name");
		String infos = getPara("infos");
		StringBuffer params = new StringBuffer();
		StringBuffer edits = new StringBuffer();
		String[] info = infos.split(",");
		for (int i = 0; i < info.length; i++) {
			String str = getPara("info_" + info[i]);
			String editStr = getPara("edit_" + info[i]);
			edits.append(editStr + ",");
			params.append(AppUtils.StringUtil(str) == null ? 0 + "," : str + ",");
		}
		String editContent = getStrParam(edits);
		String paramsContent = getStrParam(params);
		List<Record> records = getCurdAutoRecords(table_name);
		int flag = 0;
		if (records != null && !records.isEmpty()) {
			flag = Db.update("update sys_crud_auto set modify_time=?,field_length=?,field_edit=? where table_name=?",
					new Object[] { DateTimeUtil.getTime(), paramsContent, editContent, table_name });
		}
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 进入生成模块页面
	 */
	public void addModule() {
		renderJsp("add_module.jsp");
	}

	/**
	 * 获得所有二级菜单
	 */
	public void getMenuList() {
		List<Record> records = Db.find("select * from sys_menu_info where pid is not null");
		renderJson(records);
	}

	/**
	 * 生成模块-保存
	 */
	public void saveModule() {
		String menu_id = getPara("menu_id");
		String start_status = getPara("start_status");
		String table_name = getPara("table_name");
		String dialog_width = getPara("dialog_width");
		String dialog_height = getPara("dialog_height");
		int flag = Db.update(
				"update sys_crud_auto set menu_id=?,start_status=?,modify_time=?,dialog_width=?,dialog_height=? where table_name=?",
				new Object[] { menu_id, start_status, DateTimeUtil.getTime(), dialog_width, dialog_height,
						table_name });
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 关联表信息
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void addData() throws UnsupportedEncodingException {
		String table_name = getPara("table_name");
		String item_field = getPara("item_field");
		setAttr("table_name", table_name);
		setAttr("item_field", item_field);
		renderJsp("add_data.jsp");
	}

	/**
	 * 根据表名称查出该表的所有字段
	 */
	public void getFieldsByTableName() {
		String table_name = getPara("table_name");
		List<Record> records = Db.find("select column_name from user_tab_cols where table_name=?", table_name);
		renderJson(records);
	}

	/**
	 * 保存关联表信息
	 */
	public void saveGlTableInfo() {
		String table_name = getPara("table_name");
		String fieldN = getPara("field_name");
		String item_field = getPara("item_field");
		String tableName = getPara("gl_table_name");
		String column_name = getPara("gl_field");
		String columnName = getPara("selct_field");
		String columnNames = getPara("search_field");
		String search_field_name = getPara("search_field_name");
		String search_field_type = getPara("search_field_type");
		List<Record> records = Db.find("select * from sys_gl_crud_auto where field_name=? and table_name=?",
				new Object[] { fieldN, table_name });
		int flag = 0;
		if (records != null && !records.isEmpty()) {
			flag = Db.update(
					"update sys_gl_crud_auto set gl_table_name=?,gl_field=?,selct_field=?,search_field=?,item_field=?,search_field_name=?,search_field_type=? where field_name=? and table_name=?",
					new Object[] { tableName, column_name, columnName, columnNames, item_field, search_field_name,
							search_field_type, fieldN, table_name });
		} else {
			flag = Db.update(
					"insert into sys_gl_crud_auto(id,table_name,field_name,gl_table_name,"
							+ "gl_field,selct_field,search_field,item_field,search_field_name,search_field_type)values(?,?,?,?,?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), table_name, fieldN, tableName, column_name, columnName,
							columnNames, item_field, search_field_name, search_field_type });
		}
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 获得关联表信息详情
	 */
	public void getGlDetail() {
		String table_name = getPara("table_name");
		String item_field = getPara("item_field");
		List<Record> records = Db.find("select * from sys_gl_crud_auto where table_name=? and field_name=?",
				new Object[] { table_name, item_field });
		Record record = new Record();
		if (records != null && !records.isEmpty()) {
			record = records.get(0);
		}
		setAttr("record", record);
		renderJson();
	}

	/**
	 * 查询是否有主键
	 */
	public void getPkField() {
		String infos = getPara("infos");
		String table_name = getPara("table_name");
		List<Record> records = Db.find("SELECT A .column_name FROM user_cons_columns A,user_constraints b "
				+ "WHERE A .constraint_name = b.constraint_name " + "AND b.constraint_type = 'P' AND A .table_name = ?",
				table_name);
		Map<String, Object> map = new HashMap<String, Object>();
		int flag = 0;
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			if (("," + infos + ",").indexOf(("," + record.getStr("column_name")) + ",") != -1) {
				flag = 1;
			} else {
				map.put("info", "请在此表中选择主键【" + record.getStr("column_name") + "】");
			}
		} else {
			map.put("info", "此表没有主键，请在该表中设计主键！");
		}
		map.put("flag", flag);
		renderJson(map);
	}

	/**
	 * 设置查询语句
	 */
	public void addSearchSql() {
		String type = getPara("type");
		String table_name = getPara("table_name");
		List<Record> records = getCurdAutoRecords(table_name);
		List<Record> rs = Db.find("select * from sys_gl_crud_auto where table_name=?", table_name);
		List<Object[]> objs = new ArrayList<Object[]>();
		for (int i = 0; i < rs.size(); i++) {
			Record record = rs.get(i);
			Object[] obj = new Object[7];
			String search_field = record.getStr("search_field");
			String item_field = record.getStr("item_field");
			String gl_table_name = record.getStr("gl_table_name");
			String field_name = record.getStr("field_name");
			String gl_field = record.getStr("gl_field");
			String search_field_name = record.getStr("search_field_name");
			String search_field_type = record.getStr("search_field_type");
			if (AppUtils.StringUtil(item_field) != null) {
				obj[0] = Dict.SYS_INFO_TABLE;
			} else {
				obj[0] = gl_table_name;
			}
			if (AppUtils.StringUtil(item_field) != null) {
				obj[1] = Dict.SYS_INFO_TABLE_FIELD;
			} else {
				obj[1] = search_field;
			}
			obj[2] = field_name;
			if (AppUtils.StringUtil(item_field) != null) {
				obj[3] = Dict.SYS_INFO_TABLE_GL_FIELD;
			} else {
				obj[3] = gl_field;
			}
			obj[4] = search_field_name;
			if (AppUtils.StringUtil(item_field) != null) {
				obj[5] = 1;
			} else {
				obj[5] = search_field_type;
			}
			obj[6] = item_field;
			objs.add(obj);
		}
		StringBuffer sqlContent = new StringBuffer("select ");
		StringBuffer fromContent = new StringBuffer("from " + table_name);
		StringBuffer whereContent = new StringBuffer();
		// select语句
		String sql_content = "";
		// from语句
		String extra_sql_content = "";
		// where语句
		String where_sql_content = "";
		// order by语句
		String order_by_sql_content = "";
		// 列表字段名称
		String list_field_names = "";
		// 列表字段
		String fields = "";
		// 列表字段类型
		String field_types = "";
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			String list_field = record.getStr("list_field");
			list_field_names = record.getStr("list_field_names");
			sql_content = record.getStr("sql_content");
			extra_sql_content = record.getStr("extra_sql_content");
			where_sql_content = record.getStr("where_sql_content");
			order_by_sql_content = record.getStr("order_by_sql_content");
			field_types = record.getStr("field_types");
			fields = record.getStr("fields");
			if (type.equals("0")) {
				String[] listFields = list_field.split(",");
				for (int i = 0; i < listFields.length; i++) {
					sqlContent.append(table_name + "." + listFields[i] + ",");
				}
				if (objs != null && !objs.isEmpty()) {
					for (int i = 0; i < objs.size(); i++) {
						Object[] obj = objs.get(i);
						sqlContent.append(obj[0] + "." + obj[1]);
					}
				}
				sql_content = getStrParam(sqlContent);
			}
			if (type.equals("0")) {
				fields = list_field;
				if (objs != null && !objs.isEmpty()) {
					for (int i = 0; i < objs.size(); i++) {
						Object[] obj = objs.get(i);
						fields += "," + obj[1];
					}
				}
			}
			if (type.equals("0")) {
				field_types = record.getStr("list_field_type");
				if (objs != null && !objs.isEmpty()) {
					for (int i = 0; i < objs.size(); i++) {
						Object[] obj = objs.get(i);
						field_types += "," + obj[5];
					}
				}
			}
			if (type.equals("0")) {
				String list_field_txt = record.getStr("list_field_txt");
				list_field_names = list_field_txt;
				if (objs != null && !objs.isEmpty()) {
					for (int i = 0; i < objs.size(); i++) {
						Object[] obj = objs.get(i);
						list_field_names += "," + obj[4];
					}
				}
			}
			if (type.equals("0")) {
				if (objs != null && !objs.isEmpty()) {
					for (int i = 0; i < objs.size(); i++) {
						Object[] obj = objs.get(i);
						if (obj[0].equals(Dict.SYS_INFO_TABLE)) {
							fromContent.append(" left join " + obj[0] + " on " + table_name + "." + obj[2] + " = "
									+ obj[0] + "." + obj[3] + " and " + obj[0] + "."
									+ Dict.SYS_INFO_TABLE_KEY + "='" + obj[6] + "'");
						} else {
							fromContent.append(" left join " + obj[0] + " on " + table_name + "." + obj[2] + " = "
									+ obj[0] + "." + obj[3]);
						}

					}
				}
				extra_sql_content = fromContent.toString();
			}
			if (type.equals("0")) {
				whereContent.append("where 1=1");
				where_sql_content = whereContent.toString();
			}
		}
		List<Record> fieldTypeRecords = Db.find("select * from sys_param_info where key='FIELD_TYPE' order by val asc");
		StringBuffer strBuffer = new StringBuffer();
		if (fieldTypeRecords != null && !fieldTypeRecords.isEmpty()) {
			for (int i = 0; i < fieldTypeRecords.size(); i++) {
				Record record = fieldTypeRecords.get(i);
				String val = record.getStr("val");
				String remark = record.getStr("remark");
				strBuffer.append(val + "【" + remark + "】" + "  ");
			}
		}
		setAttr("sql_content", sql_content);
		setAttr("extra_sql_content", extra_sql_content);
		setAttr("where_sql_content", where_sql_content);
		setAttr("order_by_sql_content", order_by_sql_content);
		setAttr("fields", fields);
		setAttr("list_field_names", list_field_names);
		setAttr("field_types", field_types);
		setAttr("table_name", table_name);
		setAttr("strBuffer", strBuffer.toString());
		renderJsp("add_search_sql.jsp");
	}

	/**
	 * 保存自定义sql语句
	 */
	public void saveSqlContent() {
		String fieldTypes = getPara("fieldTypes");
		String listFieldNames = getPara("listFieldNames");
		String table_name = getPara("table_name");
		String sqlContent = getPara("sqlContent");
		String extrasqlContent = getPara("extrasqlContent");
		String whereSqlContent = getPara("whereSqlContent");
		String fields = getPara("fields");
		String order_by_sql_content = getPara("order_by_sql_content");
		int flag = Db.update(
				"update sys_crud_auto set sql_content=?,extra_sql_content=?,order_by_sql_content=?,where_sql_content=?,list_field_names=?,fields=?,field_types=? where table_name=?",
				new Object[] { sqlContent, extrasqlContent, order_by_sql_content, whereSqlContent, listFieldNames,
						fields, fieldTypes, table_name });
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 设置关联查询字段类型
	 */
	public void setFieldType() {
		String table_name = getPara("table_name");
		String infos = getPara("infos");
		List<String> list = new ArrayList<String>();
		if (infos.contains(",")) {
			String[] info = infos.split(",");
			for (int i = 0; i < info.length; i++) {
				list.add(info[i]);
			}
		} else {
			list.add(infos);
		}
		setAttr("table_name", table_name);
		setAttr("list", list);
		setAttr("infos", infos);
		setAttr("listArr", JSONArray.fromObject(list).toString());
		renderJsp("add_glFieldType.jsp");
	}

	/**
	 * 根据表名获取详细信息
	 */
	public void getInfoByTableName() {
		String table_name = getPara("table_name");
		List<Record> records = getCurdAutoRecords(table_name);
		Record record = null;
		if (records != null && !records.isEmpty()) {
			record = records.get(0);
		}
		setAttr("datas", record);
		renderJson();
	}

	/**
	 * 根据menu_id获取该模块是否用过
	 */
	public void getInfoByMenuId() {
		String menu_id = getPara("menu_id");
		String table_name = getPara("table_name");
		List<Record> records = Db.find("select * from sys_crud_auto where menu_id=? and table_name!=?",
				new Object[] { menu_id, table_name });
		int flag = 0;
		if (records != null && !records.isEmpty()) {
			flag = 1;
		}
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 删除
	 */
	public void del() {
		String ids = getPara("ids");
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				deleteCrudAndGlRecords(uuid);
			}
		} else {
			deleteCrudAndGlRecords(ids);
		}
		renderNull();
	}

	/**
	 * 删除具体操作
	 * 
	 * @param uuid
	 */
	private void deleteCrudAndGlRecords(String uuid) {
		List<Record> records = Db.find("select * from sys_crud_auto where id=?", uuid);
		if (records != null && !records.isEmpty()) {
			Record record = records.get(0);
			String table_name = record.getStr("table_name");
			Db.update("delete from sys_crud_auto where id = ? ", uuid);
			Db.update("delete from sys_gl_crud_auto where table_name=?", table_name);
			Db.update("delete from " + table_name + " where 1=1");
		}

	}
	/**
	 * 判断生成的文件是否存在
	 */
	public void isGenerateCode(){
		Map<String,Object> map=new HashMap<String,Object>();
		int status=0;
		String ids=getPara("ids");
		String[]id=ids.split(",");
		for(int i=0;i<id.length;i++){
			String destPath=PropertiesContent.get("dest_path");
			File file=new File(destPath+"/"+id[i]);
			if(!file.exists()){
				status=1;
				map.put("info","表名【"+id[i]+"】生成的代码文件不存在,请重新生成代码再下载！");
				break;
			}
		}
		map.put("status",status);
		renderJson(map);
	}

	/**
	 * 下载代码
	 * 
	 * @throws IOException
	 */
	public void downloadCode() throws IOException {
		String ids = getPara("ids");
		String dest_path = PropertiesContent.get("dest_path");
		HttpServletResponse response = this.getResponse();
		// zip格式下载
		// 设置响应头,MIMEtype告诉浏览器传送的文件类型
		response.setContentType("application/x-zip-compressed");
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			// 此变量用于保存导出文件名集合
			List<String> fileNameList = new ArrayList<String>();
			// 循环开始----!------!------!----!-----!----!--!----!--!------!----------!--------
			for (int i = 0; i < array.length; i++) {
				fileNameList.add(array[i]);
			}
			// inline;参数让浏览器弹出下载窗口,而不是在网页中打开文件.filename设定文件名
			response.setHeader("Content-Disposition", "inline; filename=download.zip");
			// 通过response获得ServletOutputStream对象
			ServletOutputStream sos = response.getOutputStream();
			// 获得ZipOutputStream对象
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(sos));
			for (int i = 0; i < fileNameList.size(); i++) {
				ArrayList<String> listFileName = new ArrayList<String>();
				String path = dest_path + fileNameList.get(i);
				getAllFileName(path, listFileName);
				for (int j = 0; j < listFileName.size(); j++) {
					// 文件名称
					String file_name = listFileName.get(j);
					// 得到要下载的文件对象
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(path + "/" + file_name));
					// 在zip文件中新建一个文件
					out.putNextEntry(new ZipEntry(fileNameList.get(i) + "/" + file_name));
					// 逐字读出写入
					int c;
					while ((c = in.read()) != -1) {
						out.write(c);
					}
					in.close();
				}

			}
			out.close();

		} else {
			// inline;参数让浏览器弹出下载窗口,而不是在网页中打开文件.filename设定文件名
			response.setHeader("Content-Disposition", "inline; filename=" + ids + ".zip");
			// 通过response获得ServletOutputStream对象
			ServletOutputStream sos = response.getOutputStream();
			// 获得ZipOutputStream对象
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(sos));
			ArrayList<String> listFileName = new ArrayList<String>();
			String path = dest_path + ids;
			getAllFileName(path, listFileName);
			for (int j = 0; j < listFileName.size(); j++) {
				// 文件名称
				String file_name = listFileName.get(j);
				// 得到要下载的文件对象
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(path + "/" + file_name));
				// 在zip文件中新建一个文件
				out.putNextEntry(new ZipEntry(file_name));
				// 逐字读出写入
				int c;
				while ((c = in.read()) != -1) {
					out.write(c);
				}
				in.close();
			}

			out.close();
		}
		renderNull();
	}

	private void getAllFileName(String path, ArrayList<String> fileName) {
		File file = new File(path);
		File[] files = file.listFiles();
		String[] names = file.list();
		if (names != null)
			fileName.addAll(Arrays.asList(names));
		for (File a : files) {
			if (a.isDirectory()) {
				getAllFileName(a.getAbsolutePath(), fileName);
			}
		}
	}

}

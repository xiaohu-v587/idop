package com.goodcol.controller.dop;


import java.util.ArrayList;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * 图谱要素配置
 * @author 陈佳争
 * @date 2019-05-24
 */
@RouteBind(path = "/imgSetCfg")
@Before({ManagerPowerInterceptor.class})
public class ImgSetCfgCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ImgSetCfgCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		renderJsp("imgSetCfg.jsp");
	}
	
	/**
	 * 获得首页列表数据
	 */
	 public void getList(){
		// 获取当前用户信息
			//String userNo = getCurrentUser().getStr("USER_NO");
			//String orgnum = getCurrentUser().getStr("ORG_ID");
			// 获取查询条件
			String dimension = getPara("indicator_dimension");
			String module = getPara("module");
			String code = getPara("indicator_code");
			String name = getPara("indicator_name");
			
			int pageNum = getParaToInt("pageIndex") + 1;
			int pageSize = getParaToInt("pageSize", 10);
			
			// 查询语句
			String selectSql = " select  t.id, t.indicator_name, " +
					"(select remark from sys_param_info where key = 'bqwd' and val = t.indicator_dimension) indicator_dimension, " +
					"(select remark from sys_param_info where key = 'dop_ywtype' and val = t.module) module, " +
					"indicator_code ";
			String extraSql = " FROM dop_graph t ";

			StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1");
			List<String> sqlStr = new ArrayList<String>();
			// 查询条件
			if (AppUtils.StringUtil(dimension) != null) {
				whereSql.append(" AND  t.indicator_dimension = ? ");
				sqlStr.add(dimension);
			}
			if (AppUtils.StringUtil(name) != null) {
				whereSql.append("  and t.indicator_name like ? ");
				sqlStr.add("%" + name + "%");
			}
			if (AppUtils.StringUtil(module) != null && !"1001".equals(module)) {
				whereSql.append("  and t.module = ? ");
				sqlStr.add(module);
			}
			if (AppUtils.StringUtil(code) != null) {
				whereSql.append("  and t.indicator_code = ?");
				sqlStr.add(code);
			}
			
			// 排序
			whereSql.append(" ORDER BY t.indicator_code ");
			extraSql += whereSql.toString();
			// 查询
			Page<Record> r = Db.paginate(pageNum, pageSize, selectSql,extraSql, sqlStr.toArray());
			// 赋值
			setAttr("data", r.getList());
			setAttr("total", r.getTotalRow());
			// 打印日志
			log.info("getList--r.getList():" + r.getList());
			log.info("getList--r.getTotalRow():" + r.getTotalRow());
			// 记录日志
			LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "图谱配置", "3", "图谱配置-查询");
			log.info("图谱配置-查询");
			// 返回json数据
			renderJson();
	 }
	
	 /**
		 * 跳转至监测参数新增页面
		 */
//		@Before(PermissionInterceptor.class)
		public void toAdd() {
			render("imgSetForm.jsp");
		}
	
		//新增标签
		public void save(){
			//获取前台数据
			//主表主键
			String id = getPara("id");
			String indicatorName = getPara("indicator_name");
			String indicatorCode = getPara("indicator_code");
			String module = getPara("module");
			String dimension = getPara("dimension");
			int flag = 0;
			if (AppUtils.StringUtil(id)!=null) {
				String updateTime = DateTimeUtil.getNowDate();
				String updateUser = getCurrentUser().getStr("USER_NO");
				String sql = "update dop_graph set indicator_code = ? , indicator_name = ?, module = ?, indicator_dimension = ?, update_user = ?, update_time = ? where id = ?";
				flag = Db.update(sql,indicatorCode,indicatorName,module,dimension,updateUser,updateTime,id);
			} else {
				id = AppUtils.getStringSeq();
				String createTime = DateTimeUtil.getNowDate();
				String createUser = getCurrentUser().getStr("USER_NO");
				String sql = "insert into dop_graph(id, indicator_code, indicator_name, indicator_dimension, module, create_user, create_time) values(?,?,?,?,?,?,?)";
				flag = Db.update(sql,id,indicatorCode,indicatorName,dimension,module,createUser,createTime);
			}
			setAttr("flag", flag);
			// 记录日志
			log.info("新增标签");
			renderJson();
		}
		
		//删除
		public void delete(){
			String id = getPara("key");
			Db.update(" DELETE FROM dop_graph T WHERE T.ID=?",id);
			renderJson();
		}
		
		/**
		 * 跳转至标签编辑页面
		 */
//		@Before(PermissionInterceptor.class)
		public void toEdit() {
			render("imgSetForm.jsp");
		}
		
		//根据主键，获取主键对应图谱配置详细数据
		public void getDetailById(){
			String id = getPara("key");
			/* 获取id对应监测信息*/
			String sql = "select id, indicator_code, indicator_name, module, indicator_dimension dimension from dop_graph where id=?";
			//获取主表数据
			Record re = Db.findFirst(sql, id);	
			setAttr("datas", re);
			// 记录日志
			log.info("图谱配置-获取详情");
			renderJson();
		}
}

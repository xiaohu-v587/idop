package com.goodcol.controller.dop;


import java.util.ArrayList;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * 算法基本信息
 * @author 刘东源
 * @date 2018-11-09
 */
@RouteBind(path = "/algorith")
@Before({ManagerPowerInterceptor.class})
public class AlgorithmCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ParamCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
/*		String userno = getCurrentUser().getStr("USER_NO");
		String username = getCurrentUser().getStr("NAME");
		String  orgid = getCurrentUser().getStr("ORG_ID");
		setAttr("userno", userno);
		setAttr("username", username);
		setAttr("org", orgid);*/
		render("AlgJbsfList.jsp");
	}
	
	
	/**
	 * 查询所有算法
	 */
	public void getList() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
		String ywtype = getPara("ywtype");
		String apply = getPara("apply");
		String indexnum = getPara("indexnum");
		String indexname = getPara("indexname");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		// 查询语句
		String selectSql = " select  t.id,t.ywtype, t.indexnum,t.indexname, t.assigntype, (select remark from sys_param_info where key = 'dop_caltype'||t.assigntype and val = t.caltype) caltype, t.apply, t.marks, t.weight, t.describtion, t.byzd1, t.byzd2 ";
		String extraSql = " FROM DOP_ALGORITHM_INFO t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(ywtype) != null) {
			whereSql.append(" AND  t.ywtype = ? ");
			sqlStr.add(ywtype);
		}
		if (AppUtils.StringUtil(apply) != null) {
			whereSql.append("  and t.apply = ? ");
			sqlStr.add(apply);
		}
		if (AppUtils.StringUtil(indexname) != null) {
			whereSql.append("  and t.indexname like ? ");
			sqlStr.add("%"+indexname+"%");
		}
		if (AppUtils.StringUtil(indexnum) != null) {
			whereSql.append("  and t.indexnum like ? ");
			sqlStr.add("%"+indexnum+"%");
		}
		// 排序
		whereSql.append(" ORDER BY indexname,apply ");
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "基本算法管理", "3", "基本算法管理-查询");
		log.info("基本算法管理-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 跳转至算法编辑页面
	 */
	@Before(PermissionInterceptor.class)
	public void toEdit() {
		render("AlgorithmForm.jsp");
	}
	
	/**
	 * 跳转至算法新增页面
	 */
	@Before(PermissionInterceptor.class)
	public void toAdd() {
		render("AlgorithmForm.jsp");
	}
	
	//新增保存算法
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		//计算方式
		String caltype = getPara("caltype");
		//适用层级
		String apply = getPara("apply");
		//描述
		String describtion = getPara("describtion");
		//权重
		String weight = getPara("weight");
		//满分
		String marks = getPara("marks");
		//业务模块
		String ywtype = getPara("ywtype");
		//赋值方式
		String assigntype = getPara("assigntype");
		//指标预警编号
		String indexnum = getPara("indexnum");
		//指标预警名称
		String indexname = getPara("indexname");
		//预警扣分条件个数
		int num = getParaToInt("num");
		//区间赋值个数
		int code = getParaToInt("node");
		//保存算法主表
		int flag = Db.update("	INSERT INTO dop_algorithm_info (id,ywtype, indexnum,indexname, assigntype, caltype, apply, marks, weight, describtion)  VALUES(?,?,?,?,?,?,?,?,?,?)",
				new Object[] {id,ywtype, indexnum,indexname, assigntype, caltype, apply, marks, weight, describtion});
		if(flag > 0){
			//新增子表
			if("01".equals(assigntype)){
				//区间赋值
				if(code > 0){
					for(int i = 1;i<=code ; i++){
						//要素一
						String element1 = getPara("ele1_"+i);
						//要素二
						String element2 = getPara("ele2_"+i);
						//符号一
						String operator1 = getPara("ope1_"+i);
						//符号二
						String operator2 = getPara("ope2_"+i);
						//得分
						String inmarks = getPara("inmarks_"+i);
						if(operator1 != null && !"".equals(operator1) ){
							//保存算法子表
							Db.update("	INSERT INTO dop_algorithmson_info (id, pid, element1, operator1, element2, operator2, marks)  VALUES(?,?,?,?,?,?,?)",
									new Object[] {AppUtils.getStringSeq(),id,element1, operator1, element2, operator2, inmarks});
						}
					}
				}
			}else if("02".equals(assigntype)){
				//预警扣分
				//预警扣分
				if(num > 0){
					for(int i = 1;i<=num ; i++){
						if("01".equals(caltype)){
							//要素一
							String levels = getPara("levels_"+i);
							//得分
							String wamarks = getPara("wamarks_"+i);
							if(levels != null && !"".equals(levels) ){
								//保存算法子表
								Db.update("	INSERT INTO dop_algorithmson_info (id, pid,levels, marks)  VALUES(?,?,?,?)",
										new Object[] {AppUtils.getStringSeq(),id,levels, wamarks});
							}
						}else{
							//得分
							String wamarks = getPara("wamarks_"+i);
							if(wamarks != null && !"".equals(wamarks) ){
								//保存算法子表
								Db.update("	INSERT INTO dop_algorithmson_info (id, pid, marks)  VALUES(?,?,?)",
										new Object[] {AppUtils.getStringSeq(),id, wamarks});
							}
							
						}
					}
				}
			}
		}
		// 记录日志
		log.info("基本算法管理-新增");
		renderJson();
	}

	
	//编辑保存算法
	@Before(Tx.class)
	public void update(){
		//获取前台数据
		//主表主键
		String id = getPara("id");
		//计算方式
		String caltype = getPara("caltype");
		//适用层级
		String apply = getPara("apply");
		//描述
		String describtion = getPara("describtion");
		//权重
		String weight = getPara("weight");
		//满分
		String marks = getPara("marks");
		//业务模块
		String ywtype = getPara("ywtype");
		//赋值方式
		String assigntype = getPara("assigntype");
		//指标预警编号
		String indexnum = getPara("indexnum");
		//指标预警名称
		String indexname = getPara("indexname");
		//预警扣分条件个数
		int num = getParaToInt("num");
		//区间赋值个数
		int code = getParaToInt("node");
		//修改算法表数据
		Db.update("UPDATE dop_algorithm_info SET ywtype=?, indexnum=?,indexname=?, assigntype=?, caltype=?, apply=?, marks=?, weight=?, describtion=?  WHERE ID=? ",
					new Object[] {ywtype, indexnum,indexname, assigntype, caltype, apply, marks, weight, describtion, id});
		
			//先删除子表数据
			Db.update("delete from dop_algorithmson_info   WHERE pid=? ",
					new Object[] {id});
			//新增子表
			if("01".equals(assigntype)){
				//区间赋值
				if(code > 0){
					for(int i = 1;i<=code ; i++){
						//要素一
						String element1 = getPara("ele1_"+i);
						//要素二
						String element2 = getPara("ele2_"+i);
						//符号一
						String operator1 = getPara("ope1_"+i);
						//符号二
						String operator2 = getPara("ope2_"+i);
						//得分
						String inmarks = getPara("inmarks_"+i);
						if(operator1 != null && !"".equals(operator1) ){
							//保存算法子表
							Db.update("	INSERT INTO dop_algorithmson_info (id, pid, element1, operator1, element2, operator2, marks)  VALUES(?,?,?,?,?,?,?)",
									new Object[] {AppUtils.getStringSeq(),id,element1, operator1, element2, operator2, inmarks});
						}
					}
				}
			}else if("02".equals(assigntype)){
				//预警扣分
				if(num > 0){
					for(int i = 1;i<=num ; i++){
						if("01".equals(caltype)){
							//要素一
							String levels = getPara("levels_"+i);
							//得分
							String wamarks = getPara("wamarks_"+i);
							if(levels != null && !"".equals(levels) ){
								//保存算法子表
								Db.update("	INSERT INTO dop_algorithmson_info (id, pid,levels, marks)  VALUES(?,?,?,?)",
										new Object[] {AppUtils.getStringSeq(),id,levels, wamarks});
							}
						}else{
							//得分
							String wamarks = getPara("wamarks_"+i);
							if(wamarks != null && !"".equals(wamarks) ){
								//保存算法子表
								Db.update("	INSERT INTO dop_algorithmson_info (id, pid, marks)  VALUES(?,?,?)",
										new Object[] {AppUtils.getStringSeq(),id, wamarks});
							}
							
						}
					}
				}
			}
		// 记录日志
		log.info("基本算法管理-修改");
		renderJson();
	}
	
	//根据主键，获取主键对应算法详细数据
	public void getAlgorithmById(){
		String id = getPara("key");
		
		/* 获取id对应算法*/
		String sql = "SELECT t.id,t.ywtype, t.indexnum,t.indexname, t.assigntype, t.caltype, t.apply, t.marks, t.weight, t.describtion, t.byzd1, t.byzd2"
				+ "  from dop_algorithm_info t  where t.id = '"+id+"' ";
		//获取主表数据
		Record re = Db.findFirst(sql);	
		
		//获取子表数据
		String sql1 = "select id, pid, element1, operator1, element2, operator2, marks,levels  from dop_algorithmson_info  where pid = '"+id+"' ";
		List<Record> list = Db.find(sql1);	
		
		setAttr("record", re);
		setAttr("list", list);
		setAttr("size", list.size());
		// 记录日志
		log.info("基本算法管理-获取详情");
		renderJson();
	}
	
	//删除算法
	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		//根据id删除算法表数据
		int flag = Db.update(" DELETE FROM dop_algorithm_info T WHERE T.ID='"+id+"' ");
		//删除参数表数据
		if(flag > 0){
			Db.update(" DELETE FROM dop_algorithmson_info T WHERE T.PID='"+id+"' ");
			setAttr("record", 1);
		}else{
			setAttr("record", 0);
		}
		renderJson();
	}
	
	//获取所有考核项
	public void findAllAlgorithm(){
		String sql = " select id,sfmc from T_JXKH_ALGORITHM where 1=1  ";
		List<Record> list = Db.find(sql);
		// 记录日志
		log.info("基本算法管理-查询考核项列表");
		renderJson(list);
	}
	
	/**
	 * 字典映射
	 **/
	public void getDict() {
		String key = getPara("key");
		List<Record> list = Db.find("select val,remark from SYS_PARAM_INFO where key = ?", key);
		setAttr("datas", list);
		renderJson();
	}

}

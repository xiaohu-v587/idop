package com.goodcol.controller.dop;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * 指定关注配置
 * @author 陈佳争
 * @date 2019-05-07
 */
@RouteBind(path = "/assignedFollow")
//@Before({ManagerPowerInterceptor.class})
public class AssignedFollowCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(AssignedFollowCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		render("AssignedFollowConfig.jsp");
	}
	
	
	/**
	 * 查询所有指定关注项
	 */
	public void getList() {
		// 获取当前用户信息
//		String userNo = getCurrentUser().getStr("USER_NO");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 查询语句
		String selectSql = " select id, case assigned_type when '1' then '管理关注' when '2' then '条线关注' when '3' then '网点关注' end" +
				"||(select remark from sys_param_info where key='dop_ywtype'  and val = t.busi_module)" +
				"|| (select warning_name from dop_warning_param where warning_code=t.mark_code )" +
				"|| (select remark from sys_param_info where key='dop_follow_type'  and val =t.follow_type) as describe ";
		String extraSql = " from dop_my_follow  t  ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and assigned_type is not null");
		List<String> sqlStr = new ArrayList<String>();
		// 排序
		whereSql.append(" ORDER BY t.change_time ");
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "指定关注配置", "3", "指定关注配置-查询");
		log.info("指定关注配置-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 保存指定关注项
	 */
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		//指定类型
		String assigned_type = getPara("assigned_type");
		//业务模块
		String busi_module = getPara("busi_module");
		//关注类别
		String follow_type = getPara("follow_type");
		//业务子类
		String val = getPara("sub_busi_code");
		String sub_busi_code=null;
		Record  list =Db.findFirst(" select val from dop_gangedmenu_info where id =? ",val);
		if(list !=null ){
			sub_busi_code=list.getStr("val");
		}else{
			sub_busi_code="";
		}
		String mark_code=getPara("mark_code");
		//关注者机构号
		String org = getCurrentUser().getStr("ORG_ID");
//		//关注人
//		String follower = getCurrentUser().getStr("USER_NO");
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		//当前时间
		String change_time = simpleFormat.format(new Date());
		//保存信息
		int flag = Db.update("	INSERT INTO dop_my_follow (id,busi_module, follow_type, sub_busi_code,org,change_time,mark_code, assigned_type)  " +
				" VALUES(?,?,?,?,?,?,?,?)",
				new Object[] {id, busi_module, follow_type, sub_busi_code,org,change_time,mark_code, assigned_type});
		// 记录日志
		if(flag > 0){
			setAttr("flag", flag);
		}else{
			setAttr("flag", 0);
		}
		log.info("指定关注配置-新增");
		renderJson();
	}

	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		//根据id删除关注表数据
		if(AppUtils.StringUtil(id) != null){
			String[] ids = id.split(",");
			for(String everId : ids){
				Db.update(" DELETE FROM dop_my_follow T WHERE T.ID='"+everId+"' ");
				setAttr("record", 1);
			}
		}else{
			setAttr("record", 0);
		}
		renderJson();
	}
}

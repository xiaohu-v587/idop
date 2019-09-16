/**
 * 
 */
package com.goodcol.controller.zxglctl;

import java.util.ArrayList;
import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 * 
 */
@RouteBind(path = "/pccm_acc_search")
@Before({ ManagerPowerInterceptor.class })
public class PccmAccSearchCtl extends BaseCtl {
	/**
	 * 进入首页
	 */
	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 获取对账单数据
	 */
	public void getList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		String sql = "select p.*,s.name as curr,s1.name as direct,s2.name as tra";
		String extrasql = " from pccm_acc_search p left join (select name,val from gcms_param_info where key='CURRENCY') s on p.currency=s.val left join (select name,val from gcms_param_info where key='DIRECTION') s1 on p.direction=s1.val left join (select name,val from gcms_param_info where key='TRADE_TYPE') s2 on p.trade_type=s2.val where 1=1";
		StringBuffer sb = new StringBuffer();
		String start_time = getPara("start_time");
		String end_time=getPara("end_time");
		String currency=getPara("currency");
		String direction=getPara("direction");
		String trade_type=getPara("trade_type");
		String customercode=getPara("customercode");
		String acc_no=getPara("acc_no");
		String apponent_acc=getPara("apponent_acc");
		String acc_code=getPara("acc_code");
		String trade_org=getPara("trade_org");
		if (AppUtils.StringUtil(start_time) != null) {
			if(AppUtils.StringUtil(end_time)!=null){
				sb.append(" and (p.create_time>=? and p.create_time<=?)");
				listStr.add(start_time);
				listStr.add(end_time);
			}else{
				sb.append(" and p.create_time>=?");
				listStr.add(start_time);
			}
		}else{
			if(AppUtils.StringUtil(end_time)!=null){
				sb.append(" and p.create_time<=?");
				listStr.add(start_time);
			}
		}
		if(AppUtils.StringUtil(currency)!=null){
			sb.append(" and p.currency=?");
			listStr.add(currency);
		}
		if(AppUtils.StringUtil(direction)!=null){
			sb.append(" and p.direction=?");
			listStr.add(direction);
		}
		if(AppUtils.StringUtil(trade_type)!=null){
			sb.append(" and p.trade_type=?");
			listStr.add(trade_type);
		}
		if(AppUtils.StringUtil(customercode)!=null){
			sb.append(" and p.customercode like ?");
			listStr.add("%"+customercode+"%");
		}
		if(AppUtils.StringUtil(acc_no)!=null){
			sb.append(" and p.acc_no like ?");
			listStr.add("%"+acc_no+"%");
		}
		if(AppUtils.StringUtil(apponent_acc)!=null){
			sb.append(" and p.apponent_acc like ?");
			listStr.add("%"+apponent_acc+"%");
		}
		if(AppUtils.StringUtil(acc_code)!=null){
			sb.append(" and p.acc_code like ?");
			listStr.add("%"+acc_code+"%");
		}
		if(AppUtils.StringUtil(trade_org)!=null){
			sb.append(" and p.trade_org like ?");
			listStr.add("%"+trade_org+"%");
		}
		Page<Record> r = Db.use("default").paginate(pageNum, pageSize, sql,
				extrasql + sb.toString() + " order by p.create_time desc",
				listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	public void del(){
		String ids = getPara("ids");
		if (ids.contains(",")) {
			String[] array = ids.split(",");
			for (int i = 0; i < array.length; i++) {
				String uuid = array[i];
				Db.use("default").update(
						"delete from pccm_acc_search where id=?",
						new Object[] { uuid });
			}
		} else {
			Db.use("default").update("delete from pccm_acc_search where id=?",
					new Object[] { ids });
		}
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "对账单查询", "6",
				"对账单查询-删除");
		renderNull();
	}
}

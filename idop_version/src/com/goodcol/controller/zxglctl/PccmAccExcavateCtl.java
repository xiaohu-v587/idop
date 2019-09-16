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
@RouteBind(path = "/pccm_acc_excavate")
@Before({ ManagerPowerInterceptor.class })
public class PccmAccExcavateCtl extends BaseCtl{

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
		String sql = "select *";
		String extrasql = " from pccm_acc_excavate where 1=1";
		StringBuffer sb = new StringBuffer();
		String customercode = getPara("customercode");
		if (AppUtils.StringUtil(customercode) != null) {
			sb.append(" and customercode like ?");
			listStr.add("%" + customercode + "%");
		}
		String label_flag = getPara("label_flag");
		if (AppUtils.StringUtil(label_flag) != null) {
			sb.append(" and label_flag like ?");
			listStr.add("%" + label_flag + "%");
		}
		Page<Record> r = Db.use("default").paginate(pageNum, pageSize, sql,
				extrasql + sb.toString() + " order by create_time desc",
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
						"delete from pccm_acc_excavate where id=?",
						new Object[] { uuid });
			}
		} else {
			Db.use("default").update("delete from pccm_acc_excavate where id=?",
					new Object[] { ids });
		}
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "对账单挖掘", "6",
				"对账单挖掘-删除");
		renderNull();
	}
}

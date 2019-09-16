package com.goodcol.controller.zxglctl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.kit.PathKit;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmKPIParaServer;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 质效管理系统首页相关查询
 * 2018年5月31日14:30:24
 * @author liutao
 */
@RouteBind(path = "/zxindex")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class IndexController extends BaseCtl{
	private PccmKPIParaServer server = new PccmKPIParaServer();
	@Override
	public void index() {
		
	}

	/**
	 * 首页雷达图数据查询
	 * 2018年5月31日14:33:13
	 * @author liutao
	 */
	public void indexRadarMap(){
		Record result = new Record();
		Record m = getCurrentUser();
		//String userId = m.getStr("ID");
		final String userNo = m.getStr("USER_NO");
		
		for(int i = 1; i <= 4; i++){
			final Record flag1= new Record();
			flag1.set("flag", false);
			final Record flag2= new Record();
			flag2.set("flag", false);
			final Record flag3= new Record();
			flag3.set("flag", false);
			final Record flag4= new Record();
			flag4.set("flag", false);
			final Record flag5= new Record();
			flag5.set("flag", false);
			final Record r = new Record();
			final String timePoint = i + "";
			long startTime = System.currentTimeMillis();
			new Thread(new Runnable() {
				@Override
				public void run() {
					//1、存款贷款
					Record fund = server.findFundByCustId(userNo, timePoint);
					r.set("incomday", fund.get("incomday"));//存款
					r.set("loansday", fund.get("loansday"));//贷款
					flag1.set("flag", true);
				}
			}).start();
			/*//2、价值客户数和营销达成率(成功营销客户数/总营销客户数)
			Record costNum = server.findCustomerNumByCustId(userId, timePoint, "1");
			r.set("costnum", costNum.get("customerNum"));//价值客户数
			Record succNum = server.findCustomerNumByCustId(userId, timePoint, "2");
			Record sumNum = server.findCustomerNumByCustId(userId, timePoint, "3");
			BigDecimal succ = succNum.get("customerNum");//成功营销客户数
			BigDecimal sum = sumNum.get("customerNum");//总营销客户数
			if(sum.intValue() == 0){
				r.set("reach", "0");
			}else{
				double reach = succ.intValue()/sum.intValue();//营销达成率
				if(reach == 1){
					r.set("reach", 1);
				}else{
					r.set("reach", String.format("%.2f", reach));
				}
				
			}
			*雷达图修改为个人七层分类，机构六层分类
			*/
			/*----------------------雷达图修改 2018年7月4日16:08:07 liutao--------------------*/
			new Thread(new Runnable() {
				@Override
				public void run() {
					//2、加权（分别查询一层到五层客户数，然后根据EXCEL大表中加权计算规则计算加权数）
					Record weighting = server.findPersonWeighting(userNo, timePoint);
					r.set("weighting", weighting.get("newNum"));
					flag2.set("flag", true);
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					//3、KPI
					Record kpi = server.findKPIByCustNo(userNo, timePoint);
					r.set("kpi", kpi.get("sumKpi"));
					flag3.set("flag", true);
				}
			}).start();
			//4、手工报表处理效率
			float efficiency = 0f;
			r.set("efficiency", efficiency);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					//5、潜在客户达成率
					Record latentReach = server.findPersonLatentReach(userNo, timePoint);
					r.set("latentreach", latentReach.get("reach"));
					flag4.set("flag", true);
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					//6、商机客户转化率
					Record markReach = server.findPersonMarkReach(userNo, timePoint);
					r.set("markreach", markReach.get("reach"));
					flag5.set("flag", true);
				}
			}).start();
			while(!(flag1.getBoolean("flag") && flag2.getBoolean("flag") && flag3.getBoolean("flag")
					&& flag4.getBoolean("flag") && flag5.getBoolean("flag"))){}
			//把查询出来的结果放到结果集里面
			result.set("r" + i, r);
			long endTime = System.currentTimeMillis();
			System.out.println("第" + i + "次执行: " + (endTime - startTime));
		}
		setAttr("result", result);
		renderJson();
	}
	
	/**
	 * 获取KPI明星客户经理
	 * 2018年5月28日15:19:00
	 * @author liutao
	 * @param period 期次
	 * @return 返回KPI明星客户
	 */
	public void findKPIStarCustomer(){
		String roleType = getPara("roleType");
		List<Record> records = server.findKPIStarCustomer(roleType);
		String webRootPath = PathKit.getWebRootPath();
		for (Record record : records) {
			String head_url = webRootPath + record.getStr("head_url");
			if(!new File(head_url).exists()){
				head_url = "/upload/7.jpg";
				record.set("head_url", head_url);
			}
		}
		setAttr("records", records);
		renderJson();
	}
	
	/**
	 * 查询营销达成率明星客户经理
	 * 2018年6月1日10:43:53
	 * @author liutao
	 * @return
	 */
	public void findReachStarCustomer(){
		String roleType = getPara("roleType");
		List<Record> records = server.findReachStarCustomer(roleType);
		String webRootPath = PathKit.getWebRootPath();
		for (Record record : records) {
			String head_url = webRootPath + record.getStr("head_url");
			if(!new File(head_url).exists()){
				head_url = "/upload/7.jpg";
				record.set("head_url", head_url);
			}
		}
		setAttr("records", records);
		renderJson();
	}
	
	/**
	 * 查询工作日历
	 * 2018年6月6日14:26:04
	 * @author liutao
	 */
	public void findCalendar(){
		Record m = getCurrentUser();
		String userId = m.getStr("ID");
		String userNo = m.getStr("USER_NO");
		
		List<Record> list = server.findCalendar(userId, userNo);
		setAttr("list", list);
		renderJson();
	}
	
	/**
	 * 查询首页代办任务数（商机客户待处理数、价值客户待处理数、客户池待认领/分配数）
	 * 2018年6月7日16:56:39
	 * @author liutao
	 */
	public void findTaskNum(){
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		Record r = server.findTaskNum(userNo);
		setAttr("r", r);
		renderJson();
	}
	
	/**
	 * 查询菜单信息
	 * 2018年6月8日14:33:26
	 * @author liutao
	 */
	public void findMenuInfoByUrl(){
		String type = getPara("type");
		String url = "zxtask/marketCustomer";
		boolean flag = false;
		if("reach".equals(type)){
			url = "zxtask/marketCustomer";
			flag = true;
		} else if("cont".equals(type)){
			url = "zxCustClaim?flag=1";
			flag = true;
		} else if("lurk".equals(type)){
			url = "zxtask/prospectiveCustomer";
			flag = true;
		} else if("downTask".equals(type)){
			url = "zxDownTask";
			flag = true;
		}
		Record node = server.findMenuInfoByUrl(url);
		if(flag){
			setAttr("node", node);
		}
		renderJson();
	}
	
	/**
	 * 打开所有代办任务页面窗口
	 * 2018年5月14日15:31:26
	 * @author liutao
	 */
	public void allCalendarTask(){
		render("/pages/login/allCalendarTask.jsp");
	}
	
	/**
	 * 查找工作日历的所有任务
	 * 2018年6月9日09:24:21
	 * @author liutao
	 */
	public void findAllCalendarTask() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String custNo = getPara("customercode");
		String custName = getPara("customername");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		Page<Record> r = server.findAllCalendarTask(pageNum, pageSize, custNo, custName, userNo);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 修改个人信息
	 * 2018年6月27日09:16:05
	 * @author liutao
	 */
	public void alterUserInfo() {
		render("/pages/zxrole/roleSetup.jsp");
	}
	
	/**
	 * 查询是否是领导角色，如果是领导角色返回相应代表值，不是领导返回0
	 * 举例：如果当前登录人是省行领导则返回1、分行返回2、支行返回3，责任中心(网点)返回4，否则返回0
	 * 2018年7月5日10:45:10
	 * @author liutao
	 */
	public void findUserRole(){
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		List<Record> records = server.findWhetherLead(userNo);
		if(null != records && records.size() > 0){
			int roleLevel = 9;
			for (Record record : records) {
				String name = record.getStr("name");
				if(StringUtils.isNotBlank(name)){
					if(name.contains("省行")){
						if(roleLevel > 1){
							setAttr("flag", "1");
							roleLevel = 1;
						}
					}else if(name.contains("二级分行")){
						if(roleLevel > 2){
							setAttr("flag", "2");
							roleLevel = 2;
						}
					}else if(name.contains("中心支行")){
						if(roleLevel > 3){
							setAttr("flag", "3");
							roleLevel = 3;
						}
					}else if(name.contains("责任中心")){
						if(roleLevel > 4){
							setAttr("flag", "4");
							roleLevel = 4;
						}
					}else{
						if(roleLevel > 4){
							setAttr("flag", "0");
							roleLevel = 9;
						}
					}
				}else{
					if(roleLevel > 4){
						setAttr("flag", "0");
						roleLevel = 9;
					}
				}
			}
		}else{
			setAttr("flag", "0");
		}
		renderJson();
	}
	
	/**
	 * 查询首页机构雷达图相关数据
	 * 2018年7月5日14:21:20
	 * @author liutao
	 */
	/*
	public void indexOrgRadarMap(){
		String leadRole = getPara("leadRole");
		Record result = new Record();
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		for(int i = 1; i <= 4; i++){
			Record r = new Record();
			String timePoint = i + "";
			//1、存款贷款
			Record fund = server.findOrgMoneyday(userNo, leadRole, timePoint);
			r.set("incomday", fund.get("incomday"));//存款
			r.set("loansday", fund.get("loansday"));//贷款
			//2、加权（分别查询一层到五层客户数，然后根据EXCEL大表中加权计算规则计算加权数）
			Record weighting = server.findOrgWeighting(userNo, leadRole, timePoint);
			r.set("weighting", weighting.get("newNum"));
			//4、手工报表处理效率
			float efficiency = 0f;
			r.set("efficiency", efficiency);
			//5、潜在客户达成率
			Record latentReach = server.findOrgLatentReach(userNo, leadRole, timePoint);
			r.set("latentreach", latentReach.get("reach"));
			//6、商机客户转化率
			Record markReach = server.findOrgMarkReach(userNo, leadRole, timePoint);
			r.set("markreach", markReach.get("reach"));
			//把查询出来的结果放到结果集里面
			result.set("r" + i, r);
		}
		setAttr("result", result);
		renderJson();
	}*/
	
	/**
	 * 进入角色修改页面
	 * 2018年7月25日11:33:49
	 * @author liutao
	 */
	public void alterUserRole(){
		render("/pages/zxrole/roleChange.jsp");
	}
	
	/**
	 * 查询首页机构雷达图相关数据 ==>修改成查询定时任务插入的表数据
	 * 2018年8月8日16:51:57
	 * @author liutao
	 */
	public void indexOrgRadarMap(){
		String leadRole = getPara("leadRole");
		Record result = new Record();
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		for(int i = 1; i <= 4; i++){
			String timePoint = i + "";
			Record r = server.findOrgRadarMap(userNo, leadRole, timePoint);
			float efficiency = 0f;
			r.set("efficiency", efficiency);
			//把查询出来的结果放到结果集里面
			result.set("r" + i, r);
		}
		setAttr("result", result);
		renderJson();
	}
		
	/**
	 * 查询个人雷达图相关数据
	 * 2018年9月1日10:24:04
	 * @author liutao
	 */
	public void indexRadarMapNew(){
		Record result = new Record();
		Record m = getCurrentUser();
		//String userId = m.getStr("ID");
		final String userNo = m.getStr("USER_NO");
		
		for(int i = 1; i <= 4; i++){
			String timePoint = i + "";
			//查询个人雷达图数据
			Record r = server.findPersonRadarMap(userNo, timePoint);
			float efficiency = 0f;
			r.set("efficiency", efficiency);
			result.set("r" + i, r);
		}
		setAttr("result", result);
		renderJson();
	}
	
	/**
	 * 查询营销达成率明星客户经理
	 * 2018年6月1日10:43:53
	 * @author liutao
	 * @return
	 */
	public void findReachStarLatent(){
		String roleType = getPara("roleType");
		List<Record> records = server.findReachStarLatent(roleType);
		String webRootPath = PathKit.getWebRootPath();
		for (Record record : records) {
			String head_url = webRootPath + record.getStr("head_url");
			if(!new File(head_url).exists()){
				head_url = "/upload/7.jpg";
				record.set("head_url", head_url);
			}
		}
		setAttr("records", records);
		renderJson();
	}
}

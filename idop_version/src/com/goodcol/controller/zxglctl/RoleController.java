package com.goodcol.controller.zxglctl;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.RoleServer;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.anatation.RouteBind;

@RouteBind(path = "/zxrole")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class RoleController extends BaseCtl{
	private RoleServer roleServer = new RoleServer();

	@Override
	public void index() {
		
	}
	
	/**
	 * 获取字典数据中角色列表
	 * 2018年5月4日14:41:26
	 * @author liutao
	 */
	public void getAllRoleList() {
		List<Map<String, String>> list = roleServer.getAllRoleList();
		renderJson(list);
	}
	
	/**
	 * 保存用户申请的角色
	 * 2018年5月7日17:08:55
	 * @author liutao
	 */
	public void saveApplyRole(){
		//UploadFile file = getFile("upload_file", "headImg");
		String user_id = getPara("user_id");
		String orgId = getPara("orgId");
//		String role_id = getPara("role_id");
//		String nick_name = getPara("nick_name");
//		String phone = getPara("phone");
		try {
//			Record m = getCurrentUser();
//			String userNo = m.getStr("USER_NO");
//			String fileName = System.currentTimeMillis() + userNo;
//			if(file!=null){
//				File src = file.getFile();
//				String suffix = src.getName().substring(src.getName().indexOf("."));
//				String path = src.getParent();
//				fileName = path + File.separator + fileName + suffix;
//				fileName = fileName.replace("\\", "/");
//				File dest=new File(fileName);
//				file.getFile().renameTo(dest);
//				fileName = fileName.substring(fileName.indexOf("upload") - 1);
//				//setAttr("flag", flag);
//			}
			String sql = "update sys_user_info set org_id = ? where id = ? ";
			Db.use("default").update(sql,orgId, user_id);
		} catch (Exception e) {
			e.printStackTrace();
			//setAttr("flag", false);
		}
		//renderJson();
		renderNull();
	}
	
	/**
	 * 进入申请角色列表页面
	 * 2018年5月10日14:22:27
	 * @author liutao
	 */
	public void applyRolePage(){
		renderJsp("approvalApplyRole.jsp");
	}
	
	/**
	 * 获取所有的申请角色列表
	 * 2018年5月10日14:22:27
	 * @author liutao
	 */
	public void getApplyRoleList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String name = getPara("name");
		String applyStatus = getPara("applyStatus");
		Page<Record> r = roleServer.getApplyRoleList(pageNum, pageSize, name, applyStatus);
		
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 进入角色审核页面
	 * 2018年5月10日17:58:10
	 * @author liutao
	 */
	public void approval(){
		renderJsp("approval.jsp");
	}
	
	/**
	 * 保存角色申请的审核结果
	 * 2018年5月10日18:58:16
	 * @author liutao
	 * @throws Exception 
	 */
	public void saveApproval() throws Exception{
		String ids = getPara("ids");
		String applyStatus = getPara("applyStatus");
		String approvalState = getPara("approvalState");
		boolean flag = roleServer.saveApproval(ids, applyStatus, approvalState);
		setAttr("flag", flag);
		renderJson();
	}
	
	/**
	 * 进入申请角色列表页
	 * 2018年6月14日11:07:50
	 * @author liutao
	 */
	public void applyRoleListPage(){
		renderJsp("applyRoleListPage.jsp");
	}
	
	/**
	 * 查询所有角色(首次登录系统可申请的角色)
	 * 2018年6月14日10:40:39
	 * @author liutao
	 */
	public void applyRoleList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String name = getPara("name");
		Page<Record> r = roleServer.applyRoleList(pageNum, pageSize, name);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 打开添加获取修改页面
	 * 2018年6月14日11:25:41
	 * @author liutao
	 */
	public void roleForm() {
		render("roleForm.jsp");
	}
	
	/**
	 * 查询角色信息
	 * 2018年6月14日15:50:22
	 * @author liutao
	 */
	public void findRoleInfoById(){
		String id = getPara("id");
		Record r = roleServer.findRoleInfoById(id);
		setAttr("r", r);
		renderJson();
	}
	
	/**
	 * 保存角色信息
	 * 2018年6月14日16:13:39
	 * @author liutao
	 */
	@Before(Tx.class)
	public void saveApplyRoleInfo(){
		String id = getPara("id");
		//String name = getPara("name");
		String spec_level = getPara("spec_level");
		String trans_stand = getPara("trans_stand");
		String promote_stand = getPara("promote_stand");
		String next_value = getPara("next_value");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		String time = DateTimeUtil.getNowDate();
		Record r = new Record();
		r.set("id", id);
		//r.set("name", name);
		r.set("spec_level", spec_level);
		r.set("trans_stand", trans_stand);
		r.set("promote_stand", promote_stand);
		r.set("next_value", next_value);
		r.set("MODIFY_USER_NO", userNo);
		r.set("MODIFY_TIME", time);
		boolean flag = roleServer.saveApplyRoleInfo(r);
		setAttr("flag", flag);
		renderJson();
	}
	
	/**
	 * 进行首次登录系统时取消角色申请记录保存
	 * 2018年7月3日11:22:34
	 * @author liutao
	 * @param user_id
	 * @return
	 */
	@Before(Tx.class)
	public void cancel(){
		String user_id = getPara("user_id");
		boolean flag = roleServer.cancel(user_id);
		setAttr("flag", flag);
		renderJson();
	}
}

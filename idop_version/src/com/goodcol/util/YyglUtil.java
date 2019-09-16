package com.goodcol.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;



/**
 * 共通方法
 * 
 * @author zhang_zy
 *
 */
public class YyglUtil {
	
	// 记录通用审批经过的所有角色
	public static String COMMON_APPLY_ROLES_RES50 = "RES50";
	
	// 角色名称
	public static String ROLE_NMAE_ZHIHANG_HANGZHANG = "支行行长";
	
	// 角色名称
	public static String ROLE_NMAE_FENHANG_HANGZHANG = "分行行长";
	
	// 总行编号
	public static String ROOT_ORG_NUM = "99990000";
	
	// 角色级别0：柜员级 1：支行级 2：分行级 3：总行级
	public static String ROLE_LEVEL_0 = "0";
	
	// 角色级别0：柜员级 1：支行级 2：分行级 3：总行级
	public static String ROLE_LEVEL_1 = "1";
	
	// 角色级别0：柜员级 1：支行级 2：分行级 3：总行级
	public static String ROLE_LEVEL_2 = "2";
	
	// 角色级别0：柜员级 1：支行级 2：分行级 3：总行级
	public static String ROLE_LEVEL_3 = "3";
	
	// 角色主键
	
	// 状态： 0
	public static String STATUS_0 = "0";
	
	// 状态：1
	public static String STATUS_1 = "1";
	
	// 字典管理：地区代码
	public static String PARAM_KEY_1006 = "1006";
	
	// 字典管理：事务类别
	public static String PARAM_KEY_1010 = "1010";
	
	// 字典管理：通用事务申请事项
	public static String PARAM_KEY_10000 = "10000";
	
	// 字典管理：事务审批金额配置
	public static String PARAM_KEY_10001 = "10001";
	
	// 操作类型 0：新增 1：修改
	public static String PAGE_TYPE_ADD = "0";
	
	// 操作类型 0：新增 1：修改
	public static String PAGE_TYPE_UPDATE = "1";
	
	// 字符串 true
	public static String STRING_TRUE = "true";
	
	// 字符串 false
	public static String STRING_FALSE = "false";
	
	// 是否需要打印 0：不需要 1：需要
	public static String NEED_PRINT_FALSE = "0";
	
	// 是否需要打印 0：不需要 1：需要
	public static String NEED_PRINT_TRUE = "1";
	
	// 日期格式yyyy-MM-dd
	public static String YYYY_MM_DD = "yyyy-MM-dd";
	
	// 日期格式yyyy-MM-dd HH:mm:ss
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	// 日期格式yyyyMMddHHmmss
	public static String YYYY_MM_DD_HH_MM_SS_14 = "yyyyMMddHHmmss";
	
	// 日期格式yyyyMMdd
	public static String YYYYMMDD = "yyyyMMdd";
	
	// 日期格式HHmmss
	public static String HHMMSS = "HHmmss";
	
	
	// 待审批信息
	public static String APPLY_MESSAGE_0 = "您有一条待审批事项，请尽快登陆运营综合管理系统处理！";
	
	// 审批完成信息
	public static String APPLY_MESSAGE_1 = "您有一条事项审批结束，请尽快登陆运营综合管理系统查看！";
	
	// 分配任务信息
	public static String APPLY_MESSAGE_2 = "您有一条待分配的任务，请尽快登陆运营综合管理系统查看！";
	
	// 执行任务信息
	public static String APPLY_MESSAGE_3 = "您有一条待执行的任务，请尽快登陆运营综合管理系统查看！";
	
	// 执行任务完成信息
	public static String APPLY_MESSAGE_4 = "您有一条已执行完成的任务，请尽快登陆运营综合管理系统查看！";
	
	// 协助总行信息交流 抢任务信息
	public static String APPLY_MESSAGE_5 = "您有一条抢任务信息，请尽快登陆运营综合管理系统查看！";
	
	// 应急事件
	public static String APPLY_MESSAGE_6 = "您有一条应急事件需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理审批
	public static String APPLY_MESSAGE_7 = "您有一条协同代理审批任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理协助
	public static String APPLY_MESSAGE_8 = "您有一条协同代理协助任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理退回
	public static String APPLY_MESSAGE_9 = "您有一条协同代理退回任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理完成
	public static String APPLY_MESSAGE_10 = "您有一条协同代理完成任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理追加资料
	public static String APPLY_MESSAGE_11 = "您有一条协同代理追加资料任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 协同代理追加资料完成
	public static String APPLY_MESSAGE_12 = "您有一条协同代理追加资料完成任务需要处理，请尽快登陆运营综合管理系统查看！";
	
	// 登记簿待审批信息
	public static String APPLY_MESSAGE_13 = "你有一条登记簿信息待审批，请尽快登陆运营综合管理系统查看！";
	
	// 登记簿被退回信息
	public static String APPLY_MESSAGE_14 = "你有一条登记簿信息被退回，请尽快登陆运营综合管理系统查看！";
	
	
	/**
	 * 判断是否空字符
	 * 
	 * @param str 字符
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date,String format){
		
		// 格式化时间
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 根据传入字典编号获取字典信息
	 * 
	 * @param param_class 字典分类
	 * @param param_key 字典键值
	 * @return
	 */
    public static String getParamWithKey(String param_class,String param_key) {
    	String back_value = null;
		List<Record> list = Db.find("select remark from sys_param_info where key='"+ param_class + "' and val='"+param_key+"' and status = '1'");
		if(list != null && list.size() > 0){
			back_value = list.get(0).getStr("REMARK");
		}
		return back_value;
    }
    
	/**
	 * 根据传入字典编号获取字典信息
	 * 
	 * @param param_class 字典分类
	 * @param param_key 字典键值
	 * @return
	 */
    public static List<Record> getParamList(String param_class) {
    	return Db.find("select id,val,remark,res1 from sys_param_info where key='"+ param_class + "' and status = '1'");
    }
    
    /**
     * 通用事务审批，根据当前用户的角色获取审批的用户列表
     * 
     * @param role_id 角色
     * @param org_id 部门编号
     * @param role_level 角色等级
     * 
     * @return
     */
    public static List<Record> getUserListByRoleForCommonApply(String role_id,String org_id,String role_level){
    	List<Record> lr = Db.find("select next_operator_group,next_operator_group_name from yygl_affairs_common where current_operator='"+ role_id+"'");
    	List<Record> list = null;
    	
    	// 下一个审批角色的角色ID
    	String operator_role_id = null;
    	
    	// 下一个审批角色的角色等级
    	String operator_role_level = null;
    	
    	// 是总行
    	String whereOrg = org_id;
		if(lr != null && lr.size() > 0){
			StringBuffer sql = new StringBuffer("(");
			String next_operator_group = lr.get(0).getStr("NEXT_OPERATOR_GROUP");
			if(!YyglUtil.isEmpty(next_operator_group)){
				String[] next_operator_group_arr = next_operator_group.split(",");
				for(int i=0;i<next_operator_group_arr.length;i++){
					sql.append("'");
					
					// 下一个审批角色的角色ID
					operator_role_id = next_operator_group_arr[i];
					list = Db.find("select role_level from sys_role_info t where id ='"+ operator_role_id +"'");
					operator_role_level = list.get(0).getStr("ROLE_LEVEL");
					if(!isEmpty(role_level) && !isEmpty(operator_role_level)){
							
							// 非柜员的情况下，夸两级为总行
							if(Integer.parseInt(role_level) > 0 && Integer.parseInt(operator_role_level)> 2){
								list = Db.find("select ORGNUM from sys_org_info where stat='1' and BY2='0' AND BY3= '1'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									for(int j=0;j<list.size();j++){
										whereOrg = whereOrg +"','"+list.get(j).getStr("ORGNUM");
									}
								}
							}else if(Integer.parseInt(role_level) > 0 && Integer.parseInt(operator_role_level)== 2){
								list = Db.find("select SSFH from sys_org_info t where stat='1' and orgnum ='"+ org_id +"'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									whereOrg = whereOrg +"','"+list.get(0).getStr("SSFH");
									
									list = Db.find("select orgnum from sys_org_info where stat='1' and by2='1' and ssfh = '"+ list.get(0).getStr("SSFH") +"'");
									for(int j=0;j<list.size();j++){
										whereOrg = whereOrg +"','"+list.get(j).getStr("ORGNUM");
									}
								}
							}else{
								
								// 小微支行
								list = Db.find("select BY2,UPID from sys_org_info t where stat='1' and orgnum ='"+ org_id +"'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									
									// 机构级别 0:总行 1：分行 2：一级支行 3：二级支行
									if("3".equals(list.get(0).getStr("BY2"))){
										whereOrg = whereOrg+"','"+list.get(0).getStr("UPID");
									}else{
										whereOrg = whereOrg+"','"+org_id;
									}
								}
								
							}
					}
					sql.append(operator_role_id);
					if(i==next_operator_group_arr.length-1){
						sql.append("'");
					}else{
						sql.append("',");
					}
				}
				sql.append(")");
				
				String querySql = "select  id,  name, UPID, flag from (select  id,  name, '' AS UPID, 'org' as flag,role_seq from sys_role_info where id in "+sql
					+ " union "
					+ " select a.user_no as id, a.name, role_id as UPID, 'user' as flag,'000' role_seq from sys_user_info a,sys_role_info b  "
					+ " where a.role_id = b.id and a.stat='0' ";
					
						
				// 跨行级审批，如支行到分行
				if(!YyglUtil.isEmpty(whereOrg)){ 
					querySql += " and org_id in ('"+whereOrg+"')";
				}
				querySql += " and b.id in "+sql;
				querySql +=")order by role_seq desc";
				lr = Db.find(querySql.toString());
			}
		}
		return lr;
    }
    
    /**
     * 登记簿审批，根据当前用户的角色获取审批的用户列表
     * 
     * @param role_id 角色
     * @param org_id 部门编号
     * @param role_level 角色等级
     * 
     * @return
     */
    public static List<Record> getUserListByRoleForDjbApply(String role_id,String org_id,String role_level,String djbid){
    	List<Record> lr = Db.find("select next_operator_group,next_operator_group_name from t_djb_lcpz where instr(current_operator,'"+ role_id+"')>0 and djbid = '"+djbid+"'");
    	List<Record> list = null;
    	
    	// 下一个审批角色的角色ID
    	String operator_role_id = null;
    	
    	// 下一个审批角色的角色等级
    	String operator_role_level = null;
    	
    	// 是总行
    	String whereOrg = org_id;
		if(lr != null && lr.size() > 0){
			StringBuffer sql = new StringBuffer("(");
			String next_operator_group = lr.get(0).getStr("NEXT_OPERATOR_GROUP");
			if(!YyglUtil.isEmpty(next_operator_group)){
				String[] next_operator_group_arr = next_operator_group.split(",");
				for(int i=0;i<next_operator_group_arr.length;i++){
					sql.append("'");
					
					// 下一个审批角色的角色ID
					operator_role_id = next_operator_group_arr[i];
					list = Db.find("select role_level from sys_role_info t where id ='"+ operator_role_id +"'");
					operator_role_level = list.get(0).getStr("ROLE_LEVEL");
					if(!isEmpty(role_level) && !isEmpty(operator_role_level)){
							
							// 非柜员的情况下，夸两级为总行
							if(Integer.parseInt(role_level) > 0 && Integer.parseInt(operator_role_level)> 2){
								list = Db.find("select ORGNUM from sys_org_info where stat='1' and BY2='0' AND BY3= '1'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									for(int j=0;j<list.size();j++){
										whereOrg = whereOrg +"','"+list.get(j).getStr("ORGNUM");
									}
								}
							}else if(Integer.parseInt(role_level) > 0 && Integer.parseInt(operator_role_level)== 2){
								list = Db.find("select upid as orgnum from sys_org_info t where stat='1' and orgnum ='"+ org_id +"'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									whereOrg = whereOrg +"','"+list.get(0).getStr("orgnum");
									
									list = Db.find("select orgnum from sys_org_info where stat='1'  and orgnum = '"+ list.get(0).getStr("orgnum") +"'");
									for(int j=0;j<list.size();j++){
										whereOrg = whereOrg +"','"+list.get(j).getStr("ORGNUM");
									}
								}
							}else{
								
								// 小微支行
								list = Db.find("select BY2,UPID from sys_org_info t where stat='1' and orgnum ='"+ org_id +"'");
								
								// 获取上一级部门编号
								if(list != null && list.size() > 0){
									
									// 机构级别 0:总行 1：分行 2：一级支行 3：二级支行
									if("3".equals(list.get(0).getStr("BY2"))){
										whereOrg = whereOrg+"','"+list.get(0).getStr("UPID");
									}else{
										whereOrg = whereOrg+"','"+org_id;
									}
								}
								
							}
					}
					sql.append(operator_role_id);
					if(i==next_operator_group_arr.length-1){
						sql.append("'");
					}else{
						sql.append("',");
					}
				}
				sql.append(")");
				
				String querySql = "select  id,  name, UPID, flag from (select  id,  name, '' AS UPID, 'org' as flag,'000' role_seq from sys_role_info where id in "+sql
					+ " union "
					+ " select a.user_no as id, a.name, role_id as UPID, 'user' as flag,'000' role_seq from sys_user_info a,sys_role_info b  "
					+ " where a.role_id = b.id and a.stat='0' ";
					
						
				// 跨行级审批，如支行到分行
				if(!YyglUtil.isEmpty(whereOrg)){ 
					querySql += " and org_id in ('"+whereOrg+"')";
				}
				querySql += " and b.id in "+sql;
				querySql +=")order by role_seq desc";
				lr = Db.find(querySql.toString());
			}
		}
		return lr;
    }
    
    /**
	 * 从数据库获取sequence
	 * 
	 */
	public static String getSequenceNo() {
		Record r = Db.findFirst("select get_lsh(to_char(sysdate,'yyyymmdd')) lsh from dual");
		if(r != null) {
			return r.getStr("lsh");
		}
		return null;
	}
}

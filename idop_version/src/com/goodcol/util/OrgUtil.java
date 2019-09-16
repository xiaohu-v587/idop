package com.goodcol.util;

import java.util.List;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * 机构公用类
 * 
 * @author
 */
public class OrgUtil {
	private static OrgUtil org = null;

	private OrgUtil() {
 
	}

	public static OrgUtil getIntanceof() {
		if (org == null) {
			org = new OrgUtil();
		}
		return org;
	} 
	
	/**
	 * 获取用户的数据范围，角色级别与用户的数据范围 两个比较，取大的
	 */
	/*
	public  String getSubOrg(String user_no) {
		String sql = " select  LISTAGG('''' || orgnum || '''') WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\") orgs"
				+ "	from sys_org_info where stat = 1 "
				+ " start with orgnum = "
				+ " ( select o.orgnum from sys_org_info o, sys_user_info u  where o.orgnum = u.org_id and u.user_no= ? )"
				+ " connect by prior id = upid ";
		Record r = Db.findFirst(sql, user_no);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	*/
	/**
	 * 根据用户号 查询本机构和所有的子机构 
	 */
	public  String getSubOrg(String user_no) {
		String sql = " select  LISTAGG('''' || orgnum || '''') WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\") orgs"
				+ "	from sys_org_info where stat = 1 "
				+ " start with orgnum = "
				+ " ( select o.orgnum from sys_org_info o, sys_user_info u  where o.orgnum = u.org_id and u.user_no= ? )"
				+ " connect by prior id = upid ";
		Record r = Db.findFirst(sql, user_no);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	
	/**
	 * 根据机构号 查询本机构和所有的子机构 结果是一个以单引号隔开的字符串
	 */
	public  String getSubOrgId(String org_id) {
		//String sqlfh="select remark from SYS_PARAM_INFO where key='1021'"
		String sql ="select  LISTAGG(','||'''' || SYS_ORG_INFO.orgnum || '''') "
				+ "WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\") orgs "
				+ "from sys_org_info where stat = 1 start with orgnum = ? "
				+ "connect by prior \"ID\" = upid  ";
		Record r = Db.findFirst(sql, org_id);
		String subOrgs = r.getStr("orgs").substring(1);
		return subOrgs;
	}
	
	/**
	 * 根据机构号 查询本机构和所有的子机构 结果是一个以单引号隔开的字符串
	 */
	public  String getSubOrgbyorg(String orgnum) {
		String sql = " select  LISTAGG( ''''|| orgnum || '''') WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\") orgs  "
				+ "	from sys_org_info where stat = 1 "
				+ " start with orgnum = "
				+ " ? "
				+ " connect by prior id = upid ";
		Record r = Db.findFirst(sql, orgnum);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	
	/**
	 * 根据机构号 查询本机构和所有的子机构
	 *  结果是一个以单引号隔开的字符串,但是机构号只取前两个数字
	 *  这个方法用于事务那一块，对应的是区域代码，如('90','05','01')
	 */
	public  String getSubBrandbyorg(String orgnum) {
		String sql = " select  LISTAGG( '''' || substr(orgnum,0,2) || '''') WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\")  orgs "
				+ "	from sys_org_info where stat = 1 "
				+ " start with orgnum = "
				+ " ? "
				+ " connect by prior id = upid ";
		Record r = Db.findFirst(sql, orgnum);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	

	/**
	 * 根据机构id,获取该机构对象以及所有的子机构对象
	 * @param orgnum
	 * @return
	 */
     public static List<Record> getDownReId(String id){
    	List<Record> records=Db.find("select * from sys_org_info where by5 like ? and stat='1'","%"+id+"%");
    	 return records;
     }
	
	
	/**
	 * 根据机构号,获取该机构的机构id以及所有的上级机构主键id,
	 * 并用逗号分割
	 * @param orgnum
	 * @return
	 */
	public static String getUpOrgId(String orgnum){
		Record r=Db.findFirst("select * from sys_org_info where orgnum=?",orgnum);
		String upOrgId=r.getStr("by5");
		return upOrgId;
	}
	
	/**
	 * 根据机构号,获取该机构的机构号以及所有的上级机构机构号,
	 * 并用逗号分割
	 * @param orgnum
	 * @return
	 */
	public static String getUpOrgNum(String orgnum){
		String sql="SELECT LISTAGG( '' || orgnum || ',') WITHIN GROUP( ORDER BY \"ID\") orgs "
				+ "FROM (SELECT \"ID\",ORGNUM from SYS_ORG_INFO where \"ID\" "
				+ "IN(select regexp_substr((select by5 from sys_org_info where orgnum=? and stat='1'),'[^,]+',1,rownum)   "
				+ "from SYS_ORG_INFO connect by rownum<=100) )";
		Record r = Db.findFirst(sql, orgnum);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
		
	}
     
	/**
	 * 根据机构号,获取该机构的机构名称以及所有的上级机构机构名称,
	 * 并用逗号分割
	 * @param orgnum
	 * @return
	 */
	public static String getUpOrgName(String orgnum){
		String sql="SELECT LISTAGG( '' || orgname || ',') WITHIN GROUP( ORDER BY \"ID\") orgs "
				+ "FROM (SELECT \"ID\",orgname from SYS_ORG_INFO where \"ID\" "
				+ "IN(select regexp_substr((select by5 from sys_org_info where orgnum=? and stat='1'),'[^,]+',1,rownum)   "
				+ "from SYS_ORG_INFO connect by rownum<=100) )";
		Record r = Db.findFirst(sql, orgnum);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
		
	}

	/**
	 * 根据用户号,获取该机构的机构名称以及上级机构的机构名称，
	 * 用逗号分割
	 * @param userid
	 * @return
	 */
	public static String getUpOrgNameByUser(String userid){
		String sql = "SELECT LISTAGG( '' || orgname || ',') WITHIN GROUP( ORDER BY \"ID\") orgs "
				+ "FROM (SELECT \"ID\",orgname from SYS_ORG_INFO where \"ID\" "
				+ "IN(select regexp_substr((select by5 from sys_org_info where  stat='1' and orgnum="
				+ "( select o.orgnum from sys_org_info o, sys_user_info u  where o.orgnum = u.org_id and u.user_no= ?)),'[^,]+',1,rownum)   "
				+ "from SYS_ORG_INFO connect by rownum<=100) )";
		Record r = Db.findFirst(sql, userid);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	
	/**
	 * 根据用户号,获取该机构的机构号以及上级机构的机构号，
	 * 用逗号分割
	 * @param userid
	 * @return
	 */
	public static String getUpOrgIdByUser(String userid){
		String sql = "SELECT LISTAGG( '' || orgnum || ',') WITHIN GROUP( ORDER BY \"ID\") orgs "
				+ "FROM (SELECT \"ID\",orgnum from SYS_ORG_INFO where \"ID\" "
				+ "IN(select regexp_substr((select by5 from sys_org_info where  stat='1' and orgnum="
				+ "( select o.orgnum from sys_org_info o, sys_user_info u  where o.orgnum = u.org_id and u.user_no= ?)),'[^,]+',1,rownum)   "
				+ "from SYS_ORG_INFO connect by rownum<=100) )";
		Record r = Db.findFirst(sql, userid);
		String subOrgs = r.getStr("orgs");
		return subOrgs;
	}
	/**
	 * 根据机构id查询子机构号
	 * @param org_id
	 * @return
	 */
	public static String getSubOrgnums(String org_id) {
		//String sqlfh="select remark from SYS_PARAM_INFO where key='1021'"
		String sql ="select  LISTAGG(','||'''' || SYS_ORG_INFO.orgnum || '''') "
				+ "WITHIN GROUP( ORDER BY SYS_ORG_INFO.\"ID\") orgs "
				+ "from sys_org_info where stat = 1 start with id = ? "
				+ "connect by prior \"ID\" = upid  ";
		Record r = Db.findFirst(sql, org_id);
		String subOrgs = r.getStr("orgs").substring(1);
		return subOrgs;
	}
	
	/**
	 * 
	 * 角色等级为支行的人的查询范围
	 * 
	 * */
	public String getZhOrgId(String org_id){
		String sql = " select  wm_concat( '''' || orgnum || '''') orgs from sys_org_info"
				+ " where stat = '1'  start with orgnum = ? connect by prior orgnum = upid ";
		
		List<Record> list = Db.find(sql, org_id);
		String subOrgs = "";
		if(list != null && list.size() > 0){
			subOrgs = list.get(0).getStr("orgs");
		}
		return subOrgs;
	}
	
	/**
	 * 
	 * 角色等级为分行的人的查询范围
	 * 
	 * */
	public String getFhOrgId(String org_id) {
		String sql = "";
/*		sql = " select  wm_concat( '''' || orgnum || '''') orgs from sys_org_info where stat = 1  start with orgnum = "
				+ "(select orgnum from sys_org_info where orgname like '%分行' start with orgnum = ? connect by orgnum = prior upid) connect by prior orgnum = upid ";
*/
		sql = " select  wm_concat( '''' || orgnum || '''') orgs from sys_org_info left where stat = '1'  start with orgnum in "
				+ "(select a.orgnum from sys_org_info a left join sys_org_info b on a.upid = b.orgnum where a.stat='1' and b.stat='1' and b.upid is null and a.upid is not null start with a.orgnum = ? connect by a.orgnum = prior a.upid) connect by prior orgnum = upid ";
		List<Record> list = Db.find(sql, org_id);
		String subOrgs = "";
		if(list != null && list.size() > 0){
			subOrgs = list.get(0).getStr("orgs");
		}
		return subOrgs;
	}

}

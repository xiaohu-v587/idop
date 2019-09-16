package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 重点报表展示
 * @author 张强强
 * @date 2018-11-19
 *
 */
@RouteBind( path = "/importantBbView")
@Before({ManagerPowerInterceptor.class})
public class ImportantBbView extends BaseCtl{

	// 记录日志用
	public static Logger log = Logger.getLogger(ParamCtl.class); 
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		render("ImportantBbView.jsp");
	}
	
	/**
	 * 查询所有相对数据
	 * 
	 */
	public void queryData() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
		String orgid = getPara("orgid");
		// 查询语句
		Record r= Db.findFirst("select  t.id,t.br_no, t.up_br_no,t.br_name, t.deptlevel,t.name,t.orgid from a_dap_dept_para_js t where t.id ="+"'"+orgid+"'");
		// 赋值
		setAttr("data", r);
		// 打印日志
		log.info("getList--r):" + r);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "查询报表", "99", "报表-查询");
		log.info("报表-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询所有相对数据
	 * 
	 */
	public void querySonData() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
		String parenttype = getPara("parenttype");
		// 查询语句
		List<Record> r= Db.find("select t.bb_name from dop_bbtype t where t.parent_type ="+"'"+parenttype+"'");
		// 赋值
		setAttr("data", r);
		// 打印日志
		log.info("getList--r):" + r);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "查询报表", "99", "报表-查询");
		log.info("报表-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询所有相对数据
	 * 
	 */
	public void querySonDataBybm() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
		String str = getPara("str");
		// 查询语句
		Record r= Db.findFirst("select  t.spare1,t.bb_name from dop_bbtype t where t.bb_type ="+"'"+str+"'");
		// 赋值
		setAttr("data", r);
		// 打印日志
		log.info("getList--r):" + r);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "查询报表", "99", r.getStr("bb_name"));
		log.info("报表-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 跳转报表页面
	 */
	public void queryPage(){
		String str = getPara("str");
		// 赋值
		setAttr("data", str);
		setAttr("ipaddress",ParamContainer.getDictName("ipaddress", "ipaddress"));
		if(str.equals("gywdyytp")||str.equals("jgwdyytp")||str.equals("gywdtp")||str.equals("jgwdtp")){
			render("threeParamPage.jsp");
		}else{
			render("twoParamPage.jsp");
		}
	}
	
	private final static int[] areacode={1601,1637,1833,2078,2274,2302,2433,2594,2787,3106,3212,3472,3635,3722,3730,3858,4027,4086,4390,4558,4684,4925,5249,5590};
	private final static String[] letters={"a","b","c","d","e","f","g","h","j","k","l","m","n","o","p","q","r","s","t","w","x","y","z"};
	
	public  void getAllFirstLetter(){
		String _str = "";
		String str = getPara("bbname");
		if(str==null ||str.trim().length()==0){
			_str = "";
		}
			for(int i=0;i<str.length();i++){
				_str =_str+getFirstLetter(str.substring(i,i+1));
			}
		setAttr("data", _str);
		renderJson();
	}
	public  String getFirstLetter(String str){
		str = conversionstr(str,"GB2312","ISO8859-1");
		if(str.length()>1){
			int li_sectorCode = (int)str.charAt(0);//区码
			int li_positionCode = (int)str.charAt(1);//位码
			li_sectorCode = li_sectorCode-160;
			li_positionCode = li_positionCode-160;
			int li_secposcode = li_sectorCode*100+li_positionCode;//汉子区位码
			if(li_secposcode>1600&&li_secposcode<5590){
				for (int i=0;i<23;i++){
					if(li_secposcode>=areacode[i] && li_secposcode<areacode[i+1]){
						str = letters[i];
						break;
					}
				}
			}
			else{
				str = conversionstr(str,"ISO8859-1","GB2312");
				str = str.substring(0,1);
			}
		}
		return str;
		
	}
	public  String conversionstr(String str,String charsetName,String toCharSetName){
		try{
			str = new String(str.getBytes(charsetName),toCharSetName);
		}catch(UnsupportedEncodingException e){
			System.out.println(e.getMessage()+"字符串转码异常");
		}
		return str;
		
	}
}

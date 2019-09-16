package ${packageName};

import java.util.List;
import java.util.ArrayList;

import com.goodcol.util.AppUtils;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 *  @author ${author}
 */
@RouteBind(path = "${url_path?lower_case}")
@Before({ ManagerPowerInterceptor.class })
public class ${className?lower_case?cap_first+"Ctl"} {
	/**
	 * 首页
	 */
	public void index() {
		renderJsp("index.jsp");
	}
	/**
	 * 获得首页列表数据
	 */
	 public void getList(){
		${params}
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<String> listStr = new ArrayList<String>();
		StringBuffer sb=new StringBuffer();
		${paramContents}
		<#if sql_content?? && extra_sql_content?? && where_sql_content??>
			String sql="${sql_content}";
			String extrasql="${extra_sql_content} ${where_sql_content}"+sb.toString()+" ${order_by_sql_content!''}";
		<#else>
			String sql = "select ${list_field?lower_case}";
			String extrasql = " from  ${table_name?lower_case} where 1=1"+sb.toString();
		</#if>
	
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql, listStr.toArray());
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	 }
	/**
	 * 增加或修改页面
	 */
	public void form() {
		renderJsp("form.jsp");
	}
	/**
	 * 保存或更新
	 */
	public void save() {
		String id = getPara("${pkField?lower_case}");
		${gParam}
		int flag = 0;
		// 根据id是否为空来判断是新增还是修改
		if (AppUtils.StringUtil(id)!=null) {
			flag = Db.update("update ${table_name?lower_case} set ${setSqlContent?lower_case} where ${pkField?lower_case}=?",new Object[]{${saveOrUpdateContent},id});
		} else {
			id=AppUtils.getStringSeq();
		<#assign sufields="${save_update_field}">		
		<#if ",${sufields},"?index_of(",${pkField},")!=-1>
			flag = Db.update("insert into ${table_name?lower_case} (${save_update_field?lower_case}) values (${sbContent})",new Object[]{${save_update_field?lower_case}});
		<#else>
			flag = Db.update("insert into ${table_name?lower_case} (${pkField?lower_case},${save_update_field?lower_case}) values (?,${sbContent})",new Object[]{AppUtils.getStringSeq(),${save_update_field?lower_case}});
		</#if>
		}
		setAttr("flag",flag);
		renderJson();
		
	}
	/**
	 * 删除
	 */
	public void delete() {
		String ids=getPara(0);
		if(ids.contains(",")){
			String []array=ids.split(",");
			for(int i=0;i<array.length;i++){
				String id=array[i];
				Db.update("delete from ${table_name?lower_case} where ${pkField}=?",new Object[]{id});
			}
		}else{
			Db.update("delete from ${table_name?lower_case} where ${pkField}=?",new Object[]{id});	
		}
		setAttr("result",success);
		renderJson();
	}
	/**
	 * 获得填充表单的详情
	 */
	public void getDetail(){
		String id=getPara(0);
		String table_name=getPara("table_name");
		String sql="select * from "+table_name+" where 1=1";
		StringBuffer sb=new StringBuffer();
		List<String> listStr=new ArrayList<String>();
		if(AppUtils.StringUtil(id)!=null){
			sb.append(" and ${pkField}= ?");
			listStr.add(id.trim());
		}
		List<Record> list=Db.find(sql+sb.toString(),listStr.toArray());
		setAttr("record",list.get(0));
		renderJson();
	}
}
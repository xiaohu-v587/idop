import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.filter.stat.StatFilter;
import com.goodcol.core.plugin.activerecord.ActiveRecordPlugin;
import com.goodcol.core.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.dialect.OracleDialect;
import com.goodcol.core.plugin.druid.DruidPlugin;
import com.goodcol.job.MissionIssueJob;
import com.goodcol.util.PropertiesContent;

import net.sf.json.JSONObject;
/**
 * 测试工具，打印测试结果问题
 * <br/>
 * 打印格式：
 * @author hp
 *
 */
public class TestQzUtils {
	static{eeee
		DruidPlugin druidPlugin = new DruidPlugin(
				PropertiesContent.get("jdbc.url"),
				PropertiesContent.get("jdbc.username"),
				PropertiesContent.get("jdbc.password"),
				PropertiesContent.get("jdbc.driverClassName"));
		druidPlugin.addFilter(new StatFilter());
		druidPlugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin("default", druidPlugin);
		arp.setShowSql(true);
		arp.setDialect(new OracleDialect());
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		arp.start();
	}

	
	public static void main(String[] args) {
		//TestQzUtils tq = new TestQzUtils();
		//tq.getTrackTasksList();
		
		new MissionIssueJob().execute(null);
	}
	/**
	 * @Title: getTrackTasksList
	 * @Description: 任务跟踪查询
	 * @author Wangsq
	 * @date 2019-07-31
	 *
	 */
	public void getTrackTasksList() {
		
		//已发起的任务数据id
		String missionIssueId = "D1EC9FC08DAD4A56A1FB8AD934092595";
		
		//当前节点为已发起节点，需要组装数据，建立最基础的关联关系
		
		//当前需要的数据情况
		
		StringBuffer issuesql = new StringBuffer();
		issuesql.append("select ");
		issuesql.append("t.id, ");
		issuesql.append("s.user_no, ");
		issuesql.append("s.col_1, ");//已反馈人数
		issuesql.append("s.col_2, ");//未反馈人数
		issuesql.append("(case when s.col_2 = 0 then 0 else s.col_2/(col_3)*100 end ) col_3_z, ");//任务进度
		issuesql.append("col_3, ");//任务执行总数
		issuesql.append("t.mission_issue_status, ");//任务状态
		issuesql.append("'' upid ");
		issuesql.append("from yygl_mission_issue t left join ");
		issuesql.append("( ");
		issuesql.append("select  ");
		issuesql.append("id, ");
		issuesql.append("(select mission_launch_userno from yygl_mission_config where id = t1.mission_config_id) user_no, ");
		issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and user_mission_status in ('02') and mission_issue_id = t1.id) col_1, ");//已反馈人数
		issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and user_mission_status in ('01','03') and mission_issue_id = t1.id) col_2, ");//未反馈人数
		issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and mission_issue_id = t1.id) col_3 ");//总任务数据
		issuesql.append("from yygl_mission_issue t1  where t1.id = ?");
		issuesql.append(") s on t.id = s.id where t.id = ? ");
		
		//组装待处理任务节点数据
		
		StringBuffer usersql = new StringBuffer();
		usersql.append("select  ");
		usersql.append("t.id, ");
		usersql.append("s.user_no, ");
		usersql.append("s.col_1, ");//已反馈人数 
		usersql.append("s.col_2, ");//未反馈人数
		usersql.append("(case when s.col_2 = 0 then 0 else s.col_1/(col_3)*100 end ) col_3_z, ");//任务进度
		usersql.append("col_3, ");//任务执行总数
		usersql.append("t.user_mission_status, ");//任务状态
		usersql.append("(case when up_mission_id is null then mission_issue_id else up_mission_id end ) upid ");
		usersql.append("from yygl_mission_user t left join ");
		usersql.append("( ");
		usersql.append("select  ");
		usersql.append(" id, ");
		usersql.append(" user_no, ");
		usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('02') and up_mission_id = t1.id) col_1, ");//已反馈人数
		usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('01','03') and up_mission_id = t1.id) col_2, ");//未反馈人数
		usersql.append("(select count(1) from yygl_mission_user where  up_mission_id = t1.id) col_3 ");//总任务数据
		usersql.append("from yygl_mission_user t1 where t1.mission_issue_id = ?");
		usersql.append(") s on t.id = s.id where t.mission_issue_id = ? ");
		
		
		//查询数据
		
		//查询发起节点
		Record issueR = Db.findFirst(issuesql.toString(),missionIssueId,missionIssueId);
		
		//查询处理节点
		List<Record> userList = Db.find(usersql.toString(),missionIssueId,missionIssueId);
		Map<String,Object> r = issueR.getColumns();
		formatReportData(r, userList);
		
		JSONObject json = JSONObject.fromObject(r);
		System.out.println(json.toString());
	}
	
	@SuppressWarnings({ "unchecked" })
	private Map<String,Object> formatReportData(Map<String,Object> r,List<Record> list){
		//检查该id下是否还有子节点 name,value,children
		List<Map<String, Object>> childList =  getChildList(r.get("id")+"", list);
		if(childList.size() > 0){
			r.put("children", new ArrayList<Map<String, Object>>());
			for (Map<String, Object> record : childList) {
				formatReportData(record, list);
				((ArrayList<Map<String, Object>>)r.get("children")).add(record);
			}
		}
		return r;
	}
	
	
	private List<Map<String,Object>> getChildList(String id,List<Record> list){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (Record record : list) {
			if(id.equals(record.get("upid"))){
				result.add(record.getColumns());
				//list.remove(record);
			}
		}
		return result;
	}
}

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我发起的任务</title>
</head>
<style type="text/css">
html, body {
	margin: 0;
	padding: 0;
	border: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

</style>
<body>
	<div id="panel1" class="nui-panel" title="我发起的任务" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="nui-fit" style="overflow: hidden">
			   	<form id="form1">
				 <table style="table-layout: fixed;" class="search_table" width="100%">
					<tr>
							<th align="right">任务开始日期（始）：</th>
							<td align="left">
								<input id="stime" style="width: 165px;" name="stime" class="nui-datepicker" allowInput="false" onvaluechanged="retchange" /></td>
							<th align="right">任务开始日期（止）：</th>
							<td align="left">
								<input id="etime" style="width: 165px;" name="etime" class="nui-datepicker" allowInput="false" onvaluechanged="retchange" /></td>
							<th align="right">任务名称：</th>
							<td align="left">
							<input id="missionName" name="missionName" class="nui-textbox" style="width: 165px;" emptyText=""  />
							</td>
					</tr>
					<tr>
						<th align="right">任务状态：</th>
						<td align="left">
							<input id="missionIssueStatus" name="missionIssueStatus"  style="width: 165px;"  class="nui-combobox" url="<%=root%>/param/getParam?key=yygl_mission_status" 
								valueField="val" textField="remark"  emptyText="请选择..."  multiSelect="false" />
						</td>
						<th align="right">任务类型：</th>
						<td align="left">
							<select id="missionType" name="missionType"
									style="width: 165px" class="nui-combobox" value=""
									emptyText="请选择..." showNullItem="false" nullItemText="全部">
								<option value="">请选择...</option>
								<option value="0">周期任务</option>
								<option value="1">一次性任务</option>
							</select>
						</td>
						<th align="right">任务分类：</th>
						<td align="left">
							<input id="missionFlag" name="missionFlag" class="nui-combobox" style="width: 165px;" url="<%=root%>/param/getParam?key=yygl_mission_flag"
								   valueField="val" textField="remark" emptyText="请选择..." allowInput="true" valueFromSelect="true" showNullItem="true" />
						</td>
					</tr>
					</table>
				</div>
			</form>
		</div>
	
	<div class="nui-toolbar" style="padding: 2px; border-bottom: 0;">
		<table style="width: 100%;">
			<tr>
				<td>
					<a class="nui-button" id="searchBtn" onclick="onSearch()">查询</a>
					<a class="nui-button" onclick="reset()">重置</a>
					<a class="nui-button" id="revokeBtn" onclick="revokeTask()">撤销</a>
					<a class="nui-button" id="stopBtn" onclick="stopTask()">终止</a>
					<a class="nui-button" id="viewSummaryDateBtn" onclick="getViewSummarData()">查看汇总结果</a>
					<a class="nui-button" id="implementSituationBtn" onclick="taskImplemSituation()">任务接收人执行情况</a>
					<a class="nui-button" id="downBtn" onclick="download()" >下载</a>
				</td>
				
			</tr>
		</table>
	</div>
	<div class="nui-fit">
		<div id="datagrid1" dataField="datas" class="nui-datagrid" style="width: 100%; height: 95%;" url="<%=root%>/missionView/getList"
			autoEscape="false" showEmptyText="true" emptyText="没有对应的记录" pageSize="10" allowCellWrap="true" sortMode="client" allowUnselect="false"
			allowCellSelect="false" allowResize="true" allowSortColumn="true">
			<div property="columns">
				<div field="MISSION_ISSUE_ID" visible="false">id</div>
				<div field="MISSION_CONFIG_ID" visible="false">id</div>
				<div type="checkcolumn" name="checkcolumn"></div>
				<div headerAlign="center" type="indexcolumn">序号</div>
				<div field="mission_name" width="100px" allowSort="true" headerAlign="center" align="center" >任务名称</div>
				<div field="mission_flag1" width="60px" allowSort="true" headerAlign="center" align="center" >任务分类</div>
				<div field="remark" width="40px" allowSort="true" headerAlign="center" align="center" >任务状态</div>
				<div field="mission_strat_ymd" width="60px"  allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd HH:mm:ss">任务开始时间</div>
				<div field="mission_end_ymd" width="60px" allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd HH:mm:ss">任务结束时间</div>
				<!-- <div field="FEEDBACK_WBS" width="70px" allowSort="true" headerAlign="center" align="center" renderer="getFeedBackWBS">任务反馈率 <br/>（完成进度）</div>
				<div field="COUNT1" visible="false">任务接收人数</div>
				<div field="COUNT2" visible="false" >已反馈人数</div> -->
				<div field="id" renderer="operateRenderer" headerAlign="center" align="center" header="操作">
				</div>
			</div>
		</div>
	<div><font style="color:red;">请注意：任务反馈率（完成进度）不会影响任务状态，只有到达任务截至时间的任务或者终止的任务才会被更新为“已结束”状态。<br/></font></div>
	</div>
	<iframe id="callbackIframe" name="callbackIframe" style="display: none;"></iframe>
</body>
</html>
<script type="text/javascript">
	/**
	 * 初始化处理
	 */
	$G.parse(); //将页面的标签转换为可$Gui可处理的对象
	var grid = $G.get("datagrid1"); //获取datagrid1标签
	var form = $G.getForm("form1");
	grid.load();
	/**
	 * 根据任务的开始/结束时间，查询该时间范围内生成的任务
	 */
	function onSearch() {
		var stime = $G.get("stime").getValue(); // 开始时间
		var etime = $G.get("etime").getValue(); // 结束时间
		var missionName = $G.get("missionName").getValue(); // 任务名称
		var missionIssueStatus= $G.get("missionIssueStatus").getValue();
		var missionType = $G.get("missionType").getValue();
		var missionFlag = $G.get("missionFlag").getValue();
		// datagrid重新加载
		grid.load({
			stime : stime ,
			etime : etime ,
			missionName : missionName ,
			missionIssueStatus : missionIssueStatus,
            missionType : missionType,
            missionFlag : missionFlag
		});
	}

	/**
	 * 结束时间必须大于开始时间
	 */
	function retchange() {
		var starttime = $G.get("stime").getValue();
		starttime = starttime.substring(0, 4) + starttime.substring(5, 7)
				+ starttime.substring(8, 10);
		var endtime = $G.get("etime").getValue();
		endtime = endtime.substring(0, 4) + endtime.substring(5, 7) + endtime.substring(8, 10);

		if ((starttime != "" && starttime != null)
				&& (endtime != "" && endtime != null)) {
			if (starttime > endtime) {
				m$Galert("结束时间不能小于开始时间,请重新选择！");
				$G.get("searchBtn").disable();
			} else {
				$G.get("searchBtn").enable();
			}
		}
	}
	
	/**
	 * 撤销任务
	 */
	function revokeTask(){
		var rows = grid.getSelecteds();
		if (rows.length == 1) {
			$G.confirm("确定要撤销本次任务吗？", "撤销任务", function(action) {
				if (action == "ok") {
					$.ajax({
						url : "<%=root%>/missionView/revokeTasks?missionIssueID="+rows[0].id,
						success : function(text) {
							if (text == "1000") {
								$G.alert("无此权限，请与管理员联系！");
								return;
							}
							var flag = text;
							if (Number(flag) >= 0) {
								$G.alert("任务撤销成功！");
							} else {
								$G.alert("任务撤销失败！");
							}
							grid.reload();
						}
					});
				}
			})
		} else {
			$G.alert("至少从任务一览中，选择一条数据。");
		}
	}
	
	/**
	 * 终止任务
	 */
	function stopTask(){
		var rows = grid.getSelecteds();
		if(rows[0].mission_issue_status=="05"){
			alert("已撤销的任务不能终止！")
		} else if (rows.length == 1) {
			$G.confirm("确定要终止本次任务吗？", "终止任务", function(action) {
				if (action == "ok") {
					$.ajax({
						url : "<%=root%>/missionView/stopTasks?missionIssueID="+rows[0].id,
						success : function(text) {
							if (text == "1000") {
								$G.alert("无此权限，请与管理员联系！");
								return;
							}
							var flag = text;
							if (Number(flag) >= 0) {
								$G.alert("任务终止成功！");
							} else {
								$G.alert("任务终止失败！");
							}
							grid.reload();
						}
					});
				}
			})
		} else {
			$G.alert("至少从任务一览中，选择一条数据。");
		}
	}
	
	
	/**
	 * 任务接收人执行情况
	 * 
	 *
	 */
	function taskImplemSituation(){
		var row = grid.getSelected();
		if(row){
			var titleName = "";
			titleName = "任务接收人执行情况";
			if(row.mission_issue_status=="05"){
				$G.alert("已撤销的任务不能查看任务接收人执行情况！ ")
			} else {
				$G.open({
					url : "<%=root%>/missionView/taskImplementSituation?id="+row.id+"&missionflag="+row.mission_flag,
					title : titleName,
					width : "80%",
					height : "90%",
					onload : function() {
						/* var iframe = this.getIFrameEl();
						var data = {missionConfigId:row.mission_config_id,};
						iframe.contentWindow.SetData(data); */
					}
				});
			}
	    } else {
	   		$G.alert("请先选中一条记录！");
	    }
     }
	
	
	/**
	 * 打开查看汇总页面
	 */
	 function getViewSummarData(){
	    var row = grid.getSelected();
	    console.log(row)
	    if(row){
	    	var missionflag = row.mission_flag;
			var url = "<%=root%>/missionView/viewSummaryData?id="+row.id+"&missionIssueId="+row.id+"&type=issue_table"+"&missionflag="+missionflag+"&pidtype="+3;
			var bizParams = {id:row.id,missionIssueId:row.id,missionflag:missionflag}
			$G.showmodaldialog("查看汇总结果", url,"80%", "90%", bizParams, function(data){
		    });
	    } else {
	    	$G.alert("请先选中一条记录！");
	    }
	    
	}
	
	
    //下载我发起的任务列表
	function download(){
		 var createfrom = form.getData();
		 createfrom.pageSize = 9999999;
		 createfrom.pageIndex = 0;
		 createfrom.columns = "任务名称,任务分类,任务状态,任务开始时间,任务结束时间";
		 createfrom.headers = "mission_name,mission_flag1,remark,mission_strat_ymd,mission_end_ymd";
		 createfrom["form"] = {action:"<%=root%>/missionView/downloadTaskList"};
		 downFile(createfrom);
    } 
	
	 //操作
	 function operateRenderer(e){
			var id = e.row.mission_config_id;
			var missionIssueId = e.row.id;
			var missionname = e.row.mission_name;
			var missionflag = e.row.mission_flag;
			var obj = " <a class=\"nui-button\" href='javascript:trackTask(\""+id+"\",\""+missionIssueId+"\",\""+missionname+"\")' style='text-decoration:none'>任务跟踪</a>";
			 if(missionflag == 0){
				 obj += "   <a class=\"nui-button\" href='javascript:downTeplment(\""+id+"\")' style='text-decoration:none'>下载模板</a>"; 
			 }
			return obj;
		}
	 
	 
	//下载模板
	function downTeplment(id){
		 var createfrom = {};
		 createfrom.id = id;
		 createfrom["form"]={action: "<%=root%>/missionconfig/downloadTemplet" }; 
		 downFile(createfrom);
	}
	
	//页面表单提交后，后台返回时调用的函数
    function callback(msg){
  	  	$G.alert(msg);
    }
	
	 // 任务跟踪
	 function trackTask(id,missionIssueId,missionname) {
	   	var titleName = "";
		titleName = "任务跟踪";
		$G.open({
			url : "<%=root%>/missionView/trackTasks?id="+missionIssueId+"&missionname="+missionname+"&type=issue_table",
			title : titleName,
			width :" 100%",
			height : "100%",
			onload : function() {
			}
		});
	 }
	
	
	/**
	 * 反馈进度
	 */
	function getFeedBackWBS(e){
		var val = e.value;
		var row = e.record;
		var total = row.COUNT1;
		var feedbacked = row.COUNT2;
		var percent = 0;
		if (row.FEEDBACK_FLAG == null ){
			if (total != 0 ){
				return Math.round((feedbacked / total) * 100,2) + "%";
			}
		} else if (row.FEEDBACK_FLAG == "1"){
			return "该任务无需反馈";
		}
	}
	
	
	/**
	 * 清空查询条件
	 */
	function reset(){
		form.reset();
	}
	
	
</script>
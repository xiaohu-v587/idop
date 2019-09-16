<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@include file="/common/nuires.jsp" %>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待处理的任务</title>
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
	<div id="panel1" class="nui-panel" title="待处理任务" style="width:100%;" showToolbar="false" showCollapseButton="false"
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
	<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;">
		<table style="width: 100%;">
			<tr>
				<td>
					<a class="mini-button" id="searchBtn" onclick="onSearch()">查询</a>
					<a class="mini-button" onclick="reset()">重置</a>
					<a class="mini-button" id="revokeBtn" onclick="getViewSummarData()">查看汇总结果</a>
					<a class="mini-button" id="implementSituationBtn" onclick="taskImplemSituation()">任务接收人执行情况</a>
					<!-- <a class="mini-button" id="signBtn" onclick="confirmTasks()">确认签收</a> -->
					<a class="mini-button" id="feedBackBtn" onclick="feedBackTasks()">无需反馈</a>
					<a class="mini-button" id="downBtn" onclick="download()" >下载</a>
				</td>
				
			</tr>
		</table>
	</div>
	<div class="mini-fit">
		<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width: 100%; height: 95%;" url="<%=root%>/missionForUser/getList"
			autoEscape="false" showEmptyText="true" emptyText="没有对应的记录" pageSize="10" allowCellWrap="true" sortMode="client" allowUnselect="false"
			allowCellSelect="false" allowResize="true" allowSortColumn="true" onselectionchanged="onSelectionChanged">
			<div property="columns">
				<div field="MISSION_ISSUE_ID" visible="false">id</div>
				<div field="MISSION_CONFIG_ID" visible="false">id</div>
				<div type="checkcolumn" name="checkcolumn"></div>
				<div headerAlign="center" type="indexcolumn">序号</div>
				<div field="mission_name" width="100px" allowSort="true" headerAlign="center" align="center" >任务名称</div>
				<div field="mission_flag1" width="60px" allowSort="true" headerAlign="center" align="center" >任务分类</div>
				<div field="remark" width="40px" allowSort="true" headerAlign="center" align="center" >任务状态</div>
				<div field="mission_strat_ymd" width="60px"  allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd hh:mm:ss">任务开始时间</div>
				<div field="mission_end_ymd" width="60px" allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd hh:mm:ss">任务结束时间</div>
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
	$G.parse(); //将页面的标签转换为可miniui可处理的对象
	var grid = $G.get("datagrid1"); //获取datagrid1标签
	var form = $G.getForm("form1");
	grid.load();
	onSelectionChanged();
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
	
	//当选中grid中的行时判断按钮是否显示   
    function onSelectionChanged(e){  
   		var row = grid.getSelected();
   		if(row){
   	   		if(row.mission_isfeedback == "1"){
   	   			$G.get("feedBackBtn").enable();
   	   		} else{
   	   			$G.get("feedBackBtn").disable();
   	   		}
   		} else {
   	   		$G.get("feedBackBtn").disable();
   		}
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
				$G.alert("结束时间不能小于开始时间,请重新选择！");
				$G.get("searchBtn").disable();
			} else {
				$G.get("searchBtn").enable();
			}
		}
	}
	
	/**
	 * 任务接收人执行情况
	 */
	function taskImplemSituation(){
		var row = grid.getSelected();
		console.log(row)
		if(row){
	    	var titleName = "";
			titleName = "任务接收人执行情况";
			if(row.user_mission_status=="05"){
				$G.alert("已撤销的任务不能查看任务接收人执行情况！ ")
			} else {
				$G.open({
					url : "/missionForUser/taskImplementSituation?id="+row.mission_issue_id+"&upMissionId="+row.id+"&missionflag="+row.mission_flag,
					title : titleName,
					width : "80%",
					height : "90%",
					onload : function() {
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
	 function getViewSummarData(viewID,configID){
	    var row = grid.getSelected();
	    console.log(row)
	    var missionflag = row.mission_flag;
	    if(row){
			var url = "<%=root%>/missionForUser/viewSummaryData?id="+row.id+"&missionIssueId="+row.mission_issue_id+"&type=user_table"+"&missionflag="+missionflag;
			var bizParams = {id:row.id,missionIssueId:row.mission_issue_id,missionflag:missionflag}
			$G.showmodaldialog("查看汇总结果", url,"80%", "90%", bizParams, function(data){
		    });
	    } else {
	    	$G.alert("请先选中一条记录！");
	    }
	}
	
	
    //下载待处理任务列表
	function download(){
		 var createfrom = form.getData();
		 createfrom.pageSize = 9999999;
		 createfrom.pageIndex = 0;
		 createfrom.columns = "任务名称,任务分类,任务状态,任务开始时间,任务结束时间";
		 createfrom.headers = "mission_name,mission_flag1,remark,mission_strat_ymd,mission_end_ymd";
		 createfrom["form"] = {action:"<%=root%>/missionForUser/downloadTaskList"};
		 downFile(createfrom);
    } 
	
	 //操作
	 function operateRenderer(e){
		 var id = e.row.id;
		 var missionIssueId = e.row.mission_issue_id;
		 var missionname = e.row.mission_name;
		 var missionflag = e.row.mission_flag;
		 var status = e.row.user_mission_status;
		 var missionRequire = e.row.mission_require;
		 /*
		 	1.任务转发后，如果接收人还有未反馈情况时，不允许打开反馈页面，如果都已反馈时，可打开反馈页面！
		 	2.任务转发后，接收人再次转发时，不允许收回
		 */
		 var  obj = "";
		
		 if(isSign(e.row)){
			 obj += "  <a class=\"nui-button\" href='javascript:signTasks()' style='text-decoration:none'>签收</a> " 	 
		 }else{
			 obj += "<a class=\"nui-button\" href='javascript:trackTask(\""+id+"\",\""+missionIssueId+"\",\""+missionname+"\")' style='text-decoration:none'>任务跟踪</a> ";
			 obj += "  <a class=\"nui-button\" href='javascript:retoaction(\""+id+"\",\""+missionIssueId+"\",\""+missionflag+"\",\""+status+"\", \""+missionRequire+"\", \""+e.row.forward_flag+"\")' style='text-decoration:none'>反馈</a>";
			 if(e.row.forward_flag == 0){
				 obj += "  <a class=\"nui-button\" href='javascript:recover(\""+id+"\")' style='text-decoration:none'>收回</a> " 
			 } else if(e.row.forward_flag == 1){
				 obj += "   <a class=\"nui-button\" href='javascript:forwardTask(\""+id+"\",\""+missionIssueId+"\",\""+missionname+"\")' style='text-decoration:none'>转发</a> "; 
			 } 
			 if(missionflag == 0){
				 obj += "   <a class=\"nui-button\" href='javascript:downTeplment(\""+id+"\",\""+missionIssueId+"\")' style='text-decoration:none'>下载模板</a>"; 
			 }
		 }
		 
			return obj;
	}
	// 该记录是否需要签收
	function isSign(row){
		var flag = false; 
		//加入判定 如果当前配置信需要进行签收，但是当前任务未签收时，此时不允许 执行反馈，转发动作，必须先进行签收
		if("1" == row.mission_sign){//需要进行签收动作，此时去检查任务是否已签收
			 if("1" == row.confirm_flag){//还未进行确认
				 flag = true;//需要先进行签收
			 }
		}else{
			 
		}
		return flag;
	}
	 
	 
	//下载模板
	function downTeplment(id,missionIssueId){
		 var createfrom = {};
		 createfrom.id = id;
		 createfrom.missionIssueId = missionIssueId;
		 createfrom["form"]={action: "<%=root%>/missionForUser/downloadTaskTemplet" }; 
		 console.log(createfrom)
		 downFile(createfrom);
	}
	
	//页面表单提交后，后台返回时调用的函数
    function callback(msg){
  	  	$G.alert(msg);
    }
	 
	 //转发任务
	 function forwardTask(id,missionIssueId,missionName){
	    var titleName = "";
		titleName = "任务转发";
		$G.open({
			url : "/missionForUser/forwardTask?id="+id+"&missionIssueId="+missionIssueId+"&missionName="+missionName,
			title : titleName,
			width : 1000,
			height : 520,
			onload : function(action) {
				
			},
			ondestroy: function (action) {
		           	 if(action == "save") {
		           		//$G.alert("转发成功!");
		           		grid.reload(); 
		           	  }else if(action == "cancel") {
		           		  //$G.alert("转发失败!")
		           		//  grid.reload(); 
		           	  }
	            }
		});	 
	 }
	 
	 //收回任务
	 function recover(id){
		if (id) {
			$G.confirm("确定要收回本次任务吗？", "收回任务", function(action) {
				if (action == "ok") {
					$.ajax({
						url : "missionForUser/recoverTasks?id="+id,
						success : function(text) {
							var flag = text;
							if (Number(flag) >= 0) {
								$G.alert("任务收回成功！");
							} else if(Number(flag) == -1){
								$G.alert("任务收回失败！");
							} else if(Number(flag) == -2){
								$G.alert("该任务已被下级转发，无法收回！");
							} else if(Number(flag) == -3){
								$G.alert("该任务未转发给他人或者已收回任务！");
							} else{
								$G.alert("任务收回异常！");
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
	 
	 
	//签收任务
	 function signTasks(){
		 var row = grid.getSelected();
		 console.log(row)
		if (row) {
			$G.confirm("确定要签收本次任务吗？", "是否签收", function(action) {
				if (action == "ok") {
					$.ajax({
						url : "missionForUser/signTasks?id="+row.id,
						success : function(text) {
							var flag = text;
							if (Number(flag) >= 0) {
								$G.alert("任务签收成功！");
							} else {
								$G.alert("任务签收失败！");
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
	
	
	//无需反馈
	 function feedBackTasks(){
		var row = grid.getSelected();
		if (row) {
			$G.confirm("确定要结束本次任务吗？", "提示", function(action) {
				if (action == "ok") {
					$.ajax({
						url : "missionForUser/notFeedBackTasks?id="+row.id,
						success : function(text) {
							var flag = text;
							if (Number(flag) >= 0) {
								$G.alert("任务反馈成功！");
							} else {
								$G.alert("任务反馈失败！");
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
	 
	 
	//跳转反馈页面
	 function retoaction(id,missionIssueId,missionflag,status,missionRequire,forward_flag) {
		var url = "<%=root%>/missionForUser/retoaction?id="+id+"&missionIssueId="+missionIssueId+"&missionflag="+missionflag+"&status="+status+"&missionRequire="+missionRequire;
		var bizParams = {id:id,missionIssueId:missionIssueId,missionflag:missionflag,status:status}
		$.ajax({
			url : "missionForUser/isOpenRetoactionView?id="+id+"&forward_flag="+forward_flag,
			success : function(text) {
				var flag = text;
				if (Number(flag) == 0) {
					var width = "100%",height="100%";
					if(missionflag == "1"){
						width= "60%";
						height = "90%";
					}
					$G.showmodaldialog("任务填报", url,width, height, bizParams, function(data){
						grid.reload();
				    });
				} else {
					$G.alert("当前子任务还有未反馈或退回情况！");
				}
			}
		});
	 }
	 
	
	// 任务跟踪
	 function trackTask(id,missionIssueId,missionname) {
	   	var titleName = "";
		titleName = "任务跟踪";
		$G.open({
			url : "/missionView/trackTasks?id="+id+"&missionname="+missionname+"&type=user_table",
			title : titleName,
			width : "100%",
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
	
	 function CloseWindow(action) {            
     	
         if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
         else window.close();            
     }
     
     function onCancel(e) {
         CloseWindow("cancel");
     }
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务接收人执行情况</title>
</head>
<body>
<div style="border: 0px;">
		<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;" id="inner_querypanel">
			<form id="form1" method="post">
				<input name="pageIndex" class="mini-hidden" value="0" /> 
				<input name="pageSize" class="mini-hidden" value="20" />
				<div style="padding-left: 11px; padding-bottom: 5px;">
					<table style="table-layout: fixed;" id="detailTable">
					<tr>
						<td align="left">任务状态：</td>
						<td align="left" style="width: 130px;">
							<input id="missionIssueStatus" name="missionIssueStatus"  style="width: 120px;"  class="mini-combobox" url="<%=root%>/param/getParam?key=yygl_mission_status" 
								valueField="val" textField="remark"  emptyText="请选择..."  multiSelect="false" />
						</td>
						<td>
						<a class="mini-button" id="searchBtn" onclick="onSearch()">查询</a>
						<a class="mini-button" onclick="reset()">重置</a>
						</td>
					</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	<div class="mini-fit">
		<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width: 100%; height: 95%;" url="<%=root%>/missionForUser/getTaskImplemSituation?id=${id}&upMissionId=${upMissionId}"
			autoEscape="false" showEmptyText="true" emptyText="没有对应的记录" pageSize="10" allowCellWrap="true" sortMode="client" allowUnselect="false" multiSelect="true" 
			allowCellSelect="false" allowResize="true" allowSortColumn="true">
			<div property="columns">
				<div field="MISSION_ISSUE_ID" visible="false">id</div>
				<div field="MISSION_CONFIG_ID" visible="false">id</div>
				<div type="checkcolumn" name="checkcolumn"></div>
				<div headerAlign="center" type="indexcolumn">序号</div>
				<div field="name" width="60px" allowSort="true" headerAlign="center" align="center" >处理人</div>
				<div field="mission_strat_ymd" width="60px"  allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd ">任务转发时间</div>
				<div field="remark" width="60px" allowSort="true" headerAlign="center" align="center" >任务状态</div>
				<div headerAlign="left" renderer="operateRenderer"  width="150px" align="left" header="操作">
			</div>
		</div>
	</div>
   </div>
   <div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<form id="down" action="<%=root%>/missionFileReport/downloadFilesToZip?type=impleStitu" method="post" target="callbackIframe">
				<input id="id" name="id" class="nui-hidden" /> 
				<input id="userno" name="userno" class="nui-hidden" /> 
				<a id="sendMessageBtn" class="nui-button"  onclick="sendUrgMessage()">发送催办消息</a>
				<a id="btnDownload" class="nui-button" onclick="onDownloads()" style="clear:both;">打包导出反馈文件</a>
				<a id="btnCancle" class="nui-button" onclick="onCancel()" style="clear:both;">关闭</a>
		</form>   
	</div>
</body>
</html>
<script type="text/javascript">
/**
 * 初始化处理
 */
mini.parse(); //将页面的标签转换为可miniui可处理的对象
var grid = mini.get("datagrid1"); //获取datagrid1标签
var form = mini.getForm("form1");
grid.load();
isHide();

	//操作
	function operateRenderer(e){
		var id = e.row.id;
		var userno = e.row.user_no;
		var missionName = e.row.mission_name;
		var missionIssueId = e.row.mission_issue_id;
		var userMissionStatus = e.row.user_mission_status;
		var forwardFlag = e.row.forward_flag;
		var missionflag = "${missionflag}";
		
		var pidtype;
		//判断查看的附件是反馈附件还是退回说明附件  0--反馈  1--退回 
		//模板附件--1   2--上传的说明附件   3--反馈的附件   4--退回附件
		if(userMissionStatus == "02"){
			pidtype = "3";
		} else if(userMissionStatus == "04"){
			pidtype = "4";
		}
		var obj = "<a class=\"nui-button\" href='javascript:show(\""+id+"\",\""+missionIssueId+"\",\""+userno+"\",\""+pidtype+"\")' style='text-decoration:none'>查看</a>  "
				+"<a class=\"nui-button\" href='javascript:reciprocalRecords(\""+id+"\")' style='text-decoration:none'>往来记录</a> ";
		if(userMissionStatus == "02" && "${type}" != "finshUser_table"){
			obj += "<a class=\"nui-button\" href='javascript:goBack(\""+id+"\",\""+missionName+"\")' style='text-decoration:none'>退回</a> "
		} 
		if(forwardFlag == "0"){
			obj += " <a class=\"nui-button\" href='javascript:distributeSituation(\""+id+"\",\""+missionIssueId+"\",\""+missionflag+"\")' style='text-decoration:none'>分发情况</a>"
		}
		if(missionflag == "0"){
			obj += " <a class=\"nui-button\" href='javascript:downs(\""+id+"\",\""+missionIssueId+"\")' style='text-decoration:none'>下载</a>";
		}
		
		return obj;
	}
	
	
	/**
	 * 打开查看反馈页面
	 */
	 function show(id,missionIssueId,userno,pidtype){
		var missionflag = "${missionflag}";
		var url = "<%=root%>/missionForUser/retoactionView?id="+id+"&missionIssueId="+missionIssueId+"&missionflag="+missionflag+"&userno="+userno+"&pidtype="+pidtype;
		var bizParams = {id:id,missionIssueId:missionIssueId,missionflag:missionflag,userno:userno}
		var width = "80%",height="90%";
		if(missionflag == "1"){
			width= "50%";
			height = "50%";
		}
		$G.showmodaldialog("查看反馈结果", url,width, height, bizParams, function(data){
	    });
	}
	
	//下载后台保存数据与查看时一样
	function downs(id,missionIssueId){
		 var createfrom = {};
		 createfrom.id = id;
		 createfrom.missionIssueId = missionIssueId;
		 createfrom["form"]={action: "<%=root%>/missiondatareport/downloadRetoaction" }; 
		 downFile(createfrom);
	}
	
	
	//跳转至退回页面
	function goBack(id,missionName){
		var titleName = "";
		titleName = "退回";
		$G.open({
			url : "<%=root%>/missionView/goBack?id="+id+"&missionName="+missionName,
			title : titleName,
			width : 800,
			height : 500,
			onload : function() {
				/* var iframe = this.getIFrameEl();
	            var data = { data:id};
	            iframe.contentWindow.SetData(data); */
			},
			ondestroy: function (action) {
		           	 if(action == "save") {
		           		  grid.reload(); 
		           	  }else if(action == "cancel") {
		           		  grid.reload(); 
		           	  }
	            }
		});
	}
	
	//跳转至查看往来记录页面
	function reciprocalRecords(id) {
	   	var titleName = "";
		titleName = "查看往来记录";
		$G.open({
			url : "/missionView/reciprocalRecords?id="+id,
			title : titleName,
			width : 800,
			height : 600,
			onload : function() {
			}
		});
	}

	//跳转至查看分发情况页面
	function distributeSituation(id,missionIssueId,missionflag) {
	   	var titleName = "";
		titleName = "查看分发情况";
		$G.open({
			url : "/missionView/distributeSituation?id="+id+"&missionIssueId="+missionIssueId+"&missionflag="+missionflag,
			title : titleName,
			width : 950,
			height : 600,
			onload : function() {
			}
		});
	}
	
	//发送催办消息 
	function sendUrgMessage(){
		var rows = grid.getSelecteds();
		var titleName = "";
		titleName = "发送催办消息";
		var ids = "";
		var usernos = "";
		for (var i = 0;i<rows.length;i++){
			if(i < rows.length-1){
				ids += rows[i].id+",";
				usernos += rows[i].user_no+",";
			} else {
				ids += rows[i].id;
				usernos += rows[i].user_no;
			}
		}
		if (rows.length == 0) {
			$G.alert("请从未反馈列表中选择短信接收人（可多选）。");
			return;
		}else{
			$G.open({
				url : "/missionView/sendUrgeMessage?id="+ids+"&usernos="+usernos,
				title : titleName,
				width : 600,
				height : 275,
				onload : function() {
				}
			});
		}
	}
	
	
	// 打包下载反馈文件
	function onDownloads(){
		 var createfrom = {id:"${upMissionId}",type:"user_table",missionflag : "${missionflag}"};
		 createfrom["form"] = {action:"<%=root%>/missionView/downloadPackRetoaction"};
		 downFile(createfrom);
	}
	
	//页面表单提交后，后台返回时调用的函数
    function callback(msg){
  	  	mini.alert(msg);
    }
	
	//查询条件
	function onSearch() {
		var missionIssueStatus= $G.get("missionIssueStatus").getValue();
		// datagrid重新加载
		grid.load({
			missionIssueStatus : missionIssueStatus,
		});
	}
	
	
	function isHide(){
		var type = "${type}";
		if(type == "finshUser_table"){
			$G.get("sendMessageBtn").disable();
		} else {
			$G.get("sendMessageBtn").enable();
		}
	}
	
	/**
	 * 清空查询条件
	 */
	function reset(){
		form.reset();
	}
	
	/**
	 * 取消按钮按下时的事件
	 */
	function onCancel(e) {
		CloseWindow("cancel");
	}
	/**
	 * 关闭窗体的事件
	 */
	function CloseWindow(action) {
		if (window.CloseOwnerWindow)
			return window.CloseOwnerWindow(action);
		else
			window.close();
	}
</script>
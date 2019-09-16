<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看分发情况</title>
</head>
<body>
<div style="border: 0px;">
		<div class="nui-toolbar" style="padding: 2px; border-bottom: 0;" id="inner_querypanel">
			<form id="form1" method="post">
				<input name="pageIndex" class="nui-hidden" value="0" /> 
				<input name="pageSize" class="nui-hidden" value="20" />
				<div style="padding-left: 11px; padding-bottom: 5px;">
					<table style="table-layout: fixed;" id="detailTable">
					<tr>
						<td align="left">任务状态：</td>
						<td align="left" style="width: 130px;">
							<input id="missionIssueStatus" name="missionIssueStatus"  style="width: 120px;"  class="nui-combobox" url="<%=root%>/param/getParam?key=yygl_mission_status" 
								valueField="val" textField="remark"  emptyText="请选择..."  multiSelect="false" />
						</td>
						<td>
						<a class="nui-button" id="searchBtn" onclick="onSearch()">查询</a>
						<a class="nui-button" onclick="reset()">重置</a>
						</td>
					</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	<div class="nui-fit">
		<div id="datagrid1" dataField="datas" class="nui-datagrid" style="width: 100%; height: 95%;" url="<%=root%>/missionView/getDistributeSituationList?id=${id}&missionIssueId=${missionIssueId}"
			autoEscape="false" showEmptyText="true" emptyText="没有对应的记录" pageSize="10" allowCellWrap="true" sortMode="client" allowUnselect="false"
			allowCellSelect="false" allowResize="true" allowSortColumn="true">
			<div property="columns">
				<div field="MISSION_ISSUE_ID" visible="false">id</div>
				<div field="MISSION_CONFIG_ID" visible="false">id</div>
				<div type="checkcolumn" name="checkcolumn"></div>
				<div headerAlign="center" type="indexcolumn">序号</div>
				<div field="name" width="150px" allowSort="true" headerAlign="center" align="center" >处理人</div>
				<div field="mission_strat_ymd" width="60px"  allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd ">任务转发时间</div>
				<div field="remark" width="40px" allowSort="true" headerAlign="center" align="center" >任务状态</div>
				<div headerAlign="center" renderer="operateRenderer"  width="120px" align="center" header="操作">
				</div>
			</div>
		</div>
	</div>
	
	<div class="nui-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		
		<form id="down" action="<%=root%>/missionFileReport/downloadFilesToZip?type=impleStitu" method="post" target="callbackIframe">
				<input id="id" name="id" class="nui-hidden" /> 
				<input id="userno" name="userno" class="nui-hidden" /> 
				<a id="btnDownload" class="nui-button" onclick="onDownloads()" style="clear:both;">打包导出反馈文件</a>
				<a id="btnCancle" class="nui-button" onclick="onCancel()" style="clear:both;">关闭</a>
		</form>  
	</div>
</body>
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


/**
 * 根据任务的开始/结束时间，查询该时间范围内生成的任务
 */
function onSearch() {
	var missionIssueStatus= $G.get("missionIssueStatus").getValue();
	// datagrid重新加载
	grid.load({
		missionIssueStatus : missionIssueStatus
	});
}

//操作
function operateRenderer(e){
	console.log(e.row)
	var id = e.row.id;
	var missionIssueId = e.row.mission_issue_id;
	var userno = e.row.user_no;
	var userMissionStatus = e.row.user_mission_status
	var forwardFlag = e.row.forward_flag;
	var missionflag = "${missionflag}";
	var pidtype;
	if(userMissionStatus == "02"){
		pidtype = "3";
	} else {
		pidtype = "4";
	}
	var obj = "<a class=\"nui-button\" href='javascript:show(\""+id+"\",\""+missionIssueId+"\",\""+missionflag+"\",\""+userno+"\",\""+pidtype+"\")' style='text-decoration:none'>&nbsp;&nbsp;查看&nbsp;&nbsp;</a>";
	if(missionflag == "0"){
		obj += " <a class=\"nui-button\" href='javascript:downs(\""+id+"\",\""+missionIssueId+"\")' style='text-decoration:none'>下载</a>";
	}
	if(forwardFlag == "0"){
		obj += " <a class=\"nui-button\" href='javascript:distributeSituation(\""+id+"\",\""+missionIssueId+"\")' style='text-decoration:none'>&nbsp;&nbsp;分发情况&nbsp;&nbsp;</a>"
	}
	return obj;
}

//跳转至查看分发情况页面
function distributeSituation(id,missionIssueId) {
   	var titleName = "";
	titleName = "查看分发情况";
	$G.open({
		url : "/missionView/distributeSituation?id="+id+"&missionIssueId="+missionIssueId+"&missionflag=${missionflag}",
		title : titleName,
		width : 950,
		height : 600,
		onload : function() {
		}
	});
}

/**
 * 打开查看页面
 */
 function show(id,missionIssueId,missionflag,userno,pidtype){
	var url = "<%=root%>/missionForUser/retoactionView?id="+id+"&missionIssueId="+missionIssueId+"&missionflag="+missionflag+"&userno="+userno+"&pidtype="+pidtype;
	var bizParams = {id:id,missionIssueId:missionIssueId}
	$G.showmodaldialog("查看反馈结果", url,"60%", "80%", bizParams, function(data){
    });
}

//打包下载反馈文件
function onDownloads(){
	 var createfrom = {id:"${id}",type:"user_table",missionflag : "${missionflag}"};
	 createfrom["form"] = {action:"<%=root%>/missionView/downloadPackRetoaction"};
	 downFile(createfrom);
}


//下载后台保存数据与查看时一样
function downs(id,missionIssueId){
	 var createfrom = {};
	 createfrom.id = id;
	 createfrom.missionIssueId = missionIssueId;
	 createfrom["form"]={action: "<%=root%>/missiondatareport/downloadRetoaction" }; 
	 downFile(createfrom);
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
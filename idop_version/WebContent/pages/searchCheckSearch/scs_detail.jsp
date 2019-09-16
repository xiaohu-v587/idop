<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询查复查询-详情页面</title>
<%@include file="/common/nuires.jsp" %>
<style type="text/css">
		.labelname{
			width:100px;
		}
		</style>
</head>
<body>
<div class="mini-tabs" id="tabs1" onactivechanged="onActivechanged()" style="height: 100%; width: 100%;" activeIndex="0" dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
	<div id="tab1" title="查询查复详情">
		<input name="pageType" class="nui-hidden"/>      
			<form id="form1" method="post">
				<input name="flownum" class="nui-hidden"/>
	           	<table id="detailTable"  width="98%">
	               	<tr>
	                   	<td align="right" class="labelname">业务模块：</td>
	                   	<td align="left">    
	                       	<input name="biz_module" class="nui-textbox" allowInput="false" style="width:90%;"/>
	                   	</td>
	                   	<td align="right" class="labelname">预警名称：</td>
	                    <td align="left">    
	                        <input name="warning_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
	                    </td>
	               	</tr>
					<tr>
						<td align="right" class="labelname">查询编号：</td>
	                    <td align="left">    
	                        <input name="search_no" class="nui-textbox" allowInput="false" style="width:90%;"/>
	                    </td>
	                   	<td align="right" class="labelname">查询日期：</td>
	                   	<td align="left">    
							<input name="search_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
						</td>
					</tr>
					<tr>
						<td align="right" class="labelname">查询截止日期：</td>
	                   	<td align="left">    
							<input name="search_enddate" class="nui-textbox" allowInput="false" style="width:90%;"/>
						</td>
	                    <td align="right" class="labelname" allowInput="false">查询机构：</td>
						<td align="left">    
	                       	<input name="search_org" class="nui-textbox" allowInput="false" style="width:90%;"/>
	                   	</td> 
					</tr>
					<tr>
	                    <td align="right" class="labelname">查询人：</td>
						<td align="left">    
	                       	<input name="searcher" class="nui-textbox" allowInput="false" style="width:90%;"/>
	                   	</td> 
	                   	<td align="right" class="labelname">查复日期：</td>
				        <td align="left">								
				        	<input name="check_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
				        </td>
					</tr>
					<tr>
						<td align="right" class="labelname">查复机构：</td>
				        <td align="left">								
				          	<input name="check_org" class="nui-textbox" allowInput="false" style="width:90%;"/>
				        </td>
				        <td align="right">查复人：</td>
						<td align="left">
							<input name="checker" class="nui-textbox" allowInput="false" style="width:90%;"/>
						</td>
					</tr>
					<tr>
						<td align="right" class="labelname">查询查复状态：</td>
				        <td align="left">								
				          	<input name="sc_status" class="nui-textbox" allowInput="false" style="width:90%;"/>
				        </td>
				         
					</tr>
					
					<tr>
					<td align="right" class="labelname">交易信息：</td>
					<td align="left" colspan="3">
					<div class ="nui-fit" width="820" height="240px">
						<div id="datagrid" dataField="data" class="mini-datagrid" style="width:96%;height:240px;" sortMode="client" allowUnselect="false"
						oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root %>/warning/getDetailDatas"
						autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
						pageSize="5">
							<div id="detailColumns" property="columns"> 
							</div>
					   	</div> 
					   	</div>
					</td>
				</tr>
				<tr>
				<td align="right" valign="top" >已上传附件：</td>
					<td colspan="3"> 
						<span id="files"  style="color:#F00"></span>
						<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:96%;height:220px;" sortMode="client" allowUnselect="false"
									     oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root%>/common/queryFiles"
									    autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" 
									    ondrawcell="ondrawcell" pageSize="5">
					        <div property="columns">                    
								<div field="id" visible="false" ></div>
								<div field="pid" visible="false" >任务父id</div>
								<div field="filename" headerAlign="center" align="center" >附件名称</div>
					            <div field="createdatef" headerAlign="center" align="center" >上传日期</div>                
					            <div field="proc" headerAlign="center" align="center" >操作</div>
					        </div>
   						</div> 
					</td>
				</tr>
				<tr>
					<td align="right" class="labelname">查询事项：</td>
					<td align="left" colspan="3">
						<input name="search_matter" class="nui-textarea" style="width:96%;height:80px;" allowInput="false" ></input>
					</td>
				</tr>
				<tr>
					<td align="right" class="labelname">查复说明：</td>
					<td align="left" colspan="3">
						<input name="check_remark" class="nui-textarea" style="width:96%;height:80px;"   onvaluechanged="datachanege" allowInput="false"></input>
					</td>
				</tr>
				</table>
				</form>
				<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
				<a class="nui-button" onclick="onCancel">返回</a>  
			</div> 
	</div>
	<div id="tab2" title="查询查复流转" name="check_process" >
		<div class="nui-fit">
	    	<div id="datagrid2" class="nui-datagrid" url="<%=root%>/searchCheck/getCheckList" style="width: 100%;height: 90%;" 
	    		allowRowSelect="false" allowHeaderWrap="true" allowCellWrap="true">
		        <div property="columns">   
		       		<div field="search_date" width="15%" dateFormat="yyyy-MM-dd " headerAlign="center"  align="center">查询日期</div>                
		            <div field="search_name" width="10%" headerAlign="center" align="center">查询人</div>         
		            <div field="check_date" width="15%" dateFormat="yyyy-MM-dd " headerAlign="center"  align="center">查复日期</div>                
		            <div field="check_name" width="10%" headerAlign="center" align="center">查复人</div>		            
		            <div field="action" width="15%" headerAlign="center"  align="center" renderer="">状态</div>
		            <div id="remarks" field="remark" width="65%" headerAlign="center"  align="center" >说明</div>
		        </div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$G.parse();
var form = $G.getForm("form1");
var grid = mini.get("datagrid");
var grid1 = mini.get("datagrid1");
var grid2 = mini.get("datagrid2");
var grid3 = mini.get("datagrid3");
var tabs = $G.get("tabs1");


function setData(data){
	var infos = $G.clone(data);
	var pageType=infos.pageType;
	$G.getbyName("pageType").setValue(pageType);
	$G.getbyName("flownum").setValue(infos.flownum);
	if(pageType=="detail"){
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			form.setData(text.record);
		});
		$G.postByAjax({flownum:infos.flownum},"<%=root%>/searchCheckSearch/getDetail",ajaxConf);
	}
	getDetailHeader(infos.flownum);
	grid1.load({pid: infos.flownum});
}

//获取明细header
function getDetailHeader(check_flownum){
	var url = "<%=root%>/warning/getDetailHeader";
	var ajaxConf = new GcdsAjaxConf();
	ajaxConf.setIsShowProcessBar(false);
	ajaxConf.setIsShowSuccMsg(false);
	ajaxConf.setIsAsync(false);
	ajaxConf.setSuccessFunc(function(text){
		grid.set({columns:text.headers});
		grid.load({check_flownum:check_flownum});
	});
	ajaxConf.postByAjax({check_flownum:check_flownum}, url);
}

function onActivechanged(e){
	var tab = tabs.getActiveTab();
	if(tab.name == "check_process")grid2.load({check_flownum: $G.getbyName("search_no").getValue()});
}

function ondrawcell(e){
	if(e.field == "proc") { // 操作
	   	e.cellStyle="text-align:center";
		e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURIComponent(e.record.filename,'utf-8')+"&filePath="+encodeURIComponent(e.record.filepath,'utf-8')+"' target='_blank'>查看</a>";
	 }
}

function onCancel(e) {
	$G.closemodaldialog("cancel");
}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件类型汇总</title>
</head>
<body>
<div class="mini-fit">
		<div id="filelist" dataField="datas" class="nui-datagrid" style="width:100%;height:90%;" sortMode="client" 
			allowUnselect="false" autoEscape="false"  showEmptyText="true" emptyText="没有对应的附件信息" 
			ondrawcell="filedraw_1" pageSize="20" url="<%=root %>/missionFileReport/queryFiles?id=${id}&type=${type}&pidtype=3">
	        <div property="columns">
	        	<div type="indexcolumn" align="center" headerAlign="center">序号</div>                    
	            <div field="id" visible="false" ></div>
				<div field="pid" visible="false" >任务父id</div>
				<div field="filename" headerAlign="center" align="center" allowsort="true">反馈附件名称</div>
				<div field="username" headerAlign="center" align="center" allowsort="true">反馈人</div>
	            <div field="createdate" headerAlign="center" align="center" allowsort="true" renderer="renderTime">反馈日期</div>                
	            <div field="yxptid"  visible="false"></div>                
	            <div field="proc" headerAlign="center" align="center" >操作</div>
	        </div>
		</div>
		<div class="nui-toolbar"  style="text-align: center;padding-top: 20px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<form id="down" action="<%=root%>/missionFileReport/downloadFilesToZip?type=summary" method="post"	target="callbackIframe">
				<input id="id" name="id" class="mini-hidden" /> 
				<a id="btnDownload" class="mini-button" onclick="onDownloads()" style="clear:both;">打包导出反馈文件</a>
				<a id="btnCancle" class="mini-button" onclick="onCancel()" style="clear:both;">关闭</a>
			</form>   
		</div>
</div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("filelist"); //获取datagrid1标签
	grid.load();
	
	function setData(data){
		
	}
	
	function filedraw_1(e){
      	if(e.field == "proc") { // 操作
      		e.cellStyle="text-align:center";
      		e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>";
      	}
    }
	
	function onDownloads(){
		var createfrom = {id:"${id}",type:"issue_table"};
		 createfrom["form"] = {action:"<%=root%>/missionFileReport/downloadSummary"};
		 downFile(createfrom);
	}
	
	 //取消
	 function onCancel(e) {
	     CloseWindow("cancel");
	 }
	
	 // 关闭窗口
	 function CloseWindow(action) {            
	     if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
	     else window.close();            
	 }
	 
	 function renderTime(e){
	    	return renderTimes(e.value)
	}
</script>
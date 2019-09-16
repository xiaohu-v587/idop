<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>附件查看</title>
	<%@ include file="/common/nuires.jsp"%>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<style type="text/css">
	html, body {
		font-size: 12px;
		padding: 0;
		margin: 0;
		border: 0;
		height: 100%;
		overflow: hidden;
	}
	
	#detailTable tr {
		line-height: 30px;
	}
	</style>
</head>
</head>
<body>
	<div class="mini-fit">
		<div id="datagrid1" dataField="datas" class="mini-datagrid"
			style="width: 100%; height: 100%;" sortMode="client"
			allowUnselect="false" oncellclick="" onrowdblclick=""
			onselectionchanged="" onload=""
			url="<%=root%>/common/queryFiles?pid=<%=request.getParameter("id")%>&pidtype=<%=request.getParameter("pidtype")%>" autoEscape="false"
			onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息"
			ondrawcell="ondrawcell" >
			<div property="columns">
				<div field="id" visible="false"></div>
				<div field="pid" visible="false">任务父id</div>
				<div field="filename" headerAlign="center" align="center">附件名称</div>
				<div field="createdate" headerAlign="center" align="center">上传日期</div>
				<div field="yxptid" visible="false"></div>
				<div field="proc" headerAlign="center" align="center">操作</div>
			</div>
		</div>
	</div>

	<div class="mini-toolbar"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnCancle" class="mini-button" iconCls="icon-close"
			onclick="onCancel()">关闭</a>
	</div>
</body>
<script type="text/javascript">
    $G.parse();
    var grid = $G.get("datagrid1");
    grid.load();
	//标准方法接口定义
    function SetData(data) {
    }
    
    function CloseWindow(action) {        
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
    //取消  
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
  	//翻译字段
    function ondrawcell(e){
    	if(e.field == "proc") { // 操作
    		e.cellStyle="text-align:center";
    		e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>";
    	}
    }
</script>

</html>
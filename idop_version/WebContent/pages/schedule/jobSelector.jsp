<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp" %>
<!--
   * 查看所有定时任务Job类
   *
   * @reviewer 
-->
<html>
<head>
    <title>定时任务管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@ include file="/common/nuires.jsp" %>
</head>
<body>
  	<div class="nui-fit">
	  	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" borderStyle="border-left:0;border-bottom:0;border-right:0;" showPager="false"
	  		url="<%= request.getContextPath() %>/schedule/getAllTaskClasses" multiSelect="false" onselectionchanged="onSelectionchanged" sortMode="client">
	        <div property="columns">
	        	<div type="checkcolumn" name="select"></div>
		        <div headerAlign="center" type="indexcolumn">序号</div>
				<div field="rwbm" width="100" headerAlign="center" allowSort="true">任务编码</div>
				<div field="rwlm" width="140" headerAlign="center" allowSort="true">任务类名</div>
	        </div>
	    </div>
   	</div>  
      <div class="nui-toolbar" style="text-align:center;padding-top:8px;padding-bottom:8px;" 
      borderStyle="border-left:0;border-bottom:0;border-right:0;">
      	<a id="btnCancel" class="nui-button" iconCls="icon-close" onclick="onCancel()" style="margin-right:20px;">
      		取消
		</a>
		<a id="btnOk" class="nui-button" iconCls="icon-ok" onclick="onOk()" enabled="false">
			确定
		</a> 
	</div> 
	
	<script type="text/javascript">
		changeButtonPosition($('.nui-toolbar:last'));
	   	$G.parse();
	   	var grid = $G.get("datagrid1");
		grid.load();
	  //选择行变化事件
		function onSelectionchanged(e){
			var sel = e.select;						
			if(sel){
				$G.get("btnOk").enable();
			}else{
				$G.get("btnOk").disable();  
			}
		} 
	   	
	   	function onOk() {
			$G.closemodaldialog("ok");
		}
		
		function onCancel() {
			$G.closemodaldialog("cancel");
		}

        function getData() {
			return grid.getSelected();
        }
    </script>
    
</body>
</html>
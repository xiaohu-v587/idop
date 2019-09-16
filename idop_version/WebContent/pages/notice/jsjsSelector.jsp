<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp" %>
<!--
   * 查看所有接受角色
   *
   * @reviewer 
-->
<html>
<head>
    <title>接收角色选择</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@ include file="/common/nuires.jsp" %>
</head>
<body>
  	<div class="nui-fit">
	  	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;"  showPager="false" sortMode="client" allowUnselect="false"
				url="<%=root%>/notice/getAllJsjs" onselectionchanged="onSelectionchanged" multiSelect="true" autoEscape="false"
				onshowrowdetail="onShowRowDetail">
		        <div property="columns">
		              <div type="checkcolumn"  name="checkCloumn"></div>
		        <div headerAlign="center" type="indexcolumn">序号</div>
				<div field="name" width="100" headerAlign="center" allowSort="true">角色名称</div>
				<div field="id" width="100" visible="false" headerAlign="center" allowSort="true" >角色编码</div>
	        </div>
	    </div>
   	</div>  
      <div class="nui-toolbar" style="text-align:center;padding-top:8px;padding-bottom:8px;" 
      borderStyle="border-left:0;border-bottom:0;border-right:0;">
      	<a id="btnCancel" class="nui-button" iconCls="" onclick="onCancel()" style="margin-right:20px;">
      		取消
		</a>
		<a id="btnOk" class="nui-button" iconCls="" onclick="onOk()" enabled="false">
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
			var rows = grid.getSelecteds();
			if(rows.length>0){
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
        	var rows = grid.getSelecteds();
        	var js="";
        	var bm="";
        	for(var i=0;i<rows.length;i++){
        		js=rows[i].name+","+js;
        		bm=rows[i].id+","+bm;
        	}
        	var json={jsjs:js,jsjsbm:bm}
			return json;
        }
    </script>
    
</body>
</html>
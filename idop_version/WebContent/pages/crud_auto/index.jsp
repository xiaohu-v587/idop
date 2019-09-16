<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>增删改查配置化</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="增刪改查配置化" style="width:100%;height:74px;" showToolbar="false" showCollapseButton="false"
			showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="nui-fit" style="overflow: hidden">
		   	</div>
		</div>
		
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="peizhi()" plain="true">配置属性</a>
						<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
						<a class="mini-button" iconCls="icon-download" id="removeBtn" onclick="downloadCode();" plain="true">下载代码</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/crud_auto/getList" multiSelect="true">
		        <div property="columns">
		        	  <div type="checkcolumn"  name="checkCloumn"></div>
		              <div field="id" visible="false"></div>
		              <div field="table_name">表名</div>
		              <div field="query_criteria">查询条件</div>
		              <div field="list_field">列表字段</div>
		              <div field="save_update_field">增加、修改字段</div>
		              <div field="name">菜单名称</div>
		              <div field="status" renderer="renderStatus">是否已生成代码</div>
		        </div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	grid.load();
	function peizhi(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			if(rows.length=1){
				var url = "<%=root%>/crud_auto/form?table_name="+rows[0].table_name;
				var bizParams = {pageType:"add"};
				$G.showmodaldialog("配置属性", url, 1000, 500, bizParams, function(action){
					grid.reload();
				});
			}else{
				$G.alert("有且只能选择一条记录");
			}
			
		}else{
			var url = "<%=root%>/crud_auto/form";
			var bizParams = {pageType:"add"};
			$G.showmodaldialog("配置属性", url, 1000, 500, bizParams, function(action){
				grid.reload();
			});
		}
	}
	//删除
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/crud_auto/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}
	function downloadCode(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			var ids = "";
            for(var index = 0;index < rows.length;index++){
            	if(rows[index].status==0){
            		$G.alert("表名【"+rows[index].table_name+"】还未生成代码，请先生成代码再下载！");
            		return false;	
            	}
            	if(ids == ""){
                	ids = rows[index].table_name;
              	} else {
                	ids += "," + rows[index].table_name;
              	}
            }
          	var ajaxConf = new GcdsAjaxConf();
          	ajaxConf.setIsShowSuccMsg(false);
        	ajaxConf.setSuccessFunc(function (text){
            	var status=text.status;
            	if(status==1){
            		$G.alert(text.info);
            		return false;
            	}
            	window.location.href="<%=root%>/crud_auto/downloadCode?ids="+ids;
            });
        	$G.postByAjax({"ids":ids}, "<%=root%>/crud_auto/isGenerateCode", ajaxConf);
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	function renderStatus(e){
		var status=mini.getDictText("FIELD_EDIT",e.value);
		return status;
	}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>公告信息维护</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="公告信息维护" style="width:100%;height:74px;" showToolbar="false" showCollapseButton="false"
			showFooter="false" allowResize="false" collapseOnTitleClick="false">
			<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">公告标题：</th>
							<td align="left">
								<input id="ggbt" name="ggbt" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right"></th>
							<td align="left">
							</td>
		                    <th align="right"></th>
							<td align="left">
							</td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
		
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
						<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">查看</a>
            	     	<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
            	     	<!-- <a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载附件</a> -->
					</td>
                    <td style="white-space:nowrap;">
                    	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a> 
						<a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
                    </td>
				</tr>
			</table>
		</div>
		<div class="nui-fit">
			<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;"  sortMode="client" allowUnselect="false"
				url="<%=root%>/notice/getList" oncellclick=""  onselectionchanged="onSelectionChanged" multiSelect="true" autoEscape="false"
				onshowrowdetail="onShowRowDetail">
		        <div property="columns">
		              <div type="checkcolumn"  name="checkCloumn"></div>
		              <div headerAlign="center" type="indexcolumn">序号</div>
		              <div field="id"  visible="false"  width="50" headerAlign="center" allowSort="true">id</div>
		              <div field="fbr"  width="160" allowSort="true" align="center" headerAlign="center">发布人</div>
		              <div field="fbsj" dateFormat="yyyy-MM-dd HH:mm:ss" width="160" allowSort="true" align="center" headerAlign="center">发布时间</div>
		              <div field="jsjsmc"  width="160" allowSort="true" align="center" headerAlign="center">接收角色</div>
		              <div field="bt"  width="160" allowSort="true" align="center" headerAlign="center">标题</div>
		               <div field="fjdz"  width="160" allowSort="true" align="center" headerAlign="center">附件</div>
		              <div field="llcs"  width="160" allowSort="true" align="center" headerAlign="center">浏览次数</div>
		        </div>
			</div>  
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var form = $G.getForm("form1");
	grid.load();

	function add(){
		var url = "/notice/form";
		$G.showmodaldialog("新增公告", url, 720, 550, null, function(action){
			grid.load();
		});
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "/notice/form";
			var bizParams = {pageType:"edit",data:row,ly:"notice"};
	        $G.showmodaldialog("查看公告", url, 720, 550, bizParams, function(action){
		    	 grid.load();
		    });
		}else{
			$G.alert("请先选中一条记录！");
		}
	}
	
	
	function del(){
		var rows = grid.getSelecteds();
		if(rows.length>0){
			$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
				if (action == 'ok') {
					var ids = "";
					var fjdz="";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].id;
	                    	fjdz = rows[index].fjdz;
	                  	} else {
	                    	ids += "," + rows[index].id;
	                  	}
	                }
                	$G.postByAjax({"id":ids,"fjdz":fjdz}, "/notice/del");
                	 grid.load();
				}
            });
		}else{
			$G.alert("请先选择一行数据！");
		}
	}

	function search(){
		 var ggbt = $G.getbyName("ggbt").getValue();
         grid.load({bt:ggbt});
	}
	
	
	

	//重置
	function reset(){
		form.reset();
	}

	function onActionRenderer(e){
		var obj = '<a href="javascript:userFun()" style="img-decoration:none"><img src="<%=root%>/resource/nui/themes/icons/user.png" border="0"></a>';
		return obj;
	}

	

	function onSelectionChanged(){
		var rows = grid.getSelecteds();
		if(rows.length>1){
			$G.get("editBtn").disable();
		}else{
			$G.get("editBtn").enable();
		}
	}
</script>
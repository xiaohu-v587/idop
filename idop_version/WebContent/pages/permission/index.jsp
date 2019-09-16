<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
 	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/common/nuires.jsp"%>
	</head>
	<style type="text/css">
	    html, body{
	        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    }
	    
	    #contentbox {
		position: relative;
		width: 100%;
		height: 100%;
	}
	
	#functiondiv {
		height: 100%;
		margin-top: 0;
		margin-left: 262px;
		margin-bottom: 0;
		margin-right: 302px;
	}
	
	#menutreediv {
		height: 100%;
		width: 100%;
		position: absolute;
		top: 0px;
		left: 0px;
	}
	
	#urldiv {
		height: 100%;
		margin-top: 0;
		margin-left: 302px;
		margin-bottom: 0;
		margin-right: 0;
	}
	
	.custom_footer {
		text-align: right;
		padding: 2px;
		background:transparent;
	}
	    
	    
	</style>
	<body>
		<div class="mini-splitter" style="width: 100%; height: 100%;"handlerSize="2" borderStyle="border:0;">
			<!--start left－角色显示区：top－查询toolbar，bottom－角色grid -->
			<div size="300px" minSize="300px" showCollapseButton="true" expanded="true" style="border-right:0px;">
				<div class="mini-toolbar" >
					<input id="key" class="mini-textbox" maxlength="64" style="width: 150px;"  emptyText="请输入角色名称" onenter="onKeyEnter" /> 
					<a class="mini-button" iconCls="" onclick="search()" >查询</a>
				</div>
				<div class="mini-fit">
					<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 100%;" allowMoveColumn="false"
						url="<%=root%>/permission/getRole" onselectionchanged="onSelectionChanged" selectOnLoad="true" allowUnselect="false" 
						borderStyle="border-top:0;border-bottom:0;" showTotalCount="false" showPageInfo="false">
						<div property="columns" allowMove="false" allowResize="false">
							<div type="indexcolumn" headerAlign="center" width="30px" allowMove="false" allowResize="false" align="right">序号</div>
							<div field="id" visible="false"></div>
							<div field="pid" visible="false"></div>
							<div field="main_page" visible="false"></div>
							<div field="name" width="120" headerAlign="center" allowSort="false" allowMove="false" allowResize="false">角色名称</div>
						</div>
					</div>
				</div>		
			</div>
			<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
				<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;" 
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
	                 <div title="菜单权限" >
	                 	<div id="contentbox"> 
	                 		<div id="menutreediv">
								<div class="nui-panel" title="菜单树" iconCls="" style="width: 100%; height: 100%;" showToolbar="false" showCloseButton="false" showFooter="true">
									<ul id="menuTree" class="mini-tree" style="width: 100%; height:100%;"  dataField="datas" showTreeIcon="true" url="<%=root%>/permission/getRoleTree"
										textfield="name" idfield="id" parentfield="pid" resultAsTree="false" expandOnNodeClick="false" expandOnLoad="true">   
									</ul>
									<div property="footer" class="custom_footer">
										<a id="choosemenu" iconCls="" class="nui-button" onClick="chooseMenu()" ><font color="#ffffff">选择权限</font></a>
									</div>
								</div>
							</div>
						</div>
					</div> 
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid = $G.get("datagrid1");
	var tree = $G.get("menuTree");
	
	grid.load();
        
	function search(){
		var name = $G.get("key").getValue();
		grid.load({name:name});
	}
        
	function onSelectionChanged(e){
		var grid2 = e.sender;
		var record = grid2.getSelected();
		var id = record.id;
		if(e.select){
			tree.load({id:id});
		}
	}
        
	//弹出选择菜单树界面
	function chooseMenu(){
		var tree = mini.get("menuTree");
		var row = grid.getSelected();	//获取角色列表选中行对象
		var id = row.id;
		var mainpage = row.main_page;
		if(row){
			var bizParams = { action: "new",id:id,mainpage:mainpage};
			var url="<%=root%>/permission/getChoiceTree";
			$G.showmodaldialog("选择菜单树", url, 400, 500, bizParams, function(action){
				tree.load({id:id,mainpage:mainpage}); 
		    });
    	}
	}
</script>

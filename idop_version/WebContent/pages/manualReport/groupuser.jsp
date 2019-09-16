<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/nuires.jsp"%>
<style type="text/css">
.mini-grid-newRow {
	background: none;
}
</style>
</head>
<body>
	<div class="nui-splitter" style="width: 100%; height: 100%;"
		borderStyle="border:0;">
		<div size="240" maxSize="300" minSize="180" showCollapseButton="true"
			style="border-width: 1px; overflow: auto;">
			<div showFooter="true" class="nui-panel-body" style="height: 100%;">
				<ul id="tree1" class="nui-tree"
					url="<%=root%>/org/getOrgList"
					style="width: 100%; height: 100%; padding: 5px;" dataField="datas"
					showTreeIcon="true" textfield="orgname" idfield="orgnum"
					parentfield="upid" resultAsTree="false" expandOnLoad="0">
				</ul>
			</div>
		</div>
		<div size="100%" showCollapseButton="false" expanded="true" style="border-left: 0px;">
			<div id="datagrid1" idField="ehr" class="nui-datagrid" sortMode="client" 
				style="width: 100%; height: 60%;" url="<%=root%>/org/getListOrg"
				multiSelect="true" allowUnselect="true">
				<div property="columns">
					<div width="30" type="checkcolumn" name="checkCloumn"></div>
					<div type="indexcolumn" width="30" align="right">序号</div>
					<div field="orgnum" width="80" align="right" headerAlign="left">机构号</div>
					<div field="orgname" width="150" align="left" headerAlign="left">机构名称</div>
					<div field="ehr" width="60" align="right" headerAlign="left">EHR号</div>
					<div field="username" width="60" align="left" headerAlign="left">姓名</div>
					<div field="phone" width="80" align="right" headerAlign="left">电话</div>
				</div>
			</div>
			<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px;"
				borderStyle="border-left:0;border-bottom:0;border-right:0;">
				<a id="okBtn" class="mini-button" iconCls="" onclick="onOk()">确定</a>
				<span class="separator"></span>
				<a id="addBtn" class="mini-button" iconCls="" onclick="add()">创建群组</a>
			</div>
			<div id="datagrid2" idField="ehr" class="nui-datagrid" style="width: 100%;" showPager="false">
				<div property="columns">
					<!-- <div width="30" type="checkcolumn" name="checkCloumn"></div>
					<div type="indexcolumn" width="30">序号</div> -->
					<div field="orgnum" width="80" align="left" headerAlign="left">机构号</div>
					<div field="orgname" width="150" align="left" headerAlign="left">机构名称</div>
					<div field="ehr" width="60" align="left" headerAlign="left">EHR号</div>
					<div field="username" width="60" align="left" headerAlign="left">姓名</div>
					<div field="phone" width="80" align="right" headerAlign="left">电话</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var tree = $G.get("tree1");
	var grid1 = $G.get("datagrid1");
	var grid2 = $G.get("datagrid2");
	grid1.load();
	
	//选中节点，右侧展示
	tree.on("nodeclick", function(e) {
		grid1.load({ orgid : e.node.id });
		
	});
	
	//加载机构完将选择的列表还原回去
	grid1.on("load",function(e){
		var rows = grid2.getData();
		for(var i=0;i<rows.length;i++){
			if(grid1.getRow(rows[i].ehr)!=null){
				grid1.setSelected(grid1.getRow(rows[i].ehr));
			}
		}
	})
	
	/* grid1.on("rowselect",function(e){
		grid2.addRow(e.records[0],0);
	});
	
	grid1.on("rowdeselect",function(e){
		grid2.removeRow(e.records[0],true);
	}); */
	
	grid1.on("rowclick",function(e){
		if(grid1.isSelected(e.record)){
			grid2.addRow($G.clone(e.record),0);
		}else{
			grid2.removeRow(grid2.getRow(e.record.ehr),true);
		}
	});
	
	function setData(data){
		data = $G.clone(data);
		if (data.pageType == "noadd") {
            $G.get("addBtn").hide();
        }
	}
	
	function getData(){
		var ids = new Array();
		var names = new Array();
		var rows = grid2.getData();
		for(var i=0;i<rows.length;i++){
			ids.add(rows[i].ehr);
			names.add(rows[i].username);
		}
		if(rows.length>0){
			var json = "{\"ids\":\""+ids.toString()+"\",\"names\":\""+names.toString()+"\"}";
			return JSON.parse(json);
		}else{
			return null;
		}
		
	}
	
	function onOk(){
		$G.closemodaldialog("ok");
	}
	
	function add() {
		var rows = grid2.getData();
		if(rows.length>0){
			$G.prompt("请输入群组名称：","新建群组",function(action,value){
				if(action=="ok"){
					if(value==""){
						$G.alert("创建失败！群组名称不能为空");
					}else{
						var ids = new Array();
						for(var i=0;i<rows.length;i++){
							ids.add(rows[i].ehr);
						}
						$G.alert("创建成功！"+value);
					}
				}
			});
		}else{
			$G.alert("您还没有选择联系人");
		}
	}
	
</script>
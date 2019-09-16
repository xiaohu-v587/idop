<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp"%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div style="height:90%">
			<div class="nui-splitter" style="width: 100%; height: 100%" borderStyle="border:0;">
				<div size="350" maxSize="360" minSize="180" showCollapseButton="true"
					style="border-width: 1px; overflow: auto; border-top: 0; border-left: 0; border-bottom: 0;">
					<div id="panel1" class="nui-panel" title="机构" style="width: 100%; height: 100%">
						<div showFooter="true" class="nui-panel-body" style="height: 100%;">
							<div style="white-space: nowrap; width: 95%;">
								<input id="userid" name="userid" class="mini-textbox" emptyText="请输入员工号" style="margin-top: 2%; width: 40%;" />
								<input id="username" name="username" class="mini-textbox" emptyText="请输入姓名" style="margin-top: 2%; width: 40%;" /> 
								<a class="mini-button" iconCls="icon-search" onclick="search()" style="margin-top: 2%">查询</a>
							</div>
							<div id="tree1"  class="mini-treegrid" url="<%=root%>/manualReport/getOrgList"
								showTreeIcon="true" style="width: 100%; height: 93%;"
								treeColumn="orgname" idField="orgnum" parentField="upid"
								resultAsTree="false" checkRecursive="true" autoCheckParent="false"
								allowResize="true" dataField="datas"
								showCheckBox="false">
								<div property="columns">
									<div headerAlign="center" type="indexcolumn">序号</div>
									<div field="orgnum" visible="false">机构号</div>
									<div field="orgname" name="orgname" width="160"	headerAlign="left">对象名称</div>
									<div field="orgnum" width="80" visible="false" headerAlign="left">对象</div>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				<div size="100%" minSize="500" showCollapseButton="true" expanded="true" style="border-left: 0;padding-right: 5px">
							<b>已选择：</b>
				<input id="tbl1" name="tbl1" class="mini-textboxlist" style="width:100%;height:70%" allowInput="false"
					textName="tblName" value="" text="" valueField="id" textField="text"/>
				</div>
			</div>
		</div>
		<div class="mini-fit" style="height:8%" borderStyle="border-top:1px solid;">
			<div class="mini-toolbar" style="text-align: center; padding:10px 0;height:30%;box-sizing:border-box" borderStyle="border-left:0;border-right:0;border-bottom:0">
				<a id="okBtn" class="mini-button" iconCls="" onclick="onOk()">确定</a>
				<span class="separator"></span>
				<a id="cancelBtn" class="mini-button" iconCls="" onclick="onCancel()">取消</a>
				<span class="separator noadd"></span>
				<a id="addBtn" class="mini-button noadd" iconCls="" onclick="add()">创建群组</a>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var tree = $G.get("tree1");
	//var grid = $G.get("datagrid1");
	var tbl = $G.get("tbl1");
	//var form = $G.getForm("form1");
	var act = "";
	//grid.load();
	
	function setData(data) {
		data = $G.clone(data);
		if (data.pageType == "noadd") {
			$(".noadd").hide();
		}
		if(data.name){
			var items = data.items;
			var ids = '';
			for(var i=0,len = items.length;i<len;i++){
					if(i > 0){
						ids += ',';	
					}
					ids += items[i].user_no+"_"+items[i].orgnum;
			}
			tbl.setValue(ids);
			tbl.setText(data.name);
		}
	}

	function getData() {
			var ids = tbl.getValue().split(",");
			var items = [];
			var tmp = [];
			for(var i=0;i<ids.length;i++){
				tmp = ids[i].split("_");
				
				items.push({orgnum:tmp[1],role_id:"",user_no:tmp[0]});
			}
			return {items:items,itemNames:tbl.getText(),flag:"1"};
	}
	
	//根据输入的员工号和姓名查找相应员工并返显
	function search(){			  
	  	 var userid = mini.getbyName("userid").getValue();
	  	 var username = mini.getbyName("username").getValue();
	  	 if(userid == "" && username == ""){
	  		 mini.alert("请先输入员工号或姓名！");
	  		 return;
	  	 }
	  	 tree.load(encodeURI("<%=root%>/manualReport/getOrgList?userid="+userid+"&username="+username)); 
	  	 tree.reload();
	}
	
	tree.on("nodeclick", function(e) {
		//选中节点，右侧展示
		/* grid1.load({ orgid : e.node.id }); */
		var node = tree.getSelectedNode();
		var upnode = tree.getParentNode(node);
		if(node.flag == 'ORG'){
			return;
		}
		if(tbl.getValue()){
			var ids = tbl.getValue().split(",");
			if(ids.contains(node.orgnum+"_"+node.upid)){
				$G.alert("您已经选择了 "+node.orgname+upnode.orgname);
			}else{
				tbl.setValue(tbl.getValue()+","+node.orgnum+"_"+node.upid);
				tbl.setText(tbl.getText().concat(node.orgname+upnode.orgname));
			}
		}else{
			tbl.setValue(node.orgnum+"_"+node.upid);
			tbl.setText(node.orgname+upnode.orgname);
		}
	});

/* 	grid.on("rowclick", function(e) {
		if(tbl.getValue()){
			var ids = tbl.getValue().split(",");
			if(ids.contains(e.record.user_no)){
				$G.alert("您已经选择了 "+e.record.name);
			}else{
				tbl.setValue(tbl.getValue()+","+e.record.user_no);
				tbl.setText(tbl.getText().concat(e.record.name));
			}
		}else{
			tbl.setValue(e.record.user_no);
			tbl.setText(e.record.name);
		}
	}); */

	//查询
	/* function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
 */
	function onOk() {
		$G.closemodaldialog("ok");
	}

	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}

	function add() {
		var records = tbl.getValue();
		if (records.length > 0) {
			$G.prompt("请输入群组名称：", "新建群组", function(action, value) {
				if (action == "ok") {
					if (value == "") {
						$G.alert("群组名称不能为空");
					} else {
						var urlStr = "<%=root%>/manualReport/saveGroup";
					    var ajaxConf = new GcdsAjaxConf();
					    ajaxConf.setIsShowProcessBar(false);
						ajaxConf.setIsShowSuccMsg(false);
					    ajaxConf.setSuccessFunc(function (text){
					    	if(text.result=='success'){
					    		act = "addgroup";
					    		$G.alert("群组创建成功！弹窗关闭后请下拉选择","提示",function(){
					    			$G.closemodaldialog("ok");
					    		});
					    	}else{
					    		$G.alert("群组创建失败");
					    	}
						});
					    var formjson = $G.encode({groupName:value,groupNote:"快捷创建",itemOrgIds:getData().org,itemUserIds:getData().usr});
					    $G.postByAjax({"form":formjson}, urlStr, ajaxConf);
					}
				}
			});
		} else {
			$G.alert("您还没有选择对象");
		}
	}
</script>
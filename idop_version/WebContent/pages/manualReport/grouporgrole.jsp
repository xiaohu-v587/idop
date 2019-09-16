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
				<div size="240" maxSize="300" minSize="180" showCollapseButton="true"
					style="border-width: 1px; overflow: auto; border-top: 0; border-left: 0; border-bottom: 0;">
					<div id="panel1" class="nui-panel" title="机构和角色" style="width: 100%; height: 100%">
						<div showFooter="true" class="nui-panel-body" style="height: 100%;">
							<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;" plain="false">
							    <div title="机构" >
							    	<ul id="tree1" class="mini-tree" url="<%=root%>/org/getOrgList" textfield="orgname" idfield="orgnum" parentfield="upid" 
										style="width: 100%; height: 100%; padding: 5px;" dataField="datas" showTreeIcon="true"  expandOnLoad="0"
										checkOnTextClick="true" resultAsTree="false" checkRecursive="false" value="<%=request.getAttribute("org") %>">
									</ul>
							    </div>
							    <div title="角色" >	
							    	<div id="tree2" dataField="datas" class="nui-treegrid" url="<%=root%>/manualReport/getRoleList"
										showTreeIcon="true" style="width: 100%; height: 100%; padding: 5px;"
										treeColumn="showname" idField="shownum" parentField="upid"
										resultAsTree="false" checkRecursive="false" autoCheckParent="false"
										allowResize="true" dataField="datas"
										showCheckBox="false">
										<div property="columns">
											<div headerAlign="center" type="indexcolumn">序号</div>
											<div field="shownum" visible="false">机构号</div>
											<div field="showname" name="showname" width="160" headerAlign="left">对象名称</div>
											<div field="shownum" width="80" visible="false" headerAlign="left">对象</div>
										</div>
									</div>
							    </div>
							</div>
						</div>
					</div>
				</div>
				<div size="100%" minSize="500" showCollapseButton="true" expanded="true" style="border-left: 0;padding-right: 5px">
							<b>已选择机构：</b>
							<input id="tbl1" name="tbl1" class="mini-textboxlist" style="width:100%;height:45%" allowInput="false"
								textName="tblName" value="" text="" valueField="id" textField="text"/>
							<b>已选择角色：</b>
							<input id="tbl2" name="tbl2" class="mini-textboxlist" style="width:100%;height:45%" allowInput="false"
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
	var tree1 = $G.get("tree1");
	var tree2 = $G.get("tree2");
	//var grid = $G.get("datagrid1");
	var tbl1 = $G.get("tbl1");
	var tbl2 = $G.get("tbl2");
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
		
			var isFir = true;
			for(var i=0;i<items.length;i++){
				if(!isNullObj(items[i].orgnum)){
					if(isFir){
						isFir = false
					}else{
						ids += ',';	
					}
					ids += "org"+items[i].orgnum;	
				}
			}
			
			tbl1.setValue(ids);
			
			ids = '';
			isFir = true;
			for(var i=0;i<items.length;i++){
				if(!isNullObj(items[i].role_id)){
					if(isFir){
						isFir = false
					}else{
						ids += ',';	
					}
					ids += "role"+items[i].role_id;	
				}
			}
			
			tbl2.setValue(ids);	
			console.log(data)
			var names = data.name.split("$|$");
			tbl1.setText(names[0])
			tbl2.setText(names[1])
		}
	}

	function getData() {
		var ids1 = tbl1.getValue().split(",");
		var ids2 = tbl2.getValue().split(",");
		var items = [];
		
		for(var i=0;i<ids1.length;i++){
			items.push({orgnum:ids1[i].substr(3),role_id:"",user_no:""});
		}
		
		for(var i=0;i<ids2.length;i++){
			items.push({orgnum:"",role_id:ids2[i].substr(4),user_no:""});
		}
		
		return {items:items,itemNames:tbl1.getText()+"$|$"+tbl2.getText(),flag:"1"};
	}
	
	tree1.on("nodeclick", function(e) {
		//选中节点，右侧展示
		/* grid1.load({ orgid : e.node.id }); */
		var org = tree1.getSelectedNode();
		if(tbl1.getValue()){
			var ids = tbl1.getValue().split(",");
			if(ids.contains("org"+org.orgnum)){
				$G.alert("您已经选择了 "+org.orgname);
			}else{
				tbl1.setValue(tbl1.getValue()+",org"+org.orgnum);
				tbl1.setText(tbl1.getText().concat(org.orgname));
			}
		}else{
			tbl1.setValue("org"+org.orgnum);
			tbl1.setText(org.orgname);
		}
	});
	
	tree2.on("nodeclick", function(e) {
		//选中节点，右侧展示
		/* grid1.load({ orgid : e.node.id }); */
		var node = tree2.getSelectedNode();
		if(isNullObj(node.upid)){
			return;
		}
		if(tbl2.getValue()){
			var ids = tbl2.getValue().split(",");
			if(ids.contains("role"+node.shownum)){
				$G.alert("您已经选择了 "+node.showname);
			}else{
				tbl2.setValue(tbl2.getValue()+",role"+node.shownum);
				tbl2.setText(tbl2.getText().concat(node.showname));
			}
		}else{
			tbl2.setValue("role"+node.shownum);
			tbl2.setText(node.showname);
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
		var records = tbl1.getValue();
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
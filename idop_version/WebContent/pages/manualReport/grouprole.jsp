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
					<div id="panel1" class="nui-panel" title="角色" style="width: 100%; height: 100%">
						<div showFooter="true" class="nui-panel-body" style="height: 100%;">
							<div id="tree1" dataField="datas" class="nui-treegrid" url="<%=root%>/manualReport/getRoleList"
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
					ids += "org"+items[i].role_id;
			}
			tbl.setValue(ids);
			tbl.setText(data.name);
		}
	}

	function getData() {
		var ids = tbl.getValue().split(",");
		var items = [];
		for(var i=0;i<ids.length;i++){
			items.push({orgnum:"",role_id:ids[i].substr(4),user_no:""});
		}
		return {items:items,itemNames:tbl.getText(),flag:"1"};
	}
	
	tree.on("nodeclick", function(e) {
		//选中节点，右侧展示
		/* grid1.load({ orgid : e.node.id }); */
		var node = tree.getSelectedNode();
		if(isNullObj(node.upid)){
			return;
		}
		if(tbl.getValue()){
			var ids = tbl.getValue().split(",");
			if(ids.contains("role"+node.shownum)){
				$G.alert("您已经选择了 "+node.showname);
			}else{
				tbl.setValue(tbl.getValue()+",role"+node.shownum);
				tbl.setText(tbl.getText().concat(node.showname));
			}
		}else{
			tbl.setValue("role"+node.shownum);
			tbl.setText(node.showname);
		}
	});

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
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
				<div size="450" maxSize="460" minSize="180" showCollapseButton="true"
					style="border-width: 1px; overflow: auto; border-top: 0; border-left: 0; border-bottom: 0;">
					<div id="panel1" class="nui-panel" title="机构" style="width: 100%; height: 100%">
						<div showFooter="true" class="nui-panel-body" style="height: 100%;">
							<div style="white-space: nowrap; width: 95%;">
								<form id="form1">
									<input id="userid" name="userid" class="mini-textbox" emptyText="请输入员工号" style="margin-top: 2%;width:40%; " />
									<input id="username" name="username" class="mini-textbox" emptyText="请输入姓名" style="margin-top: 2%;width:40%;  " /> 
									<br/>
									<input id="roleid" name="roleid"  class="mini-combobox" style="margin-top: 2%;width:40%;  "multiSelect="true" textfield="name" valuefield="id" emptyText="请选择角色..."
                                		url="<%=root%>/role/getCombox"  showNullItem="true" nullItemText="请选择..." />
									<input id="orgnum" name="orgnum" style="margin-top: 2%;width:40%;  " class="mini-treeselect" url="<%=root %>/org/getListByUser"
										dataField="datas" textField="orgname" valueField="orgnum" parentField="upid" valueFromSelect="true" multiSelect="true"
										expandOnLoad="0" emptyText="请选择机构" allowInput="false" showClose="true" oncloseclick="onCloseClick" 
										showRadioButton="false" showFolderCheckBox="true" popupWidth="286" popupHeight="470" popupMaxHeight="600" />
								</form>	
								<a class="mini-button" onclick="search()" style="margin-top: 2%">查询</a>
								<a class="mini-button" onclick="reset()" style="margin-top: 2%">清除</a>
							</div>
							<div id="grid1" class="mini-datagrid" style="width:100%;height:80%;"  url="<%=root%>/manualReport/getNewUserList" dataField="datas" 
								 showPager="true" showPageInfo = "true" multiSelect="false" pageSize="10" sizeList="[10,20,50,100]" onrowclick="gridrowclick">
								    <div property="columns">
								       	<div headerAlign="center" type="indexcolumn">序号</div>
										<div field="user_no" visible="false">用户号</div>
										<div field="name" name="orgname"	headerAlign="left">用户名称</div>
										<div field="rolename" headerAlign="left">角色</div>
										<div field="orgname"   headerAlign="left">机构</div>
								    </div>
							</div>
						</div>
					</div>
				</div>
				<div size="100%" minSize="300" showCollapseButton="true" expanded="true" style="border-left: 0;padding-right: 5px;overflow:hidden;">
							<b>已选择：</b>
					<div style="border: 0;padding: 0;margin: 0;height: 100%;width: 100%;overflow: scroll;">
						<input id="tbl1" name="tbl1" class="mini-textboxlist" style="width:100%;height:95%" allowInput="false"
						textName="tblName" value="" text="" valueField="id" textField="text"/>
					</div>
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
	var grid = $G.get("grid1");
	//var grid = $G.get("datagrid1");
	var tbl = $G.get("tbl1");
	var form = $G.getForm("form1");
	var act = "";
	grid.load();
	
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
					ids += items[i].orgnum+"_"+items[i].role_id+"_"+items[i].user_no;
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
			tmp = ids[i].split('_');
			items.push({orgnum:tmp[0],role_id:tmp[1],user_no:tmp[2]});
		}
		
		return {items:items,itemNames:tbl.getText(),flag:"1"};
	}
	function gridrowclick(e) {
		//选中节点，右侧展示
		var row = e.row;
		if(tbl.getValue()){
			var ids = tbl.getValue().split(",");
			if(ids.contains(row.orgnum+"_"+row.role_id+"_"+row.user_no)){
				$G.alert("您已经选择了 "+row.name+row.orgname+row.rolename);
			}else{
				tbl.setValue(tbl.getValue()+","+row.orgnum+"_"+row.role_id+"_"+row.user_no);
				tbl.setText(tbl.getText().concat(row.name+row.orgname+row.rolename));
			}
		}else{
			tbl.setValue(row.orgnum+"_"+row.role_id+"_"+row.user_no);
			tbl.setText(row.name+row.orgname+row.rolename);
		} 
	}

	//查询
	 function search(){
		var data = form.getData();
		grid.load(data);
	}

	//重置
	function reset(){
		form.reset();
	}
 
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
	
	//机构下拉框清空
	function onCloseClick(e) {
	    var obj = e.sender;
	    obj.setText("");
	    obj.setValue("");
	}
</script>
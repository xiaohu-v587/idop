<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/common/nuires.jsp"%>
    <title>参数管理</title>
	<style type="text/css">
    	html, body{
        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    	}
	</style> 
</head>
<body >
	<div id="layout1" class="mini-layout" style="width:99.8%;height:99.7%;" borderStyle="border-right:1px #aaa solid;">
		<div title="center" region="center" style="border:0px;">
			<div class="mini-toolbar" style="border-bottom:0;padding:0px;">
				<table style="width:100%;">
					<tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()">新增</a>
							<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()">编辑</a>
							<a class="mini-button" iconCls="icon-edit" id="startBtn" onclick="startOrStop()">启用/停用</a>
							<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()">删除</a>
							<a class="mini-button" iconCls="icon-close" id="cancel_removeBtn" onclick="cancelRemove()" visible="false">取消</a>
							<a class="mini-button" iconCls="icon-ok" id="confirm_removeBtn" onclick="confirmRemove()" visible="false">确定</a>
							<!-- <a class="mini-button" iconCls="icon-undo" id="addBtn" onclick="init()" >刷新缓存</a> -->
						</td>
						<td style="white-space:nowrap;">
							<%-- <input id="key1" class="mini-combobox" style="width:150px;" textfield="name" valuefield="val" emptyText="字典名称"
								url="<%=root%>/zxparam/getCombox"  allowInput="true" showNullItem="true" nullItemText="请选择..."/>
								--%>
							<font color="#FFFFFF">字典编号/字典名称:&nbsp;&nbsp;</font><input id="key1" name="key1" class="mini-textbox" style="width:150px;" />
							<a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
						</td>
					</tr>
				</table>
			</div>
             
			<div class="mini-fit">
				<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;height:100%;" sortMode="client" allowUnselect="false"
					url="<%=root%>/zxparam/getList" oncellclick=""  onselectionchanged="onSelectionChanged"  onload="controlBtnState()"
				    autoEscape="false" >
					<div property="columns">
						<div type="checkcolumn" name="checkCloumn"></div>
						<div headerAlign="center" type="indexcolumn"  align="right">序号</div>
				        <div field="id"  visible="false"  width="50" headerAlign="center" allowSort="true"><fmt:message key="ID"/></div>
				        <div field="key" width="100"   allowSort="true" headerAlign="center" align="left">字典编号</div>
	                    <div field="name" width="140" allowSort="true" headerAlign="center" align="left">字典名称</div>
	                    <div field="val"   allowSort="true" headerAlign="center" align="left">属性键值</div>
	                    <div field="remark" width="300"   allowSort="true" headerAlign="center" align="left">中文信息</div>
	                    <div field="sortnum"   allowSort="true" headerAlign="center" align="right">排列序号</div>
	                    <div field="status"   allowSort="true" renderer="onStartRenderer" headerAlign="center" align="left">状态</div>
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
	var seletecIndex;
	var layout = $G.get("layout1");
	grid.load();
	
	//企业是否达到预警
	function onStartRenderer(e){
		var a = e.row.status;
		if(a=='0'){
			var s = '停用';
		}else{
			var s = '启用';
		}
		return s;
	}

	//按字典编号查询
	function search() {
	    var key1 = $G.get("key1").getValue();
	    grid.load({key:key1});
	}

	/*添加字典信息*/
	function add(){
		var row = grid.getSelected();
		var key = null;
		var name = null;
		if(row){
			key = row.key;
			name = row.name;
		}
		// else{
		//	key  = $G.get("key1").getValue();
		//	name = $G.get("key1").getText();
		//}
		var url="<%=root%>/zxparam/add";
		var bizParams = {pageType: "add",key:key,name:name};
		$G.showmodaldialog("新增字典信息", url, 500, 240, bizParams, function(action){
       		grid.reload();
		});
	}

	function startOrStop(){
		var row = grid.getSelected();//获取一条记录
		var id=row.id;//获取ID
		var status = row.status;

		var url ="";
		var msg="";
		if(status == "1"){
			msg="确定停用？";
			url = "<%=root%>/zxparam/changeStatus1";
		}else if (status == "0"){
			msg="确定启用？";
			url = "<%=root%>/zxparam/changeStatus0";
		}
		
		$G.confirm(msg, "确定",function (action){
			if(action == "ok"){
				var ajaxConf = new GcdsAjaxConf();
				ajaxConf.setIsShowProcessBar(true);
				ajaxConf.setIsShowSuccMsg(true);
			    ajaxConf.setSuccessFunc(function (text){
			    	grid.reload();
				});
				$G.postByAjax({uuid:id},url,ajaxConf);
			}
		});
	}

	//修改字典信息
	function edit() {
		var row = grid.getSelected();
		if(row){
			var url="<%=root%>/zxparam/add";
			var bizParams = {pageType: "edit",id: row.id};
			$G.showmodaldialog("编辑字典信息", url, 500, 240, bizParams, function(action){
	       		grid.reload();
			});
		}else{
			$G.alert("请选择一行数据");
		}
	} 


	function callback(){
		$G.alert("sssss");
	}

	/*删除字典信息*/
	function remove() {
		$G.get("datagrid1").setMultiSelect(true);
		$G.get("datagrid1").setAllowUnselect(true);
		$G.get("datagrid1").updateColumn("checkCloumn");
		$G.get("datagrid1").setShowPager(false);
		$G.get("confirm_removeBtn").setVisible(true);
		$G.get("cancel_removeBtn").setVisible(true);
		$G.get("removeBtn").setVisible(false);
		if(grid.getSelecteds().length > 0) {
			$G.get("confirm_removeBtn").enable();
		}else {
			$G.get("confirm_removeBtn").disable();       	
		}
    	$G.get("editBtn").disable();
    	$G.get("addBtn").disable();
    	$G.get("removeBtn").disable();
	}  

	/*取消删除字典信息*/
 	function cancelRemove(){
 		$G.get("datagrid1").setMultiSelect(false);
 		$G.get("datagrid1").setAllowUnselect(false);
 		$G.get("datagrid1").updateColumn("checkCloumn");
 		$G.get("datagrid1").setShowPager(true);
 	
	 	if(grid.getSelecteds().length > 1) {
	 		grid.clearSelect();
	 		grid.select(grid.getRow(seletecIndex));
	 	}
 	
	    $G.get("confirm_removeBtn").setVisible(false);
	    $G.get("cancel_removeBtn").setVisible(false);
	    $G.get("removeBtn").setVisible(true);
	    $G.get("addBtn").enable();
	    $G.get("removeBtn").disable();
        
    	if(grid.getSelected()) {
			$G.get("addBtn").enable();
			$G.get("editBtn").enable();
			$G.get("removeBtn").enable();
        }
 	}


 /*确认删除字典信息*/
 function confirmRemove() {
 	   var rows = grid.getSelecteds();
        if (rows.length > 0) {
     	   if (row)
           $G.confirm("确定修改数据字典启动/停止状态？", "确定？", function (action) {
               if (action == "ok") {
                   var ids = [];
                   for (var i = 0, l = rows.length; i < l; i++) {
                       var r = rows[i];
                       ids.push(r.ID);
                   }
                   var id = ids.join(',');
                   grid.loading("操作中，请稍后......");
                   $.ajax({
                       url: "<%=root%>/zxparam/del?uuid=" +id,
                       success: function (text) {
                    	   var record = $G.decode(text).record;
                    	   //$G.alert(record);
                    	   if(record=="1"){
                    		   $G.alert("修改成功!");
                    	   }
                           grid.reload();
                       }
                   });
               }
           });
        } 
 }


//控制按钮的状态 
function controlBtnState() {
	 $G.get("startBtn").disable();
	 //$G.get("stopBtn").disable();
	 $G.get("editBtn").disable();
  	 $G.get("removeBtn").disable();
  if ($G.get("cancel_removeBtn").visible == true) {
  	if(grid.getSelecteds().length > 0) {
 			$G.get("confirm_removeBtn").enable();
 		} else {
 			$G.get("confirm_removeBtn").disable();
 		}
  }
}

//当选中grid中的行    
function onSelectionChanged(e){  
	 var rows = grid.getSelecteds();
	 if($G.get("datagrid1").showPager==true) {
		seletecIndex = grid.indexOf(rows[0]);
	}
	 
	 if (rows.length > 0) {
     	if ($G.get("confirm_removeBtn").visible == true) {
     		$G.get("confirm_removeBtn").enable();
     	} else {
				$G.get("editBtn").enable();
	            $G.get("removeBtn").enable();
	            $G.get("startBtn").enable();
         }
  } else {
  	$G.get("confirm_removeBtn").disable();
  }
}

	function init(){	
		$G.GcdsConfirm("确定刷新缓存吗？", "刷新提示", function(action) {
			if (action == 'ok') {
                var ajaxConf = new GcdsAjaxConf();
                ajaxConf.setSuccessFunc(function (){
                    grid.reload();
                });
                $G.postByAjax({}, "<%=root%>/zxparam/init", ajaxConf);
              }
		});
		
	}
	
	function del(){
		var row = grid.getSelected();
		var id = row.id;
		$G.GcdsConfirm("确定删除选中记录？", "删除提示", function(action) {
			if (action == "ok") {
				var ajaxConf = new GcdsAjaxConf();
	            ajaxConf.setSuccessFunc(function (){
	            	grid.reload();
	            });
	            $G.postByAjax({"id":id}, "<%=root%>/zxparam/delete", ajaxConf);
			}
		});
	}
</script>
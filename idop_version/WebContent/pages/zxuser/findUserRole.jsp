<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>

<html>
	<head>
		<title>用户角色管理</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		
		.userRoleLegendCls{
			color: #ffffff;
		}
  		</style>
	</head>
	<body>
		<div class="mini-fit" id= "bigDiv">
            <form id="form1" method="post" style="height: 100%;">
				<input class="mini-hidden" id="roleId"/>
            	<input class="mini-hidden" id="roleName"/>
            	<input class="mini-hidden" id="delIds"/>
            	<input class="mini-hidden" id="addGridRows"/>
				<div style="width:99%; height: 99%; "  >
					<table style="height: 98%; width: 100%; border: #aaa solid 0px;">
						<tr style="height: 100%">
							<td style="width: 44%; height:100%; border: #aaa solid 0px;">
								<fieldset style="border:solid 1px #aaa; height: 98%;">
									<legend class="userRoleLegendCls">待分配角色</legend>
									<div id="datagrid1" class="mini-datagrid" style="width:100%;height:96%;" multiSelect="true"
										url="XXX" dataField="datas" idfield="id" valuefield="id" showPager="false">
										<div property="columns">
									        <div field="name" headerAlign="center" align="center">角色名称</div> 
									    </div>
									</div>
								</fieldset>
							</td>
							<td style="width: 10%; height:100%; border: #aaa solid 0px;" align="center">
								<input type="button" value="&gt;" onclick="add()" style="width:60px;" /><br />
								<br><input type="button" value="&lt;&lt;" onclick="removes('1')" style="width:60px;"/><br />
								<br><input type="button" value="&lt;" onclick="removes('0')" style="width:60px;"/><br />
							</td>
							<td style="width: 44%; height:100%; border: #aaa solid 0px;">
								<fieldset style="border:solid 1px #aaa; height: 98%;">
									<legend class="userRoleLegendCls">已分配角色</legend>
									<div id="datagrid2" class="mini-datagrid" style="width:100%;height:96%;" multiSelect="true"
										url="XXX" dataField="datas" idfield="id" valuefield="id" showPager="false">
										<div property="columns">
									        <div field="name" headerAlign="center" align="center">角色名称</div> 
									    </div>
									</div>
								</fieldset>
							</td>
						</tr>
					</table>
				</div>
            </form>
       	</div>
       	<div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
       		borderStyle="border-left:0;border-bottom:0;border-right:0;">
            <a id="btnCancle" class="mini-button" iconCls="icon-close" onclick="onCancel()" >关闭</a>
            <a id="btnOk" class="mini-button" iconCls="icon-ok" onclick="saveUserRole()" enabled="false" style="margin-right: 20px; ">确定</a>
      	</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	var grid2 =$G.get("datagrid2");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	var uid = "";
	
	//页面加载初始化
	function setData(data){
		var infos = $G.clone(data);
		grid.setUrl("<%=root%>/zxuser/distributeRoleList?id="+infos.id);
		grid.load();
		grid2.setUrl("<%=root%>/zxuser/userRoleList?id="+infos.id);
		grid2.load();
		uid = infos.id;
	}
	
	function add(){
		var rows = grid.getSelecteds();
		var rowsNum = rows.length;
		if(rows != null && rowsNum > 0){
			if(rowsNum > 50){
				$G.alert("单次移动条数过多");
				return;
			}
			loading();
			grid.removeRows(rows);
			grid2.addRows(rows);
			$("#datagrid2 .mini-grid-rows-content").find("table tr").removeClass("mini-grid-newRow");
			unloading();
			$G.get("btnOk").enable();
		}else{
			mini.alert("请选中一条或是多条！");
		}
	}

	function removes(e){
		var rows;
		if(e == "0"){ // 选中的行
			rows = grid2.getSelecteds();
  	  	}else if(e == "1"){ // 全部的行
			rows = grid2.getData();
		}
		var rowsNum = rows.length;
		if(rows != null && rowsNum > 0){
			if(rowsNum > 50){
				$G.alert("单次移动条数过多");
				return;
			}
			loading();
			grid2.removeRows(rows);
			grid.addRows(rows);
			$("#datagrid1 .mini-grid-rows-content").find("table tr").removeClass("mini-grid-newRow");
			unloading();
			$G.get("btnOk").enable();
		}else{
			mini.alert("请选中一条或是多条！");
		}
	}
	
	function loading() {
        mini.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '操作中...'
        });
    }
    
    function unloading() {
        setTimeout(function () {
            mini.unmask(document.body);
        }, 0);
    }
    
    //保存用户角色
    function saveUserRole(){
    	var rows = grid2.getData();
		var saveIds = [];
		if(rows != null){
			for(var i = 0; i < rows.length; i++){
				saveIds.push("'" + rows[i].id + "'");
			}
		}
		saveIds = saveIds.toString();
		
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsAsync(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setSuccessFunc(function (data) {
			if(data.flag == "1"){
				$G.alert("更新成功!");
			}else if(data.flag == "0"){
				$G.alert("更新失败!");
			}else if(data.flag == "-1"){
				$G.alert("更新出现异常!");
			}else{
				$G.alert("更新失败!");
			}
		});
      	var urlStr = "<%=root%>/zxuser/updateUserRole";
		$G.postByAjax({saveIds: saveIds, uid: uid}, urlStr, ajaxConf);
    }
    
 	// 取消  
    function onCancel(e) {
    	$G.closemodaldialog("cancel");
    } 
</script>
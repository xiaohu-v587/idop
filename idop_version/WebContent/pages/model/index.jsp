<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
  		<script src="js/ajaxfileupload.js" type="text/javascript"></script>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="流程模型控制管理" style="width:100%;height:111px;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">机构：</th>
							<td align="left">
								<input id="orgSelect" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="orgnum" textfield="orgname" valuefield="id" parentfield="upid"  
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
							</td>
		                   	<th align="right">机构号：</th>
							<td align="left">
								<input id="jgh" name="jgh" class="mini-textbox" style="width:165px;"/>
							</td>
		                    <th align="right">员工号：</th>
							<td align="left">
								<input id="userno" name="userno" class="mini-textbox" style="width:165px;"/>
							</td>
	               		</tr>
	               		<tr>
		                   	<th align="right">姓名：</th>
							<td align="left">
								<input id="name" name="name" class="mini-textbox" style="width:165px;"/>
							</td>
		                   	<th align="right"></th>
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
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="add()" plain="true">新增</a>
          	     			<a class="mini-button" iconCls="icon-edit" id="editBtn" onclick="edit()" plain="true">编辑</a>
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="del()" plain="true">删除</a>
          	     			<!-- 
          	     			<a class="mini-button" iconCls="icon-remove" id="removeBtn" onclick="deploy()" plain="true">部署</a>
          	     			 -->
                			<a class="mini-button" iconCls="icon-download"  id="download" onclick="download()" plain="true">下载</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="icon-undo" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="icon-search" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/model/modelList" style="width: 100%;height: 100%;"
		    		multiSelect="true" allowUnselect="false" autoEscape="false">
			        <div property="columns">            
			            <div type="indexcolumn" name="indexCloumn"></div>    
			            <div type="checkcolumn" name="checkCloumn"></div>    
			            <div field="id"  headerAlign="center" allowSort="true"  align="center">Id</div>                
			            <div field="key" allowSort="true" headerAlign="center" align="center">KEY</div> 
			            <div field="name"  allowSort="true" headerAlign="center" align="center">名称</div> 
			            <div field="version"  allowSort="true" headerAlign="center" align="center" >版本号</div>
			            <div field="createtime" dateFormat="yyyy/MM/dd HH:mm:ss"  allowSort="true" headerAlign="center" align="center" >创建时间</div>
			            <div field="lastupdatetime" dateFormat="yyyy/MM/dd"  allowSort="true" headerAlign="center" align="center" >最后更新时间</div>
			            <div field="metaInfo"  renderer="onMetaInfoRenderer" allowSort="true" headerAlign="center" align="center" >元数据</div>
			            <div name="action" width="60" renderer="onActionRenderer_deploy" headerAlign="center" align="center">角色用户分配</div>
			            <div field="id"  renderer="onActionRenderer" allowSort="true" headerAlign="center" align="center" >是否挂起</div>
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	
	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	
	grid.load();
	
	
	function onActionRenderer_deploy(e){
		var obj = '<a href="javascript:deploy(' + e.record.id + ')" style="img-decoration:none">部署</a>';
		return obj;
	}
	

	//新增
	function add() {
        var url = "<%=root%>/model/createModelTemplate";
        var url = "<%=root%>/actividesig";
        var bizParams = {pageType:"add"};
        $G.showmodaldialog("定义新的流程定义", url, "100%", "100%", bizParams, function(action){
	    	 grid.reload();
	    });
	}

	//编辑
	function edit(){
		var row = grid.getSelected();
		if (row) {
			var url = "<%=root%>/position/form";
			var bizParams = {pageType:"edit",id:row.id};
	        $G.showmodaldialog("编辑岗位", url, 500, 250, bizParams, function(action){
		    	 grid.reload();
		    });
		}else{
			$G.alert("请先选中一条记录！");
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
                	$G.postByAjax({"ids":ids}, "<%=root%>/model/del", ajaxConf);
              	}
            });
		}else{
			$G.alert("请选中一条记录");
		}
	}

	//重置密码
	function deploy(id){
		var ajaxConf = new GcdsAjaxConf();
    	ajaxConf.setSuccessFunc(function (){
        	grid.reload();
        });
    	$G.postByAjax({"modelId":id}, "<%=root%>/model/deploy", ajaxConf);
	}

	function showSvgTip() {
		alert('点击"编辑"链接,在打开的页面中打开控制台执行\njQuery(".ORYX_Editor *").filter("svg")\n即可看到svg标签的内容.');
	}
	
	//下载
	function download(){
		var orgnum=$G.getbyName("orgnum").getValue();
		var jgh=$G.getbyName("jgh").getValue();
		var userno=$G.getbyName("userno").getValue();
		var name=$G.getbyName("name").getValue();
		window.location="<%=root%>/user/download?orgnum=" + orgnum + "&jgh=" + jgh + "&userno=" + userno + "&name=" + name;   
	}
	
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
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
	
	function onSuspendedRenderer(e){
		var value = e.value;
		var obj = '';
		if(value)
			obj ='<a href="javascript:updateState()">激活</a>'
		if(!value)
			obj ='<a href="javascript:updateState()">挂起</a>'
		return obj;
	}
	function updateState(){
		var row = grid.getSelected();
		if(row){
			$G.GcdsConfirm("确定执行？", "提示", function(action) {
				if (action == 'ok') {
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                    	grid.reload();
                    });
                	var state = '';
                	if(row.suspended)
                		state = 'active';
        			if(!row.suspended)
        				state = 'suspend';
                	$G.postByAjax({"state":state,"processdefinitionid":row.id}, "<%=root%>/workflow/updateState", ajaxConf);
              	}
			});
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	
	function onActionRenderer(e){
		var value = e.value;
		var obj = '导出(<a href="<%=root%>/model/export?modelId='+e.record.id+'&type=bpmn" target="_blank">BPMN</a>|&nbsp;<a href="<%=root%>/model/export?modelId='+e.record.id+'&type=json" target="_blank">JSON</a>|&nbsp;<a href="javascript:;" onclick="showSvgTip()">SVG</a>)';
		return obj;
	}
		
</script>


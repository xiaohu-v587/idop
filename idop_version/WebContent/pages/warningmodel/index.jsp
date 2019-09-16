<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 预警模型管理主界面
	*
	* @author 陆磊磊
	* @date 2018-11-26    
-->
	<head>
		<title>预警模型管理主界面</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
		<style type="text/css">
			html,body {
				margin: 0;
				padding: 0;
				border: 0;
				width: 100%;
				height: 100%;
				overflow: hidden;
			}
			.search_li {
			    width: 80px;
			}
		</style>
	</head>
<body>
	<div class="nui-fit" style="overflow: hidden">
		<div style="overflow: auto;">
   		<form id="form1">
   			<div  class="search_box" width="100%">
				<ul>
                   	<li class="search_li" align="left">业务模块：</li>
					<li align="left">
						<input class="mini-combobox" width="150px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module" onvaluechanged="moduleChanged" valueField="val" textField="remark" ></input>
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">模型类型:</li>
					<li align="left">
						<input name="warning_type_code"  class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true"  
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600"  width="150px" />
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">模型编号:</li>
					<li align="left">
						<input class="mini-textbox" width="150px" id="warning_code" name="warning_code" height="25px"/>
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">模型名称:</li>
					<li align="left">
						<input class="mini-textbox" width="150px" id="warning_name" name="warning_name" height="25px"/>
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">模型状态:</li>
					<li align="left">
						<input name="is_use" id="is_use"  class="mini-combobox" width="150px" url="<%=root%>/param/getDict?key=dop_markstate" 
           	 				valueField="val" textField="remark" required="true" />
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">是否人工认定:</li>
					<li align="left">
						<input class="mini-combobox" name="is_manual_indentify" width="150px"  valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_sfrgsd"
                			id="is_manual_indentify" required="true" ></input>
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">是否需要复查:</li>
					<li align="left">
						<input class="mini-combobox" name="is_confirm" width="150px"  valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_sfsjhqr"
                			id="is_confirm"  required="true" ></input>
					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">模型维度:</li>
					<li align="left">
						<input class="mini-combobox" width="150px" nullItemText="" emptyText="请选择..."  showNullItem="false"   url="<%=root%>/param/getDict?key=warn_wd" 
           	 				id="warning_dimension" name="warning_dimension" onvaluechanged="typeValueChanged()"  valueField="val" textField="remark" required="true"></input>

					</li>
				</ul>
				<ul>
                   	<li class="search_li" align="left">是否发送短信:</li>
					<li align="left">
						<input class="mini-combobox" width="150px"  nullItemText="" ="请选择..." showNullItem="false"  url="<%=root%>/param/getDict?key=dxtz" 
           	 				id="is_key_dxtz" name="is_key_dxtz"  onvaluechanged="typeValueChanged1()"  valueField="val" textField="remark" ></input>
					</li>
				</ul>
			</div>
   		</form>
   		</div>
   		<div class="nui-toolbar" style="border-bottom:0;padding:1px;height: 32px;border-top:0;border-left:1;border-right:1;">
			<table style="width:100%;">
	    		<tr>
	    			<td style="white-space:nowrap;text-align:center;">
						<a class="nui-button"  onClick="search()" style="margin-right:50px;">查询</a>
						<a class="nui-button"  onClick="exportData()" style="margin-right:50px;">导出</a>
						<a class="nui-button"  onclick="reset()">重置</a>
	    			</td>
	    		</tr>
			</table>
		</div> 
		<div class="nui-toolbar" style="border-bottom:1;padding:1px;height: 32px;border-top:0;border-left:1;border-right:1;">
			 <table style="width:100%;">
	            <tr>
	            	<td style="white-space:nowrap;text-align:center;">
	            		<a class="nui-button"  onClick="detail()" id="detail" style="margin-right:50px;">详情</a>
	            		<a class="nui-button"  onClick="add()" id="add" style="margin-right:50px;" visible="true">新增</a>
     					<a class="nui-button"  onClick="edit()" id="edit" style="margin-right:50px;">修改</a>
						<a class="nui-button"  onClick="do_undo()" id="do_undo" style="margin-right:50px;">停用/启用</a>
						<a class="nui-button"   onclick="batchThreshold" id="batch"  style="margin-right:50px;" visible="false">批量阀值设置</a>
	    			</td>
	            </tr>
	       	</table>   
		</div>
		<div class="nui-fit" style="width:100%;">
			<div id="datagrid1"  dataField="data" class="nui-datagrid" sortMode="client" allowUnselect="false" pageSize="10"
              	style="width: 100%;height: 100%;" url="<%=root%>/warningmodel/getList" multiSelect="false" showPageIndex="true"
              	 autoEscape="false" showPager="true" >
            	<div property="columns">  
            	   <div type="checkcolumn" name="checkColumn" width="35"></div>
            	   <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	               <div field="busi_module_re" width="100" align="center"  headerAlign="center">业务模块</div>            
	               <div field="warning_type_re" width="100" align="center" headerAlign="center">模型类型</div>
	               <div field="warning_code" width="100" align="center" headerAlign="center">模型编号</div>                  
	               <div field="warning_name" width="100" align="center" headerAlign="center" >模型名称</div>
	               <div field="is_use" width="120" align="center" headerAlign="center" renderer="onWarningModelStateRender">模型状态</div>
	               <div field="warning_level" width="120" align="center" headerAlign="center" renderer="onWarningLevelRender">模型等级</div>
	               <div field="is_manual_indentify" width="120" align="center" headerAlign="center">是否需要认定</div>
	               <div field="check_level" width="120" align="center" headerAlign="center">核查层级</div>
	               <div field="is_confirm" width="120" align="center" headerAlign="center">是否需要复查</div>
	               <div field="warning_dimension" width="100" align="center" headerAlign="center" >模型维度</div>
	               <div field="is_key_dxtz" width="100" align="center" headerAlign="center" >是否发送短信</div>
           		</div>
 			</div>
		</div>
   	</div>


	
  <script type="text/javascript">
	mini.parse();
	var form = $G.getForm("form1");
	var grid=$G.get("datagrid1");
	grid.load();
	
	
	

	
	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("warning_type_code").setValue("");
		var url="<%=root%>/warningmodel/getWarningtypecodeList?val="+val;
		$G.getbyName("warning_type_code").setUrl(url);
		
	}
	
	//详情
	function detail(){
		var row=grid.getSelected();
		if(row){
			var url="<%=root%>/warningmodel/detail";
	        var bizParams={warning_code:row.warning_code};
	        $G.showmodaldialog("详情", url, 700, 650, bizParams, function(action){
	        	grid.reload();
		    });
		}else{
			$G.GcdsAlert("请先选中一条记录");
		}
	}
	
	
	//新增
	function add(){
		var pageType="add";
		var url="<%=root%>/warningmodel/add?pageType="+pageType;
        var bizParams={pageType:"add"};
        $G.showmodaldialog("新增", url, 700, 650, bizParams, function(action){
        	grid.reload();
	    });
	}
	
	//停用/启用
	function do_undo(){
		var row=grid.getSelected();
		if(row){
			var state=row.is_use;// state=0 停用  state=1 启用
			var warning_code=row.warning_code;
			if(state=='1'){
				$G.GcdsConfirm("确定停用？", "提示", function(action) {
					if (action == 'ok') {
		            	
		            	var ajaxConf = new GcdsAjaxConf();
		            	ajaxConf.setSuccessFunc(function (){
		            		grid.reload();
		                });
		            	$G.postByAjax({"warning_code":warning_code,is_use:'0'}, "<%=root%>/warningmodel/changeWarningModelState", ajaxConf);
		          	}
		        });	
			}else{
				$G.GcdsConfirm("确定启用？", "提示", function(action) {
					if (action == 'ok') {
		            	var ajaxConf = new GcdsAjaxConf();
		            	ajaxConf.setSuccessFunc(function (){
		            		grid.reload();
		                });
		            	$G.postByAjax({"warning_code":warning_code,is_use:'1'}, "<%=root%>/warningmodel/changeWarningModelState", ajaxConf);
		          	}
		        });
				
			}
		}else{
			$G.GcdsAlert("请先选择一条记录");
		}
		
	}
	
	//编辑
	function edit(){
		var row = grid.getSelected();
		var pageType="edit";
		if(row){
			var url="<%=root%>/warningmodel/edit?pageType="+pageType;
	        var bizParams={warning_code:row.warning_code,pageType:pageType};
	        $G.showmodaldialog("编辑", url, 700, 650, bizParams, function(action){
	        	grid.reload();
		    });
		}else{
			$G.GcdsAlert("请先选择一条记录");
		}
	}
	
	//预警模型状态 启用、停用
	function onWarningModelStateRender(e){
		var textVal = mini.getDictText("dop_markstate",e.value);
		return textVal;
	}
	
	//预警模型等级
	function onWarningLevelRender(e){
		var textVal = mini.getDictText("dop_warning_lvl",e.value);
		return textVal;
		
	}
	
	
	//查询
	function search(){
		grid.load(form.getData());
	}
	
	//导出
	function exportData(){
		var busi_module = $G.getbyName("busi_module").getValue(); 
		var warning_type_code = $G.getbyName("warning_type_code").getValue();
		var warning_code = $G.getbyName("warning_code").getValue();
		var warning_name = $G.getbyName("warning_name").getValue();
		var url = "<%=root%>/warningmodel/download?busi_module="+busi_module+"&warning_type_code="+warning_type_code+"&warning_code="+warning_code+"&warning_name="+warning_name;
		window.location=url;
	}
	
	function reset(){
		form.reset();
	}
  	
  	
  </script>
</body>
</html>
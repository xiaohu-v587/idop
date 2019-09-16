<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 数据集市主界面
	*
	* @author 陆磊磊
	* @date 2018-11-20
-->
	<head>
		<title>数据集市主界面</title>
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
	   			<div class="search_box" width="100%">
	   				<ul>
	   					<li class="search_li">业务模块：</li>
	                	<li align="left">
	                		<input class="mini-combobox" width="180px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
	           	 				id="busi_module" name="busi_module" onvaluechanged="moduleChanged" valueField="val" textField="remark" ></input>
	                	</li>
	                </ul>	
	                <ul>
	                	<li class="search_li">业务类型:</li>
	                	<li>
		                	<input name="sub_busi_code" class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true" oncloseclick="onCloseClick" 
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600" style="width:180px;" />
	                	</li>
	                </ul>
	               <ul> 	
	                	<li class="search_li">指标编号:</li>
	                	<li>
	                		<input class="mini-textarea" width="180px"  id="mark_code" name="mark_code" height="25px"/>
	                	</li>
	                </ul>
	                <ul>
	                	<li class="search_li">指标名称:</li>
	               		<li colspan="1">
							<input class="mini-textarea" width="180px"  id="mark_name" name="mark_name" height="25px"/>
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
	            		<a class="nui-button"  onClick="add()" id="add" style="margin-right:50px;">新增</a>
     					<a class="nui-button"  onClick="edit()" id="edit" style="margin-right:50px;">编辑</a>
						<a class="nui-button"  onClick="do_undo()" id="do_undo" style="margin-right:50px;">停用/启用</a>
						<a class="nui-button"  onclick="adjust()" id="adjust"  style="margin-right:50px;">调整展示层级</a>
						<span class="separator"></span>
						<a class="nui-button"   onclick="setKey()"  id="setKey" style="margin-right:50px;">关键指标设置</a>
<!-- 						<a class="nui-button" iconCls="icon-undo"  onclick="checkall()"  id="checkall" style="margin-right:50px;">全选</a> -->
<!-- 						<a class="nui-button" iconCls="icon-undo"  onclick="selectnone()"  id="noneselect" style="margin-right:50px;">全不选</a> -->
						<a class="nui-button"   onclick="invert()"  id="invert" style="margin-right:50px;" visible="false">反选</a>
						<a class="nui-button"   onclick="confirmSetkey()"  id="confirmSetkey" style="margin-right:50px;" visible="false">设为关键指标</a>
						<a class="nui-button"   onclick="confirmRemovekey()"  id="confirmRemovekey" style="margin-right:50px;" visible="false">取消关键指标</a>
						<a class="nui-button"   onclick="close_key()"  id="close_key" style="margin-right:50px;" visible="false">关闭</a>
	    			</td>
	            </tr>
	       	</table>   
		</div>
		<div class="nui-fit" style="width:100%;">
			<div id="datagrid1"  dataField="data" class="nui-datagrid" sortMode="client" allowUnselect="false" pageSize="10"
              	style="width: 100%;height: 100%;" url="<%=root%>/datamart/getList" multiSelect="false" showPageIndex="true"
              	onselectionchanged="onSelectionChanged" onload="controlBtnState" autoEscape="false" showPager="true" >
            	<div property="columns">  
            	   <div type="checkcolumn" name="checkColumn" width="35"></div>
            	   <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	               <div field="busi_module_re" width="100" align="center"  headerAlign="center">业务模块</div>            
	               <div field="sub_re" width="100" align="center" headerAlign="center">业务类型</div>
	               <div field="mark_code" width="100" align="center" headerAlign="center">指标编号</div>                  
	               <div field="mark_name" width="100" align="center" headerAlign="center" >指标名称</div>
	               <div field="is_use" width="120" align="center" headerAlign="center" renderer="onMarkStateRender">指标状态</div>
	               <div field="source" width="120" align="center" headerAlign="center" renderer="onMarkSourceRender">指标来源</div>
	               <div field="display_level" width="120" align="center" headerAlign="center" renderer="onDisplayLevelRender">展示层级</div><!--  -->
	               <div field="is_key_mark" width="120" align="center" headerAlign="center" renderer="onKeyRender">是否关键指标</div>
           		</div>
 			</div>
		</div>
   	</div>


	
  <script type="text/javascript">
	mini.parse();
	//
	var form = $G.getForm("form1");
	var grid=$G.get("datagrid1");
	grid.load();
	var selectIndex;
	
	
	//控制按钮状态
	function controlBtnState(){
		$G.get("do_undo").disable();
		$G.get("edit").disable();
		$G.get("adjust").disable();
		$G.get("setKey").disable();
		if($G.get("close_key").visible==true){
			if(grid.getSelecteds().length>0){
				$G.get("confirmSetkey").enable();
				$G.get("confirmRemovekey").enable();
				$G.get("invert").enable();
			}else{
				$G.get("confirmSetkey").disable();
				$G.get("confirmRemovekey").disable();
				$G.get("invert").disable();
			}
			
		}
		
	}
	


	//行选中时触发
	function onSelectionChanged(e){
		var rows=grid.getSelecteds();
		if($G.get("datagrid1").showPager==true){
			selectIndex=grid.indexOf(rows[0]);
		}
		if(rows.length>0){
			if($G.get("confirmSetkey").visible==true){
				$G.get("confirmSetkey").enable();
				$G.get("invert").enable();
				$G.get("confirmRemovekey").enable();
				
			}else{
				$G.get("do_undo").enable();
				$G.get("edit").enable();
				$G.get("adjust").enable();
				$G.get("setKey").enable();
			}			
		}else{
			$G.get("do_undo").disable();
			$G.get("edit").disable();
			$G.get("adjust").disable();
			$G.get("confirmSetkey").disable();
			$G.get("confirmRemovekey").disable();
		}
	}

	
	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("sub_busi_code").setValue("");
		var url="<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+val;
		$G.getbyName("sub_busi_code").setUrl(url);
	}
	
	//关键指标设置
	function setKey(){
		$G.get("datagrid1").setMultiSelect(true);
		$G.get("datagrid1").setAllowUnselect(true);
		$G.get("datagrid1").updateColumn("checkColumn");
		$G.get("datagrid1").setShowPager(false);
		$G.get("invert").setVisible(true);
		$G.get("confirmSetkey").setVisible(true);
		$G.get("confirmRemovekey").setVisible(true);
		$G.get("close_key").setVisible(true);
		$G.get("setKey").setVisible(false);
		$G.get("do_undo").disable();
		$G.get("edit").disable();
		$G.get("adjust").disable();
		
		
	}
	
	//关闭关键指标设置
	function close_key(){
		$G.get("datagrid1").setMultiSelect(false);
		$G.get("datagrid1").setAllowUnselect(false);
		$G.get("datagrid1").updateColumn("checkColumn");
		$G.get("datagrid1").setShowPager(true);
		if(grid.getSelecteds().length>1){
			grid.clearSelect();
			grid.select(grid.getRow(selectIndex));
		}
		$G.get("invert").setVisible(false);
		$G.get("confirmSetkey").setVisible(false);
		$G.get("confirmRemovekey").setVisible(false);
		$G.get("close_key").setVisible(false);
		$G.get("setKey").setVisible(true);
		if(grid.getSelected()){
			$G.get("do_undo").enable();
			$G.get("edit").enable();
			$G.get("adjust").enable();
			$G.get("setKey").enable();
		}else{
			$G.get("do_undo").disable();
			$G.get("edit").disable();
			$G.get("adjust").disable();
			$G.get("setKey").disable();
		}
	}
	
	//删除
	function remove(){
		var row=grid.getSelected();
		var source=row.source;
		$G.GcdsConfirm("确定删除选中记录？", "提示", function(action) {
			if (action == 'ok') {
				if(source=="3"){
					$G.alert("指标来源系统数据，无法删除");
				}
            	var id=row.id;
            	var ajaxConf = new GcdsAjaxConf();
            	ajaxConf.setSuccessFunc(function (){
            		grid.reload();
                });
            	$G.postByAjax({"id":id}, "<%=root%>/datamart/remove", ajaxConf);
          	}
        });
		
	}
	
	//停用/启用
	function do_undo(){
		var row=grid.getSelected();
		var state=row.is_use;// state=0 停用  state=1 启用
		if(state=='1'){
			$G.GcdsConfirm("确定停用？", "提示", function(action) {
				if (action == 'ok') {
	            	var mark_code=row.mark_code;
	            	var ajaxConf = new GcdsAjaxConf();
	            	ajaxConf.setSuccessFunc(function (){
	            		grid.reload();
	                });
	            	$G.postByAjax({"mark_code":mark_code,is_use:'0'}, "<%=root%>/datamart/changeMarkState", ajaxConf);
	          	}
	        });	
		}else{
			$G.GcdsConfirm("确定启用？", "提示", function(action) {
				if (action == 'ok') {
	            	var mark_code=row.mark_code;
	            	var ajaxConf = new GcdsAjaxConf();
	            	ajaxConf.setSuccessFunc(function (){
	            		grid.reload();
	                });
	            	$G.postByAjax({"mark_code":mark_code,is_use:'1'}, "<%=root%>/datamart/changeMarkState", ajaxConf);
	          	}
	        });
			
		}
	}
	
	
	//调整展示层级
	function adjust(){
		var row = grid.getSelected();
        var url="<%=root%>/datamart/adjustDisplayLevel";
        var bizParams={mark_code:row.mark_code,busi_module:row.busi_module,sub_busi_code:row.sub_busi_code,mark_name:row.mark_name,display_level:row.display_level};
        $G.showmodaldialog("调整展示层级", url, 600, 300, bizParams, function(action){
        	grid.reload();
	    });
    	
	}
	
	
	//编辑
	function edit(){
		var row = grid.getSelected();
        var url="<%=root%>/datamart/edit";
        var bizParams={mark_code:row.mark_code};
        $G.showmodaldialog("编辑", url, 750, 550, bizParams, function(action){
        	grid.reload();
	    });
		
	}
	
	//编辑
	function add(){
		//var row = grid.getSelected();
        var url="<%=root%>/datamart/add";
        //var bizParams={mark_code:row.mark_code};
        $G.showmodaldialog("新增", url, 750, 550, "", function(action){
        	grid.reload();
	    });
		
	}
	
	// 设为关键指标
	function confirmSetkey(){
		var rows=grid.getSelecteds();
		top.okBtnPosition="left";
		$G.GcdsConfirm("确定将选中记录设为关键指标？", "提示", function(action) {
			if (action == 'ok') {
            	var ids = "";
                for(var index = 0;index < rows.length;index++){
					if(ids == ""){
                    	ids = rows[index].mark_code;
                  	} else {
                    	ids += "," + rows[index].mark_code;
                  	}
                }
            	var ajaxConf = new GcdsAjaxConf();
            	ajaxConf.setSuccessFunc(function (){
            		grid.reload();
                });
            	$G.postByAjax({"ids":ids,"is_key_mark":"1"}, "<%=root%>/datamart/setKey", ajaxConf);
          	}
        });
	}
	
	
	// 取消关键指标
	function confirmRemovekey(){
		var rows=grid.getSelecteds();
		top.okBtnPosition="left";
		$G.GcdsConfirm("确定将选中记录取消关键指标？", "提示", function(action) {
			if (action == 'ok') {
            	var ids = "";
                for(var index = 0;index < rows.length;index++){
					if(ids == ""){
                    	ids = rows[index].mark_code;
                  	} else {
                    	ids += "," + rows[index].mark_code;
                  	}
                }
            	var ajaxConf = new GcdsAjaxConf();
            	ajaxConf.setSuccessFunc(function (){
            		grid.reload();
                });
            	$G.postByAjax({"ids":ids,"is_key_mark":"0"}, "<%=root%>/datamart/setKey", ajaxConf);
          	}
        });
	}
	
	
	//反选
	function invert(){
		var rows1=grid.getSelecteds();
		grid.selectAll(false);
		grid.deselects(rows1,false);
	}
	
	
	
	
	//指标状态 启用、停用
	function onMarkStateRender(e){
		var textVal = mini.getDictText("dop_markstate",e.row.is_use);
		return textVal;
		
	}
	
	//指标来源
	function onMarkSourceRender(e){
		var textVal = mini.getDictText("dop_marksource",e.row.source);
		return textVal;
	}
	
	//是否关键指标
	function onKeyRender(e){
		var textVal = mini.getDictText("dop_iskey",e.value);
		return textVal;
	}
	
	
	// 展示层级中文显示
	function onDisplayLevelRender(e){
		var d1=e.value.split(",")[0];
		var d2=e.value.split(",")[1];
		var d3=e.value.split(",")[2];
		var d4=e.value.split(",")[3];
		var d="";
		var list=[];
		if(d1=="1"){
			list.push("省行");
		}
		if(d2=="1"){
			list.push("分行");
		}
		if(d3=="1"){
			list.push("管辖支行");
		}
		if(d4=="1"){
			list.push("网点");
		}
		for(var i=0;i<list.length;i++){
			if(d==""){
				d=list[i];
			}else{
				d+=","+list[i];
			}
		}
		return d;
		
		
	}
	 
	
	//查询
	function search(){
		grid.load(form.getData());
	}
	
	//导出
	function exportData(){		
		var busi_module = $G.getbyName("busi_module").getValue(); 
		var sub_busi_code = $G.getbyName("sub_busi_code").getValue();
		var mark_code = $G.getbyName("mark_code").getValue();
		var mark_name = $G.getbyName("mark_name").getValue();
		var url = "<%=root%>/datamart/download?busi_module="+busi_module+"&sub_busi_code="+sub_busi_code+"&mark_code="+mark_code+"&mark_name="+mark_name;
		window.location=url;
	}
	
	//重置
	function reset(){
		form.reset();
	}
  	
  	
  </script>
</body>
</html>
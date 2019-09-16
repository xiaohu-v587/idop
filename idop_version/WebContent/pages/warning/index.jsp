<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
 		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		ul,li{
  			list-style:none;
  		}
  		.search_box ul{
  			float:left;
  			width:300px;
  		    margin:5px 0 5px 0;
  		    padding-left:20px;
  		   
  		} 
  		.search_box ul li{
  			float:left;
  		} 
  		.search_li{
  			width:60px;
  			
  		}
  		</style>
	</head>
	<%
	Object model = request.getAttribute("model");
	
	%>
	<body>
					<input id = "model" name = "model" class = "nui-hidden" value="<%=model%>"/>
				<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;"  onactivechanged="onActivechanged()" 
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
	                 <div id="tab1" name="org" title="待认定"  style="width: 100%; height: 100%;">
		       				<form id="form1">
		       				<table>
		          				<tr>
		          					<th >业务模块：</th>
					            	<td >
						                <input id="ywtype" name="ywtype" width="155px"  class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype" value = "<%=model%>"
						                 onvaluechanged="onywTypeChanged1" textfield="remark" valuefield="val" emptyText="请选择..." />
					            	</td>
					            	<%-- <th>业务类型：</th>
		          					<td>
		          						 <input id="ywlx" name="ywlx" class="mini-treeselect" url="<%=root%>/warning/getywTypeList" dataField="datas" 
									textfield="remark" valuefield="id" parentfield="upid" emptyText="请选择..."
									valueFromSelect="false" multiSelect="false"  expandOnLoad="0" 
									allowInput="false" showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
									popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
		          					</td> --%>
		          					<th >机构名称：</th>
		          					<td >								
		          						<input id="orgSelect" class="mini-treeselect"  width="155px"   url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged="onOrgChange"
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600"/>
		          					</td>
		          					<th >预警时间：</th>
		          					<td colspan="3" style="width:300px;">
										<input id="start_time" name="start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;"/>
										<span style="color:white;float:left;">至</span>
										<input id="end_time" name="end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;"/>
		          					</td>
		          				</tr>
		          				<tr>
		          				<th >预警类型：</th>
				          			<td >	
				          				<input name="warning_type" id = "warning_type1" width="155px"  class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
				                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
				                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
											allowInput="false" showClose="true" oncloseclick="onCloseClick" 
											showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
											popupHeight="470" popupMaxHeight="600" onvaluechanged="warnTypechange1"/>
									</td>
						        <th>预警名称：</th>
								<td>
							       <input class="mini-combobox" width="155px"  nullItemText="请选择..."  multiSelect="false" emptyText="请选择..."  url="" 
	           	 				id="warn_name1" name="warn_name"    valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
						        </td>
						        <th >业务时间：</th>
	          					<td colspan="3" style="width:300px;">
									<input id="biz_start_time" name="biz_start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;"/>
									<span style="color:white;float:left;">至</span>
									<input id="biz_end_time" name="biz_end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;"/>
	          					</td>
							</tr>
		        			</table>
		        			</form>
		        			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
		        				<table style="width:100%;">
					        		<tr>
					        			<td style="white-space:nowrap;text-align:center;">
					 						<a class="nui-button"  onClick="search()" style="margin-right:50px;">查询</a>
					 						<a class="nui-button"  id="addBtn" onclick="reset()">重置</a>
					        			</td>
					        		</tr>
		        				</table>
		    				</div> 
		    				<hr/>
		    				<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
		        				<table style="width:100%;">
					        		<tr>
					        			<td style="white-space:nowrap;text-align:center;">
					 						<a class="nui-button" iconCls="" onClick="singleConfirm()" style="margin-right:20px;">单笔认定</a>
					 						<a class="nui-button" iconCls="" onclick="multiConfirm()">批量认定</a>
					 						<a class="nui-button" iconCls="" onClick="multiNegate()" style="margin-left:20px;">批量否定</a>
					        			</td>
					        		</tr>
		        				</table>
		    				</div> 
		          			<div class="nui-fit"  style="width: 100%; height: 100%;">
		               			<div id="datagrid1"  idfield="id" class="nui-datagrid" sortMode="client" allowUnselect="false" multiSelect="true"
		               			 sizeList="[10,20,50,100,500]" pageSize="10" 
					               	style="width: 100%;height: 100%;" url="<%=root%>/warning/getList" autoEscape="false"  >
					             	<div property="columns">  
					             	   <div type="checkcolumn" name="checkColumn" width="35"></div>
						               <div field="create_time" width="100" align="center" dateFormat="yyyy-MM-dd" headerAlign="center">预警时间</div>   
						               <div field="biz_time" width="100" align="center" dateFormat="yyyy-MM-dd" headerAlign="center">业务时间</div>          
						               <div field="orgname" width="100" align="center" headerAlign="center">机构名称</div>
						               <div field="flownum" width="100" align="center" headerAlign="center">预警编号</div>
						               <div field="warning_name" width="100" align="center" headerAlign="center">预警名称</div>                  
						               <div field="busi_module" width="100" align="center" headerAlign="center" renderer="onywTypeRender">业务模块</div>
						               <div field="warning_type_code" width="120" align="center" headerAlign="center" renderer="onWarningTypeRender">预警类型</div>
						               <div field="warning_level" width="120" align="center" headerAlign="center" renderer="onWarningLevelRender">预警等级</div>
						               <div field="warning_status" width="120" align="center" headerAlign="center" renderer="onWarningStatusRender">预警状态</div>
					            	</div>
		         				</div>
		          			</div> 
					</div>
					<div id="tab2" name="position"  title="已认定" >
							<form id="form2">
								<table >
			          				<tr>
		          					<th>业务模块：</th>
					            	<td>
						                <input id="ywtype" name="ywtype" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype"  value = "<%=model%>"
						                 onvaluechanged="onywTypeChanged2" textfield="remark" valuefield="val" emptyText="请选择..." style="width:155px;"/>
					            	</td>
		          					<th>认定状态：</th>
		          					<td>								
		          						<input name="signal_stat" class="nui-combobox" url="<%=root %>/param/getDict?key=dop_signal_stat" 
		          						valuefield="val" textfield="remark" style="width:155px;"/>
		          					</td>
		          					<th style="float: right;">预警时间：</th>
		          					<td colspan="3">
										<input id="start_time" name="start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:145px;"/>
										<span style="color:white;float:left;">至</span>
										<input id="end_time" name="end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:145px;"/>
		          					</td>
		          					<th style="float: right;">业务时间：</th>
		          					<td colspan="3">
										<input id="biz_start_time" name="biz_start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:145px;"/>
										<span style="color:white;float:left;">至</span>
										<input id="biz_end_time" name="biz_end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:145px;"/>
		          					</td>
		          				</tr>
		          				<tr>
								<th>预警类型：</th>
								<td>
							      <input name="warning_type" id = "warning_type2"  class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
			                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
			                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" 
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:155px;" onvaluechanged="warnTypechange2"/>
						        </td>
						        <th>预警名称：</th>
								<td>
							       <input class="mini-combobox" width="155px"  nullItemText="请选择..."  multiSelect="false" emptyText="请选择..."  url="" 
	           	 				id="warn_name2" name="warn_name"    valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
						        </td>
						         <th>机构名称：</th>
								<td>
							      <input id="orgSelect" class="mini-treeselect"  width="300px"   url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged="onOrgChange"
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600"/>
						        </td>
							</tr>
			        			</table>
		        			</form>
		        			<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
		        				<table style="width:100%;">
					        		<tr>
					        			<td style="white-space:nowrap;text-align:center;">
					 						<a class="nui-button" onClick="searchHave()" style="margin-right:50px;">查询</a>
					 						<a class="nui-button" id="addBtn" onclick="resetHave()">重置</a>
					        			</td>
					        		</tr>
		        				</table>
		    				</div> 
		    				<hr/>
		    				<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
								 <table style="width:100%;">
						            <tr>
						            	<th style="text-align:left;padding-left:50px;">已认定预警信号明细</th>
						                <td style="white-space:nowrap;text-align:right;padding-right:50px;">
						                	<a class="nui-button" iconCls="" onclick="indentifyDetail()">认定详情</a>  
						                </td>
						            </tr>
						       	</table>   
							</div>
		          			<div class="nui-fit" style="width:100%;">
		               			<div id="datagrid2"  idfield="id" class="nui-datagrid" sortMode="client" allowUnselect="false"
					               	style="width: 100%;height: 100%;" url="<%=root%>/warning/getHaveList"  multiSelect="true" autoEscape="false"  >
					             	<div property="columns">  
					             	   <div field="create_time" width="100" align="center" dateFormat="yyyy-MM-dd" headerAlign="center">预警时间</div> 
					             	   <div field="biz_time" width="100" align="center" dateFormat="yyyy-MM-dd" headerAlign="center">业务时间</div> 
					             	   <div field="orgname" width="100" align="center" headerAlign="center">机构名称</div>
						               <div field="flownum" width="100" align="center" headerAlign="center">预警编号</div>
						               <div field="warning_name" width="100" align="center" headerAlign="center">预警名称</div>                  
						               <div field="busi_module" width="100" align="center" headerAlign="center" renderer="onywTypeRender">业务模块</div>
						               <div field="warning_type_code" width="120" align="center" headerAlign="center" renderer="onWarningTypeRender">预警类型</div>
						               <div field="warning_level" width="120" align="center" headerAlign="center" renderer="onWarningLevelRender">预警等级</div>
						               <div field="warning_status" width="120" align="center" headerAlign="center" renderer="onWarningStatusRender">预警状态</div>
						               <div field="indentify_status" width="120" align="center" headerAlign="center" renderer="onIndentifyStatusRender">认定状态</div>
					            	</div>
		         				</div>
		          			</div> 
					</div>
				</div>
	</body>
</html>

<script type="text/javascript">
	$G.parse();
	var tabs = $G.get("tabs1");
	var tree = $G.get("tree1");
	var grid1 = $G.get("datagrid1");
	var grid2 = $G.get("datagrid2");
	var form1 = $G.getForm("form1");
	var form2 = $G.getForm("form2");
	//$G.get("ywlx").load();
	var model = $G.get("model").getValue();
	grid1.load({"ywtype":model});
	grid2.load({"ywtype":model});


    //单笔认定
    function singleConfirm(){
    	var row = grid1.getSelected();
    	if(row){
    		var flownum = row.flownum;
            var url="<%=root%>/warning/singleConfirm?flownum="+flownum;
	        var bizParams = { pageType: "singleConfirm", flownum:flownum};
	        $G.showmodaldialog("单笔认定", url, 1000, 550, bizParams, function(action){
	        	grid1.reload();
		    });
    	}else{
    		$G.alert("请先选择一行数据！");
    	}
	}
    //批量认定
    function multiConfirm(){
    	var rows = grid1.getSelecteds();
    	if(rows.length>0){
    		var warning_codes = "";
    		for(var index = 0;index < rows.length;index++){
    			if(warning_codes == ""){
    				warning_codes = rows[index].flownum;
    			} else {
    				warning_codes += "," + rows[index].flownum;
    			}
    		}
    		var url="<%=root%>/warning/multiConfirm";
    		var bizParams = { pageType: "multiConfirm", warning_codes:warning_codes};
    		$G.showmodaldialog("批量认定", url, 600, 230, bizParams, function(action){
    			grid1.reload();
    		});
    	}else{
    		$G.alert("请至少选择一行数据！");
    	}
    }
    //批量否定
    function multiNegate(){
    	var rows = grid1.getSelecteds();
    	if(rows.length>0){
<%--    		top.okBtnPosition="left";
    		$G.GcdsConfirm("确定批量否定选中记录？", "批量否定提示", function(action) {
				if (action == 'ok') {
                	var ids = "";
	                for(var index = 0;index < rows.length;index++){
						if(ids == ""){
	                    	ids = rows[index].flownum;
	                  	} else {
	                    	ids += "," + rows[index].flownum;
	                  	}
	                }
                	var ajaxConf = new GcdsAjaxConf();
                	ajaxConf.setSuccessFunc(function (){
                		grid1.reload();
                    });
                	$G.postByAjax({"ids":ids}, "<%=root%>/warning/multiSave", ajaxConf);
              	}
            });--%>
            var warning_codes = "";
    		for(var index = 0;index < rows.length;index++){
    			if(warning_codes == ""){
    				warning_codes = rows[index].flownum;
    			} else {
    				warning_codes += "," + rows[index].flownum;
    			}
    		}
    		var url="<%=root%>/warning/multiDisConfirm";
    		var bizParams = { pageType: "multiDisConfirm", warning_codes:warning_codes};
    		$G.showmodaldialog("批量否定", url, 600, 230, bizParams, function(action){
    			grid1.reload();
    		});
    	}else{
    		$G.alert("请至少选择一行数据！");
    	}
    }
    //已认定预警详情
    function indentifyDetail(){
    	var row = grid2.getSelected();
    	if(row){
    		var flownum = row.flownum;
            var url="<%=root%>/warning/singleConfirm";
	        var bizParams = { pageType: "indentifyDetail", flownum:flownum};
	        $G.showmodaldialog("认定详情", url, 1000, 550, bizParams, function(action){
	        	grid2.reload();
		    });
    	}else{
    		$G.alert("请先选择一行数据！");
    	}
    }
	
	//查询
	function search(){
		var data = form1.getData();
		grid1.load(data);
	}

	//重置
	function reset(){
		form1.reset();
	}
	
	//查询
	function searchHave(){
		var data = form2.getData();
		grid2.load(data);
	}

	//重置
	function resetHave(){
		form2.reset();
	}
	
	
	
	function onActionRenderer(e){
		var obj = '<a href="javascript:userFun()" style="img-decoration:none"><img src="<%=root%>/resource/nui/themes/icons/user.png" border="0"></a>';
		return obj;
	}

	function onActivechanged(e){
		/* 
		var node = tree.getSelectedNode();
		var tab = tabs.getActiveTab();
    	if(tab.name == "org")grid.load({ pid: node.orgnum}); 
    	if(tab.name == "position")position_grid.load({orgnum: node.orgnum}); */
	}
	function onywTypeChanged1(e){
		$G.get("warning_type1").setValue("");
		$G.get("warn_name1").setValue("");
		var url="<%=root%>/warning/getWarningTypeList?val="+e.value;
		$G.get("warning_type1").setUrl(url);
		//$G.get("ywlx").select(1);
	}
	function onywTypeChanged2(e){
		$G.get("warning_type2").setValue("");
		$G.get("warn_name2").setValue("");
		var url="<%=root%>/warning/getWarningTypeList?val="+e.value;
		$G.get("warning_type2").setUrl(url);
		//$G.get("ywlx").select(1);
	}
	function onywTypeRender(e){
		if(e.row.out_date==1){
			e.rowStyle="color:red;";
		}
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	}
	/* function onWarningTypeRender(e){
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	} */
	function onWarningLevelRender(e){
		var textVal = mini.getDictText("dop_warning_lvl",e.value);
		return textVal;
	}
	function onWarningStatusRender(e){
		var textVal = mini.getDictText("dop_warning_sta",e.value);
		return textVal;
	}
	function onIndentifyStatusRender(e){
		var textVal = mini.getDictText("dop_indent_stat",e.value);
		return textVal;
	}	
	function warnTypechange1(e){
		var warning_type = $G.get("warning_type1").getValue();
		$G.get("warn_name1").setValue("");
		var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
		$G.get("warn_name1").setUrl(url);
		
	}
	function warnTypechange2(e){
		var warning_type = $G.get("warning_type2").getValue();
		$G.get("warn_name2").setValue("");
		var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
		$G.get("warn_name2").setUrl(url);
		
	}
	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }
</script>
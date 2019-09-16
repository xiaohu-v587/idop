<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
 		<%
	Object model = request.getAttribute("model");
	Object flag = request.getAttribute("flag");
	
	%>
	<body>
		<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
			   	<div class="nui-fit" style="overflow: hidden">
			   		<input id = "model" name = "model" class = "nui-hidden" value="<%=model%>"/>
					<input id = "flag" name = "flag" class = "nui-hidden" value="<%=flag%>"/>
			   		<form id="form1">
				   		<table   >
							<tr>
			                   	<th style="width:8%">业务模块：</th>
						        <td>
							        <input id="ywtype" name="ywtype" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype"  value="<%=model%>"
							                 textfield="remark" valuefield="val" emptyText="请选择..." style="width:100%;" onvaluechanged="onywTypeChanged"/>
						        </td>
			                   
								<th  style="width:8%">复查状态：</th>
								<td align="left">
									<input name="approv_stat" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_approv_stat" 
			          					value="0"	textfield="remark" valuefield="val" emptyText="请选择..." style="width:100%;"/>
								</td>
								<th style="width:8%">预警时间：</th>
			          			<td colspan="3" >
									<input id="start_time" name="start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:36%;"/>
									<span style="color:white;float:left;">至</span>
									<input id="end_time" name="end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:36%;"/>
			          			</td>
			          			<th style="width:8%">业务时间：</th>
			          			<td colspan="3">
									<input id="biz_start_time" name="biz_start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:36%;"/>
									<span style="color:white;float:left;">至</span>
									<input id="biz_end_time" name="biz_end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:36%;"/>
			          			</td>
							</tr>
							<tr>
								<th style="width:8%">预警类型：</th>
								<td>
							      <input name="warning_type" id = "warning_type"  class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
			                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
			                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" 
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:100%;" onvaluechanged="warnTypechange"/>
						        </td>
						        <th style="width:8%">预警名称：</th>
								<td>
							       <input class="mini-combobox" width="100%"  nullItemText="请选择..."  multiSelect="false" emptyText="请选择..."  url="" 
	           	 				id="warn_name" name="warn_name"    valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
						        </td>
						       	<th style="width:8%" >机构名称：</th>
								<td align="left">
									<input id="orgSelect" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged="onOrgChange"
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:100%;"/>
								</td>
							</tr>
				   		</table>
			   		</form>
			   		<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
			        	<table style="width:100%;">
			        		<tr>
						        <td style="white-space:nowrap;text-align:center;">
						 			<a class="nui-button" iconCls="" onClick="search()" style="margin-right:50px;">查询</a>
						 			<a class="nui-button" iconCls="" onClick="reset()">重置</a>
						 		</td>
						    </tr>
			        	</table>
			    	</div> 
			   	</div>
		</div>
		<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/warning_approv/getList" style="width: 100%;height: 100%;" 
		    		allowRowSelect="false" allowHeaderWrap="true">
			        <div property="columns">            
			        	<div field="flownum" width="" headerAlign="center"  align="center" renderer="onRender">操作</div>
			            <div field="create_time" width="" dateFormat="yyyy-MM-dd" headerAlign="center"  align="center">预警时间</div>                
			            <div field="biz_time" width="" dateFormat="yyyy-MM-dd" headerAlign="center"  align="center">业务时间</div>  
			            <div field="flownum" width=""  headerAlign="center" align="center">预警编号</div>
			            <div field="warning_code" width="100" visible="false">预警编码</div>           
			            <div field="warning_name" width="" headerAlign="center" align="center">预警名称</div> 
			            <div field="orgname" width="" headerAlign="center" align="center">机构名称</div> 
			            <div field="busi_module" width="" headerAlign="center" align="center" renderer="onywTypeRender">业务模块</div> 
			            <div field="warning_type_code" width="" headerAlign="center" align="center" renderer="onWarningTypeRender">预警类型</div>
			            <div field="warning_level" width="" headerAlign="center" align="center" renderer="onWarningLevelRender">预警等级</div>
			            <div field="warning_status" width="" headerAlign="center" align="center" renderer="onWarningStatusRender">预警状态</div>
			            <div field="last_check_stat" width="" headerAlign="center" align="center" renderer="onCheckStatRender">核查状态</div>
			            <div field="last_check_time" width="" dateFormat="yyyy-MM-dd" headerAlign="center" align="center">核查日期</div>
			            <div field="last_checker" width="" headerAlign="center" align="center">核查人</div>
			             <div field="last_approval_stat" width="" headerAlign="center" align="center" renderer="onApprovalStatRender">复查状态</div>
			            <div field="last_approval_time" width="" dateFormat="yyyy-MM-dd" headerAlign="center" align="center">复查日期</div>
			            <div field="last_approver" width="" headerAlign="center" align="center">复查人</div>
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
    var flag = $G.get("flag").getValue();
    var model = $G.get("model").getValue();
	//flag不为空时，首页跳转过来
	if(flag != null && flag != ""&& flag != "null"){
		grid.load({"flag":flag,"ywtype":model});
	}else{
		grid.load({"approv_stat":"0","ywtype":model});
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
	//重置查询
	function reset(){
		form.reset();
	}
	
	//复核
	function recheck(flownum){
		var row = grid.findRow(function(row){
			if(row.flownum==flownum){
				return true;
			}
		});
		if(row.last_check_stat=="0"){
			$G.alert("请先进行核查");
			return;
		}
		var warning_code = row.warning_code;
		var url = "<%=root%>/warning_approv/recheck";
		var bizParams = {pageType:"warningReCheck",flownum:flownum,warning_code:warning_code};
		$G.showmodaldialog("预警复查",url,1000,520,bizParams,function(){
			grid.reload();
		});
	}
	//添加操作链接
	function onRender(e){
		var row = grid.findRow(function(row){
			if(row.flownum==e.value){
				return true;
			}
		});
		var op = "";
		//超时行变红色
		if(row.out_date==1){
			e.rowStyle="color:red;";
		}
		if(row.last_approval_stat==0){
			op = '<a id="link_check" href="javascript:recheck(\''+e.value+'\')"><font color="blue">复查</font></a>';
		}
		return op;
	}
	function onywTypeRender(e){
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	}
	function onApprovalStatRender(e){
		var textVal = mini.getDictText("dop_approv_stat",e.value);
		return textVal;
	}
	
	function onWarningLevelRender(e){
		var textVal = mini.getDictText("dop_warning_lvl",e.value);
		return textVal;
	}
	function onWarningStatusRender(e){
		var textVal = mini.getDictText("dop_warning_sta",e.value);
		return textVal;
	}
	function onCheckStatRender(e){
		var textVal = mini.getDictText("dop_check_stat",e.value);
		return textVal;
	}
	function onywTypeChanged(e){
		var val=$G.get("ywtype").getValue();
		$G.getbyName("warning_type").setValue("");
		$G.get("warn_name").setValue("");
		var url="<%=root%>/warning/getWarningTypeList?val="+val;
		$G.getbyName("warning_type").setUrl(url);
	}
	
	function warnTypechange(e){
		var warning_type = $G.get("warning_type").getValue();
		$G.get("warn_name").setValue("");
		var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
		$G.get("warn_name").setUrl(url);
		
	}
</script>


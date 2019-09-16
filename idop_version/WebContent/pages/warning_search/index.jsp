<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
   		<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		
  		</style>
	</head> 
	<body>
		<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
			<div id="panel1" class="nui-toolbar" title="预警信息查询" style="width:100%;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			   	<div style="overflow: auto;">
			   		<form id="form1">
			   		
			   		
						<div  class="search_box" width="100%">
							<ul>
			                   	<li class="search_li" align="right">机构名称：</li>
								<li align="left">
									<input id="orgid" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged="onOrgChange"
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:165px;" value="<%=request.getAttribute("org") %>"/>
								</li>
							</ul>
							
							<ul>
								<li class="search_li" align="right">柜员名称：</li>
				                <li align="left" colspan="1" width="165px;">
									<input id="followed_teller" name="followed_teller" class="mini-lookup" style="width:165px;" textField="name"
										valueField="user_no" popupWidth="auto" popup="#gridPanel1" grid="#collarGrid" value="" text="" 
										onvalidation="" onvaluechanged="" />
								</li>
									<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width: 380px; height: 200px;"
										showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0">
										<div property="toolbar" style="width: 380px; padding: 5px; padding-left: 8px; text-align: center;">
											<div style="float: left; padding-bottom: 2px;">
												<span>员工号或姓名：</span> 
												<input id="keyText" class="mini-textbox" style="width: 120px;" emptyText="请输入" onenter="onSearchClick('collarGrid')"  maxlength="20" vtype="maxLength:20" />
												<a class="mini-button" onclick="onSearchClick('collarGrid')">查询</a>
												<a class="mini-button" onclick="onClearClick('followed_teller')">清除</a>
											</div>
											<div style="float: left; padding-bottom: 2px;"> &nbsp;&nbsp;<a class="mini-button" onclick="onClose('followed_teller')">关闭</a>
											</div>
											<div style="clear: both;"></div>
										</div>
										<div id="collarGrid" class="mini-datagrid" style="width: 380px; height: 100%;" borderStyle="border:0"
											showPageSize="false" showPageIndex="false" showPager="false" sortMode="client" onrowclick="">
											<div property="columns">
												<div type="checkcolumn"></div>
												<div field="org_id" visible="false"></div>
												<div field="name" align="center" headerAlign="center" width="90px;" allowSort="true">用户名</div>
												<div field="user_no" align="center" headerAlign="center" width="50px;" allowSort="true">员工号</div>
												<div field="orgname" align="center" headerAlign="center" width="150px;" allowSort="true">机构名称</div>
											</div>
										</div>
									</div>
				             </ul>
				             
				             <ul>
			                   	<li class="search_li" align="right">业务模块：</li>
						        <li>
							        <input id="ywtype" name="ywtype" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype" 
							                 onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
						        </li>
						     </ul>
						      
						     <ul>
			          			<li class="search_li" align="right">预警类型：</li>
			          			<li>	
			          				<input name="warning_type" id = "warning_type"  class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
			                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
			                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick"  
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:165px;" onvaluechanged="warnTypechange"/>
								</li>
		               		 </ul>
		               		 <ul>	
			          			<li class="search_li" align="right">预警名称：</li>
			          			<li>								
			          				<input class="mini-combobox" width="165px"  nullItemText="请选择..."  multiSelect="true" emptyText="请选择..."  url="" 
	           	 				id="warn_name" name="warn_name"  valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
			          			</li>
			          		</ul>
		               		 	<ul>	
			          			<li class="search_li" align="right">预警等级：</li>
			          			<li>								
			          				<input name="warning_level" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_warning_lvl" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
			          			</li>
			          		</ul>
			          		
		               		
			          		<ul>	
			          			<li class="search_li" align="right">重点预警：</li>
								<li align="left">
									<input name="is_key_warning" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_is_key_warn" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
								</li>
		               		</ul>
		               		
			          		<ul>
			          			<li class="search_li" align="right">认定状态：</li>
								<li align="left">
									<input name="indentify_status" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_indent_stat" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
								</li>
							</ul>	
							
			          		<ul>
			                   	<li class="search_li" align="right">核查状态：</li>
								<li align="left">
									<input name="check_stat" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_check_stat" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
								</li>
		               		</ul>
		               		
		               		<ul>
			                   	<li class="search_li" align="right">复查状态：</li>
								<li align="left">
									<input name="approval_stat" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_approv_stat" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
								</li>
		               		</ul>
		               		<ul>
		               			<li class="search_li" align="right">是否问题：</li>
			          			<li>								
			          				<input name="is_question" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_is_question" 
			          						textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
			          			</li>
			          		</ul>
			          		
		               		<ul style="width:500px;">
		               			<li class="search_li" align="right">预警时间：</li>
			          			<li colspan="1">
									<input id="start_time" name="start_time"  value="<%=request.getAttribute("start_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
									<span style="float:left;">至</span>
									<input id="end_time" name="end_time"  value="<%=request.getAttribute("end_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
			          			</li>
			          		</ul>	
			        		
			        		<ul style="width:500px;">
		               			<li class="search_li" align="right">业务时间：</li>
			          			<li colspan="1">
									<input id="biz_start_time" name="biz_start_time" value="<%=request.getAttribute("biz_start_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
									<span style="float:left;">至</span>
									<input id="biz_end_time" name="biz_end_time" value="<%=request.getAttribute("biz_end_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
			          			</li>
			          		</ul>	
			          		
				   		</div>
			   		</form>  
			   		<div class="nui-toolbar" style="float:left;border:0;padding:0px;height: 32px;">
			        	<table style="width:20%;">
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
		</div>
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
		            	<th style="text-align:left;padding-left:50px;">预警信号明细</th>
		                <td style="white-space:nowrap;text-align:right;padding-right:50px;">
		                	<a class="nui-button" iconCls="" onclick="warningDetail()">预警详情</a>  
		                    <a class="nui-button" iconCls="" onclick="download()" style="width:64px;">导出</a>
		                </td>
		            </tr>
		       	</table>   
		</div>
		<div class="nui-fit">
	    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/warning_search/getList" style="width: 100%;height: 100%;" 
	    		multiSelect="false">
		        <div property="columns">            
		            <div field="create_time" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">预警时间</div>                
		            <div field="data_date" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">业务时间</div>  
		            <div field="warning_code" width="100" visible="false">预警编码</div>                
		            <div field="flownum" width=""  allowSort="true" headerAlign="center" align="center">预警编号</div>            
		            <div field="warning_name" width="150" allowSort="true" headerAlign="center" align="center">预警名称</div> 
		            <div field="bancsid" width="80" allowSort="true" headerAlign="center" align="center" >机构号</div>
		            <div field="orgname" width="100" allowSort="true" headerAlign="center" align="center">机构名称</div> 
		            <div field="busi_module" width="100" allowSort="true" headerAlign="center" align="center" renderer="onywTypeRender">业务模块</div> 
		            <div field="warning_type_code" width="80" allowSort="true" headerAlign="center" align="center">预警类型</div>
		            <div field="warning_level" width="80" allowSort="true" headerAlign="center" align="center" renderer="onWarningLevelRender">预警等级</div>
		            <div field="is_key_warning" width="80" allowSort="true" headerAlign="center" align="center" renderer="onIsKeyWarningRender">重点预警</div>
		            <div field="indentify_status" width="80" allowSort="true" headerAlign="center" align="center" renderer="onIndentifyStatusRender">认定状态</div>
		            <div field="last_check_stat" width="80" allowSort="true" headerAlign="center" align="center" renderer="onCheckStatRender">核查状态</div>
		            <div field="last_approval_stat" width="80" allowSort="true" headerAlign="center" align="center" renderer="onApprovalStatRender">复查状态</div>
		            <div field="is_question" width="80" allowSort="true" headerAlign="center" align="center" >是否问题</div>
		            <div field="last_checker" width="80" allowSort="true" headerAlign="center" align="center" >最后核查人</div>
		            <div field="checker_remark" width="80" allowSort="true" headerAlign="center" align="center" >核查说明</div>
		        </div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var keyText = mini.get("keyText"); 
	var grid =$G.get("datagrid1");
	var layout = $G.get("layout1");
	var form = $G.getForm("form1");
	var flag = '${flag}';
	
	var ywtype='${ywtype}';
	var yw=$G.get("ywtype");
	yw.setValue(ywtype);
	
	var url="<%=root%>/warning/getWarningTypeList?val="+ywtype;
	$G.getbyName("warning_type").setUrl(url);
	
    var warning_type='${warning_type}';
	var warning=$G.get("warning_type");
	warning.setValue(warning_type);
	

	var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
	$G.get("warn_name").setUrl(url);
	
	var warn_name='${warn_name}';
	var warn=$G.get("warn_name");
	warn.setValue(warn_name);
	
	
	
	
	if(flag == 1){
		var map ='${datas}';
		grid.load(JSON.parse(map));
	}else if (flag == 'bizMonitor') {
		var obj = JSON.parse('${datas}');				
		$G.get("ywtype").setValue(obj.ywtype);
		var url="<%=root%>/warning/getWarningTypeList?val="+obj.ywtype;
		$G.getbyName("warning_type").setUrl(url);
		$G.getbyName("warning_type").setValue(obj.warning_type);
		$G.getbyName('warning_type').doValueChanged();
		$G.getbyName('warn_name').setValue(obj.warn_name);
		$G.getbyName('biz_start_time').setValue(obj.biz_start_time);
		$G.getbyName('biz_end_time').setValue(obj.biz_end_time);
		search();
	} else if (flag == '2'){
		$G.getbyName('followed_teller').setValue('${userno}');
		$G.getbyName('followed_teller').setText('${username}');
		search();
	} else{
		grid.load();
	}
	
	function onSearchClick(e) {
		var org = $G.get("orgid").getValue();
		var grid1 = mini.get(e);
		grid1.setUrl("<%=root%>/myfocus/getAllUser?org="+org);
		grid1.load({ key : keyText.value});
	}
	
	function onClose(e) {
		var lookup2 = mini.get(e);
		lookup2.hidePopup();
	}
	
	/**
	 * 清空检索条件时
	 */
	function onClearClick(e) {
		var lookup2 = mini.get(e);
		lookup2.deselectAll();
	}

	function warningDetail(){
		var row = grid.getSelected();
		if(row){
			var flownum = row.flownum;
			var warning_code = row.warning_code;
			var url = "<%=root%>/warning_search/detail?id="+flownum;			
			var bizParams = {pageType:"warningDetail",flownum:flownum,warning_code:warning_code};
			$G.showmodaldialog("预警详情",url,1000,675,bizParams,function(){
				grid.reload();
			});
		}else{
			$G.alert("请选择一行数据！");
		}
		<%-- var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setSuccessFunc(function (text){
		});
		$G.postByAjax(null,"<%=root%>/warning_search/getMyFollow",ajaxConf); --%>
	}
	//下载
	function download(){
		//var data = form.getData();
		
		<%-- var orgid = $G.getbyName("orgid").getValue();
		var ywType = $G.getbyName("ywtype").getValue(); 
		var startTime = $G.getbyName("start_time").getValue();
		var endTime = $G.getbyName("end_time").getValue();
		var warningStatus = "";
		var warningType = $G.getbyName("warning_type").getValue();
		var warningLevel = $G.getbyName("warning_level").getValue();
		var checkStat = $G.getbyName("check_stat").getValue();
		var url = "<%=root%>/warning_search/download?orgid="+orgid+"&ywtype="+ywType+"&start_time="+startTime+"&end_time="+endTime+"&warning_status="+warningStatus+"&warning_type="+warningType+"&warning_level="+warningLevel+"&check_stat="+checkStat;
		window.location=url; --%>
		var data = form.getData();
		data["isDown"] = "1";
		data['pageSize']  = 1;
		data['pageIndex'] = 0;
		$.ajax({
			url: "<%=root%>/warning_search/getList",
			data:data,
			cache: false,
			success: function (text) {
			  var returndata = mini.decode(text);
			  data['pageSize']  = 9999999;
			  data['execlfilename'] = '预警信息列表';
			  if(returndata.total>30000){
					$G.alert("导出数据不能超过30000条！");
					return;
				}
				callHeadAndTextToDataByDataGrid(grid,data);
					window.location="<%=root%>/warning_search/download?"+connUrlByJson(data);
			}
		})
		
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
	function onywTypeChanged(e){
		var val=$G.get("ywtype").getValue();
		$G.getbyName("warning_type").setValue("");
		$G.get("warn_name").setValue("");
		var url="<%=root%>/warning/getWarningTypeList?val="+val;
		$G.getbyName("warning_type").setUrl(url);
	}
	function onOrgChange(e){
		if(e.value == null || e.value == ""){
			$G.get("followed_teller").setReadOnly(true);
		}else{
			$G.get("followed_teller").setReadOnly(false);
		}
	}
	function onywTypeRender(e){
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
	function onCheckStatRender(e){
		var textVal = mini.getDictText("dop_check_stat",e.value);
		return textVal;
	}
	function onIndentifyStatusRender(e){
		var textVal = mini.getDictText("dop_indent_stat",e.value);
		return textVal;
	}
	function onApprovalStatRender(e){
		var textVal = mini.getDictText("dop_approv_stat",e.value);
		return textVal;
	}
	function onIsKeyWarningRender(e){
		var textVal = mini.getDictText("dop_is_key_warn",e.value);
		return textVal;
	}
	function warnTypechange(e){
		var warning_type = $G.get("warning_type").getValue();
		$G.get("warn_name").setValue("");
		var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type;
		$G.get("warn_name").setUrl(url);
		
	}
	
</script>


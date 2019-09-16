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
  		.search_li{
  			width:85px;
  		}
  		
  		</style>
	</head> 
	<body>
		<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
			<div id="panel1" class="nui-toolbar" title="提示信息查询" style="width:100%;" showToolbar="false" showCollapseButton="false"
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
										popupHeight="470" popupMaxHeight="600" style="width:150px;" value="<%=request.getAttribute("org") %>"/>
								</li>
							</ul>
							
							<ul>
								<li class="search_li" align="right">柜员名称：</li>
				                <li align="left" colspan="1" width="165px;">
									<input id="followed_teller" name="followed_teller" class="mini-lookup" style="width:150px;" textField="name"
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
							        <input id="busi_module" name="busi_module" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype" 
							                 onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val" emptyText="请选择..." style="width:150px;"/>
						        </li>
						     </ul>
						      <ul>	
			          			<li class="search_li" align="right">提示名称：</li>
			          			<li>								
			          				<input class="mini-combobox" width="150px"  nullItemText="请选择..."  multiSelect="false" emptyText="请选择..."  url="" 
	           	 				id="warning_name" name="warning_name" dataField="data" valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
			          			</li>
			          		</ul>
						      <ul>
		               			<li class="search_li" align="right">是否发送短信：</li>
			          			<li align="left">
									<input class="mini-combobox" width="150px"  nullItemText="" ="请选择..." showNullItem="false"  url="<%=root%>/param/getDict?key=dxtz" 
           	 							id="is_key_dxtz" name="is_key_dxtz"  onvaluechanged="typeValueChanged1()"  valueField="val" textField="remark" ></input>
								</li>
			          		</ul> 
			          		<ul>
			                   	<li class="search_li" align="right">提示状态：</li>
								<li align="left">
									<input id="prompt_status" name="prompt_status" class="nui-combobox" url="<%=root%>/param/getDict?key=prompt_status" multiSelect="true"
			          					width="150px"	textfield="remark" valuefield="val" emptyText="请选择..." />
								</li>
		               		</ul>
		               		
		               		<ul style="width:400px;visibility: hidden;">
		               			<li class="search_li" align="right" ></li>
			          			<li colspan="1">
									<input class="mini-datepicker"   style="float:left;width:150px;"/>
									<span style="float:left;"></span>
									<input class="mini-datepicker"  style="float:left;width:150px;"/>
			          			</li>
			          		</ul>	
		               		
		               		<ul style="width:400px;">
		               			<li class="search_li" align="right">提示时间：</li>
			          			<li colspan="1">
									<input id="start_time" name="start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:150px;"/>
									<span style="float:left;">至</span>
									<input id="end_time" name="end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:150px;"/>
			          			</li>
			          		</ul>	
			        		
			        		<ul style="width:400px;">
		               			<li class="search_li" align="right">业务时间：</li>
			          			<li colspan="1">
									<input id="biz_start_time" name="biz_start_time" value="<%=request.getAttribute("biz_start_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:150px;"/>
									<span style="float:left;">至</span>
									<input id="biz_end_time" name="biz_end_time" value="<%=request.getAttribute("biz_end_time") %>" class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:150px;"/>
			          			</li>
			          		</ul>	
			          		
				   		</div>
			   		</form>  
			   		<div class="nui-toolbar" style="float:left;border:0;padding:0px;height: 32px;margin-left:50px" >
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
		                <td style="white-space:nowrap;text-align:right;padding-right:50px;">
		                <a class="nui-button" iconCls="" onclick="view()">查看</a>  
		                	<a class="nui-button" iconCls="" onclick="prompt()">人工提示</a>  
		                    <a class="nui-button" iconCls="" onclick="download()" style="width:64px;">导出</a>
		                </td>
		            </tr>
		       	</table>   
		</div>
		<div class="nui-fit">
	    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/prompting_search/getList" style="width: 100%;height: 100%;" 
	    		multiSelect="false">
		        <div property="columns">  
		        	<div field="id" width="100" visible="false">id</div>                
		            <div field="prompt_time" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">提示时间</div>                
		            <div field="data_date" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">业务时间</div>  
		            <div field="prompt_no" width="100" allowSort="true" headerAlign="center" align="center">提示编号</div>              
		            <div field="prompt_name" width="100" allowSort="true" headerAlign="center" align="center">提示名称</div> 
		            <div field="org_name" width="100" allowSort="true" headerAlign="center" align="center">机构名称</div> 
		            <div field="teller_name" width="100" allowSort="true" headerAlign="center" align="center">柜员名称</div> 
		            <div field="busi_module" width="100" allowSort="true" headerAlign="center" align="center" renderer="onywTypeRender">业务模块</div> 
		       		<div field="is_key_dxtz" width="100" allowSort="true" headerAlign="center" align="center" renderer="onIsKeyDxtzRender">是否发送短信</div>
		       		<div field="is_key_jsf" width="100" allowSort="true" headerAlign="center" align="center" renderer="onIsKeyJsfRender">短信接收人</div>
		       		<div field="prompt_status" width="100" allowSort="true" headerAlign="center" align="center" >提示状态</div>
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

	
	if(flag == 1){
		var map ='${datas}';
		grid.load(JSON.parse(map));
	}else{
		grid.load();
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

	
	//下载
	function download(){
	
		var data = form.getData();
		data["isDown"] = "1";
		data['pageSize']  = 1;
		data['pageIndex'] = 0;
		$.ajax({
			url: "<%=root%>/prompting_search/getList",
			data:data,
			cache: false,
			success: function (text) {
			  var returndata = mini.decode(text);
			  data['pageSize']  = 9999999;
			  data['execlfilename'] = '提示信息列表';
			  if(returndata.total>30000){
					$G.alert("导出数据不能超过30000条！");
					return;
				}
				callHeadAndTextToDataByDataGrid3(grid,data);
					window.location="<%=root%>/prompting_search/download?"+connUrlByJson(data);	
			}
		})
		
	}
	
	//人工提示
	function prompt(){
	    var status=$G.get("prompt_status").getValue();

	    
	    if($.inArray("0",status)==0||$.inArray("1",status)==0||$.inArray("4",status)==0){
	    	mini.alert("请选择提示状态为短信发送失败、不发送短信的提示信息！");
			return;
	    }else{
			var data1 = form.getData();
			data1["isDown"] = "1";
			data1['pageSize']  = 9999999;
			data1['pageIndex'] = 0;
			$.ajax({
				url: "<%=root%>/prompting_search/getPrompt",
				data:data1,
				cache: false,
				success: function (text) {
					
					if(text.result=='false')	{
						mini.alert("请选择提示状态为存量数据、短信发送失败、不发送短信的提示信息！");
						return;
					}else{
					 var returndata = mini.decode(text);
					 var result=text.result;
				  	 data1['pageSize']  = 9999999;
				 	 data1['execlfilename'] = '提示信息列表';
				  	 if(returndata.total>30000){
						$G.alert("导出数据不能超过30000条！");
						return;
					 }
					 callHeadAndTextToDataByDataGrid3(grid,data1);
					 window.location="<%=root%>/prompting_search/download?"+connUrlByJson(data1)+"&result="+result;
					 
					}
					setTimeout("search()",1000);
				}
			})
		}
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
		var val=$G.get("busi_module").getValue();
		$G.get("warning_name").setValue("");
		var url="<%=root%>/prompting_search/getWarningNameList?val="+val;
		$G.getbyName("warning_name").setUrl(url);
	}
	
	function onOrgChange(e){
		if(e.value == null || e.value == ""){
			$G.get("followed_teller").setReadOnly(true);
		}else{
			$G.get("followed_teller").setReadOnly(false);
		}
	}

	function view(){
		var row = grid.getSelected();
		if(row){
			var id = row.id;
			var prompt_no=row.prompt_no;
			var url = "<%=root%>/prompting_search/detail?id="+id;			
			var bizParams = {pageType:"Detail",id:id,prompt_no:prompt_no};
			$G.showmodaldialog("提示详情",url,1000,700,bizParams,function(){
				grid.reload();
			});
		}else{
			$G.alert("请选择一行数据！");
		}
	}
	
	function callHeadAndTextToDataByDataGrid3(grid,data){	
		var gridcolumns = grid.getBottomColumns();
		var headers = [],columns=[];
		for(var i=1;i<gridcolumns.length;i++){
			headers.push(gridcolumns[i].header);
			columns.push(gridcolumns[i].field);
		}
		data["execlheaders"] = headers.join(",");
		data["execlcolumns"] = columns.join(",");
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
	


	
	
</script>



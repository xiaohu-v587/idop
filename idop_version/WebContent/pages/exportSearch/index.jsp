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
			<div id="panel1" class="nui-toolbar" title="下载信息查询" style="width:100%;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false" >
			   	<div style="overflow: auto;">
			   		<form id="form1">
			   		
			   		
						<div  class="search_box" width="100%">
							<ul>
			                   	<li class="search_li" align="right">机构名称：</li>
								<li align="left">
									<input id="orgSelect" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged="onOrgChange"
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:165px;"  required="true"/>
								</li>
							</ul>
					
				             
				             <ul>
			                   	<li class="search_li" align="right">业务模块：</li>
						        <li>
							        <input id="module" name="module" class="nui-combobox" url="<%=root%>/exportSearch/getDict?key=export_module"
							                 onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;" required="true"/>
						        </li>
						     </ul>
						     
						       <ul>
			          			<li class="search_li" align="right">表名称：</li>
			          			<li>	
			          				<input name="tableName" id = "tableSelect"  class="nui-combobox" url="<%=root%>/exportSearch/getTableName" 
			                       	    dataField="data" valuefield="table_id" textfield="table_desc" parentfield=""  emptyText="请选择..." style="width:165px;" required="true"/>
								</li>
		               		 </ul>
		               		<ul style="width:500px;">
		               			<li class="search_li" align="right">时间：</li>
			          			<li colspan="1">
									<input id="begindate" name="begindate"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
									<span style="float:left;">至</span>
									<input id="enddate" name="enddate"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
			          			</li>
			          		</ul>	
			        		
			          		
				   		</div>
			   		</form>  
			   		<div class="nui-toolbar" style="float:left;border:0;padding:0px;height: 32px;">
			        	<table style="width:20%;">
			        		<tr>
						        <td style="white-space:nowrap;text-align:center;">
						            <a class="nui-button" iconCls="" onclick="createTask()" style="width:64px;margin-right:20px;">生成任务</a>
						 			<a class="nui-button" iconCls="" onClick="search()" style="margin-right:20px;">查询</a>
						 			<a class="nui-button" iconCls="" onClick="reset()" style="margin-right:20px;">重置</a>
						 			
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
		            	<th style="text-align:left;padding-left:50px;">表明细</th>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/exportSearch/getTableList" style="width: 100%;height: 100%;" 
		    	 sizeList="[10,20,50,100,500]" pageSize="10" 	multiSelect="false"  autoEscape="false">
			        <div property="columns">   
			             <div field="begin_time" width="100" allowSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"  headerAlign="center" align="center" >开始时间</div> 
			            <div field="end_time" width="80" allowSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"  headerAlign="center" align="center" >结束时间</div> 
			            <div field="table_desc" width="80" allowSort="true" headerAlign="center" align="center" >表名</div>
			             <div field="status" width="80" allowSort="true" headerAlign="center" align="center" >状态</div>
			             <div field="ftp_status" width="80" allowSort="true" headerAlign="center" align="center"  visible="false" >文件状态</div>
			              <div field="remark" width="80" allowSort="true" headerAlign="center" align="center" >备注</div>
			            <div field="pkid" width="80" allowSort="true" headerAlign="center" align="center" renderer="download" >下载</div>
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
	
	//生成任务
	function createTask(){
		var orgid=$G.getbyName("orgid").getValue();
		var module=$G.getbyName("module").getValue();
		var table_id=$G.getbyName("tableName").getValue();
		var begindate=$G.getbyName("begindate").getValue();
		var enddate=$G.getbyName("enddate").getValue();
		
		if(orgid==""){
			$G.alert("机构名称必填");
			return;
		}
		if(module==""){
			$G.alert("模块必填");
			return;
		}
		
		if(begindate==""||enddate==""){
			$G.alert("开始时间和结束时间必填");
			return;
		}
		

	   var data={};
		var url="<%=root%>/exportSearch/createTask?orgid="+orgid+"&begindate="+begindate+"&enddate="+enddate+"&module="+module+"&table_id="+table_id;
		$G.mask({
			el:document.body,
			html:'加载中...',
			cls:'mini-mask-loading',
		});
		$.ajax({
			url:url,
			data:null,
			success:function(text){
				var flag=text;
				if(flag == true){
					$G.alert("生成任务成功");	
					grid.load();
				}else{
					$G.alert("生成任务失败");
				}
				$G.unmask();
			},
			error:function(u,v,x){
				$G.unmask();
			}
		});
		
		//window.location=url;
		
	}
	
	//点击下载
	function recheck(pkid){
           var data={pkid:pkid};
			var url = "<%=root%>/exportSearch/exportList";
			$.ajax({
				type:"get",
				url:url,
				data:data,
				success:function(text){
					var dlfn=text.dlfn;
					var conftp=text.conftp;
						window.location=conftp+dlfn+"";
				
						
				},
				error:function(u,v,x){
					$G.alert("下载失败");
				}
			});
		   
	}

	//下载
	function download(e){
		var row = grid.findRow(function(row){
			if(row.pkid==e.value){
				return true;
			}
		});
		
		var op = "";
		if(row.status=="任务处理成功" && row.ftp_status=="9"){
			var op = '<a id="link_check" href="javascript:recheck(\''+e.value+'\')"><font color="blue">下载</font></a>';
		}
		
		return op;	
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
		var val=e.value
		$G.getbyName("tableName").setValue("");
		var url="<%=root%>/exportSearch/getTableName?val="+val;
		$G.getbyName("tableName").setUrl(url);	

	}
	function onOrgChange(e){
		if(e.value == null || e.value == ""){
			
		}
	}
	function onywTypeRender(e){
		var textVal = mini.getDictText("dop_ywtype",e.value);
		return textVal;
	}


</script>


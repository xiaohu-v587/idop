
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>报表管理员角色移交</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>
	<div style="width:100%;height:90%; border-top: solid; border-color: #2fa2fd; border-width: 1px;">
	   	<div style="width:100%;height:15%;overflow: hidden">
	   		<form id="form2" method="post">
	   			<input id="user_id" name="user_id" class="nui-hidden"/>
		   		<table style="table-layout: fixed;" width="100%">
					<colgroup>
				       	<col width="10%"/>
				       	<col width="20%"/>
				       	<col width="15%"/>
				       	<col width="20%"/>
				       	<col width="15%"/>
				       	<col width="15%"/>
					</colgroup>
					<tr>
		                <td align="right">EHR号</td>
						<td align="left">
							<input id="user_no" name="user_no" class="mini-textbox" style="width:100px"/>
						</td>
		                <td align="right">客户经理名称：</td>
						<td align="left">
							<input id="user_name" name="user_name" class="mini-textbox" style="width:100px"/>
						</td>
						<td align="center">
							<a class="mini-button" iconCls="" onclick="reset()" >重置</a>
						</td>
						<td align="center">
							<a class="mini-button" iconCls="" onclick="search()" >查询</a>
						</td>
		            </tr>
		   		</table>
	   		</form>
	   	</div>
	   	<div style="width:100%;height:80%;">
			<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/manualReport/getAllUser" 
				style="width: 100%;height: 100%;" multiSelect="false" showPager="true" onRowClick="onRowClick">
		        <div property="columns">
		        	<div type="checkcolumn" name="checkCloumn" width="3%"></div>
		        	<div headerAlign="center" width="10%" type="indexcolumn">序号</div>
		        	<div field="user_no" width="20%" allowSort="true" headerAlign="center" align="center">EHR号</div>
		        	<div field="name" width="30%" headerAlign="center" allowSort="true"  align="center">客户经理</div>                
		            <div field="orgname" width="37%" headerAlign="center" allowSort="true"  align="center">责任中心</div>
		        </div>
			</div>
		</div>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
				borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="nui-button" iconCls="" onclick="save">确定</a> 
		<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form2 = $G.getForm("form2");
	var grid1 =$G.get("datagrid1");
	grid1.load();
	
	function setData(data){
		//var info = $G.clone(data);
		//$G.get("report_id").setValue(info.id);
	}
	
	//查询
	function search(){
		grid1.load(form2.getData());
	}

	//重置
	function reset(){
		form2.reset();
	}
	
	function onRowClick(){
		var record = grid1.getSelected();
		if(record){
			$G.get("user_id").setValue(record.user_no);
		}
	}

	/*
	 *保存数据
	 */
	function save(){
		var user_id = $G.get("user_id").getValue();
		if(null == user_id || "" == user_id){
			$G.alert("请选择一个用户!");
			return;
		}
    	var urlStr = "<%=root%>/manualReport/saveRoleTransferInfo";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
    		if("1" == text.flag){
    			alert("移交成功!");
    			$G.closemodaldialog("ok");
    		}else{
    			alert("移交失败!");
    		}
		});
	    $G.submitForm("form2", urlStr, ajaxConf);
	}			
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>
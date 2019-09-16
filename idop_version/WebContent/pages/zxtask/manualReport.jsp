<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>手工报表</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
	  		.mini-panel-body{
	  			padding: 0px;
	  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="查询条件" style="width:100%;height:65px;margin-left:5px" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
		   	<div class="nui-fit" style="overflow: hidden">
		   		<form id="form1">
			   		<table style="table-layout: fixed;" class="search_table" width="100%">
						<tr>
		                   	<th align="right">报表名称：</th>
							<td align="left"style="width:200px">
								<input id="taskName" name="taskName" class="mini-textbox" style="width:165px;"/>
							</td>
							<!--<th align="right">发送部门：</th>
							<td align="left">
								<input style="width:30%;" class="nui-combobox" name="first_org_num" 
									valueField="orgnum" textField="orgname" emptyText="请选择" nullItemText="请选择"
									data='[{"orgnum":"000000000","orgname":"江苏省分行"}]'/>
								<input style="width:30%;" class="nui-combobox" name="second_org_num" 
									valueField="orgnum" textField="orgname" emptyText="请选择" nullItemText="请选择" showNullItem="true" 
									 url="<%=root%>/pccm_standard/getSecondOrg"  onvaluechanged="onOrgChanged">
								<input style="width:30%;" class="nui-combobox" name="third_org_num" 
									valueField="orgnum" textField="orgname" emptyText="请选择" nullItemText="请选择" showNullItem="true"/>
							</td>-->
							</td>
							<td style="">
	                	<a class="nui-button" iconCls="" onclick="search()">查询</a>
	                	<span class="separator"></span>
	                	<a class="nui-button" iconCls="" onclick="reset()">重置</a>
	                </td>
	               		</tr>
			   		</table>
		   		</form>
		   	</div>
		</div>
		<div class="nui-toolbar" style="border-bottom:0;padding:0px;height: 32px;border-top:0;">
			 <table style="width:100%;">
	            
	       	</table>
		</div>
		<div class="nui-fit" style = "margin-left:15px">
	    	<div id="datagrid1" class="nui-datagrid" url="<%=root%>/manualReport/getMyReports" style="width: 100%;height: 100%" onrowdblclick="detail">
		        <div property="columns">
		            <!-- <div type="checkcolumn" name="checkCloumn"></div> -->
		            <div headerAlign="center" type="indexcolumn" align="center">序号</div>
		            <div field="id" visible="false">接受人任务id</div>
		            <div field="task_detail" visible="false">报表说明</div>
		            <div field="task_orgassociation" visible="false">机构关联字段</div>
		            <div field="report_id" visible="false">报表id</div>
		            <div field="fillway" visible="false">填报方式</div>
		            <div field="table_head" visible="false">列名</div>
		            <div field="fields_location" visible="false">列类型</div>
		            <div field="serial_number" visible="false">下拉列表key</div>
		            <div field="task_name" width="100" allowSort="true" headerAlign="center" align="center">报表名称</div>
		            <div field="issuer_org" width="100" allowSort="true" headerAlign="center" align="center">发送部门</div>
		            <div field="task_enddate" width="100" allowSort="true" headerAlign="center" align="center">最迟发送日期</div>
		            <div field="remain_day" width="100" allowSort="true" headerAlign="center" align="center" renderer="leftday">剩余时间(天)</div>
		            <div field="task_status_name" width="100" allowSort="true" headerAlign="center" align="center" renderer="sendback">操作状态</div>
		            <div width="100" headerAlign="center" align="center" renderer="onRender">操作</div>
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
	var level = '<%=request.getAttribute("level")%>';
	grid.load();

	//机构联动查询
	function onOrgChanged(e){
		var second_org_num=$G.getbyName('second_org_num');
		var third_org_num=$G.getbyName('third_org_num');
		var orgnum=second_org_num.getValue();
		third_org_num.setValue("");
		var url="<%=root%>/pccm_standard/getThridOrg?orgnum="+orgnum;
		third_org_num.setUrl(url);
		third_org_num.select("");
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
	
	function detail(e){
		var record = e.record;
		var url = "<%=root%>/manualReport/detail";
        $G.showmodaldialog("报表详情", url, 800, 600, record, function(action){});
	}
	
	function dateformat(e){
		var value = e.value;
		if(value) return $G.formatDate(value,'yyyy-MM-dd');
		return "";
	}
	
	function leftday(e){
		if(e.value<0){
			return "0";
		}
		return e.value;
	}
	
	//添加操作链接
	function onRender(e){
		var record = e.record;
		var index = grid.indexOf(record);
		var op = "";
		if(record.status == 2){
			if(record.task_status == 3 || record.task_status == 4){
				op += '<a href="javascript:reply('+index+')"><font color="blue">查看</font></a>'
					+ '&emsp;<a href="javascript:retract('+index+')"><font color="blue">报送撤回</font></a>';
			}else if(record.task_receive_user){
				op += '<a href="javascript:end('+index+')"><font color="blue">无需报送</font></a>'
					+ '&emsp;<a href="javascript:reply('+index+')"><font color="blue">报送</font></a>';
				if(record.forward > 0){
					op += '&emsp;<a href="javascript:callback('+index+')"><font color="blue">转发撤回</font></a>';
					op += '&emsp;<a href="javascript:list('+index+')"><font color="blue">查看</font></a>&emsp;'
				} else {
					if(level!="4"){
						op += '&emsp;<a href="javascript:forward('+index+')"><font color="blue">转发</font></a>';
					}
				}
			}else{
				op += '<a href="javascript:claim('+index+')"><font color="blue">认领</font></a>';
			}
		}else{
			op += '<a href="javascript:replys('+index+')"><font color="blue">查看</font></a>';
		}
		return op;
	}
	
	//查看列表
	function list(index){
		var record = grid.getRow(index);
		var url = "<%=root%>/manualReport/listOfsp";
		var bizParams = {rid:record.id, name:record.task_name, rate:record.task_frequency, end_time:record.task_enddate, status:record.task_status};
	    $G.showmodaldialog("完成情况列表", url, 800, 600, bizParams, function(data){
			grid.reload();
		});
	}
	
	//认领
	function claim(index){
		var record = grid.getRow(index);
		$G.GcdsConfirm("确定认领此任务吗？", "提示", function(action) {
			if (action == 'ok') {
            	var ajaxConf = new GcdsAjaxConf();
            	ajaxConf.setSuccessFunc(function (data){
            		if(data.result=="success"){
            			grid.reload();
            		}
                });
            	$G.postByAjax({"id":record.id}, "<%=root%>/manualReport/claim", ajaxConf);
          	}
        });
	}
	
	//无需报送
	function end(index){
		var record = grid.getRow(index);
		$G.GcdsConfirm("确定此任务无需报送吗？（注意：将删除已保存的报表内容）", "提示", function(action) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (){
                   	grid.reload();
                });
               	$G.postByAjax({id:record.id}, "<%=root%>/manualReport/end", ajaxConf);
            }
        });
	}
	
	//转发
	function forward(num){
		var row = grid.getRow(num);
		var url = "<%=root%>/manualReport/goForward";
		var bizParams = {id:row.id, rid:row.report_id, name:row.task_name, desc:row.task_detail, status:row.task_status, finish:0};
        $G.showmodaldialog("转发", url, 600, 400, bizParams, function(action){
	    	 grid.reload();
	    });
	}
	
	//报送
	function reply(num){
		var row = grid.getRow(num);
		var bizParams = {id:row.id,rid:row.report_id,name:row.task_name,status:row.status,task_status:row.task_status,receive_org:row.task_receive_org,org_field:row.org_field,table:row.task_table_name,desc:row.task_detail,fillway:row.task_fillway,zdmc:row.table_head,zdlx:row.fields_location,zdbh:row.serial_number};
		var url = "<%=root%>/manualReport/goReply";
		 $.ajax({
	 		url : "<%=root%>/manualReport/querydata",
	 		data:{rid:row.report_id,id:row.id},
	 		async:false,
	 		success : function(text) {
	 			var flag = text.flag;
	 				if(flag=="true"){
	 					 $G.showmodaldialog("报送", url, 900, 600, bizParams, function(action){
	    	 			 	grid.reload();
	   					 });
	 				}else{
	 					nui.alert("存在部分转发机构未报送，您暂时无法报送数据！");
	 				}
	 			}
	 		}) 
	}
	//报送
	function replys(num){
		var row = grid.getRow(num);
		var bizParams = {id:row.id,rid:row.report_id,name:row.task_name,status:row.status,task_status:row.task_status,receive_org:row.task_receive_org,org_field:row.org_field,table:row.task_table_name,desc:row.task_detail,fillway:row.task_fillway,zdmc:row.table_head,zdlx:row.fields_location,zdbh:row.serial_number};
		var url = "<%=root%>/manualReport/goReply";
	 					 $G.showmodaldialog("报送", url, 900, 600, bizParams, function(action){
	    	 			 	grid.reload();
	   					 });
	}
	
	//撤回
	function retract(index){
		var row = grid.getRow(index);
		$.ajax({
			url : "<%=root%>/manualReport/checkcondition",
	 		data:{rid:row.report_id,id:row.id},
	 		async:false,
	 		success : function(text) {
	 			var flag = text.flag;
	 				if(flag=="true"){
	 					$G.GcdsConfirm("确定撤回此任务吗？", "提示", function(action) {
							if (action == 'ok') {
               					var ajaxConf = new GcdsAjaxConf();
               					ajaxConf.setSuccessFunc(function (data){
                   				grid.reload();
                				});
               						$G.postByAjax({id:row.id}, "<%=root%>/manualReport/retract", ajaxConf);
            					}
        					});
	 				}else{
	 					nui.alert("上级机构已报送，您暂时无法撤回！");
	 				}
	 			}
				
		})
		
	}
	
	//转发撤回
	function callback(index){
		var row = grid.getRow(index);
		$G.GcdsConfirm("确定撤回已转发的任务吗？（注意：将逐层删除下级的报表）", "提示", function(action) {
			if (action == 'ok') {
               	var ajaxConf = new GcdsAjaxConf();
               	ajaxConf.setSuccessFunc(function (data){
                   	grid.reload();
                });
               	var formjson = $G.encode({id:row.id});
               	$G.postByAjax({form:formjson}, "<%=root%>/manualReport/forward", ajaxConf);
            }
        });
	}
	
	//加上退回原因
	function sendback(e){
		if(e.record.task_status==2 && e.record.return_reason){
			return e.value + " 原因：" + e.record.return_reason;
		}else{
			return e.value;
		}
	}

</script>
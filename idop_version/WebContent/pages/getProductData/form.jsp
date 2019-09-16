<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>    
	<div class="mini-fit">
	<!-- 	<input name="pageType" class="nui-hidden"/>   -->    
		<form id="form1"  method="post">
         	<input name="processinstanceid" id="processinstanceid" class="nui-hidden"/> 
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
				<tr>
						<td align="right">请假类型：</td>
						<td colspan="3" align="left">    
							<input id="leavetype" style="width:220px;" name="leavetype" class="mini-combobox" style="width:125px;" textfield="remark" valuefield="val" emptyText="请选择..."
                                url="<%=root%>/leave/getCombox"  showNullItem="true" nullItemText="请选择..." />
		              	</td>
	              	</tr> 
               	<tr>
                   	<td align="right">开始时间：</td>
                   	<td align="left">    
                       <input id="starttime" name="starttime" class="nui-datepicker" allowInput="false"  style="width:80%"/>
                   	</td>
                   		<td align="right">结束时间：</td>
                    <td align="left">    
                        <input id="endtime" name="endtime" class="nui-datepicker" allowInput="false"  style="width:80%"/>
                    </td>
               	</tr>
               	<tr>
                   	<td align="right">请假原因：</td>
                    <td align="left"  colspan="3">    
                    	<textarea class="nui-textarea" id="reason" name="reason" emptyText="请输入" style="width:92%;height:80px"></textarea>
                    </td>
               	</tr>
               	<tr id="b" style="display: none">
                   	<td align="right"  id="lct" >流程图：</td>
                    <td align="left"  colspan="3">    
                    	<a target="_blank" href="javascript:checklct()" id="cklct">查看流程图</a>
                    </td>
               	</tr>
               	<tr id="a" style="display: none">
                   	<td align="right"  id="yj" >审批意见：</td>
                    <td align="left"  colspan="3">    
                    	<textarea class="nui-textarea" id="spyj" name="spyj" emptyText="无审批意见" style="width:92%;height:55px"></textarea>
                    </td>
               	</tr>
               	
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">流转</a>  
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");

	 function setData(data){
		var infos = $G.clone(data);
	 	var pageType=infos.pageType;
		if(pageType=="xq"){
			    var json = data.data;
	            form.setData(json);
	            $G.get("leavetype").setReadOnly(true);
	            $G.get("starttime").setReadOnly(true);
	            $G.get("endtime").setReadOnly(true);
	            $G.get("reason").setReadOnly(true);
	            $G.get("spyj").setReadOnly(true);
	            $G.get("processinstanceid").setValue(json.processinstanceid); 
	            document.getElementById("a").style.display="";
	            document.getElementById("b").style.display="";
	            $.ajax({type:"post",url:"<%=root%>/leave/hqpz", dataType:"json", data:"taskid="+json.processinstanceid,
				       success:function(data){
				        $G.get("spyj").setValue(data.pz); 
			}
			  });
		}
	} 
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/leave/startWorkflow";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function checklct(){
		var taskid=  $G.get("processinstanceid").getValue(); 
		 $.ajax({type:"post",url:"<%=root%>/leave/hqProcessId", dataType:"json", data:"taskid="+taskid,
		       success:function(data){
		       if(data.processDefinitionId=="1"&&data.tId=="1"){
		    	   $G.alert("该流程以结束")
		       }else{
		    	   window.location.href="/leave/readResource?processDefinitionId="+data.processDefinitionId+"&executionId="+data.tId;
		       }
	}
	  });
		
	}
</script>

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
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
        	<input id ="id" name="id" class="nui-hidden"/>
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="18%"/>
				       	<col width="37%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
				<tr>                                                                                       
	                <th align="right"><span id="cust_no_lb"></span></th>                                   
					<td align="left">                                                              
						<input id="customercode" name="customercode" class="nui-textbox" allowInput="false" style="width:165px;"/>                
					</td>                                                                          
	                <td align="right">客户名称：</td>                                    
					<td align="left">                                                              
						<input id="name" name="name" class="nui-textbox" allowInput="false" style="width:165px;"/>        
					</td>                                                                           
	             </tr>
               	<tr>
                   	<td align="right">申请方案：</td>
					<td align="left" colspan="3">
						<input id="remark" name="remark" class="nui-textarea" required="true" width="400px"  height="65px" emptyText="请输入申请申诉原因及申请分配方案"/>
					</td>
               	</tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");

	function setData(data){
		var infos = $G.clone(data);
		if(infos.flag=="1"){
			document.getElementById("cust_no_lb").innerHTML= "客户号：";
		}else if(infos.flag=="2"){
			document.getElementById("cust_no_lb").innerHTML= "虚拟客户号：";
		}
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			form.setData(text.record);
			$G.getbyName("id").setValue(infos.id);
		});
		$G.postByAjax({id:infos.id},"<%=root%>/zxCustClaim/getDetail",ajaxConf);
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/zxCustClaim/claimCompSave";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
    		$G.alert("操作成功");
    		$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function onOrgChanged(){
		var org_id = $G.get("org_id").getValue();
		if(org_id){
			var url="<%=root%>/custPuni/getListByOrgId?org_id="+org_id;
			$G.get("cust_id").setUrl(url);
		}
	}
	
	function onTypeChanged(){
		var deduction="";
		var punish_type = $G.get("punish_type").getValue();
		if("0"==punish_type){
			deduction = "5";
		} else if("1"==punish_type){
			deduction = "10";
		}
		$G.get("deduction").setValue(deduction);
	}
	
	function onCustChanged(){
		$G.get("cust_name").setValue($G.get("cust_id").getText());
	}
	
</script>
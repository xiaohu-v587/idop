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
<form id="form1" method="post">
	<div class="mini-fit">
		
        	<input id="id" name="id" class="nui-hidden"/>
        	<input name="claim_prop_all" class="nui-hidden"/>
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
               	<tr>                                                                                       
	                <td align="right">客户号：</td>                                      
					<td align="left">                                                              
						<input id="customercode" name="customercode" class="nui-textbox" allowInput="false" style="width:165px;"/>                
					</td>                                                                          
	                <td align="right">客户名称：</td>                                    
					<td align="left">                                                              
						<input id="name" name="name" class="nui-textbox" allowInput="false" style="width:165px;"/>        
					</td>                                                                           
	             </tr>
<!-- 	             <tbody id="tbody"></tbody>                                                              -->
			</table>
			<div style="padding-left: 26px; padding-top: 20px;">待认领比例：<span id="toProp"></span></div>
		
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<span>认领比例：<input id="prop" name="prop" class="nui-textbox" vtype="range:1,100" style="width:165px;" required="true" onvaluechanged="check()" /> %</span> 
	</div>
	</form>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">认领</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var toProp;
	var msg;

	function setData(data){
		var infos = $G.clone(data);
		var to_claim_prop = infos.to_claim_prop;
		
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
			form.setData(text.record);
// 			if(text && text.record && text.record.claim_cust_mgr_name){
// 				var claimCustMgrName=text.record.claim_cust_mgr_name.split(",");
// 				var claimProp=text.record.claim_prop.split(",");
// 				var htmlstr="";
// 				var claim_prop_all=0;
// 				for(var i=0;i<claimCustMgrName.length;i++){
// 					htmlstr+="<tr><td align='right'>客户经理"+(i+1)+"：</td>"
// 						+"<td align='left'>"
// 						+"<input class='nui-textbox' allowInput='false' style='width:165px;' value='"+claimCustMgrName[i]+"'/>"
// 						+"</td>"
// 						+"<td align='right'>认领比例：</td>"
// 						+"<td align='left'>"
// 						+"<input class='nui-textbox' allowInput='false' style='width:165px;' value='"+claimProp[i]+"'/>%"
// 						+"</td></tr>";
// 					claim_prop_all += parseInt(claimProp[i]);
// 				}
// 				var trStr="<tr><td align='right'>待认领比例：</td>"
// 					+"<td align='left' colspan='3'>"
// 					+"<input id='claim_prop_all' value="+(100-claim_prop_all)+" allowInput='false' class='nui-textbox' style='width:165px;'/>%"
// 					+"</td></tr>";
// 				$("#tbody").html(trStr+htmlstr);
// 				$G.parse();
// 				$G.getbyName("claim_prop_all").setValue(100-claim_prop_all);
// 			};
			toProp = to_claim_prop.replace("%","");
			$G.getbyName("id").setValue(infos.id);
			$G.getbyName("prop").setValue(to_claim_prop.replace("%",""));
			document.getElementById("toProp").innerHTML= to_claim_prop;
		});
		$G.postByAjax({id:infos.id},"<%=root%>/zxCustClaim/getDetail",ajaxConf);
	}
	
	/*
	 *保存数据
	 */
	function save(){
		if(msg){
			$G.alert(msg);return;
		}
    	var urlStr = "<%=root%>/zxCustClaim/claimSave";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	$G.alert("认领成功");
    		$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function check() {
		var prop = $G.getbyName("prop").getValue();
		if(prop&&toProp){
			if(parseInt(prop)>parseInt(toProp)){
				msg = "认领比例不能超过待认领比例"+toProp+"%！";
				$G.alert(msg);return;
			}else{
				msg = "";
			}
		}
    }
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>
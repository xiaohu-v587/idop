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
       	<input name="ids" class="nui-hidden"/>
       	<input name="codes" class="nui-hidden"/>
       	<input name="claim_prop_max" class="nui-hidden"/>
       	<input name="pro" class="nui-hidden"/>
        <table style="table-layout: fixed;" id="detailTable"  width="100%">
			<colgroup>
			       	<col width="50%"/>
			       	<col width="50%"/>
			</colgroup>
              	<tr>                                                                                       
                <td align="right">批量待认领比例最大值：</td>                                      
				<td align="left">                                                              
					<span id="claim_prop_max"></span>%         
				</td>                                                                         
             </tr>                                                            
		</table>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<span>认领比例：<input id="prop" name="prop" class="nui-textbox" vtype="int" style="width:165px;" required="true" onvaluechanged="check()" />% </span> 
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
	var msg;
	function setData(data){
		var infos = $G.clone(data);
		$G.getbyName("ids").setValue(infos.ids);
		$G.getbyName("codes").setValue(infos.codes);
		$G.getbyName("claim_prop_max").setValue(infos.claim_prop_max);
		$G.getbyName("prop").setValue(infos.claim_prop_max);
		document.getElementById("claim_prop_max").innerHTML= infos.claim_prop_max;
	}
	
	/*
	 *保存数据
	 */
	function save(){
		if(msg){
			$G.alert(msg);return;
		}
    	var urlStr = "<%=root%>/zxCustClaim/claimManySave";
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
		var a = $G.getbyName("claim_prop_max").getValue();
		var b = $G.getbyName("prop").getValue();
		if(a&&b){
			if(parseInt(b)>parseInt(a)){
				msg="认领比例不能超过待认领比例的最大值"+a+"%！";
				$G.alert(msg);return;
			}else{
				msg = "";
			}
		}
		$G.getbyName("pro").setValue(b);
    }
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>
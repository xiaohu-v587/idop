<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 预警模型
	*
	* @author Luleilei
	* @date 2018-12-01
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>模型新增或编辑</title>
	<style type="text/css">
		html,body {
			margin: 0;
			padding: 0;
			border: 0;
			width: 100%;
			height: 100%;
			overflow: hidden;
		}
	</style>
</head>
<body>
    <div class="mini-fit">
    	<input name="pageType"  id="pageType" class="nui-hidden" ></input>
        <form id="dataform1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
          		<table style="table-layout: fixed;" align="center" >
          			<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module"   valueField="val" textField="remark" required="true"
           	 				onvaluechanged="moduleChanged"></input>
            			</td>
              			<td class="form_label">模型类型:</td>
              			<td >
                			<input name="warning_type_code" id="warning_type_code" class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true"  
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600" style="width:100%;" required="true"/>
            			</td>
        			</tr>
          			<tr>
	          			<%-- <c:if test="${pageType=='edit'}"> --%>
	          				<td class="form_label">模型编号:</td>
	              			<td >
	                			<input class="mini-textarea" name="warning_code" height="40px" id="warning_code" width="100%" required="true"></input>
	            			</td>
	          			<%-- </c:if> --%>
              			
              			<td class="form_label">模型名称:</td>
              			<td >
                			<input class="mini-textarea" width="100%"  height="40px" id="warning_name" name="warning_name" maxlength="50" required="true"></input>
            			</td>
        			</tr>
        			
            		
        			
        			<tr>
        				<td class="form_label">模型状态:</td>
              			<td >
                			<input name="is_use" id="is_use"  class="mini-combobox" width="100%" url="<%=root%>/param/getDict?key=dop_markstate" 
           	 				valueField="val" textField="remark" required="true" />
            			</td>
              			<td class="form_label">模型维度:</td>
              			<td >
                			<input class="mini-combobox" width="100%" nullItemText="" emptyText="请选择..."  showNullItem="false"   url="<%=root%>/param/getDict?key=warn_wd" 
           	 				id="warning_dimension" name="warning_dimension" onvaluechanged="typeValueChanged()"  valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">模型等级:</td>
              			<td >
                			<input class="mini-combobox" name="warning_level"   id="warning_level" width="100%" url="<%=root%>/param/getDict?key=dop_warning_lvl" 
                			valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">加工频率:</td>
              			<td >
                			<input class="mini-combobox" width="100%" id="proc_rate"   name="proc_rate"  required="true"
                			data="[{id:'日',text:'日'},{id:'周',text:'周'},{id:'月',text:'月'},{id:'季',text:'季'},{id:'年',text:'年'}]"
                			></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">加工层级:</td>
              			<td >
                			<input class="mini-combobox" name="proc_level"   id="proc_level" width="100%" url="<%=root%>/param/getDict?key=dop_apply" 
                			valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">是否需要复查:</td>
              			<td >
                			<input class="mini-combobox" name="is_confirm"   valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_sfsjhqr"
                			id="is_confirm" width="100%" required="true"  onvaluechanged="rdChanged"></input>
            			</td>
        			</tr>
        			<tr style="display:none">
	          			<%-- <c:if test="${pageType=='edit'}"> --%>
	          				<td class="form_label">阀值类型:</td>
	              			<td >
	                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=threshold_type" 
           	 				id="threshold_type" name="threshold_type"   valueField="val" textField="remark" required="true" onvaluechanged="typeChanged"
           	 				></input>
	            			</td>
	          			<%-- </c:if> --%>
              			
              			<td class="form_label">初始阀值:</td>
              			<td >
                			<input class="mini-textbox" width="100%"  height="40px" id="initial_z" name="initial_z" ></input>
            			</td>
        			</tr>
        			<tr style="display:none">
        				<td class="form_label">模型阀值X:</td>
              			<td >
                			<input class="mini-textbox" name="initial_x" id="initial_x"   width="100%"  ></input>
            			</td>
              			<td class="form_label">模型阀值Y:</td>
              			<td >
                			<input class="mini-textbox" width="100%"    name="initial_y" id="initial_y" ></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">是否需要认定:</td>
              			<td >
                			<input class="mini-combobox" name="is_manual_indentify"   valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_sfrgsd"
                			id="is_manual_indentify" width="100%" required="true"  onvaluechanged="rgChanged"></input>
                			
            			</td>
            			
              			<td class="form_label">核查层级:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_apply" 
           	 				id="check_level" name="check_level"   valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">1级明细:</td>
              			<td >
                			<input class="mini-combobox" width="210px"   id="first_detail" name="first_detail"    
                			data="[{id:'0',text:'无'},{id:'1',text:'有'}]" required="true" onvaluechanged="firstChanged"></input>
            			</td>
              			<td class="form_label">1级明细编号:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="first_detail_code" name="first_detail_code"    required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">2级明细:</td>
              			<td >
                			<input class="mini-combobox" width="210px"   id="second_detail" name="second_detail"   
                			data="[{id:'0',text:'无'},{id:'1',text:'有'}]" required="true" onvaluechanged="secondChanged"></input>
            			</td>
              			<td class="form_label">2级明细编号:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="second_detail_code" name="second_detail_code"    required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">赋分:</td>
              			<td >
                			<input class="mini-combobox" name="warning_eval"   data="[{id:'1',text:'是'},{id:'0',text:'否'}]"
                			id="warning_eval" width="100%" required="true"></input>
            			</td>
            			
              			<td class="form_label">重点预警:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_is_key_warn" 
           	 				id="is_key_warning" name="is_key_warning"   valueField="val" textField="remark" required="true"></input>
            			</td>
            		</tr>
            			
            		<tr>
            			<td class="form_label">是否发送短信:</td>
              			<td >
                			<input class="mini-combobox" width="100%"  nullItemText="" ="请选择..." showNullItem="false"  url="<%=root%>/param/getDict?key=dxtz" 
           	 				id="is_key_dxtz" name="is_key_dxtz" required="true" onvaluechanged="typeValueChanged1()"  valueField="val" textField="remark" ></input>
            			</td>
            			<td class="form_label">短信接收人:</td>
              			<td >
                			<input class="mini-combobox" width="100%"  nullItemText=""  showNullItem="false"  url="<%=root%>/param/getDict?key=jsf" 
           	 				id="is_key_jsf" name="is_key_jsf" required="true" onvaluechanged=""   multiSelect="true" valueField="val" textField="remark" ></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">模型详细说明:</td>
              			<td colspan="3">
                			<input  class="mini-textarea" width="100%"  height="50px" 
           	 				id="remark" name="remark"  maxlength="500" emptyText="最多输入500汉字" ></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">短信提示内容（机构）:</td>
              			<td colspan="3">
                			<input  class="mini-textarea" width="100%"  height="50px" 
           	 				id="message_org" name="message_org"  maxlength="45" emptyText="最多输入45个汉字" ></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">短信提示内容（人员）:</td>
              			<td colspan="3">
                			<input  class="mini-textarea" width="100%"  height="50px" 
           	 				id="message_person" name="message_person"  maxlength="60" emptyText="最多输入60个汉字" ></input>
            			</td>
        			</tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button"  onclick="saveJson()" style="margin-right: 20px;">确定</a>
		<a id="btnCancle" class="mini-button" onclick="onCancel()">关闭</a>
	</div>
	<script type="text/javascript">
	   $G.parse();
	  var form = $G.getForm("dataform1");
	    mini.parse();	
	  	
	  	//当预警模型维度是提示信息时，dxtz可选，不是提示信息时，dxtz和jsf都不可选
	  	function typeValueChanged() {
	  		var warnWd = mini.get("warning_dimension").getValue();
	  		var dxtzCombo = mini.get("is_key_dxtz");
	  		var jsfCombo = mini.get("is_key_jsf");
	  		var confirmCombo =mini.get("is_confirm");
	  		var indentifyCombo=mini.get("is_manual_indentify");
	  		var levelCombo=mini.get("check_level");
	  		if (warnWd == "2") {
	  			dxtzCombo.enable();
	  		 	confirmCombo.setValue("");
	  			confirmCombo.disable();
	  			indentifyCombo.setValue("");
	  			indentifyCombo.disable();
	  			levelCombo.setValue("");
	  			levelCombo.disable();
	  		} else {
	  			confirmCombo.enable();
	  			indentifyCombo.enable();
	  			levelCombo.enable();
	  			dxtzCombo.setValue("");
	  			dxtzCombo.disable();
	  			jsfCombo.setValue("");
	  			jsfCombo.disable();
	  		}
	  	}
	  	//当短信通知为是时，可选择接收方
	  	function typeValueChanged1() {
	  		var keyDxtz = mini.get("is_key_dxtz").getValue();
	  		var jsfCombo = mini.get("is_key_jsf");
	  		if (keyDxtz == "1") {
	  			jsfCombo.enable();
	  		} else {
	  			jsfCombo.setValue("");
	  			jsfCombo.disable();
	  		}
	  	}
	  
	function datachanege(){
		var data1 =$("#remark").val();
		var data2 =$("#message_org").val();
		var data3 =$("#message_person").val();
		if(data1.length>499){
			alert("最多输入500个字");
		}
		if(data2.length>44){
			alert("最多输入45个字");
		}
		if(data3.length>59){
			alert("最多输入60个字");
		}
		
	}
	
	  //保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "<%=root%>/warningmodel/saveWarningModel";
	    var pageType=$G.getbyName("pageType").getValue();
	    if(pageType=="edit"){
	    	urlStr = "<%=root%>/warningmodel/updateWarningModel";
	    }
		form.validate();
		if (form.isValid() == false){
			mini.alert("请完善表单信息。");
			mini.get("btnOk").enable();
			return;
		}else{
		    var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setSuccessFunc(function (){
				$G.closemodaldialog("ok");
			});
		    $G.submitForm("dataform1", urlStr, ajaxConf);
		}
	  }
	  
	function rdChanged(){
		var value = $G.getbyName("is_confirm").getValue();
		var warning_code = $G.getbyName("warning_code").getValue();
		$.ajax({
			url: "<%=root%>/warningmodel/getchangerd",
			data:{'warning_code':warning_code},
			cache: false,
			success: function (text) {
				if(text.result=='0000'){
					$G.getbyName("is_confirm").setValue('1');
					nui.alert("该预警有待认定任务，请认定结束后再修改!");
				}
			}
		});
		
	}
	
	function rgChanged(){
		var value = $G.getbyName("is_manual_indentify").getValue();
		var warning_code = $G.getbyName("warning_code").getValue();
		$.ajax({
			url: "<%=root%>/warningmodel/getchangerg",
			data:{'warning_code':warning_code},
			cache: false,
			success: function (text) {
				if(text.result=='0000'){
					$G.getbyName("is_manual_indentify").setValue('1');
					nui.alert("该预警有待认定任务，请认定结束后再修改!");
				}
			}
		});
	}
	  
  	//获取父页面传递来的json数据
	function setData(data) {
	    //跨页面传递的数据对象，克隆后才可以安全使用
	    var infos = mini.clone(data);
	    $G.getbyName("pageType").setValue(infos.pageType);
	    if(infos.pageType=="edit"){
	    	//编辑页面根据主键加载业务信息
		    var warning_code = infos.warning_code;
			$.ajax({
				url: "<%=root%>/warningmodel/getWarningModelByCode",
				data:{key:warning_code},
				cache: false,
				success: function (text) {
					var formd = mini.decode(text).record;
					var combobox = $G.get("warning_type_code");
					combobox.setUrl("<%=root%>/warningmodel/getWarningtypecodeList?val="+formd.busi_module);
					form.setData(formd);
					if(formd.first_detail=="1"){
						$G.getbyName("first_detail_code").setEnabled(true);
					}else{
						$G.getbyName("first_detail_code").setValue("");
						$G.getbyName("first_detail_code").setEnabled(false);
					}
					if(formd.second_detail=="1"){					
						$G.getbyName("second_detail_code").setEnabled(true);
					}else{
						$G.getbyName("second_detail_code").setValue("");
						$G.getbyName("second_detail_code").setEnabled(false);
					}
					if (formd.warning_dimension == "2") {
			  			$G.getbyName("is_key_dxtz").setEnabled(true);
			  			if(formd.is_key_dxtz=="0"){
			  				$G.getbyName("is_key_jsf").setEnabled(false);
			  			}
			  			$G.getbyName("is_confirm").setValue("");
			  			$G.getbyName("is_confirm").setEnabled(false);
			  			$G.getbyName("is_manual_indentify").setValue("");
			  			$G.getbyName("is_manual_indentify").setEnabled(false);
			  			$G.getbyName("check_level").setValue("");
			  			$G.getbyName("check_level").setEnabled(false);
			  			
			  		} else {
			  			$G.getbyName("is_confirm").setEnabled(true);
			  			$G.getbyName("is_manual_indentify").setEnabled(true);
			  			$G.getbyName("check_level").setEnabled(true);
			  			$G.getbyName("is_key_dxtz").setValue("");
			  			$G.getbyName("is_key_dxtz").setEnabled(false);
			  			$G.getbyName("is_key_jsf").setValue("");
			  			$G.getbyName("is_key_jsf").setEnabled(false);
			  		}
					$G.getbyName("warning_code").setEnabled(false);
					$G.getbyName("warning_name").setEnabled(true);
					$G.getbyName("warning_dimension").setEnabled(true);
					$G.getbyName("busi_module").setEnabled(false);
					$G.getbyName("proc_level").setEnabled(false);
					$G.getbyName("proc_rate").setEnabled(false);
				}
			});
	    }else{
	    	$G.getbyName("is_use").setValue("1");
	    }
	}
  	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("warning_type_code").setValue("");
		var url="<%=root%>/warningmodel/getWarningtypecodeList?val="+val;
		$G.getbyName("warning_type_code").setUrl(url);
		
	}
	
	//1级明细改变
	function firstChanged(e){
		if(e.value=="0"){
			$G.getbyName("first_detail_code").setValue(null);
			$G.getbyName("first_detail_code").setEnabled(false);
		}else{
			$G.getbyName("first_detail_code").setEnabled(true);
		}
	}
	function typeChanged(e){
		if(e.value=="0"){
			$G.getbyName("initial_z").setValue(null);
			$G.getbyName("initial_z").setEnabled(false);
		}else{
			$G.getbyName("initial_z").setEnabled(true);
		}
	}
  	
	//2级明细改变
	function secondChanged(e){
		if(e.value=="0"){
			$G.getbyName("second_detail_code").setValue(null);
			$G.getbyName("second_detail_code").setEnabled(false);
		}else{
			$G.getbyName("second_detail_code").setEnabled(true);
		}
	}
	  
	//关闭窗口
  	function CloseWindow(action) {
		if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
		else window.close();
  	}
	
	  //取消
	  function onCancel() {
			CloseWindow("cancel");
	  }
	  
	 
	</script>
</body>
</html>
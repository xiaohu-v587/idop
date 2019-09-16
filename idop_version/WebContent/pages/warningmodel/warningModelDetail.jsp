<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 预警模型详情
	*
	* @author Luleilei
	* @date 2018-11-30
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>预警模型详情</title>
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
        <form id="dataform1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
          		<table style="table-layout: fixed;" align="center" >
          			<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module"   valueField="val" textField="remark" readonly="true"
           	 				onvaluechanged="moduleChanged"></input>
            			</td>
              			<td class="form_label">模型类型:</td>
              			<td >
	                       	    <input name="warning_type_code" id="warning_type_code" class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true"  
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600" style="width:100%;" readonly="true"/>
            			</td>
        			</tr>
          			<tr>
              			<td class="form_label">模型编号:</td>
              			<td >
                			<input class="mini-textarea" name="warning_code" height="40px" id="warning_code" width="100%" readonly="true"></input>
            			</td>
              			<td class="form_label">模型名称:</td>
              			<td >
                			<input class="mini-textarea" width="100%" height="40px" id="warning_name" name="warning_name"  readonly="true"></input>
            			</td>
        			</tr>
        			
            		
        			
        			<tr>
        				<td class="form_label">模型状态:</td>
              			<td >
                			<input name="is_use" id="is_use"  class="mini-combobox" width="100%" url="<%=root%>/param/getDict?key=dop_markstate" 
           	 				valueField="val" textField="remark" readonly="true" />
            			</td>
              			<td class="form_label">模型维度:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=warn_wd" 
           	 				id="warning_dimension" name="warning_dimension"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">模型等级:</td>
              			<td >
                			<input class="mini-combobox" name="warning_level"   id="warning_level" width="100%" url="<%=root%>/param/getDict?key=dop_warning_lvl" 
                			valueField="val" textField="remark" readonly="true"></input>
            			</td>
              			<td class="form_label">加工频率:</td>
              			<td >
                			<input class="mini-combobox" width="100%" id="proc_rate"   name="proc_rate"  readonly="true"
                			data="[{id:'日',text:'日'},{id:'周',text:'周'},{id:'月',text:'月'},{id:'季',text:'季'},{id:'年',text:'年'}]"
                			></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">加工层级:</td>
              			<td >
                			<input class="mini-combobox" name="proc_level"   id="proc_level" width="100%" url="<%=root%>/param/getDict?key=dop_apply" 
                			valueField="val" textField="remark" readonly="true"></input>
            			</td>
              			
        			</tr>
        			
        			<tr style="display:none">
        				<td class="form_label">模型阀值X:</td>
              			<td >
                			<input class="mini-textbox" name="initial_x" id="initial_x"   width="100%"  readonly="true"></input>
            			</td>
              			<td class="form_label">模型阀值Y:</td>
              			<td >
                			<input class="mini-textbox" width="100%"    name="initial_y" id="initial_y"  readonly="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">是否需要认定:</td>
              			<td >
                			<input class="mini-combobox" name="is_manual_indentify"   valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_sfrgsd"
                			id="is_manual_indentify" width="100%" readonly="true"></input>
            			</td>
            			
              			<td class="form_label">核查层级:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_apply" 
           	 				id="check_level" name="check_level"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">1级明细:</td>
              			<td >
                			<input class="mini-combobox" width="210px"   id="first_detail" name="first_detail"    
                			data="[{id:'0',text:'无'},{id:'1',text:'有'}]" readonly="true" onvaluechanged="firstChanged"></input>
            			</td>
              			<td class="form_label">1级明细编号:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="first_detail_code" name="first_detail_code"    readonly="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">2级明细:</td>
              			<td >
                			<input class="mini-combobox" width="210px"   id="second_detail" name="second_detail"   
                			data="[{id:'0',text:'无'},{id:'1',text:'有'}]" readonly="true" onvaluechanged="secondChanged"></input>
            			</td>
              			<td class="form_label">2级明细编号:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="second_detail_code" name="second_detail_code"    readonly="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">赋分:</td>
              			<td >
                			<input class="mini-combobox" name="warning_eval"   data="[{id:'1',text:'是'},{id:'0',text:'否'}]"
                			id="warning_eval" width="100%" readonly="true"></input>
            			</td>
            			
              			<td class="form_label">重点预警:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dop_is_key_warn" 
           	 				id="is_key_warning" name="is_key_warning"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
            		</tr>
            		
            		<tr>
            			<td class="form_label">是否发送短信:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=dxtz" 
           	 				id="is_key_dxtz" name="is_key_dxtz"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
            			
            			<td class="form_label">短信接收人:</td>
              			<td >
                			<input class="mini-combobox" width="100%"    url="<%=root%>/param/getDict?key=jsf" 
           	 				id="is_key_jsf" name="is_key_jsf"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
            			
        			</tr>
        			
        			<tr>
        				<td class="form_label">模型详细说明:</td>
              			<td colspan="3">
                			<input class="mini-textarea" width="100%"  height="50px" 
           	 				id="remark" name="remark"   readonly="true" maxlength="500" emptyText="最多输入500汉字"></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">短信提示内容（机构）:</td>
              			<td colspan="3">
                			<input  class="mini-textarea" width="100%"  height="50px" 
           	 				id="message_org" name="message_org" readonly="true" maxlength="500" emptyText="最多输入500汉字" ></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">短信提示内容（人员）:</td>
              			<td colspan="3">
                			<input  class="mini-textarea" width="100%"  height="50px" 
           	 				id="message_person" name="message_person" readonly="true" maxlength="500" emptyText="最多输入500汉字" ></input>
            			</td>
        			</tr>
        			
        			
        			
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnCancle" class="mini-button" onclick="onCancel()">关闭</a>
	</div>
	<script type="text/javascript">
	   $G.parse();
	  var form = new mini.Form("dataform1");
	  
	  //保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "<%=root%>/warningmodel/updateMark";
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
	  
  	//获取父页面传递来的json数据
	function setData(data) {
	    //跨页面传递的数据对象，克隆后才可以安全使用
	    var infos = mini.clone(data);
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
	    		
			}
		});
		
		
		
	}
  	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("warning_type_code").setValue("");
		var url="<%=root%>/warningmodel/getWarningtypecodeList?val="+val;
		$G.getbyName("warning_type_code").setUrl(url);
		
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
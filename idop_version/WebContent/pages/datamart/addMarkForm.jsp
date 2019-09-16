<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 指标展示层级
	*
	* @author Luleilei
	* @date 2018-11-22
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>指标展示层级</title>
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
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
    <div class="mini-fit">
        <form id="dataform1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
          		<table style="table-layout: fixed;" align="center" >
          			<tr>
              			<td class="form_label">指标编号:</td>
              			<td >
                			<input class="mini-textarea" name="mark_code"  height="25px" id="mark_code" width="210px" ></input>
            			</td>
              			<td class="form_label">指标名称:</td>
              			<td >
                			<input class="mini-textarea" width="210px" height="25px" id="mark_name" name="mark_name"  ></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="210px"    url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module"   valueField="val" textField="remark" required="true" 
           	 				onvaluechanged="moduleChanged"></input>
            			</td>
              			<td class="form_label">业务类型:</td>
              			<td >
                			<input name="sub_busi_code" id="sub_busi_code"class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true" oncloseclick="onCloseClick" 
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600" style="width:210px;" required="true"/>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">指标类型:</td>
              			<td >
                			<input name="mark_type_code"  id="mark_type_code" class="nui-treeselect"  
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true" oncloseclick="onCloseClick" 
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600" style="width:210px;" required="true"/>
            			</td>
              			<td class="form_label">指标维度:</td>
              			<td >
                			<input class="mini-combobox" width="210px"    url="<%=root%>/param/getDict?key=ZBWD" 
           	 				id="mark_dimension" name="mark_dimension"   valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">计量单位:</td>
              			<td >
                			<input class="mini-combobox" name="unit"   id="unit" width="210px" url="<%=root%>/param/getDict?key=dop_unit" 
                			valueField="val" textField="remark"></input>
            			</td>
              			<td class="form_label">货币币别:</td>
              			<td >
                			<input class="mini-combobox" width="210px" name="cur" data="[{id:'人民币',text:'人民币'},{id:'美元',text:'美元'}]" ></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">指标值类型:</td>
              			<td >
                			<input class="mini-combobox" name="value_type"   valueField="val" textField="remark"  url="<%=root%>/param/getDict?key=dop_valuetype"
                			id="value_type" width="210px" required="true"></input>
            			</td>
        			</tr>
        			
        			<tr>
        			
        				<td class="form_label">汇总层级:</td>
              			<td >
							<input class="mini-checkbox"  name="summary_level1" id="display_level1" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="省行"/>
							<input class="mini-checkbox"  name="summary_level2" id="display_level2" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="分行"/>
							<input class="mini-checkbox"  name="summary_level3" id="display_level3" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="管辖支行"/>
							<input class="mini-checkbox"  name="summary_level4" id="display_level4" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="网点"/>
            			</td>
            			
        				<td class="form_label">展示层级:</td>
              			<td >
							<input class="mini-checkbox"  name="display_level1" id="display_level1" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="省行"/>
							<input class="mini-checkbox"  name="display_level2" id="display_level2" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="分行"/>
							<input class="mini-checkbox"  name="display_level3" id="display_level3" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="管辖支行"/>
							<input class="mini-checkbox"  name="display_level4" id="display_level4" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="网点"/>
            			</td>
              			
        			</tr>
        			
        			<tr>
        				<td class="form_label">加工层级:</td>
              			<td >
                			<input class="mini-combobox" name="proc_level" id="proc_level" url="<%=root%>/param/getDict?key=dop_apply"  
                			valueField="val" textField="remark" required="true" width="210px" required="true"></input>
            			</td>
        				<td class="form_label">加工频度:</td>
              			<td >
                			<input class="mini-combobox" width="210px"   data="[{id:'日',text:'日'},{id:'周',text:'周'},{id:'月',text:'月'},{id:'季',text:'季'},{id:'年',text:'年'}]"
           	 				id="proc_rate" name="proc_rate"   required="true"></input>
            			</td>
            			
        				
              			
        			</tr>
        			
        			
        			<tr>
        				<td class="form_label">汇总方式:</td>
              			<td >
                			<input class="mini-combobox" name="total_type"  url="<%=root%>/param/getDict?key=dop_totalType" 
                			 id="total_type" width="210px" valueField="val" textField="remark" ></input>
            			</td>
              			<td class="form_label">指标1:</td>
              			<td >
                			<input class="mini-textbox" width="210px"    id="dividend" name="dividend"   ></input>
            			</td>
        			</tr>
        			
        			<tr>
        				<td class="form_label">指标2:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="divisor" name="divisor" ></input>
            			</td>
              			<td class="form_label">指标3:</td>
              			<td >
                			<input class="mini-textbox" width="210px"   id="zb3" name="zb3" ></input>
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
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button"  onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	   $G.parse();
	  var form = new mini.Form("dataform1");
	  //区间赋值条件字段标识
	  var node = 1;
	  //预警扣分条件字段标识
	  var num = 1;
	  //保存或修改数据
	  function saveJson() {
	    //保存

	    var markcode = $G.getbyName("mark_code").getValue()
	    var url = "<%=root%>/datamart/checkMark";
	    $.ajax({
			url: url,
			data:{markcode:markcode},
			cache: false,
			success: function (text) {
				if(text.count=="0"){
					 var urlStr = "<%=root%>/datamart/saveMark";
						form.validate();
						if (form.isValid() == false){
							mini.alert("请完善表单信息。");
							mini.get("btnOk").enable();
							return;
						}else{
							/* $("#dataform1").attr('action',urlStr);
							$("#dataform1").submit();//提交表单 */
						    var ajaxConf = new GcdsAjaxConf();
						    ajaxConf.setSuccessFunc(function (){
								$G.closemodaldialog("ok");
							});
						    $G.submitForm("dataform1", urlStr, ajaxConf);
						}
				}else{
					$G.alert("指标编号已存在！");
				}
			}
		});
	    
	  }
	  
  	//获取父页面传递来的json数据
	function setData(data) {
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
  	
	//2级明细改变
	function secondChanged(e){
		if(e.value=="0"){
			$G.getbyName("second_detail_code").setValue(null);
			$G.getbyName("second_detail_code").setEnabled(false);
		}else{
			$G.getbyName("second_detail_code").setEnabled(true);
		}
	}
  	
	
  	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("sub_busi_code").setValue("");
		var url="<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+val;
		$G.getbyName("sub_busi_code").setUrl(url);
		
		$G.getbyName("mark_type_code").setValue("");
		var url="<%=root%>/quotaDataExhibit/getMarktypecodeList?val="+val;
		$G.getbyName("mark_type_code").setUrl(url);
		
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
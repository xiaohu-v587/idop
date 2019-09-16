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
          		<table style="table-layout: fixed;" align="center" id="addtable">
            		<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="200px"    url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module"   valueField="val" textField="remark" readonly="true"></input>
            			</td>
              			<td class="form_label">业务名称:</td>
              			<td >
                			<input class="mini-combobox" name="sub_busi_code"   id="sub_busi_code"  valueField="val" textField="remark" width="200px" readonly="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">指标编号:</td>
              			<td >
                			<input class="mini-textarea" name="mark_code"  height="25px" id="mark_code" width="200px" readonly="true"></input>
            			</td>
              			<td class="form_label">指标名称:</td>
              			<td >
                			<input class="mini-textarea" width="200px" height="25px" id="mark_name" name="mark_name"  readonly="true"></input>
            			</td>
        			</tr>
            		
            		
        			<tr>
        				<td class="form_label" colspan="1" margin-left="50px">展示层级:</td>
              			<td colspan="2">
							<input class="mini-checkbox"  name="display_level1" id="display_level1" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="省行"/>
							<input class="mini-checkbox"  name="display_level2" id="display_level2" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="分行"/>
							<input class="mini-checkbox"  name="display_level3" id="display_level3" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="管辖支行"/>
							<input class="mini-checkbox"  name="display_level4" id="display_level4" data="[{id:'1',text:'true'},{id:'0',text:'false'}]" text="网点"/>

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
	    var urlStr = "<%=root%>/datamart/updateAdjust";
	    var d1=$G.getbyName("display_level1").getValue();
	    var d2=$G.getbyName("display_level2").getValue();
	    var d3=$G.getbyName("display_level3").getValue();
	    var d4=$G.getbyName("display_level4").getValue();
	    if(d1=="true"||d2=="true"||d3=="true"||d4=="true"){
	    	var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setSuccessFunc(function (){
				$G.closemodaldialog("ok");
			});
		    $G.submitForm("dataform1", urlStr, ajaxConf);
	    }else{
	    	$G.alert("请选择展示层级");
	    }
	  }
	  
  	//获取父页面传递来的json数据
	function setData(data) {
	    //跨页面传递的数据对象，克隆后才可以安全使用
	    var infos = mini.clone(data);
      	//编辑页面根据主键加载业务信息
	    var mark_code = infos.mark_code;
      	var mark_name = infos.mark_name;
      	var display_level = infos.display_level;
      	var busi_module = infos.busi_module;
      	var sub_busi_code = infos.sub_busi_code;
		$G.getbyName("mark_code").setValue(mark_code);
		$G.getbyName("mark_name").setValue(mark_name);
		$G.getbyName("busi_module").setValue(busi_module);
		$G.getbyName("display_level1").setValue(display_level.split(",")[0]);
		$G.getbyName("display_level2").setValue(display_level.split(",")[1]);
		$G.getbyName("display_level3").setValue(display_level.split(",")[2]);
		$G.getbyName("display_level4").setValue(display_level.split(",")[3]);
		var combobox = $G.get("sub_busi_code");
		
		combobox.setUrl("<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+busi_module);
		$G.getbyName("sub_busi_code").setValue(sub_busi_code);
		

		
		
		
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
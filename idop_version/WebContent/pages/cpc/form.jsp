<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 组合参数配置
	*
	* @author jzc4en
	* @date 2018-03-13
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>组合参数配置</title>
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
		<!-- 标识页面是查看(view)、修改(edit)、新增(add) -->
    	<input name="pageType" class="mini-hidden" />
        <form id="form1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
         		<input class="mini-hidden"  name="id"  id="id"/>
         		<input class="mini-hidden"  name="counter"  id="counter" value="0"/>
         		<input class="mini-hidden" name="tNum" id="tNum" value="0"/>
         		<input class="mini-hidden" name="wNum" id="wNum" value="0"/>
          		<table style="width: 80%" align="center" id="addtable">
            		
            		<tr>
              			<td class="form_label">监测条件名称:</td>
              			<td >
                			<input class="mini-textbox" name="monitor_name"   id="monitor_name"  required="true" ></input>
            			</td>
              			<td class="form_label">监测频率:</td>
              			<td >
                			<input class="mini-combobox" nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_rate" 
           	 				id="monitor_rate" name="monitor_rate"  onvaluechanged="onRateValueChanged" valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">监测类型:</td>
              			<td >
              				<input class="mini-hidden" name="preVal" id="preVal" />
                			<input class="mini-combobox"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_type" 
           	 				id="monitor_type" name="monitor_type"   onvaluechanged="typeValueChanged()"  valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">监测机构层级:</td>
              			<td >
                			<input class="mini-combobox"   nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_level" 
           	 				id="monitor_level" name="monitor_level"   onvaluechanged="onLevelValueChanged"  valueField="val" textField="remark" enabled="false" required="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">监测方式:</td>
              			<td >
                			<input class="mini-combobox"   nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_method" 
           	 				id="monitor_method" name="monitor_method"   onvaluechanged=""  valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">监测状态:</td>
              			<td >
                			<input class="mini-combobox"   nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_state" 
           	 				id="monitor_state" name="monitor_state"   onvaluechanged=""  valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
				</table>
				<div style="margin-left: 118px">
				<a onclick="addTarget" class="mini-button" id="addtargetbtn">添加指标条件</a>&nbsp;&nbsp;
          		<a onclick="addWarning" class="mini-button" id="addwarningbtn">添加预警条件</a>&nbsp;&nbsp;
          		<input id="qCb" name="question" class="mini-combobox" required="true" emptyText="预警类别..." url="<%=root%>/param/getDict?key=cpc_warning_cls" valueField="val" textField="remark" onvaluechanged="qValChg" />
          		<input style="margin-left: 150px;" onitemclick="tplchanged" class="mini-combobox" id="choosetpl" valueField="val" textField="name" url="<%=root%>/cpc/getTplList" emptyText="基于模板..." />
          		<a id="btnDelTpl" class="mini-button" onclick="delTpl()" style="margin-right: 20px;" visible="false">删除此模板</a> 
				<table id="additionTab">
					
				</table>
				</div>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnSaveAsTpl" class="mini-button"  onclick="saveAsTpl()" style="margin-right: 20px;">保存为模板</a>
		<a id="btnOk" class="mini-button" onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	  var form = new mini.Form("form1");
	  var counter = 0;
	  mini.parse();
	  
	  //监测频率为"日"时可添加指标条件
	  function onRateValueChanged(e) {
		  var btnAddTarget = $G.get("addtargetbtn"); 
		  if(e.value != "0") {
			  btnAddTarget.disable();
			  var btns = mini.getsbyName("t_delbtn");
			  for (var i = 0, l = btns.length; i < l; i++) {
			  	  removeEle = btns[i].el.parentElement.parentElement;
				  removeEle.parentElement.removeChild(removeEle);
				  counter--;
			  }
			  var cdtTab = document.getElementById("additionTab");
			  var trs = cdtTab.children;
			  for (var i = 0, l = trs.length; i < l; i++) {
				  var td0 = trs[i].getElementsByTagName("td")[0];
				  td0.innerHTML = "条件" + (i+1) + ":";
				  var td1 = trs[i].children[1];
				  var td2 = trs[i].children[2];
			  }
		  } else {
			  var level = $G.get("monitor_level").getValue();
			  if (level == "1" || !level){
			  	btnAddTarget.enable();
			  }
		  }
	  }
	  
	  function onLevelValueChanged(e) {
		  var btnAddTarget = $G.get("addtargetbtn"); 
		  if(e.value == "0") {
			  btnAddTarget.disable();
			  var btns = mini.getsbyName("t_delbtn");
			  for (var i = 0, l = btns.length; i < l; i++) {
			  	  removeEle = btns[i].el.parentElement.parentElement;
				  removeEle.parentElement.removeChild(removeEle);
				  counter--;
			  }
			  var cdtTab = document.getElementById("additionTab");
			  var trs = cdtTab.children;
			  for (var i = 0, l = trs.length; i < l; i++) {
				  var td0 = trs[i].getElementsByTagName("td")[0];
				  td0.innerHTML = "条件" + (i+1) + ":";
				  var td1 = trs[i].children[1];
				  var td2 = trs[i].children[2];
			  }
		  } else {
			  var rate = $G.get("monitor_rate").getValue();
			  if (rate == "0" || !rate){
			  	btnAddTarget.enable();
			  }
		  }
	  }
	  
	  function tplchanged() {
				  $G.mask({
					el:document.body,
					html:'加载中...',
					cls:'mini-mask-loading',
				  });
		  $.ajax({
			  url: "<%=root%>/cpc/getDetailById?key=" + $G.get("choosetpl").getValue(),
			  success: function(data) {
				  var formd = mini.decode(data);
				  $("#additionTab").children().remove();
				  $G.get("monitor_type").setValue(formd.record.monitor_type);
				  $G.get("monitor_rate").setValue(formd.record.monitor_rate);
				  $G.get("monitor_rate").doValueChanged();
				  if (formd.record.monitor_type == "0"){
					  $G.get("monitor_level").setValue(formd.record.monitor_level);
					  $G.get("monitor_level").doValueChanged();
				  }
				  counter = 0;
				  setUrlAndValue(formd);
				  if (formd.wList.length > 0) {
					$G.get("qCb").setValue(formd.wList[0].question);
				  }
				  $G.unmask();
			  }
		  });
	  }
	  
	  function delTpl() {
		  var val = $G.get("choosetpl").getValue();
		  if (!val) {
			  mini.alert("请选择要删除的模板");
			  return;
		  }
		  var text = $G.get("choosetpl").getText();
		  mini.confirm("确定删除该模板("+text+")？", "确定？", function(action) {
				if (action == "ok") {			
					$.ajax({
						url : "<%=root%>/cpc/delTpl?key=" + val,
						success : function(msg) {
							var ret = mini.decode(msg);
							if (ret.msg == 'success') {
								$G.alert('模板(' + text + ')已删除!');
								mini.get("choosetpl").setValue('');
								mini.get("choosetpl").load('<%=root%>/cpc/getTplList');
							}
						}
					});
				}
		  });
	  }
	  
	  function qValChg() {
		  var qArr = $G.getsbyName("question");
		  for (var i = 1; i < qArr.length; i++){
			  qArr[i].setValue(qArr[0].getValue());
		  }
		  
	  }
	  
	  function saveAsTpl() {
		  if (!$G.get("monitor_type").getValue() || !$G.get("monitor_rate").getValue()){
			mini.alert("请选择监测类型和监测频率");
			return;
		  }
		  if ($G.get("monitor_type").getValue()=="0" && !$G.get("monitor_level").getValue()){
				mini.alert("请选择监测层级");
				return;
		  }
		  $G.prompt("模板名称", "保存为模板", function(btn, tplName) {
			 if (btn == "ok") {
				$G.get("counter").setValue(counter);//传一个条件总数到后台
				//在提交之前为每个输入控件设置一个单独的name			
				changeNames();
				var urlStr = "<%=root%>/cpc/saveAsTpl?tplName=" + tplName;
				urlStr = encodeURI(urlStr);
			   	$.ajax({
			   		url: urlStr,
			   		type: "post",
			   		data: form.getData(),
			   		success: function() {
			   			$G.closemodaldialog("ok");
			   		}
			   	});
			 }
		  });
	  }
	  
		//当监测类型为机构监测时可选择监测机构层级
	  	function typeValueChanged() {
	  		var monitorType = mini.get("monitor_type").getValue();
	  		var preVal = $G.get("preVal").getValue();//改变前的监测类型值
	  		if (!((preVal == 0 && monitorType == 2) || (preVal == 2 && monitorType == 0))) {//如果不是在机构监测和业务监测之间切换,则重置所有条件中的预警选择和条件选择
	  			var targets = $G.getsbyName("target");
	  			var warnings = $G.getsbyName("warning_name");
	  			var tarTypes = $G.getsbyName("biz_type");
	  			var warnTypes = $G.getsbyName("warning_type");
	  			for (var i = 0, l = targets.length; i < l; i++) {
	  				targets[i].setValue('');
	  				if (tarTypes[i].getValue()) {
		  				targets[i].setUrl("<%=root%>/cpc/getMarkParamList?sub_busi_code="+tarTypes[i].getValue()+"&monitor_type="+monitorType);
	  				}
	  			}
	  			for (var i = 0, l = warnings.length; i < l; i++) {
	  				warnings[i].setValue('');
	  				if (warnTypes[i].getValue()) {
	  					warnings[i].setUrl("<%=root%>/cpc/getWarnNameList?val=" + warnTypes[i].getValue()+"&monitor_type="+monitorType);
	  				}
	  			}
	  		}
	  		var monitorLevelCombo = mini.get("monitor_level");
	  		if (monitorType == 0) {
	  			monitorLevelCombo.enable();
	  		} else {
	  			monitorLevelCombo.setValue('');
	  			monitorLevelCombo.disable();
	  		}
	  		$G.get("preVal").setValue(monitorType);
	  	}
		
		function setUrlAndValue(formd) {
			var tList = formd.tList;
			var wList = formd.wList;
			var record = formd.record;
			for (var i = 0; i < formd.tSize; i++) {
				var tr = addTarget();
				$G.getbyName("biz_type", tr).setUrl("<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+eval("tList[i].biz_module_target"));
				$G.getbyName("target", tr).setUrl("<%=root%>/cpc/getMarkParamList?sub_busi_code="+eval("tList[i].biz_type")+"&monitor_type="+record.monitor_type);
				$G.getbyName("biz_module_target", tr).setValue(tList[i].biz_module_target);
				$G.getbyName("biz_type", tr).setValue(tList[i].biz_type);
				$G.getbyName("operator_target", tr).setValue(tList[i].operator_target);
				$G.getbyName("target", tr).setValue(tList[i].target);
				$G.getbyName("num_target", tr).setValue(tList[i].num_target);
			}
			for (var i = 0; i < formd.wSize; i++) {
				var tr = addWarning();
				$G.getbyName("warning_type", tr).setUrl("<%=root%>/warning/getWarningTypeList?val="+eval("wList[i].biz_module_warning"));
				$G.getbyName("warning_name", tr).setUrl("<%=root%>/cpc/getWarnNameList?val="+eval("wList[i].warning_type")+"&monitor_type="+record.monitor_type);
				$G.getbyName("biz_module_warning", tr).setValue(wList[i].biz_module_warning);
				$G.getbyName("warning_type", tr).setValue(wList[i].warning_type);
				$G.getbyName("warning_name", tr).setValue(wList[i].warning_name);
				$G.getbyName("question", tr).setValue(wList[i].question);
				$G.getbyName("num_warning", tr).setValue(wList[i].num_warning);
				$G.getbyName("operator_warning", tr).setValue(wList[i].operator_warning);
			}
		}
		
	  	 //获取父页面传递来的json数据
		  function setData(data) {
		    var cData = mini.clone(data);
		    mini.getbyName("pageType").setValue(cData.action);
		    if (cData.action == "edit" || cData.action == "view") {
		      	//编辑页面根据主键加载监测配置信息
			    var id = cData.id;
				$.ajax({
					url: "<%=root%>/cpc/getDetailById",
					data:{key: id},
					cache: false,
					success: function (text) {
						var formd = mini.decode(text);
						var record = formd.record;
						form.setData(formd.record);
						if (record.monitor_type == 0) {
							$G.get("monitor_level").enable();
						}
						if (record.monitor_rate != 0 || record.monitor_level != "1") {
							$G.get("addtargetbtn").disable();
						}
						setUrlAndValue(formd);
						$G.get("preVal").setValue(record.monitor_type);
						if (cData.action == "view") {
							form.setEnabled(false);
							var tbuttons = mini.getsbyName("t_delbtn");
							for (var i = 0, l = tbuttons.length; i < l; i++) {
								tbuttons[i].hide();
							}
							var wbuttons = mini.getsbyName("w_delbtn");
							for (var i = 0, l = wbuttons.length; i < l; i++) {
								wbuttons[i].hide();
							}
							$G.get("addtargetbtn").hide();
							$G.get("addwarningbtn").hide();
							$G.get("choosetpl").hide();
							$G.get("btnCancle").setText("返回");
							$G.get("btnOk").hide();
							$G.get("btnSaveAsTpl").hide();
							$G.get("qCb").hide();
						}
						if (cData.action == "edit") {
							$G.get("choosetpl").hide();
							$G.get("btnSaveAsTpl").hide();
							if (formd.wList.length > 0) {
								$G.get("qCb").setValue(formd.wList[0].question);
							}
						}
					}
				});
		    } else if (cData.action == "addTpl") {//新增模板
		    	var tplCombo = $G.get("choosetpl");
		    	tplCombo.setEmptyText("已有模板...");
		    	$G.get("btnDelTpl").show();
		    	$("#addtable tr:not(:eq(1))").each(function() {
		    		$(this).remove();
		    	});
		    	$G.get("btnOk").hide();
// 		    	var t = $(".form_label:contains('监测机构层级')")[0];
// 		    	t.parentElement.removeChild(t);
// 		    	$("#addtable td:last").remove();
		    	$("#addtable").css({"width": '50%', "margin-left": "118px"});
		    	var td = document.createElement("td");
		    	td.innerHTML = '<input class="mini-combobox" nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=monitor_rate" id="monitor_rate" name="monitor_rate"  onvaluechanged="onRateValueChanged" valueField="val" textField="remark" required="true"></input>';
		    	$("#addtable tr").append('<td class="form_label">监测频率:</td>').append(td);
		    	$G.parse();
		    } else {//新增
		    	$G.get("btnSaveAsTpl").hide();
		    }
		  }
	  	 
	  	
	  
	  //保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "<%=root%>/cpc/save";
	    var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
	    //表示为编辑状态
	    if (pageType == "edit") {
	      urlStr = "<%=root%>/cpc/update";
	    }
		$G.get("counter").setValue(counter);//传一个条件总数到后台
		//在提交之前为每个输入控件设置一个单独的name			
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    ajaxConf.setBeforeSubmitFunc(changeNames);
	    $G.submitForm("form1", urlStr, ajaxConf);
	  }
	  
	  function changeNames() {
		  var tNum = $G.get("tNum");
			var wNum = $G.get("wNum");
			var tCounter = 0, wCounter = 0;
		  var cdtTab = document.getElementById("additionTab");
			var trs = cdtTab.children;
			for (var i = 0, l = trs.length; i < l; i++) {
				var flag = $G.getbyName("flag", trs[i]);//预警条件还是指标条件
				flag.setName("flag_"+(i+1));
				if (flag.getValue() == "t") {//target 指标
					tCounter++;
					$G.getbyName("biz_module_target", trs[i]).setName("biz_module_target_" + (i+1));
					$G.getbyName("biz_type", trs[i]).setName("biz_type_" + (i+1));
					$G.getbyName("target", trs[i]).setName("target_" + (i+1));
					$G.getbyName("operator_target", trs[i]).setName("operator_target_" + (i+1));
					$G.getbyName("num_target", trs[i]).setName("num_target_" + (i+1));
				} else {// warning 预警
					wCounter++;
					$G.getbyName("biz_module_warning", trs[i]).setName("biz_module_warning_" + (i+1));
					$G.getbyName("warning_type", trs[i]).setName("warning_type_" + (i+1));
					$G.getbyName("warning_name", trs[i]).setName("warning_name_" + (i+1));
					$G.getbyName("question", trs[i]).setName("question_" + (i+1));
					$G.getbyName("operator_warning", trs[i]).setName("operator_warning_" + (i+1));
					$G.getbyName("num_warning", trs[i]).setName("num_warning_" + (i+1));
				}
			}
			tNum.setValue(tCounter);
			wNum.setValue(wCounter);
			return true;
	  }
	  
		//业务模块改变
		function moduleChanged(e){
			var typeName = "biz_type", tarName = "target";
			if (e.source.name) {
				typeName += e.source.name.substring(17);
				tarName += e.source.name.substring(17);
			}
			var bizType = $G.getbyName(typeName, e.source.el.parentElement.parentElement);
			var target = $G.getbyName(tarName, e.source.el.parentElement.parentElement);
			bizType.setValue('');
			target.setValue('');
			target.setUrl('');
			var url="<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+e.value;
			bizType.setUrl(url);
			
		}
		
		function busi_markTypeChanged(e){
			var tarName = "target";
			if (e.source.name) {
				tarName += e.source.name.substring(8);
			}
			var target = $G.getbyName(tarName, e.source.el.parentElement.parentElement);  
			target.setValue("");
			var monitorType=$G.getbyName("monitor_type").getValue(); 
			if(monitor_type){
				var url="<%=root%>/cpc/getMarkParamList?sub_busi_code="+e.value+"&monitor_type="+monitorType;
			    target.setUrl(url);
			}
		}

	
	  
	  //新增指标条件
	  function addTarget(){
		  if (!$G.get("monitor_type").getValue() || !$G.get("monitor_rate").getValue()){
			mini.alert("请选择监测类型和监测频率");
			return;
		  }
		  if ($G.get("monitor_type").getValue()=="0" && !$G.get("monitor_level").getValue()){
				mini.alert("请选择监测层级");
				return;
		  }
		  if (counter == 30) {
			  mini.alert("条件数已达上限:30");
			  return;
		  }
		  var tab = document.getElementById("additionTab");
		  var tr = document.createElement("tr");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+ (++counter) +":";
		  var td1 = document.createElement("td");
		  var td2 = document.createElement("td");
		  
		  td1.innerHTML="<input name='biz_module_target' class='mini-combobox' url='<%=root%>/param/getDict?key=dop_ywtype' valueField='val' textField='remark' onvaluechanged='moduleChanged' required='true' emptyText='业务模块...'/>&nbsp;&nbsp;" 
		 	+ "<input name='biz_type' url='' class='mini-treeselect' required='true' dataField='data' valuefield='id' textfield='remark' parentfield='upid' valueFromSelect='false' multiSelect='false'  expandOnLoad='0' emptyText='业务类型...' allowInput='false' showClose='false'  showRadioButton='true' showFolderCheckBox='false' popupWidth='200px' popupHeight='350px' popupMaxHeight='300px' onvaluechanged='busi_markTypeChanged'/>&nbsp;&nbsp;"
			+ "<input  name='target' url='' multiSelect='false' class='mini-combobox'  popupWidth='350px' emptyText='指标...'  required='true' valueField='mark_code' textField='mark_name' width='350'></input>&nbsp;&nbsp;"
			+ "<input name='operator_target' class='mini-combobox' width='70px' required='true'  emptyText='关系' url='<%=root%>/param/getDict?key=compare_operator' valueField='val' textField='remark'/>&nbsp;&nbsp;"
			+ "<input name='num_target' class='mini-spinner' width='50px' maxValue='999999'/>";
			
		  td2.innerHTML=" <button onclick='delRow()' class='mini-button' name='t_delbtn'>删除</button><input class='mini-hidden' name='flag' value='t'/>";
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tr.appendChild(td2);
		  tab.appendChild(tr);
		  $G.parse();
		  return tr;
	  }
	  
	  //新增预警条件
	  function addWarning(){
		  if (!$G.get("monitor_type").getValue()){
			mini.alert("请选择监测类型");
			return;
	      }
		  if (counter == 30) {
			  mini.alert("条件数已达上限:30");
			  return;
		  }
		  var tab = document.getElementById("additionTab");
		  var tr = document.createElement("tr");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+(++counter)+":";
		  var td1 = document.createElement("td");
		  var td2 = document.createElement("td");
		  
		  td1.innerHTML="<input name='biz_module_warning' required='true' onvaluechanged='onywTypeChanged' class='mini-combobox' url='<%=root%>/param/getDict?key=dop_ywtype' valueField='val' textField='remark' emptyText='业务模块...'/>&nbsp;&nbsp;" 
				+ "<input name='warning_type' url='' required='true' class='mini-treeselect' dataField='data' valuefield='id' textfield='remark' parentfield='upid' valueFromSelect='false' multiSelect='false'  expandOnLoad='0' emptyText='预警类型...' allowInput='false' showRadioButton='true' showFolderCheckBox='false' popupWidth='200px' popupHeight='350px' popupMaxHeight='300px' onvaluechanged='warnTypechange'/>&nbsp;&nbsp;"
				+ "<input name='warning_name' url='' multiSelect='false' class='mini-combobox' required='true' multiSelect='false' emptyText='预警...' valueField='warning_code' textField='warning_name' popupWidth='350px' width='350'></input>&nbsp;&nbsp;"
				+ "<input name='question' class='mini-combobox' required='true' emptyText='类别...' url='<%=root%>/param/getDict?key=cpc_warning_cls' valueField='val' textField='remark' enabled='false'/>&nbsp;&nbsp;"
				+ "<input name='operator_warning' class='mini-combobox' required='true' emptyText='关系' width='70px' url='<%=root%>/param/getDict?key=compare_operator' valueField='val' textField='remark'/> &nbsp;&nbsp;"
				+ "<input name='num_warning' class='mini-spinner'  width='50px' maxValue='999999'/> &nbsp;&nbsp;" ;
				
		  td2.innerHTML=" <button onclick='delRow()' class='mini-button' name='w_delbtn'>删除</button><input class='mini-hidden' name='flag' value='w'/>";
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tr.appendChild(td2);
		  tab.appendChild(tr);
		  $G.parse();
		  $G.getbyName('question', tr).setValue($G.getsbyName('question')[0].getValue());
		  return tr;
	  }
	  
	
	  
	  //删除一行条件
	  function delRow(){
		  var eventSrc = event.srcElement;//<span>
		  var tr = eventSrc.parentElement.parentElement.parentElement;
		  var cdtTab = document.getElementById("additionTab");
		  cdtTab.removeChild(tr);
		  counter--;
		  var trs = cdtTab.children;
		  for (var i = 0, l = trs.length; i < l; i++) {
			  var td0 = trs[i].getElementsByTagName("td")[0];
			  td0.innerHTML = "条件" + (i+1) + ":";
			  var td1 = trs[i].children[1];
			  var td2 = trs[i].children[2];
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
	  
	  //业务类型改变
	  function onywTypeChanged(e){
		var warnType = "warning_type", warnName = "warning_name";
		if (e.source.name) {
			warnType += e.source.name.substring(18);
			warnName += e.source.name.substring(18);
		}
		warnType = $G.getbyName(warnType, e.source.el.parentElement.parentElement);
		warnName = $G.getbyName(warnName, e.source.el.parentElement.parentElement);
		warnType.setValue('');
		warnName.setValue('');
		var url="<%=root%>/warning/getWarningTypeList?val="+e.value;
		warnType.setUrl(url);
	  }
	  
	  //预警类型改变
	  function warnTypechange(e){
		var warnName = "warning_name";
		if (e.source.name) {
			warnName += e.source.name.substring(12);
		}
		var monitorType=$G.getbyName("monitor_type").getValue(); 
		var warnName = $G.getbyName(warnName, e.source.el.parentElement.parentElement);
		var url = "<%=root%>/cpc/getWarnNameList?val=" + e.value+"&monitor_type="+monitorType;
		warnName.setUrl(url);
	  }
	  
	
	</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>监测说明</title>
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
           	 				id="monitor_rate" name="monitor_rate"  onvaluechanged="" valueField="val" textField="remark" required="true"></input>
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
           	 				id="monitor_level" name="monitor_level"   onvaluechanged=""  valueField="val" textField="remark" enabled="false" required="true"></input>
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
				<a href="javascript:addTarget()" class="mini-button" id="addtargetbtn">添加指标条件</a>&nbsp;&nbsp;
          				<a href="javascript:addWarning()" class="mini-button" id="addwarningbtn">添加预警条件</a>&nbsp;&nbsp;
          				<input onvaluechanged="tplchanged" class="mini-combobox" id="choosetpl" valueField="val" textField="name" url="<%=root%>/cpc/getTplList" emptyText="选择模板配置..."></input>
				<table id="additionTab">
					
				</table>
				</div>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnSaveAsTpl" class="mini-button"  onclick="saveAsTpl()" style="margin-right: 20px;">保存为模板配置</a>
		<a id="btnOk" class="mini-button" onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	  var form = new mini.Form("form1");
	  var counter = 0;
	  mini.parse();
	  	
		function setUrlAndValue(formd) {
			var tList = formd.tList;
			var wList = formd.wList;
			var record = formd.record;
			for (var i = 0; i < formd.tSize; i++) {
				var tr = addTarget();
				$G.getbyName("biz_type", tr).setUrl("<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+eval("tList[i].biz_module_target"));
				$G.getbyName("target", tr).setUrl("<%=root%>/orgMonitor/getMarkParamList?sub_busi_code="+eval("tList[i].biz_type")+"&monitor_type="+record.monitor_type);
				$G.getbyName("biz_module_target", tr).setValue(tList[i].biz_module_target);
				$G.getbyName("biz_type", tr).setValue(tList[i].biz_type);
				$G.getbyName("operator_target", tr).setValue(tList[i].operator_target);
				$G.getbyName("target", tr).setValue(tList[i].target);
				$G.getbyName("num_target", tr).setValue(tList[i].num_target);
			}
			for (var i = 0; i < formd.wSize; i++) {
				var tr = addWarning();
				$G.getbyName("warning_type", tr).setUrl("<%=root%>/warning/getWarningTypeList?val="+eval("wList[i].biz_module_warning"));
				$G.getbyName("warning_name", tr).setUrl("<%=root%>/orgMonitor/getWarnNameList?val="+eval("wList[i].warning_type")+"&monitor_type="+record.monitor_type);
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
						setUrlAndValue(formd);
						$G.get("preVal").setValue(record.monitor_type);
							form.setEnabled(false);
							var buttons = mini.getsbyName("delbtn");
							for (var i = 0, l = buttons.length; i < l; i++) {
								buttons[i].hide();
							}
							$G.get("addtargetbtn").hide();
							$G.get("addwarningbtn").hide();
							$G.get("choosetpl").hide();
							$G.get("btnCancle").setText("返回");
							$G.get("btnOk").hide();
							$G.get("btnSaveAsTpl").hide();
					
					}
				});
		    
		  }
	  	 
		  //新增指标条件
		  function addTarget(){
			  form.validate();
			  if (!form.isValid()){
				mini.alert("请完善表单信息");
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
				+ "<input name='num_target' class='mini-spinner' width='50px' />";
				
			  td2.innerHTML=" <button onclick='delRow()' class='mini-button' name='delbtn'>删除</button><input class='mini-hidden' name='flag' value='t'/>";
			  tr.appendChild(td);
			  tr.appendChild(td1);
			  tr.appendChild(td2);
			  tab.appendChild(tr);
			  $G.parse();
			  return tr;
		  }
		  
		  //新增预警条件
		  function addWarning(){
			  form.validate();
			  if (!form.isValid()){
				mini.alert("请完善表单信息。");
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
					+ "<input name='question' class='mini-combobox' required='true' emptyText='类别...' url='<%=root%>/param/getDict?key=cpc_warning_cls' valueField='val' textField='remark' />&nbsp;&nbsp;"
					+ "<input name='operator_warning' class='mini-combobox' required='true' emptyText='关系' width='70px' url='<%=root%>/param/getDict?key=compare_operator' valueField='val' textField='remark'/> &nbsp;&nbsp;"
					+ "<input name='num_warning' class='mini-spinner'  width='50px'/> &nbsp;&nbsp;" ;
					
			  td2.innerHTML=" <button onclick='delRow()' class='mini-button' name='delbtn'>删除</button><input class='mini-hidden' name='flag' value='w'/>";
			  tr.appendChild(td);
			  tr.appendChild(td1);
			  tr.appendChild(td2);
			  tab.appendChild(tr);
			  $G.parse();
			  return tr;
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
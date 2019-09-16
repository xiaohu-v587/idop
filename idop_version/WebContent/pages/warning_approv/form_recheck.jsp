<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
		.labelname{
			width:100px;
		} 
	</style>
	</head>
	<body>
				<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;"  onactivechanged="onActivechanged()"
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
	                 <div id="tab1" name="check" title="预警复查" >
		       				<input name="pageType" class="nui-hidden"/>      
							<form id="form1" method="post">
					        	<input name="flownum" class="nui-hidden"/>
					        	<input name="last_check_stat" class="nui-hidden"/>
					           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
					               	<tr>
					                   	<td align="right" class="labelname">预警时间：</td>
					                   	<td align="left">    
					                       	<input name="create_time" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td>
					                   	<td align="right" class="labelname">业务时间：</td>
										<td align="left">    
										       	<input name="data_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td> 
					               	</tr>
									<tr>
										<td align="right" class="labelname">预警编号：</td>
					                    <td align="left">    
					                        <input name="flownum" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                    </td>
					                   	<td align="right" class="labelname">预警名称：</td>
					                   	<td align="left">    
											<input name="warning_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
										</td>
									</tr>
									<tr>
										 <td align="right" class="labelname">预警类型：</td>
										<td align="left">    
					                       	<input name="warning_type_code" class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
					                       	 dataField="data" valuefield="id" textfield="remark" parentfield="upid" enabled="false"
					                       	 valueFromSelect="false" multiSelect="false"  expandOnLoad="0"  emptyText="请选择..."
														allowInput="false" showClose="true" oncloseclick="onCloseClick" 
														showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
														popupHeight="470" popupMaxHeight="600" style="width:90%;"/>
					                   	</td> 
					                    <td align="right" class="labelname">业务模块：</td>
										<td align="left">    
					                       	<input name="busi_module" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype" enabled="false"
											                 onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val" emptyText="请选择..." style="width:90%;"/>
					                   	</td> 
									</tr>
									<tr>
										<td align="right" class="labelname">预警明细：</td>
										<td align="left" colspan="3">
											<div class ="nui-fit" style="position: relative;width:820px; height:240px;" >
											<div id="datagrid3" dataField="data" class="mini-datagrid" style="width:96%;height:240px;" sortMode="client" allowUnselect="false"
											oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root %>/warning/getDetailData"
											autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
											pageSize="5">
												<div id="detailColumns" property="columns"> 
												</div>
										   	</div> 
								 			<a class="nui-button" iconCls="" onclick="download()" style="width:90px;position: absolute; left: 300px; bottom: 6px;">导出预警明细</a> 
								 			</div>
										</td>
									</tr>
									<tr>
										<td align="right" class="labelname">核查说明：</td>
										<td align="left" colspan="3">
											<input name="checker_remark" class="nui-textarea" style="width:96%;height:80px;" readonly="readonly"></input>
										</td>
									</tr>
									<tr> 
										<td align="right" class="labelname">复查说明：</td>
										<td align="left" colspan="3">
											<input name="approver_remark" class="nui-textarea" style="width:96%;height:80px;" required="true"></input>
										</td>
									</tr>
									<tr>
										<td align="right" valign="top" >附件：</td>
										<td colspan="2">
											<a class="mini-button" onclick="openW" id="filebtn">点击打开上传页面</a>
										</td>
									</tr>
									<tr>
									<td align="right" valign="top" >已上传附件：</td>
										<td colspan="3"> 
											<span id="files"  style="color:#F00"></span>
											<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:96%;height:220px;" sortMode="client" allowUnselect="false"
														     oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root%>/common/queryFiles"
														    autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" 
														    ondrawcell="ondrawcell" pageSize="5">
										        <div property="columns">                    
													<div field="id" visible="false" ></div>
													<div field="pid" visible="false" >任务父id</div>
													<div field="filename" headerAlign="center" align="center" >附件名称</div>
										            <div field="createdatef" headerAlign="center" align="center" >上传日期</div>                
										            <div field="proc" headerAlign="center" align="center" >操作</div>
										        </div>
					   						</div> 
										</td>
									</tr>
									<tr id="check">
										<td align="right" valign="top" >是否问题：</td>
										<td align="left">
											<input name="is_question" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_is_question" 
											        textfield="remark" valuefield="val" emptyText="请选择..." style="width:90%;"/>
										</td>
									</tr>
									<tr id="approval">
										<td align="right" valign="top" >复查：</td>
										<td align="left">
											<input name="approval" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_approv_stat" 
												   textfield="remark" valuefield="val" emptyText="请选择..." style="width:90%;"  required="true"/>
										</td>
									</tr>
								</table>
							</form>
							<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
								<a  class="nui-button" iconCls="" onclick="save()">确定</a> 
								<a  class="nui-button" iconCls="" onclick="onCancel">取消</a> 
								<!-- <a id="recheck_yes" class="nui-button" iconCls="" onclick="save(3)">审批</a> 
								<a id="recheck_no" class="nui-button" iconCls="" onclick="save(4)">审批无效</a> -->        
							</div>
					</div>
					<div id="tab2" name="check_process"  title="预警流转" >
						<div class="nui-fit">
					    	<div id="datagrid2" class="nui-datagrid" url="<%=root%>/warning_approv/getCheckList" style="width: 100%;height: 100%;" 
					    		allowRowSelect="false" allowHeaderWrap="true" allowCellWrap="true">
						        <div property="columns">            
						            <div field="check_time" width="15%" dateFormat="yyyy-MM-dd HH:mm:ss" headerAlign="center"  align="center">日期</div>                
						            <div field="checker_name" width="10%" headerAlign="center" align="center">操作人</div>
						            <div field="action" width="10%" headerAlign="center"  align="center" renderer="onActionRender">动作</div>
						            <div field="remark" width="65%" headerAlign="center"  align="left" >说明</div>
						        </div>
							</div>
						</div>
					</div>
					<div id="tab3" name="check_remark" title="预警说明" > <!-- visible="false" -->
				<table id="remarks"  width="98%">
		               	<tr>
		                   	<td align="right" class="labelname">预警说明：</td>
		                   	<td align="left">    
		                       	<input name="remark" class="nui-textarea" enabled="false" style="width:90%;height:200px"/>
		                   	</td>
		               	</tr>
		         </table>
			<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
				<a class="nui-button" onclick="onCancel">返回</a>       
			</div>        
		</div>
				</div>
	</body>
</html>

<script type="text/javascript">
	$G.parse();
	var tabs = $G.get("tabs1");
	var form1 = $G.getForm("form1");
	//var form2 = $G.getForm("form2");
	var grid = $G.get("datagrid1");
	var grid2 = $G.get("datagrid2");
	var grid3 = $G.get("datagrid3");

	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
			var o = $G.getbyName("approval");
			$G.getbyName("approval").setData([o.data[0],o.data[1]]);//下拉列表取自字典表将待核查去掉
		if(pageType=="warningReCheck"){
			$("#check").hide();
		}
		if(pageType=="warningCheck"){
			$("#approval").hide();
		}
		$G.getbyName("pageType").setValue(pageType);
		$G.getbyName("flownum").setValue(infos.flownum);
		var pid = mini.getbyName("flownum").getValue();
		//if(pageType=="warningCheck"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form1.setData(text.record);
				getRemark(infos.warning_code,infos.flownum,pid);
			});
			$G.postByAjax({flownum:infos.flownum},"<%=root%>/warning/getDetail",ajaxConf);
		//}
		//getDetailHeader(infos.flownum);
		
		//grid.load({pid:pid});
		
	}
	function getRemark(warning_code,flownum,pid){
		$.ajax({
			url:"<%=root%>/warning_search/getRemark",
			type:'post',
			data:{warning_code:warning_code},
			cache:false,
			success:function(text){
				var remark = mini.decode(text).remark;
				$G.getbyName("remark").setValue(remark);
				getDetailHeader(flownum,pid);
			}
		});
	}
	function onActivechanged(e){
		var tab = tabs.getActiveTab();
    	if(tab.name == "check_process")grid2.load(form1.getData());
	}
	//核查、审批
	function save(){
		var url = "<%=root%>/warning_approv/save_sp";
		var ajaxConf = new GcdsAjaxConf();
		var istrue = form1.validate();
		if(istrue==false){
			$G.alert("请完整填写表格");
		}
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setSuccessFunc(function(text){
			if(text.result=='fail'){
	    		$G.alert("操作失败！");
	    	}else{
	    		$G.alert("操作成功！","提示",function(action){
	    			$G.closemodaldialog("ok");	
	    		});
	    	}
		});
		$G.submitForm("form1",url,ajaxConf);
	}
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	//获取明细header
	function getDetailHeader(flownum,pid){
		var url = "<%=root%>/warning/getDetailHeaders";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
		ajaxConf.setSuccessFunc(function(text){
			grid3.set({columns:text.headers});
			grid3.load({flownum:flownum});
			grid.load({pid:pid});
		});
		ajaxConf.postByAjax({flownum:flownum}, url);
	}
	function openW() {
		 var pid = mini.getbyName("flownum").getValue();
	        $G.open({
	            title: "上传面板",
	            url: "<%=root%>/common/goFileUpload?pid="+pid,
	            width: 600,
	            height: 350,
	            allowResize: false,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = {};  //模拟传递上传参数
	                //iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	            	grid.reload({pid:pid});
	                if (action == "ok") {
	                    var iframe = this.getIFrameEl();
	                    var data = iframe.contentWindow.GetData();
	                    //data = mini.clone(data);
	                    //var json = mini.encode(data);
	                    //alert("已完成上传数据：\n" + json);
	                }
	            }
	        });
	}
	function ondrawcell(e){
		if(e.field == "proc") { // 操作
		   	e.cellStyle="text-align:center";
			e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\")'>删除</a>";
		 }
	}
	function onActionRender(e){
		var textVal = mini.getDictText("dop_check_act",e.value);
		return textVal;
	}
	 function profile(flag,id,createdate) {
		 top.okBtnPosition="left";
			mini.confirm("是否确认删除?","删除确认",function(action){
		 		if(action == "ok"){
	           	 $.ajax({
	                   url: "<%=root%>/common/delFile",	//将请求提交至UserCtl的save方法
	    		        type: 'post',
	                    data:  {id:id,createdate:createdate},
	                    cache: false,
	                    success: function (text) {
	                    	var flag = mini.decode(text).flag;
	    		             if(flag == "1"){	
	    		            	 mini.alert("删除成功！");
	    		             	 grid.reload();
	    		             }else {	
	    		            	mini.alert("删除失败！");
	    		             }
	                    },
	                    error: function (jqXHR, textStatus, errorThrown) {
	                        alert(jqXHR.responseText);
	                        CloseWindow();
	                    }
	                });
		 		}});
		}
	    //下载预警详情
	   function download(){
		  var flownum= $G.getbyName("flownum").getValue();
		   window.location="<%=root%>/warning/download?flownum="+flownum;
	   }	 
	 
</script>
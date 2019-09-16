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
	                 <div id="tab1" name="check" title="查复" >
		       				<input name="pageType" class="nui-hidden"/>      
							<form id="form1" method="post">
					        	<input name="flownum" class="nui-hidden"/>
					        	<input name="last_check_stat" class="nui-hidden"/>
					           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
					           		<tr>
					                    <td align="right" class="labelname">业务模块：</td>
										<td align="left">    
					                       	<input name="check_model" class="nui-textbox"  allowInput="false" style="width:90%;"/>
					                   	</td> 
					                   		<td align="right" class="labelname">预警名称：</td>
					                   	<td align="left">    
											<input name="check_warningname" class="nui-textbox" allowInput="false" style="width:90%;"/>
										</td>
									</tr>
					               	<tr>
					               	<td align="right" class="labelname">查询编号：</td>
					                    <td align="left">    
					                        <input name="check_flownum" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                    </td>
					                    
					                   	<td align="right" class="labelname">查询时间：</td>
					                   	<td align="left">    
					                       	<input name="search_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td>
					                  
					                   
					               	</tr>
					               		<tr>
										 	<td align="right" class="labelname">查询机构：</td>
										<td align="left">    
										       	<input name="search_org" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td> 
					                    	<td align="right" class="labelname">查复机构：</td>
										<td align="left">    
										       	<input name="check_org" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td> 
									</tr>
									<tr>
										 	<td align="right" class="labelname">查复时间：</td>
										<td align="left">    
										       	<input name="check_date" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td> 
					                   
									</tr>
								
									<tr>
										<td align="right" class="labelname">交易信息：</td>
										<td align="left" colspan="3">
											<div id="datagrid3" dataField="data" class="mini-datagrid" style="width:96%;height:240px;" sortMode="client" allowUnselect="false"
											oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root %>/warning/getDetailDatas"
											autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
											pageSize="5">
												<div id="detailColumns" property="columns"> 
												</div>
										   	</div> 
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
										<tr>
										<td align="right" class="labelname">查询事项：</td>
										<td align="left" colspan="3">
											<input name="search_matter" class="nui-textarea" style="width:96%;height:80px;" allowInput="false" ></input>
										</td>
									</tr>
									<tr>
										<td align="right" class="labelname">查复说明：</td>
										<td align="left" colspan="3">
											<input name="check_remark" class="nui-textarea" style="width:96%;height:80px;"  required="true" emptyText="最多输入500汉字" onvaluechanged="datachanege" ></input>
										</td>
									</tr>
	                                   <tr>
										<td align="right" class="labelname">备注：</td>
										<td align="left" colspan="3">
											<input name="remarks" class="nui-textarea" style="width:96%;height:80px;" allowInput="false" ></input>
										</td>
									</tr>
								</table>
							</form>
							<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
								<a  class="nui-button" iconCls="" onclick="save()">确定</a> 
								<a  class="nui-button" iconCls="" onclick="onCancel">取消</a>     
							</div>
					</div>
					<div id="tab2" name="check_process"  title="查复流转" >
						<div class="nui-fit">
					    	<div id="datagrid2" class="nui-datagrid" url="<%=root%>/searchCheck/getCheckList" style="width: 100%;height: 90%;" 
					    		allowRowSelect="false" allowHeaderWrap="true" allowCellWrap="true">
						        <div property="columns">            
						            <div field="search_date" width="15%" dateFormat="yyyy-MM-dd" headerAlign="center"  align="center">查询日期</div> 
						            <div field="search_name" width="10%" headerAlign="center" align="center">查询人</div>   
						            <div field="check_date" width="15%" dateFormat="yyyy-MM-dd" headerAlign="center"  align="center">查复日期</div>                
						            <div field="check_name" width="10%" headerAlign="center" align="center">查复人</div>
						            <div field="action" width="10%" headerAlign="center"  align="center" renderer="">状态</div>
						            <div id="remarks1" field="remark" width="65%" headerAlign="center"  align="center" >说明</div>
						        </div>
							</div>
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
	var cxcfStatus="";//查询查复状态
	function datachanege(){
		var data =$("#check_remark").val();
		if(data.length>499){
			alert("最多输入500个字");
		}
		
	}
	function setData(data){
		var infos = $G.clone(data);
		$G.getbyName("check_flownum").setValue(infos.check_flownum);
		//if(pageType=="warningCheck"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form1.setData(text.record);
			});
			$G.postByAjax({check_flownum:infos.check_flownum},"<%=root%>/searchCheckRecall/getDetail",ajaxConf);
		//}
		getDetailHeader(infos.check_flownum);
		var pid = mini.getbyName("check_flownum").getValue();
		grid.load({pid:pid});
		//getRemark(infos.warning_code);
	}
	
	function getRemark(warning_code){
		$.ajax({
			url:"<%=root%>/warning_search/getRemark",
			type:'post',
			data:{warning_code:warning_code},
			cache:false,
			success:function(text){
				var remark = mini.decode(text).remark;
				$G.getbyName("remark").setValue(remark);
			}
		});
	}
	function onActivechanged(e){
		var tab = tabs.getActiveTab();
    	if(tab.name == "check_process")grid2.load(form1.getData());
    
	}

	//核查、审批
	function save(){
		var url = "<%=root%>/searchCheckRecall/save";
		var ajaxConf = new GcdsAjaxConf();
		var istrue = form1.validate();
		if(istrue==false){
			$G.alert("请完整填写表格");
		}else{
			$G.GcdsConfirm("确定回复查询？", "回复查询提示", function(action) {
				if (action == 'ok') {
					ajaxConf.setIsShowSuccMsg(false);
					ajaxConf.setSuccessFunc(function(text){
						if(text.result=='fail'){
				    		$G.alert("查复失败！");
				    	}else{
				    		$G.alert("操作成功！","提示",function(action){
				    			$G.closemodaldialog("ok");	
				    		});
				    		//$G.closemodaldialog("ok");
				    	}
					});
					$G.submitForm("form1",url,ajaxConf);
				}
			})	
		}
		
		
	}
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	//获取明细header
	function getDetailHeader(check_flownum){
		var url = "<%=root%>/warning/getDetailHeader";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
		ajaxConf.setSuccessFunc(function(text){
			grid3.set({columns:text.headers});
			grid3.load({check_flownum:check_flownum});
		});
		ajaxConf.postByAjax({check_flownum:check_flownum}, url);
	}
	function openW() {
		 var pid = mini.getbyName("check_flownum").getValue();
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
			e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURIComponent(e.record.filename,'utf-8')+"&filePath="+encodeURIComponent(e.record.filepath,'utf-8')+"' target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\")'>删除</a>";
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
	 
		function onActivechanged(e){
			var tab = tabs.getActiveTab();
	    	if(tab.name == "check_process")grid2.load(form1.getData());
	    
		}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<style type="text/css">
		.labelname{
			width:100px;
		}
		.mini-grid-cell-inner.mini-grid-cell-nowrap{
            white-space: normal;
            word-break: break-all;
		}
	</style>
<body>    
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;"  onactivechanged="onActivechanged()"
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
		<div id="tab1" name="detail" title="预警详情" >
				<input name="pageType" class="nui-hidden"/>      
				<form id="form1" method="post">
		        	<input name="flownum" class="nui-hidden"/>
		           	<table id="detailTable"  width="98%">
		               	<tr>
		                   	<td align="right" class="labelname">预警时间：</td>
		                   	<td align="left">    
		                       	<input name="create_time" class="nui-textbox" dateFormat="yyyy-MM-dd" allowInput="false" style="width:90%;"/>
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
							<td align="right" class="labelname">机构名称：</td>
		                   	<td align="left">    
								<input id="orgSelect" class="nui-textbox"  name="orgid" allowInput="false" style="width:90%;"/>
							</td>
		                    <td align="right" class="labelname">业务模块：</td>
							<td align="left">    
		                       	<input name="busi_module" class="nui-textbox" allowInput="false" style="width:90%;"/>
		                   	</td> 
		                   <%-- 	<td align="right" class="labelname">业务类型：</td>
							<td align="left">    
		                       	<input id="ywlx" name="ywlx" class="mini-treeselect" url="<%=root%>/warning/getywTypeList" dataField="datas" 
											textfield="remark" valuefield="id" parentfield="upid"   emptyText="请选择..."
											valueFromSelect="false" multiSelect="false"  expandOnLoad="0"
											allowInput="false" showClose="true" oncloseclick="onCloseClick" 
											showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
											popupHeight="470" popupMaxHeight="600" style="width:90%;"/>
		                   	</td>  --%>
						</tr>
						<tr>
		                    <td align="right" class="labelname">预警类型：</td>
							<td align="left">    
		                       	<input name="warning_type_code" class="nui-textbox" allowInput="false" style="width:90%;"/>
		                   	</td> 
		                   	<td align="right" class="labelname">预警状态：</td>
					        <td align="left">								
					        	<input name="warning_status" class="nui-textbox" allowInput="false" style="width:90%;"/>
					        </td>
						</tr>
						<tr>
							<td align="right" class="labelname">预警等级：</td>
					        <td align="left">								
					          	<input name="warning_level" class="nui-textbox" allowInput="false" style="width:90%;"/>
					        </td>
					         <td align="right">核查结果：</td>
							<td align="left">
								<input name="last_check_stat" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
						</tr>
						<tr>
							<td align="right" class="labelname">复查结果：</td>
					        <td align="left">								
					          	<input name="last_approval_stat" class="nui-textbox" allowInput="false" style="width:90%;"/>
					        </td>
					         <td align="right">是否问题：</td>
							<td align="left">
								<input name="is_question" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
						</tr>
						
						<tr>
							<td align="right" class="labelname">详情描述：</td>
							<td align="left" colspan="3">
								<div class ="nui-fit" style="position: relative;width:820px; height:240px;" >
								<div id="datagrid3" dataField="data" class="mini-datagrid" style="height:240px;" sortMode="client" allowUnselect="false"
								oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root %>/warning/getDetailData"
								autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
								pageSize="5">
									<div id="detailColumns" property="columns"> 
									 
									</div>
							   	</div> 
							   	 <a class="nui-button" iconCls="" onclick="download()" style="width:90px;position: absolute; left: 300px; bottom: 6px;">导出详情描述</a> 
							   	</div>
							</td>
						</tr>
						<!-- <tr>
							<td align="right" valign="top" >附件：</td>
							<td colspan="3">
								<a class="mini-button" onclick="openW" id="filebtn">点击打开上传页面</a>
							</td>
						</tr>  -->
						<tr>
							<td valign="top" align="right" >附件：</td>
							<td colspan="3"> 
								<span id="files"  style="color:#F00"></span>
								<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:96%;height:150px;" sortMode="client" allowUnselect="false"
											     oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root%>/common/queryFiles?pid=<%=request.getParameter("id") %>"
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
					</table>
				</form>
			<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
				<a class="nui-button" onclick="onCancel">返回</a>  
				      
			</div>        
		</div>
		<div id="tab2" name="check_process" title="预警流转" >
				<div id="datagrid2" class="nui-datagrid" url="<%=root%>/warning_manage/getCheckList" style="width: 100%;height: 90%;" 
					    		allowRowSelect="false" allowHeaderWrap="true" >
					<div property="columns">            
						<div field="check_time" width="15%" dateFormat="yyyy-MM-dd HH:mm:ss" headerAlign="center"  align="center">日期</div>                
						<div field="checker_name" width="10%" headerAlign="center" align="center">操作人</div>
						<div field="action" width="10%" headerAlign="center"  align="center" renderer="onActionRender">动作</div>
						<div field="remark" width="65%" headerAlign="center"  align="left" >说明</div>
					</div>
				</div>      
		</div>
		<div id="tab3" name="check_remark" title="预警说明" > <!-- visible="false" -->
				<table id="remarks"  width="98%">
		               	<tr>
		                   	<td align="right" class="labelname">预警说明：</td>
		                   	<td align="left">    
		                       	<input name="remark" class="nui-textarea" allowInput="false" style="width:90%; height:200px"/>
		                   	</td>
		               	</tr>
		         </table>      
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var grid = mini.get("datagrid1");
	var grid2 = mini.get("datagrid2");
	var grid3 = $G.get("datagrid3");
	var tabs = $G.get("tabs1");
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		$G.getbyName("flownum").setValue(infos.flownum);
		if(pageType=="warningDetail"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
				getRemark(infos.warning_code,infos.flownum);
				
			});
			$G.postByAjax({flownum:infos.flownum},"<%=root%>/warning/getDetail2",ajaxConf);
			grid.load();
		}
		//getDetailHeader(infos.flownum);
	}
	
	function onActivechanged(){
		var tab = tabs.getActiveTab();
    	//if(tab.name == "check")grid.load({ pid: node.orgnum}); 
    	if(tab.name == "check_process")grid2.load(form.getData());
	}
	
	//获取明细header
	function getDetailHeader(flownum){
		var url = "<%=root%>/warning/getDetailHeaders";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
		ajaxConf.setSuccessFunc(function(text){
			grid3.set({columns:text.headers});
			grid3.load({flownum:flownum});
		});
		ajaxConf.postByAjax({flownum:flownum}, url);
	}
	function upload(){
		$("#upload").click();
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function openW() {
		 var pid = mini.getbyName("flownum").getValue();
	        mini.open({
	            title: "上传面板",
	            url: "<%=root%>/common/goFileUpload?pid="+pid,
	            width: 600,
	            height: 350,
	            allowResize: false,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = {};  //模拟传递上传参数
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                if (action == "ok") {
	                    var iframe = this.getIFrameEl();
	                    var data = iframe.contentWindow.GetData();
	                    //data = mini.clone(data);
	                    //var json = mini.encode(data);
	                    //alert("已完成上传数据：\n" + json);
	                    grid.reload();
	                }
	            }
	        })
	    }
 //翻译字段
   function ondrawcell(e){
   	if(e.field == "proc") { // 操作
   		e.cellStyle="text-align:center";
			e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>&nbsp;";
   	}
   }
   function profile(flag,id,createdate) {
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
	function getRemark(warning_code,flownum){
		$.ajax({
			url:"<%=root%>/warning_search/getRemark",
			type:'post',
			data:{warning_code:warning_code},
			cache:false,
			success:function(text){
				var remark = mini.decode(text).remark;
				$G.getbyName("remark").setValue(remark);
				getDetailHeader(flownum);
			}
		});
	}
   
   
   function onActionRender(e){
		var textVal = mini.getDictText("dop_check_act",e.value);
		return textVal;
	}
   
   //下载预警详情
   function download(){
	  var flownum= $G.getbyName("flownum").getValue();
	   window.location="<%=root%>/warning/download?flownum="+flownum;
   }
</script>
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
			width:120px;
		}
		.mini-grid-cell-inner.mini-grid-cell-nowrap{
            white-space: normal;
            word-break: break-all;
		}
/* 		.mini-textbox{ */
/* 			width:219%; */
/* 		} */
		#jsf{
		 overflow:hiiden;
		 white-space:nowrap;
		 text-overflow:ellipsis;
		 
		}
	</style>
<body>    
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="width: 100%; height: 100%;" 
					dataField="tabs" bodyStyle="border-left:0;border-right:0px;border-bottom:0px;">
		<div id="tab1" name="detail" title="提示详情" >
				<input name="pageType" class="nui-hidden"/>      
				<input name="id" class="nui-hidden"/>
				
				<form id="form1" method="post">
		           	<table id="detailTable"  width="98%">
		               	<tr>
		                   	<td align="right" class="labelname">提示时间：</td>
		                   	<td align="left">    
		                       	<input name="prompt_time" class="nui-textbox" dateFormat="yyyy-MM-dd" allowInput="false" style="width:90%;"/>
		                   	</td>
		                   	<td align="right" class="labelname">业务时间：</td>
		                    <td align="left">    
		                        <input name="data_date" class="nui-textbox" dateFormat="yyyy-MM-dd" allowInput="false" style="width:90%;"/>
		                    </td>
		               	</tr>
						<tr>
							<td align="right" class="labelname">提示编号：</td>
		                    <td align="left">    
		                        <input name="prompt_no" class="nui-textbox" allowInput="false" style="width:90%;"/>
		                    </td>
		                   	<td align="right" class="labelname">提示名称：</td>
		                   	<td align="left">    
								<input name="prompt_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
						</tr>
						<tr>
							<td align="right" class="labelname">机构名称：</td>
		                   	<td align="left">    
								<input id="orgSelect" name="orgid" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
							<td align="right" class="labelname">柜员名称：</td>
		                   	<td align="left">    
								<input name="teller_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
		                   
						</tr>
						
						<tr>
							 <td align="right" class="labelname">业务模块：</td>
							<td align="left">    
		                       	<input name="busi_module" class="nui-textbox" allowInput="false"  style="width:90%;"/>
		                   	</td> 
					         <td align="right">是否发送短信：</td>
							<td align="left" >
								<input name="is_key_dxtz" class="nui-textbox" allowInput="false" style="width:90%;"/>
							</td>
						</tr>
						<tr>
							 <td align="right"  class="labelname">短信接收人：</td>
							<td align="left" id="jsf" onmouseover="over()">    
		                       	<input name="is_key_jsf" class="nui-textbox" url="<%=root%>/param/getDict?key=jsf" allowInput="false"
								   onvaluechanged="onywTypeChanged" textfield="remark" valuefield="val"  style="width:90%;"/>
		                   	</td> 
					         <td align="right">提示状态：</td>
							<td align="left">
								<input id="prompt_status" name="prompt_status" class="nui-textbox" allowInput="false"  style="width:90%;"/>
							</td>
						</tr>
						
						<tr>
							<td align="right" class="labelname">详情描述：</td>
							<td align="left" colspan="3">
								<div class ="nui-fit"  style="position: relative;width:800px; height:240px;">
								<div id="datagrid3" dataField="data" class="mini-datagrid" style="height:240px;" sortMode="client" allowUnselect="false"
								oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root%>/prompting_search/getDetailData"
								autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="暂无明细" 
								pageSize="5">
									<div id="detailColumns" property="columns"> 
									
									</div>
							   	</div> 
							   	<a class="nui-button" iconCls="" onclick="download()" style="width:90px;position: absolute; left: 300px; bottom: 6px;">导出详情描述</a>   
							   	</div>
							</td>
						</tr>
						
						<tr>
		                   	<td align="right" class="labelname">短信提示内容(机构)：</td>
		                   	<td align="left">    
		                       	<input name="message_org" class="nui-textarea" allowInput="false" style=" width:230% ;height:100px"/>
		                   	</td>
		                </tr>
						<tr>
		                   	<td align="right" class="labelname">短信提示内容(人员)：</td>
		                   	<td align="left">    
		                       	<input name="message_person" class="nui-textarea" allowInput="false" style="width:230% ;height:100px"/>
		                   	</td>
		               	</tr>
						
					</table>
				</form>
			<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
				<a class="nui-button" onclick="onCancel">返回</a>  
				      
			</div>        
		</div>
		
		<div id="tab2" name="check_remark" title="提示说明" > <!-- visible="false" -->
				<table id="remarks"  width="98%">
		               	<tr>
		                   	<td align="right" class="labelname">模型详细说明：</td>
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
	var grid3 = $G.get("datagrid3");
	var tabs = $G.get("tabs1");
	
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		$G.getbyName("id").setValue(infos.id);
		if(pageType=="Detail"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
				getRemark(infos.prompt_no);
				
			});
			$G.postByAjax({id:infos.id},"<%=root%>/prompting_search/getDetail",ajaxConf);
// 			grid.load();
		}
	//	getDetailHeader(infos.id);
	}
	

	
	//获取明细header
	function getDetailHeader(id){
		var url = "<%=root%>/prompting_search/getDetailHeaders";
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setIsAsync(false);
		ajaxConf.setSuccessFunc(function(text){
			grid3.set({columns:text.headers});
			grid3.load({id:id});
		});
		ajaxConf.postByAjax({id:id}, url);
	}

	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
 
	function getRemark(prompt_no){
		$.ajax({
			url:"<%=root%>/prompting_search/getRemark",
			type:'post',
			data:{prompt_no:prompt_no},
			cache:false,
			success:function(text){
				var remark = mini.decode(text).remark;
				$G.getbyName("remark").setValue(remark);
// 				getDetailHeader(id);
			}
		});
	}
   
   
   function onActionRender(e){
		var textVal = mini.getDictText("dop_check_act",e.value);
		return textVal;
	}
   
   //下载预警详情
   function download(){
   	
	 var id=$G.getbyName("id").getValue();

	 
	 window.location="<%=root%>/prompting_search/export?id="+id;
   }
   
   function over(){
	   var td=document.getElementById("jsf");
	   var input=document.getElementsByName("is_key_jsf");
	   var value=input["0"].value;
	   td.title= value;
   }
   
</script>
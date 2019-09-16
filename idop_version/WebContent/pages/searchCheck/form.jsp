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
	                 <div id="tab1" name="check" title="查询查复" >
		       				<input name="pageType" class="nui-hidden"/>      
							<form id="form1" method="post">
					           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
									<tr>
										<td align="right" class="labelname">查询编号：</td>
					                    <td align="left">    
					                        <input name="check_flownum" class="nui-textbox" allowInput="false" style="width:100%;"/>
					                    </td>
					                    
					                    <td align="right" class="labelname">查复机构：</td>
					                   	<td align="left">    
									    <input id="check_org" class="nui-textbox" name='check_org' style="width:90%;" allowInput="false"/>
								      </td>
									</tr>
									<tr>
										<td align="right" class="labelname">查复期限：</td>
										<td align="left">    
					                        <input name="check_deadline" class="nui-textbox" allowInput="false" style="width:100%;" onvaluechanged="onDeadlineChange"/>
					                   	</td> 
					                    <td align="right" class="labelname">查复截止日期：</td>
										<td align="left">    
					                      <input name="check_enddate" id="check_enddate" class="nui-textbox" allowInput="false" style="width:90%;"/>
					                   	</td> 
									</tr>
									<tr>
										<td align="right" class="labelname">查询事项：</td>
										<td align="left" colspan="3">
								        <input name="search_matter" class="nui-textarea" style="width:96%;height:80px;" required="true" allowInput="false"></input>
										</td>
									</tr>
							
									<tr>
										<td align="right" valign="top" >附件：</td>
										<td colspan="2">
											<a class="mini-button" onclick="openW" id="filebtn" name="filebtn">点击打开上传页面</a>
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
									<tr >
										<td align="right" valign="top" >查复日期：</td>
										<td align="left">
											<input name="check_date" class="nui-textbox" allowInput="false" style="width:100%;"/>
										</td>
										<td align="right" valign="top" >复查人：</td>
										<td align="left">
										<input name="check_name" class="nui-textbox" allowInput="false" style="width:90%;"/>
										</td>
									</tr>

										<tr >
										<td align="right" valign="top" >复查说明：</td>
										<td align="left">
											<input name="check_remark" id="check_remark" class="nui-textarea" style="width:220%;height:80px;" allowInput="false"/>
										</td>
									   </tr>
										<tr >
										<td align="right" valign="top" >备注：</td>
										<td align="left">
											<input name="remarks" id="remarks" class="nui-textarea" style="width:220%;height:80px;"  maxlength="500" emptyText="最多输入500汉字" onvaluechanged="datachanege"/>
										</td>
									</tr>
								</table>
							</form>
							<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
								<a name="finsh"     id="finsh" class="nui-button" iconCls="" onclick="finsh()">完成查询</a> 
								<a name="takeBack" id="takeBack" class="nui-button" iconCls="" onclick="takeBack()">查询收回</a>
								<a name="back"  id="back" class="nui-button" iconCls="" onclick="back()">退回</a>
								<a name="onCancel" class="nui-button" iconCls="" onclick="onCancel">取消</a>      
							</div>
					</div>
					<div id="tab2" name="check_process"  title="查复流转" >
						<div class="nui-fit">
					    	<div id="datagrid2" class="nui-datagrid" url="<%=root%>/searchCheck/getCheckList" style="width: 100%;height: 90%;" 
					    		allowRowSelect="false" allowHeaderWrap="true" allowCellWrap="true">
						        <div property="columns">            
						            <div field="search_date" width="15%" dateFormat="yyyy-MM-dd " headerAlign="center"  align="center">查询日期</div> 
						            <div field="search_name" width="10%" headerAlign="center" align="center">查询人</div>   
						            <div field="check_date" width="15%" dateFormat="yyyy-MM-dd " headerAlign="center"  align="center">查复日期</div>                
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
	//$G.getbyName("finsh").style.display="none";
	//$G.getbyName("back").style.display="none";
	var cxcfStatus="";//查询查复状态
	var check_name="";
	var check_flownum="";
	var CurrentNameid ='${id}';
	function datachanege(){
		var data =$("#remarks").val();
		if(data.length>499){
			alert("最多输入500个字");
		}
		
	}
	
	function setData(data){
		var infos = $G.clone(data);
		var flownum=infos.flownum;
		getCheckFlownum(flownum);
		  cxcfStatus=infos.cxcfStatus;
		  if(cxcfStatus=="1"){//当查询查复状态为：已查复状态显示
			  $("#back").show(); 
			  $("#finsh").show(); 
			  $("#takeBack").hide(); 
		  }else{
			  $("#back").hide();
			  $("#finsh").hide();
			 
		  }
		 check_nameid=infos.check_nameid;
		 if(CurrentNameid != check_nameid || cxcfStatus=="4" || cxcfStatus=="3" ){//处于查询查复完成状态或者不是当前人
			 //页面观看只给浏览不给操作
			 handlingPage();	 
		 }	
	}
	
	function handlingPage(){
		$G.getbyName("remarks").setEnabled(true);
		$G.getbyName("conservation").setEnabled(true);
		$G.getbyName("finsh").setEnabled(true);
		$G.getbyName("takeBack").setEnabled(true);
		$G.getbyName("back").setEnabled(true);
	}
	
	function getCheckFlownum(flownum){
		var url = "<%=root%>/searchCheck/getCheckFlownum";
		$.ajax({
			url:url,
			data:{flownum:flownum},
			success:function(text){
			  check_flownum=text.check_flownum;
			  $G.getbyName("check_flownum").setValue(check_flownum);

				var ajaxConf = new GcdsAjaxConf();
				ajaxConf.setIsShowProcessBar(true);
				ajaxConf.setIsShowSuccMsg(false);
			    ajaxConf.setSuccessFunc(function (text){
			    	if(text !=null && text !=""){
			    		form1.setData(text.record[0]);
			    	}
					
				});
			    
		    $G.postByAjax({check_flownum:check_flownum},"<%=root%>/searchCheck/getDetail",ajaxConf);
		    var pid = mini.getbyName("check_flownum").getValue();
			grid.load({pid:pid});
			}
		});
	}
	//保存
	function conservation(){
		var url = "<%=root%>/searchCheck/conservation";
		var ajaxConf = new GcdsAjaxConf();
		var istrue = form1.validate();
		if(istrue==false){
			$G.alert("请完整填写表格");
		}
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setSuccessFunc(function(text){
			if(text.result=='fail'){
	    		$G.alert("保存失败！");
	    	}else{
	    		$G.alert("操作成功！","提示",function(action){
	    			$G.closemodaldialog("ok");	
	    		});
	    		//$G.closemodaldialog("ok");
	    	}
		});
		$G.submitForm("form1",url,ajaxConf);
	}
	
	function conservation1(){
		var url = "<%=root%>/searchCheck/conservation";
		var ajaxConf = new GcdsAjaxConf();
		
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setSuccessFunc(function(text){
			if(text.result=='fail'){
	    		$G.alert("保存失败！");
	    	}else{
// 	    		$G.alert("操作成功！","提示",function(action){
// 	    			$G.closemodaldialog("ok");	
// 	    		});
// 	    		//$G.closemodaldialog("ok");
	    	}
		});
		$G.submitForm("form1",url,ajaxConf);
	}
	
	//完成查复
	function finsh(){
		//setrequired(false);
		$G.GcdsConfirm("确定完成查询？", "完成查询提示", function(action) {
			if (action == 'ok') {
					conservation1();
					var url = "<%=root%>/searchCheck/finsh";
					var check_flownum= mini.getbyName("check_flownum").getValue();
					var remarks=mini.getbyName("remarks").getValue();
					$.ajax({
						url:url,
						data:{check_flownum:check_flownum,remarks:remarks},
						success:function(text){
					
						if(text.result == "fail"){
							$G.alert("操作失败！");
						}else{
							$G.alert("操作成功！","提示",function(action){
		    				$G.closemodaldialog("ok");
							});
						}
					},
						error:function(a,b,c){
						$G.alert("操作失败！");
					}
				});
			}
		})
	}
	
	//takeBack 查询收回
	function takeBack(){
		
		var url="<%=root%>/searchCheck/takebackform";
		var check_flownum= mini.getbyName("check_flownum").getValue();
		var remarkold=mini.getbyName("remarks").getValue();
    	var bizParams = { check_flownum:check_flownum};
    	
		$G.showmodaldialog("收回", url, 600, 210, bizParams, function(data){
			mini.getbyName("remarks").setValue(data.remarks);
			
			$G.GcdsConfirm("确定收回查询查复？", "查询查复收回提示", function(action) {
			var remarks=mini.getbyName("remarks").getValue();
			var url = "<%=root%>/searchCheck/takeBack";
			
				if (action == 'ok') {
					$.ajax({
						url:url,
						data:{check_flownum:check_flownum,remarks:remarks},
						success:function(text){
							var result=text.result;
							if(result == "fail"){
								$G.alert("收回失败！");
							}else{
								
								$G.alert("操作成功！","提示",function(action){
					    			$G.closemodaldialog("ok");
								});
							}
						},
						error:function(a,b,c){
							$G.alert("收回失败！");
						}
					});
		         }else{
					mini.getbyName("remarks").setValue(remarkold);
				}
        	});
		});
		
	}
	

	//退回
	function back(){
			var url="<%=root%>/searchCheck/backform";
			var check_flownum= mini.getbyName("check_flownum").getValue();
			var check_deadlineold=mini.getbyName("check_deadline").getValue();
			var check_enddateold=mini.getbyName("check_enddate").getValue();
			var remarkold=mini.getbyName("remarks").getValue();
    		var bizParams = { check_flownum:check_flownum};
    		
    		$G.showmodaldialog("退回", url, 600, 230, bizParams, function(data){
    		
			mini.getbyName("check_deadline").setValue(data.check_deadline);
			mini.getbyName("check_enddate").setValue(data.check_enddate);
			mini.getbyName("remarks").setValue(data.remarks);
				
				
				
				$G.GcdsConfirm("确定退回查询？", "退回查询提示", function(action) {
				if (action == 'ok') {					
			
				var url = "<%=root%>/searchCheck/back";
				
				var check_deadline=mini.getbyName("check_deadline").getValue();
				var check_enddate=mini.getbyName("check_enddate").getValue();
				var remarks=mini.getbyName("remarks").getValue();
				$.ajax({
					url:url,
					data:{check_flownum:check_flownum,check_enddate:check_enddate,check_deadline:check_deadline,remarks:remarks},
					success:function(text){
						var result=text.result;
						if(result == "fail"){
							$G.alert("退回失败！");
						}else{
							$G.alert("操作成功！","提示",function(action){
		    				$G.closemodaldialog("ok");
							});
						}
					},
					error:function(a,b,c){
					$G.alert("退回失败！");
					}
				});
				}else{
				
				mini.getbyName("check_deadline").setValue(check_deadlineold);
				mini.getbyName("check_enddate").setValue(check_enddateold);
				mini.getbyName("remarks").setValue(remarkold);
				}
				
				})


    		});
    		
			
		
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
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
			if(tab.name == "check_process")grid2.load({check_flownum: $G.getbyName("check_flownum").getValue()});
		}
	
</script>
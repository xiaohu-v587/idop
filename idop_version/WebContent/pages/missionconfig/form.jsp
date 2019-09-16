<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden" />
	        	<input name="model_cfg_id" class="nui-hidden" />
	        	<input name="copyflag" class="nui-hidden" />
	       		<div style="padding-left:1px;padding-bottom:5px;">
					<table style="table-layout:fixed;width:100%;" align="center">
						<colgroup>
					       	<col width="10%"/>
					       	<col width="32%"/>
					       	<col width="15%"/>
					       	<col width="35%"/>
						</colgroup>
						<tr>
							<td align="right">任务名称：</td>
							<td align="left">
		                   		<input name="mission_name" class="nui-textbox" required="true" emptyText="请输入任务名称" style="width:100%"/>                   
		                  	</td>
		                  	<td align="right">任务接收群组：</td>
		                  	<td align="left" style="white-space: nowrap;">
		                  		<!-- 目前不设计多群组情况,暂时只做单群组，如有需要可按照需要进行扩展 -->
		                    	 <input name="assign_feedbank_group" multiSelect="false" style="width:73%;" class="nui-combobox"
								valueField="id" textField="group_name" emptyText="我的群组" dataField="data" allowInput="true"
								url="<%=root%>/manualReport/getGroups?pageIndex=0&pageSize=99999999&function_group=0,2" required="true"/>
								<a class="nui-button" style="float:right;margin-right:6%" onclick="select()">自定义</a>
		                  	</td>
		              	</tr>
		              	<tr>
							<td align="right">任务类型：</td>
		              		<td align="left" style="white-space: nowrap;" >
		              			<input name="mission_type" id="mission_type"  class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="yygl_mission_type" value="1" emptyText="请选择任务分类" style="width:100%" onvaluechanged="missiontypeChaged"/>
		              		</td>
		                  	<td align="right">完成天数（工作日）：</td>
		                  	<td align="left" style="white-space: nowrap;" >
		                   		<input name="mission_deadline" class="nui-textbox" required="true" maxlength="10" vtype="range:0,365;int" style="width:130px" emptyText="请输入天数" />
		                   		&nbsp;
		                   		<input name="mission_deadline_hh" class="nui-textbox"  maxlength="10" vtype="range:0,24;int" style="width:60px" emptyText="小时"/>
		                   		&nbsp;:&nbsp;	
		                   		<input name="mission_deadline_mm" class="nui-textbox"  maxlength="10" vtype="range:0,60;int" style="width:60px" emptyText="分钟" />
		                  	</td>
		              	</tr>
		              	<tr>
							<td align="right">消息通知方式：</td>
		                  	<td align="left">
								<input name="message_type" id="message_type" multiSelect="true"  class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="yygl_mission_channel" value="0" emptyText="请选择消息通知方式" style="width:100%"/>
							</td>
		                  	<td align="right">消息通知模板：</td>
		                  	<td align="left">
		                  		<!-- 后面需要对接短信模块 -->
								<!-- <input name="message_id" class="nui-combobox"   emptyText="请选择通知模板" style="width:94%;"/> -->
								<input id="message_id" name="message_id"  style="width: 94%;"  class="nui-combobox" url="<%=root%>/param/getTemp" 
								valueField="id" textField="template_title"  emptyText="请选择..."  multiSelect="false" />
							</td>
						</tr>
						<tr>
							<td align="right">任务反馈：</td>
		                  	<td align="left">
		                  		<input name="mission_isfeedback" class="nui-combobox"  data="[{id:0,text:'无需反馈'},{id:1,text:'需要反馈'}]" value="1" style="width:100%"/>
							</td>
							<td align="right">任务签收：</td>
		                  	<td align="left" style="white-space: nowrap;" >
		                   		<input name="mission_sign" class="nui-combobox"  data="[{id:0,text:'无需签收'},{id:1,text:'需要签收'}]" value="0" style="width:94%"/>
		                  	</td>
						</tr>
						<tr id="hide_day_tr">
							<td align="right"><span id="mission_loop_title">任务周期：</span></td>
		                  	<td align="left">
								<input name="mission_loop_cycel" id="mission_loop_cycel"   class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="yygl_mission_loop" value="" emptyText="请选择任务周期" style="width:100%"/>
								
							</td>
							<td align="right">周期开始天：</td>
		                  	<td align="left">
		                  		<input name="mission_strat_day" id="mission_strat_day" class="nui-textbox"  maxlength="25"  style="width:94%" vtype="range:0,365;int" emptyText="请输入周期开始天，不输入时按照默认"/>
		                  	</td>
						</tr>
						<tr>
							<td align="right">配置状态：</td>
		                  	<td align="left">
		                  		<input name="mission_activiti" class="nui-combobox"  data="[{id:0,text:'启用'},{id:1,text:'停用'}]" value="0" style="width:100%"/>
							</td>
							<td align="right">任务分类：</td>
		                  	<td align="left" style="white-space: nowrap;" >
		                   		<input name="mission_flag" id="mission_flag"  class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="yygl_mission_flag" value="0" onvaluechanged="onChangeflag" emptyText="请选择任务分类" style="width:94%"/>
		                  	</td>
						</tr>
						<tr id="temple">
							<td align="right">任务模板配置：</td>
		                  	<td align="left">
		                  		<a class="nui-button" iconCls="" onclick="modelconfig">模板配置</a> 
							</td>
							<td align="right"></td>
		                  	<td align="left" style="white-space: nowrap;" >
		                  	</td>
						</tr>
						<tr>
							<td align="right">任务说明：</td>
							<td colspan="3" align="left">    
								<input class="mini-textarea" name="mission_require" id="mission_require" emptyText="请输入备注" maxlength="2000" style="width:97.5%;height:150px"/>
			              	</td>
		              	</tr>
		              	<tr>
							<td align="right">任务说明附件：</td>
							<td colspan="3" align="left">    
								<div style="width:97.5%;">
									<jsp:include page="../common/fileupload.jsp">					
										<jsp:param name="pid" value="<%=request.getAttribute("id") %>"/>
										<jsp:param name="type" value="<%=request.getAttribute("pageType") %>"/>
										<jsp:param name="pidtype" value="2"/>
									</jsp:include>
								</div>
			              	</td>
		              	</tr>
					</table>
				</div> 
			</form>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="save">确定</a> 
			<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	// 默认强制隐藏周期选择
	$("#hide_day_tr").hide();
	
	var orgData;
	
	//当选择文件类型时，隐藏任务配置一行
	function onChangeflag()
	{
		var mission_flag = $G.get("mission_flag").getValue();
		if(mission_flag == "1"){	
			$("#temple").hide();
		}else{
			$("#temple").show();
		}
	}
	
		
 	function setData(data){
 		var infos = $G.clone(data);
 		var copyflag = infos.copyflag || '0'; //复制标识，如果是复制情况 值为 1，不是设置值为 0
		var pageType=infos.pageType;
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(true);
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.data);
			});
			$G.postByAjax({id:infos.id},"<%=root%>/missionconfig/getDetail",ajaxConf);
		}else if(pageType=="add"){
			 //复制模式下，去查询需要复制的数据返显出来，然后提供一份新的ID 保存到数据库中
			if(copyflag && copyflag == 1){
				 	var ajaxConf = new GcdsAjaxConf();
				 	ajaxConf.setIsAsync(true);
				 	ajaxConf.setIsShowProcessBar(true);
					ajaxConf.setIsShowSuccMsg(false);
				    ajaxConf.setSuccessFunc(function (text){
						form.setData(text.data);
						$G.getbyName("id").setValue('<%=request.getAttribute("id") %>');
					});
					$G.postByAjax({id:infos.id},"<%=root%>/missionconfig/getDetail",ajaxConf);
			 }
			 //模板的id，父页面传递或者，后台提供
			 $G.getbyName("id").setValue('<%=request.getAttribute("id") %>');
		}
		
		//保存相关参数
		$G.getbyName("pageType").setValue(pageType);
		$G.getbyName("copyflag").setValue(copyflag);
		missiontypeChaged({value:$G.get("mission_loop_cycel").getValue()})
 	}

 	/*
	 *保存数据
	 */
	function save(){
		//mini.get("btnOk").disable();
 		var formData = form.getData();
		form.validate();
		if (form.isValid() == false) {
			mini.alert("请完善必输项。");
			//mini.get("btnOk").enable();
			return;
		}
 		
		var pageType=$G.getbyName("pageType").getValue();
    	var urlStr = "<%=root%>/missionconfig/save";
		if(pageType=="edit"){
			urlStr = "<%=root%>/missionconfig/update";
		}
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	//任务类型值发生改变时	
 	function missiontypeChaged(e){
 		if(e.value == "1"){
 			$("#hide_day_tr").hide();
 		}else{
 			$("#hide_day_tr").show();
 		}
 	}
 	
 	//新增
	function select() {
        var url = "<%=root%>/manualReport/groupform";
        var bizParams = {pageType:"add",function_group:"0"};
        $G.showmodaldialog("新增", url, 600, 400, bizParams, function(data){
	    	 //刷新
        	$G.getbyName("assign_feedbank_group").load("<%=root%>/manualReport/getGroups?pageIndex=0&pageSize=99999999&function_group=0,2");
	    	 //$G.getbyName("assign_feedbank_group").setValue(data.groupId);
	    });
	}
	
	//模板配置
	function modelconfig(){
		//根据分类扩展配置场景
		//指向不同的模板配置页面
		var id =  $G.getbyName("model_cfg_id").getValue();
		var pid =  $G.getbyName("id").getValue();
		var mission_flag = $G.getbyName("mission_flag").getValue();
		var copyflag =  $G.getbyName("copyflag").getValue();
		var pageType =  $G.getbyName("pageType").getValue();
		
		//任务分类不能为空，因为相关与任务的模式问题
		if(isNullObj(mission_flag)){
			$G.alert("请先选择任务分类!");
			return;
		}
		//如果模板id非空，当前为新增且不是复制情况下，改为编辑模式
		if(!isNullObj(id) && pageType == "add" && copyflag != '1'){
			pageType = "edit";			
		}
		
		var url = "<%=root%>/missionconfig/addModel?id="+( copyflag == '1' ? '':id)+"&mission_flag="+mission_flag+"&pid="+pid;
		var bizParams = {id:id,mission_flag:mission_flag,pageType:pageType,pid:pid,copyflag:copyflag};
		$G.showmodaldialog("模板配置", url, "100%", "100%", bizParams, function(data){
        	if(pageType == 'add'){
        		//新增时，把模板id保存到主页面，进行关联动作 
        		$G.getbyName("model_cfg_id").setValue(data.model_cfg_id);
        	}
	    });
		
	}
		
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>
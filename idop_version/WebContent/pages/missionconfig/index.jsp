<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>任务配置</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		</style>
	</head> 
	<body>
		<div id="panel1" class="nui-panel" title="任务配置" style="width:100%;" showToolbar="false" showCollapseButton="false"
    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
    		  	<div class="nui-fit" style="overflow: hidden">
			   		<form id="form1">
				   		<table style="table-layout: fixed;" class="search_table" width="100%">
							<tr>
			                   	<th align="right">任务名称：</th>
								<td align="left">
									<input  name="mission_name" class="mini-textbox" style="width:165px;"/>
								</td>
			                   	<th align="right">任务状态：</th>
								<td align="left">
									<input name="delete_flag" class="nui-combobox"  data="[{id:0,text:'已删除'},{id:1,text:'正常'}]" value="1" style="width:165px;"/>
								</td>
			                    <th align="right">任务分类：</th>
								<td align="left">
									<input name="mission_flag" id="mission_flag"  class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="yygl_mission_flag"   style="width:165px;"/>
								</td>
		               		</tr>
		               		<tr>
			                    <th align="right">任务创建时间（开始）：</th>
								<td align="left">
									<input   name="between_createTime" class="nui-datepicker" allowInput="true" emptyText="请选择..."  style="width:165px;" />
								</td>
			                   	 <th align="right">任务创建时间（结束）：</th>
			                    <td align="left">    
			                    	<input   name="end_createTime" class="nui-datepicker" allowInput="true" emptyText="请选择..."  style="width:165px;" />
			                    </td>
		               		</tr>
				   		</table>
			   		</form>
		   		</div>
		</div>
			<div class="nui-toolbar" style="border-bottom:0;padding:0px;border-top:0;">
				 <table style="width:100%;">
		            <tr>
						<td style="width:100%;">
							<a class="nui-button" iconCls="" id="addBtn" onclick="add()" >新增</a>
							<a class="nui-button" iconCls="" id="editBtn" onclick="edit()" >修改</a>
							<a class="nui-button"  iconCls="" id="delBtn" onclick="del()" >删除</a>
							<a class="nui-button" iconCls="" id="lowBtn" onclick="lowerHair()" >发布</a>
							<a class="nui-button" iconCls="" id="addBtn" onclick="download()" >下载</a>
		                </td>
		                <td style="white-space:nowrap;">
		                	<a class="nui-button" iconCls="" onclick="reset()">重置</a>  
		                    <a class="nui-button" iconCls="" onclick="search()">查询</a>
		                </td>
		            </tr>
		       	</table>   
			</div>
			<div class="nui-fit">
		    	<div id="datagrid1" dataField="data" class="nui-datagrid" style="width:100%;height:100%;" url="<%=root%>/missionconfig/getList" multiSelect="false">
			        <div property="columns">            
			            <div type="checkcolumn" name="checkCloumn"></div>
			            <div field="id" visible="false"></div>    
			            <div field="mission_name" allowSort="true" headerAlign="center" align="left">任务名称</div>
			            <div field="mission_flag" allowSort="true" headerAlign="center" align="left"  renderer="onMissionFlagRenderer">任务分类</div>
			            <div field="mission_type" allowSort="true" headerAlign="center" align="left" renderer="onMissionTypeRenderer">任务类型</div>
			            <!-- <div field="delete_flag" allowSort="true" headerAlign="center" align="left" renderer="onDeleteFlagRenderer">任务数据状态</div>  -->
			            <div field="mission_activiti" allowSort="true" headerAlign="center" align="left" renderer="onActivitiRenderer">任务配置状态</div> 
			            <div field="mission_deadline" allowSort="true" headerAlign="center" align="left">任务期限(工作日)</div>          
			            <div field="createdate" allowSort="true" headerAlign="center" align="left" dateFormat="yyyy-MM-dd HH:mm:ss">创建时间</div>
			            <div field="id"  renderer="onActionRenderer"  allowSort="true" headerAlign="center" align="center" >操作</div>
			        </div>
				</div>
			</div>
	</body>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
 	var form = $G.getForm("form1");
	grid.load();
	
	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}
	//重置
	function reset(){
		form.reset();
	}

	
	//新增
	function add() {
		var url = "<%=root%>/missionconfig/add";
	    var bizParams = {pageType:"add"};
		var row = grid.getSelected();
	    if (row) {
	    	var id = row.id;
	    	$G.confirm("需要复制该任务？", "确定？", function (action) {
   	         	 if (action == "ok") {
   	         		bizParams["copyflag"] = 1;
   	         		bizParams["id"] = id;
   	         	 }
	   	         $G.showmodaldialog("新增任务配置", url, "80%", "90%", bizParams, function(action){
	   		    	 grid.reload();
	   		     });
	    	});
	    }else{
	    	$G.showmodaldialog("新增任务配置", url, "80%", "90%", bizParams, function(action){
		    	 grid.reload();
		    });
	    }
        
	}
	
	//修改
	function edit() {
		 var row = grid.getSelected();
	     if (row) {
	    	var id = row.id;
	        var url = "<%=root%>/missionconfig/edit?id="+id;
	        var bizParams = {pageType:"edit",id:id};
	        $G.showmodaldialog("修改任务配置", url, "80%", "90%", bizParams, function(action){
		    	 grid.reload();
		    });
	     } else {
	        $G.alert("请选中一条数据！")
	     }
	}

	//删除
	function del() {
        var row = grid.getSelected();
        if (row) {
         var id = row.id;
         $G.confirm("确定删除该任务？", "确定？", function (action) {
            if (action == "ok") {
             grid.loading("操作中，请稍后......");
             $.ajax({
               url: "<%=root%>/missionconfig/del?id="+id,
               success: function (text) {
            	   var record = $G.decode(text);
            	   if(record=="-1"){
           		 		$G.alert("进行中的任务不允许删除!");
            	   }else if(record=="-2"){
            		    $G.alert("该任务已被他人删除 !");
            	   }
                   grid.reload();
               }
             });
            }
          });
        } else {
         	$G.alert("请选中一条数据！")
        }
	}
	
	//发布
	function lowerHair() {
	  var row = grid.getSelected();
	  console.log(row)
      if (row) {
       	var id = row.id;
       	var missionName = row.missionName;
       	var messageId = row.message_id;
       	var messageType = row.message_type;
       	$G.confirm("确定发布该任务？", "确定？",
         	function (action) {
          		if (action == "ok") {
           			grid.loading("操作中，请稍后......");
           			$.ajax({
             			url: "<%=root%>/missionconfig/lowerHair?id="+id,
             			success: function (text) {
          	     			var record = $G.decode(text);
          	     			if(record=="-1"){
         		 				$G.alert("任务已被他人删除!");
          	     			}else if(record=="-2"){
         		 				$G.alert("任务下发失败,所选群组暂无人员!");
          	     			}else if(record >= "0"){
          	     				$G.alert("任务下发成功!");
          	     			}
                 			grid.reload();
             			}
           			});
          		}
         	}
       	);
      } else {
       $G.alert("请选中一条数据！")
      }
	}

	//下载
	function download(){
		 var createfrom = form.getData();
		 createfrom.pageSize = 9999999;
		 createfrom.pageIndex = 0;
		 createfrom.columns = "任务名称,任务分类,任务类型,任务状态,任务期限(工作日),创建时间";
		 createfrom.headers = "mission_name,mission_flag,mission_type,delete_flag,mission_deadline,createdate";
		 createfrom["form"] = {action:"<%=root%>/missionconfig/download"};
		 downFile(createfrom);
	}
	
	//下载模板
	function downloadTemplet(id){
		 var createfrom = {};
		 createfrom.id = id;
		 createfrom["form"]={action: "<%=root%>/missionconfig/downloadTemplet" }; 
		 downFile(createfrom);
	}
	
	//页面表单提交后，后台返回时调用的函数
    function callback(msg){
  	  	mini.alert(msg);
    }
	
    function onActionRenderer(e){
		var obj = "<a class=\"nui-button\" href='javascript:downloadTemplet(\""+e.value+"\")' style='text-decoration:none'>下载模板</a>";
		return obj;
	}
    
	//任务分类
	function onMissionFlagRenderer(e){
		return $G.getDictText("yygl_mission_flag", e.value);
	}
	//任务类型
	function onMissionTypeRenderer(e){
		return $G.getDictText("yygl_mission_type", e.value);
	}
	//任务状态
	function onDeleteFlagRenderer(e){
		return e.value == "0" ? "已删除" : "正常";
	}
	//任务激活标识
	function onActivitiRenderer(e){
		return e.value == "0" ? "启用" : "停用";
	}
	
    
</script>
</html>
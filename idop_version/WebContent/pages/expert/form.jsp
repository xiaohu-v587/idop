<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>专家分析报表参数配置</title>
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
		<!-- 标识页面是修改(edit)、新增(add) -->
    	<input name="pageType" class="mini-hidden" />
        <form id="form1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
         		<input class="mini-hidden"  name="id"  id="id"/>
         		<input class="mini-hidden"  name="counter"  id="counter" value="0"/>
         		<input class="mini-hidden" name="tNum" id="tNum" value="0"/>
         		<input class="mini-hidden" name="wNum" id="wNum" value="0"/>
          		<table style="width: 80%" align="center" id="addtable">
            		
            		<tr>
            			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="100px" nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=bi_module" 
           	 				id="module" name="module"   valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">类型:</td>
              			<td >
                			<input class="mini-combobox" width="100px" nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dict" 
           	 				id="dict" name="dict"   valueField="val" textField="remark" required="true"></input>
            			</td>
              			
        			</tr>
            		<tr>
            			<td class="form_label">名称:</td>
              			<td >
                			<input class="mini-textbox" name="page" style="width: 260px"  id="page"  required="true" ></input>
            			</td>
              		</tr>
              		<tr>
              			<td class="form_label">资源ID:</td>
              			<td >
                			<input class="mini-textbox" name="resid" style="width: 260px"  id="resid"  required="true" ></input>
            			</td>
              		</tr>
              	</table>	
			</div>
		</form>
	</div>
	
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button" onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	  var form = new mini.Form("form1");
	  var counter = 0;
	  mini.parse();
	  
	  //获取父页面传递来的json数据
	  function setData(data) {
	    var cData = mini.clone(data);
	    mini.getbyName("pageType").setValue(cData.action);
		    var id = cData.id;
			$.ajax({
				url: "<%=root%>/expert/getDetailById",
				data:{key: id},
				cache: false,
				success: function (text) {
					var formd = mini.decode(text);
					var record = formd.record;
					form.setData(formd.record);
					
				}
			});
	     
	  }
	  
	//保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "<%=root%>/expert/save";
	    var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
	    //表示为编辑状态
	    if (pageType == "edit") {
	      urlStr = "<%=root%>/expert/update";
	    }
		$G.get("counter").setValue(counter);//传一个条件总数到后台
		//在提交之前为每个输入控件设置一个单独的name			
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    //ajaxConf.setBeforeSubmitFunc(changeNames);
	    $G.submitForm("form1", urlStr, ajaxConf);
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>新增、修改</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
		<style type="text/css">
			.nui-fit, #form1, .div_fit {
				width: 100%;
			}

			.div_fit {
				padding-top:10px;
			}

			.sub_div_fit {
				float: left;
				width: 48%;
				margin-bottom:10px;
			}
			.sub_div_fit_span1{
				display:block;
				width:28%;
				text-align:right;
				float:left;
			}
			.sub_div_fit_span2{
				display:block;
				width:72%;
				float:left;
			}
		</style>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" style="margin-top: 10px"  method="post">
			<input name="pageType" class="nui-hidden"/>
	        <input name="id" class="nui-hidden" />
	        <table>
	        	<tr>
	        		<td>角色：</td>
	        		<td>
	        			<input id="role" name="role" class="nui-combobox"  
						url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" required="true" valuefield="id" style="width:170px"/>
	        		</td>
	        		<td>互斥角色：</td>
	        		<td>
	        			<input id="reject" name="reject" class="nui-combobox"  
						url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" required="true" valuefield="id" style="width:170px"
						/>
	        		</td>
	        	</tr>
	        	
	        </table>
	       	<%-- <div class="div_fit">
			<div class="sub_div_fit">
				<span class="sub_div_fit_span1">角色：</span>
					<span class="sub_div_fit_span2">
						<input id="role" name="role" class="nui-combobox"  
						url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" valuefield="id" style="width:180px"/>
					</span>
			</div>
			<div class="sub_div_fit">
				<span class="sub_div_fit_span1">互斥角色：</span>
					<span class="sub_div_fit_span2">
						<input id="reject" name="reject" class="nui-combobox"  
						url="<%=root%>/rolemutual/getCombobox" textfield="name" dataField="records" valuefield="id" style="width:180px"
						/>
					</span>
			</div>
			</div>  --%>
		</form>
	</div>  
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	
 	function setData(data){
 		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if (pageType == "edit") {
			var ajaxConf=new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setSuccessFunc(function(text) {
				console.log(text.record);
				form.setData(text.record);
				$G.get("role").setEnabled(false);
			});
			$G.postByAjax({id:infos.id},
					"<%=root%>rolemutual/getDetail",ajaxConf);
		}
 	}

 	/*
	 *新增或修改
	 */
	function save(){
    	var urlStr = "<%=root%>rolemutual/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setErrorFunc(function(){
	    	$G.alert("操作失败！");
	    });
	    ajaxConf.setSuccessFunc(function (obj){
	    	if(obj.flag=="-1"){
	    		$G.alert("互斥记录已经存在,无需操作！");
	    		return;
	    	}else if(obj.flag=='0'){
	    		$G.alert("互斥角色不能和角色一致！");
	    		return;
	    	}else{
	    		$G.closemodaldialog("ok");
	    		ajaxConf.setIsShowSuccMsg(true);
	    	}
			
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
 	/*
 	*关闭
 	*/
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>
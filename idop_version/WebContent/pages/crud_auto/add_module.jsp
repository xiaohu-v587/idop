<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>配置属性</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head> 
	<body>
		<div class="nui-fit">
			<form id="form1" method="post">
			<input class="nui-hidden" name="table_name"/>
	       	<div style="padding-left:10px;">
				<table style="table-layout:fixed;">
					<colgroup>
				       	<col width="25%"/>
				       	<col width="23%"/>
				       	<col width="25%"/>
				       	<col width="26%"/>
					</colgroup>
		
						<tr>
							<td align="right">菜单名称：</td>
							<td align="left">
		                   		<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="menu_id" class="nui-combobox" style="width:100px;" textfield="name" valuefield="id" 
                                url="<%=root%>/crud_auto/getMenuList" required="true" onitemclick="validMenu();"/>                   
		                  	</td>
		                  	<td align="right">是否启用：</td>
							<td align="left">
									<input allowInput="true" showNullItem="true" nullItemText="请选择..." emptyText="请选择..." name="start_status" class="nui-combobox" style="width:100px;" textfield="remark" valuefield="val" 
                                url="<%=root%>/param/getDict?key=MODULE_STATUS" required="true"/>
		                  	</td>
		              	</tr>
	              		<tr>
		              		<td align="right">增改对话框宽度：</td>
		              		<td align="left">
		              			<input  class="nui-textbox" name="dialog_width" 	vtype="int" style="width:100px;" required="true"/>
		              		</td>
		              		<td align="right">增改对话框高度：</td>
		              		<td align="left">
		              			<input class="nui-textbox" name="dialog_height" vtype="int" style="width:100px;" required="true"/>
		              		</td>
		              	</tr>
	              	
				</table>
			</div> 
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
	function save(){
    	var urlStr = "<%=root%>/crud_auto/saveModule";
	    var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
		ajaxConf.setErrorFunc(function(){
			$G.alert("操作失败！");
		})
		ajaxConf.setSuccessFunc(function (text){
		    	if(text.flag==1){
		    		$G.closemodaldialog("ok");
		    	}
		    });
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    }
	function setData(data){
 		var infos=$G.clone(data);
 		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(true);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setSuccessFunc(function (text){
			form.setData(text.datas);
		});
		$G.postByAjax({table_name:infos.table_name},"<%=root%>/crud_auto/getInfoByTableName",ajaxConf);
 	}
	function validMenu(){
		var table_name=$G.getbyName("table_name").getValue();
		var menu_id=$G.getbyName("menu_id").getValue();
		var ajaxConf = new GcdsAjaxConf();
		ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
		ajaxConf.setSuccessFunc(function (text){
			if(text.flag==1){
				$G.alert("该模块已经生成过,如要重新生成,请删除原来绑定的表的记录");
				$G.getbyName("menu_id").setValue("");
			}
		});
		$G.postByAjax({menu_id:menu_id},"<%=root%>/crud_auto/getInfoByMenuId?table_name="+table_name,ajaxConf);
	}
 </script>
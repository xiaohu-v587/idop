<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>字典管理编辑页面</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<%@include file="/common/nuires.jsp" %>
    <style type="text/css">
	    html, body{
	        font-size:12px;
	        padding:0;
	        margin:0;
	        border:0;
	        height:100%;
	        overflow:hidden;
	    }
    
	    #detailTable tr{
	    	line-height:30px;
	    }
    </style>
</head>
<body>
	<div class="mini-fit">
		<input class="mini-hidden" name="pageType" value="0" />
			<form id="form1" method="post">
				<input name="id" class="mini-hidden" />
					<div>
					<table style="table-layout:fixed;" width="100%">
						<colgroup>
							<col width="20%"/>
							<col width="25%"/>
							<col width="25%"/>
							<col width="30%"/>
						</colgroup>
						<tr>
							<td align="right">字典编号：</td>
							<td align="left">    
								<input id="zdbh" name="key" class="mini-textbox"  emptyText="字典编号" maxlength="20" required="true" />
							</td>
							<td align="right">字典名称：</td>
							<td align="left">    
								<input id="zdmc" name="name" class="mini-textbox" emptyText="字典名称" maxlength="50" required="true" />
							</td>
						</tr>
						<tr>
							<td align="right">属性键值：</td>
							<td align="left">    
								<input name="val" class="mini-textbox"  emptyText="属性键值"  maxlength="100"  required="true" />
                            </td>
                            <td align="right">中文信息：</td>
                            <td align="left">    
								<input name="remark" class="mini-textbox"  emptyText="中文信息"  maxlength="80"   required="true" />
                            </td>
						</tr>
						<tr>
							<td align="right">排列序号：</td>
							<td align="left">    
								<input name="sortNum" class="mini-textbox"  emptyText="排列序号"  maxlength="5"  vtype="int"/>
							</td>
							<td align="right">是否用于配置登记簿：</td>
							<td align="left">    
								<input name="isyypzdjb" id="isyypzdjb" class="nui-combobox" textfield="dictName" valuefield="dictID"  data="[{dictID:'0',dictName:'否'},{dictID:'1',dictName:'是'}]" value="0"  onvaluechanged="valueChange" />
							</td>
						</tr>  
				  
					</table>
				</div>
          </form>     
     </div>    
     <div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button" iconCls="" onclick="onOk()" enabled="false" style="margin-right: 20px;">确定</a>
		<a id="btnCancle" class="mini-button" iconCls="" onclick="onCancel()" >取消</a>
     </div>
</body>
</html>

<script type="text/javascript">

	$G.parse();
	var form =  new mini.Form("form1");
	var oldFormData;
	
	if (!oldFormData) {
		oldFormData = form.getData();
	}

	//标准方法接口定义
	function setData(data) {
		data = $G.clone(data);//跨页面传递的数据对象，克隆后才可以安全使用
		if (data.pageType == "edit") {
            $G.getbyName("pageType").setValue(data.pageType);
            var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	var record = mini.decode(text).record;
                $G.getbyName("id").setValue(record.id);
                $G.getbyName("name").setValue(record.name);
                $G.getbyName("key").setValue(record.key);                         
                $G.getbyName("val").setValue(record.val);
                $G.getbyName("remark").setValue(record.remark);
                $G.getbyName("sortNum").setValue(record.sortnum);
                $G.getbyName("isyypzdjb").setValue(record.isyypzdjb);
                $G.get("zdbh").disable();
                $G.get("zdmc").disable();
                oldFormData = form.getData();
			});
			$G.postByAjax({key:data.id},"<%=root%>/param/getDetail",ajaxConf);
		}else{
			$G.getbyName("name").setValue(data.name);
			$G.getbyName("key").setValue(data.key);  
		}
	}
	
	function ctrlOkBtn() {
		var oldData= mini.encode(oldFormData); 
		var formData = form.getData();
		var name = $(this).attr("name");
		formData[name] = $(this).val();
		var newData = mini.encode(formData);
		if (oldData == newData) {
			mini.get("btnOk").disable();
		} else {
			mini.get("btnOk").enable();
		}
}

	
	//控制确定按钮状态
	$("#form1 :input.mini-textbox-input").keyup(ctrlOkBtn);
	//$("#form1 :input.mini-textbox-input").change(ctrlOkBtn);
	
	
	function valueChange(e){
		mini.get("btnOk").enable();
	}
	//保存
	function SaveData() {
    	var urlStr = "<%=root%>/param/save";
    	var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
    	
    	if(pageType=="edit"){//表示为编辑状态
    		urlStr = "<%=root%>/param/update";
    	}

    	var ajaxConf = new GcdsAjaxConf();
 	    ajaxConf.setSuccessFunc(function (){
 			$G.closemodaldialog("ok");
 		});
 	    $G.submitForm("form1", urlStr, ajaxConf);
	}




	function GetData() {
		var o = form.getData();
		return o;
	}


	//确定保存
	function onOk() {
		SaveData();
	}

	//取消  
	function onCancel(e) {
		$G.closemodaldialog("cancel");
	}
</script>

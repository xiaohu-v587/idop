<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>无锡农商行查控平台管理系统</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
			<form id="form1" method="post">
			<input name="pageType" class="nui-hidden"/>
	        <input name="uid" class="nui-hidden" />
	        <input name="id" class="nui-hidden" />
	       	<div style="padding-left:11px;padding-bottom:5px;">
				<table style="table-layout:fixed;" align="center">
					<colgroup>
				       	<col width="20%"/>
				       	<col width="30%"/>
				       	<col width="20%"/>
				       	<col width="40%"/>
					</colgroup>
					<tr>
						<td align="right">名称：</td>
						<td align="left">
	                   		<input name="name" id="name" class="nui-textbox"  emptyText="请输入名称" />                   
	                  	</td>
	                  	<td align="right">URL地址：</td>
	                  	<td align="left">
	                    	<input name="url" id="url" class="nui-textbox"  emptyText="请输入地址" /> 
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">上级节点：</td>
	                  	<td align="left">
	                  		<input name="pid" class="nui-textbox" emptyText="请输入上级机构"/>
	                  	</td>
	                  	<td align="right">类型：</td>
	                  	<td align="left">
	                   		<input name="type" class="mini-combobox" emptyText="请输入类型" textfield="remark" valuefield="val"
	                   		       url="<%=root%>/param/getKeyList?key=CK_CDLX"  allowInput="false"/>
	                  	</td>
	              	</tr>
	              	<tr>
						<td align="right">排序：</td>
	                  	<td align="left">
							<input name="ordernum" class="nui-textbox"  emptyText="请输入排序" vtype="float"/>                  
						</td>
	                  	<td align="right">状态：</td>
	                  	<td align="left">
							<input name="enable" class="mini-combobox" emptyText="请选择状态" textfield="remark" valuefield="val"
	                   		       url="<%=root%>/param/getKeyList?key=CK_CDZT"  allowInput="false"/> 
						</td>
					</tr>
					<tr>
						<td align="right">工作流名称：</td>
						<td align="left">    
							<input name="actikey" class="mini-combobox" emptyText="请选择工作流" textfield="name" valuefield="key"
	                   		       url="<%=root%>/param/getActiKey"  allowInput="false"/> 
		              	</td>
		              	<td align="right">能否被搜索：</td>
		              	<td align="left">
		              		<input name="flag" class="mini-combobox" emptyText="请选择是否可以被搜索" onvaluechanged="OnassValueChange" textfield="remark" valuefield="val" value="0"
	                   		       url="<%=root%>/param/getKeyList?key=CK_CDSS"  allowInput="false" />
	                   	</td>	              
	                </tr>
	                <tr>   		              	
	              		<td align="right">搜索方式：</td>
	              		<td align="left">
	              		<input name="style1" class="mini-combobox" emptyText="请选择是否可以被搜索" textfield="remark" valuefield="val"
	                   		       url="<%=root%>/param/getDict?key=CK_CDFS"  multiSelect="true"  allowInput="false" value="" enabled="false" required="true"/>
	                    </td> 
	                </tr> 
		              	<tr>
	                    <td align="right">跳转路径：</td>
						<td colspan="3" align="left" >    
	                    	<input name="adress" id="adress" class="nui-textarea"  style="width:100%"  emptyText="请输入地址" enabled="false" required="true"/> 
						</td>
					</tr> 
					<tr>
						<td align="right">描述：</td>
						<td colspan="3" align="left">    
							<input name="remark" class="nui-textbox" maxlength="100" style="width:100%" />
		              	</td>
	              	</tr> 
				</table>
			</div> 
		</form>
	</div>  
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button"  onclick="save">确定</a> 
		<a class="nui-button"  onclick="onCancel">关闭</a>       
	</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	var orgData;
	
 	function setData(data){
 		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if (pageType == "edit") {
		    $.ajax({
             url: "<%=root%>/menu/getDetail",
             data:{id:infos.id},
             success: function (text) {	
               var record = $G.decode(text).record; 
               $G.getbyName("id").setValue(record.id);                           	
               $G.getbyName("name").setValue(record.name);
	           $G.getbyName("url").setValue(record.url);
	           $G.getbyName("pid").setValue(record.pid); 
	           $G.getbyName("uid").setValue(record.tid);                         
	           $G.getbyName("type").setValue(record.type);
	           $G.getbyName("ordernum").setValue(record.ordernum);
	           $G.getbyName("enable").setValue(record.enable);                         
	           $G.getbyName("remark").setValue(record.remark);
	           $G.getbyName("actikey").setValue(record.actikey);
	           $G.getbyName("flag").setValue(record.flag);
	           $G.getbyName("style1").setValue(record.style1);
	           $G.getbyName("adress").setValue(record.adress);
	           if(record.flag==1){
	 			  $G.getbyName("style1").setEnabled(true);	  
				  $G.getbyName("adress").setEnabled(true);
	           }
             }
            });
		//增加父节点，地址置空，上级机构为0
		}else if(pageType=="add"){
		  $G.getbyName("url").disable();
		  $G.getbyName("actikey").disable();
          $G.getbyName("pid").setValue("0");
          $G.getbyName("uid").setValue("0");
          $G.getbyName("pid").disable();
          $G.getbyName("enable").setValue("0");
         
	     //增加子节点，需要选中一个节点以后，再进行操作
		}else if(pageType == "addchild"){
		  $G.getbyName("pid").setValue(infos.name);
		  //用来储存pid的id
		  $G.getbyName("uid").setValue(infos.pid);
		  $G.getbyName("enable").setValue("0");
		}
 	}

 	/*
	 *保存数据
	 */
	function save(){
		var pageType=$G.getbyName("pageType").getValue();
    	var urlStr = "<%=root%>/menu/savee";
		if(pageType=="edit"){
			urlStr = "<%=root%>/menu/update";
		}
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}

	//验证机构号是否已存在
	function onOrgNumValidation(e){
		var pageType = $G.getbyName("pageType").getValue();
    	if(pageType=="edit" && orgData.orgnum == $G.getbyName("orgnum").getValue()){
        	return;
        }
		if (e.isValid) {
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsAsync(false);
			ajaxConf.setIsShowSuccMsg(false);
			ajaxConf.setIsShowProcessBar(false);
			ajaxConf.setSuccessFunc(function (text) {
				if (text.msg == "1") {
					e.errorText = "该机构号已存在";
					e.isValid = false;
				}
			});
          	var urlStr = "<%=root%>/org/checkOrgNum";
          	var infoKey = e.value;
			$G.postByAjax({orgNum:infoKey},urlStr,ajaxConf);
		}
	}
	function OnassValueChange(e){
		  if(e.value==1){    //可以进行搜索
			  $G.getbyName("style1").setEnabled(true);	  
			  $G.getbyName("adress").setEnabled(true);
		  }
		  if(e.value==0){   //不能进行搜索
			  $G.getbyName("style1").setValue("");
			  $G.getbyName("style1").setEnabled(false);
			  $G.getbyName("adress").setValue("");
			  $G.getbyName("adress").setEnabled(false);			  			
		  }
	}
 	function onCancel(e) {
		$G.closemodaldialog("ok");
    } 
 </script>
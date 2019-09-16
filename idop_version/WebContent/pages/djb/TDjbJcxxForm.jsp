<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 信息模块配置管理
   *
   * Created on 
   * @author  
   * @reviewer 
-->
	  <head>
    <title>信息模块配置管理</title>
	
</head>
  <body>
<style type="text/css">
    html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
    
</style>
	    <!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
	<div class="mini-fit" allowResize="true">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1"  action="<%=root%>/djb/save"  method="post"   enctype="multipart/form-data">
	        <div style="padding-bottom: 20px; padding-top: 20px;">
		        <input class="mini-hidden"  name="id" />
		        <table style="table-layout: fixed;" align="center">
		            <tr>
		            	<td align="250px">
		                	登记簿名称:
		            	</td>
		                <td  align="left"  style="width: 250px;">
		                	<input class="mini-textbox" style="width: 250px;"  name="djbmc" required = "true"></input>
		                </td>
			        </tr>
			        <tr>
			            <td  align="left">适用机构:</td>
						<td style="width:250px;" colspan="3"><input id="orgid" name="orgid" style="width:250px;"
									class="mini-treeselect" url="<%=root%>/org/getListByUser"
									dataField="datas" textField="orgname" valueField="id"
									parentField="upid" valueFromSelect="false" multiSelect="false"
									expandOnLoad="0" emptyText="机构" allowInput="false"
									showClose="true" oncloseclick="onCloseClick" 
									showRadioButton="true" showFolderCheckBox="false"
									popupWidth="286" popupHeight="470" popupMaxHeight="600" required = "true"/></td>
		        	</tr>
		        	<!--<tr>
		            	<td align="left">
		                	是否配置流程:
		            	</td>
		                <td  align="left"  style="width: 250px;">
		                	<input id="sfpz" name="sfpz" class="mini-combobox" style="width:250px;"
							textField="remark" valueField="val" allowInput="false"  
							valueFromSelect="true" showNullItem="true"  required = "true"  url="<%=root%>/djb/getSfpzList" />
		                </td>
			        </tr>-->
			        <tr>
		            	<td align="left">
		                	是否需要复核:
		            	</td>
		                <td  align="left"  style="width: 250px;">
		                	<input id="sfpz" name="sfpz" class="mini-combobox" style="width:250px;"
							textField="remark" valueField="val" allowInput="false"  
							valueFromSelect="true" showNullItem="false"  required = "true"  url="<%=root%>/djb/getSfpzList" value="0"/>
		                </td>
			        </tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" id="toolbardiv" style="text-align:center;padding-top:10px;padding-bottom:10px;margin-bottom:-5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="okBtn" class="mini-button" onclick="onOk" style="margin-right:20px;" >确定</a>
		<a class="mini-button" onclick="onCancel"  >取消</a>
	</div>
<script type="text/javascript">
  mini.parse();
  var form =  new mini.Form("dataform1");
  var cfg = {"THFS":"500002","ZT":"500001" }
	 //cfg.dict=${dict}
	 //toFormDict(cfg);
  function ondrawcell(e){
  	toDict(e,cfg);
  }
  function saveJson() {
  	//保存
    var urlStr = "<%=root%>/djb/save";
  	//获取当前页面是编辑还是新增状态  1.编辑
    var pageType = mini.getbyName("pageType").getValue();
    var djbmc = mini.getbyName("djbmc").getValue();
    var orgid = mini.getbyName("orgid").getValue();
    var sfpz = mini.getbyName("sfpz").getValue();
    form.validate();
    if (form.isValid() == false)
		return;
    if(pageType=="1"){
        var id = mini.getbyName("id").getValue();
        $.ajax({
  			 url:"<%=root%>/djb/update",
  			 data:{djbmc:djbmc,id:id,orgid:orgid,sfpz:sfpz},
  			 type:'post',
               cache: false,
               success: function (text) {
                   if(text.flag == "0"){
                	   CloseWindow("false");
                   }
                   if(text.flag == "1"){
						CloseWindow("edit");
                   }
               }
  		});
   }else{
	   if(djbmc == "" || orgid == ""||sfpz==""){
	    	mini.alert("请将信息填写完整！");
	    }else{
		    var o = form.getData(); 
		    var data = mini.encode([o]);           
		    form.validate();
		  //表示为编辑状态
		  	var json = mini.encode([o]);
		    $.ajax({
		         url: urlStr,
				 type: 'post',
		         data: { data: json},
		         cache: false,
		         success: function (text) {
		             mini.alert("新增成功!");
		             CloseWindow("save");
		         },
		         error: function (jqXHR, textStatus, errorThrown) {
		             alert(jqXHR.responseText);
		             CloseWindow();
		         }
		    });
		}
	}
  }
  
  // 机构下拉框清空
  function onCloseClick(e) {
      var obj = e.sender;
      obj.setText("");
      obj.setValue("");
  }
  

  
	
  //获取父页面传递来的json数据
  function SetData(data) {
    //跨页面传递的数据对象，克隆后才可以安全使用
    var  infos = mini.clone(data);
    var  action = infos.pageType;
    var id = infos.id;
    mini.getbyName("id").setValue(id);
    if (action == "edit") {
       //跨页面传递的数据对象，克隆后才可以安全使用
    	mini.getbyName("pageType").setValue("1");
        var stat = infos.stat;
     	 if(stat == "1"){
    	   mini.get("orgid").disable();
    	    mini.get("sfpz").disable();
        } 
        $.ajax({
            url: "<%=root%>/djb/getTDjbJcxxVOById",
            data:{id:id},
            cache: false,
            success: function (text) {
	            var record = mini.decode(text).record;
	            mini.getbyName("djbmc").setValue(record.djbmc);
	            mini.getbyName("orgid").setValue(record.orgid);
	            mini.getbyName("sfpz").setValue(record.sfpz);
	            oldFormData = form.getData();
     	    }
        });
     }
  }

  //确定保存或更新
   function onOk() {
       saveJson();
   }

  //取消
   function onCancel(e) {
        CloseWindow("cancel");
    }

   function CloseWindow(action) {        
       if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
       else window.close();            
   }
</script>
</body>
</html>

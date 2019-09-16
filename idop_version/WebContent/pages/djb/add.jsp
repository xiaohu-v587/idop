<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<!--
   * 登记簿流程配置新增、修改
   *
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>编辑页面</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <style type="text/css">
    html, body
    {
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
		
		<form id="form1" method="post">
			<input class="mini-hidden" name="pageType" value="0" />
			<input name="id" class="mini-hidden"/>
			<input name="djbid" id="djbid" class="mini-hidden"/>
            <div>
				 <table style="table-layout:fixed;" width="100%">
                     <colgroup>
                          <col width="50%"/>
                          <col width="50%"/>
                     </colgroup>
                     <tr>
                          <td align="right">当前操作角色是否为发起人:</td>
                          <td align="left">    
                              <input id="flag" name="flag" class="mini-checkbox" value="false" onvaluechanged="flagValueChanged(this)"/>
                          </td>
                     </tr>
                     <tr>
                          <td align="right">当前操作角色：</td>
                          <td align="left">    
                              <input id="current_operator" name="current_operator" class="mini-combobox" style="width:200px;" textField="name" valueField="id" emptyText="请选择..." multiSelect="false"
                                 url="<%=root%>/djb/getCurrentRoleList?djbid=${djbid}" onvaluechanged="checkValueChanged(this)" allowInput="false" showNullItem="true" nullItemText="请选择..." required="true"/>
                          </td>
                     </tr>
                     <tr>
                          <td align="right">上级审批(复核)角色/下级执行角色：</td>
                          <td align="left">    
                              <input id="next_operator_group" name="next_operator_group" class="mini-combobox" style="width:200px;" textField="name" valueField="id" emptyText="请选择..."
                                 url="<%=root%>/djb/getRoleList" onvaluechanged="checkValueChanged(this)" allowInput="false" showNullItem="true" nullItemText="请选择..." required="true" multiSelect="true"/>
                          </td>
                     </tr>
                     <tr>
                          <td align="right">上级审批(复核)角色/下级执行角色对应字段：</td>
                          <td align="left">    
                              <input id="zdmc" name="zdmc" class="mini-combobox" style="width:200px;" textField="yshy" valueField="ysm" emptyText="请选择..."
                                 url="<%=root%>/djb/getZdmc?djbid=${djbid}"  allowInput="false" showNullItem="true" nullItemText="请选择..." required="true" multiSelect="false"/>
                          </td>
                     </tr>
                  </table>
             </div>
          </form>
     </div>
     <div class="mini-toolbar" style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" 
       borderStyle="border-left:0;border-bottom:0;border-right:0;">
       		<a id="btnOk" class="mini-button" iconCls="icon-ok" onclick="onOk()" enabled="false">确定</a>
            <a id="btnCancle" class="mini-button" iconCls="icon-close" onclick="onCancel()" style="margin-right: 20px;">取消</a>
     </div>
    <script type="text/javascript">
        mini.parse();
        var form =  new mini.Form("form1");
        var oldFormData;
		if (!oldFormData) {
			oldFormData = form.getData();
		}
        
	    //控制确定按钮状态
		function checkValueChanged(e) {
			var oldData= mini.encode(oldFormData);
			var formData = form.getData();
			var value = e.value;
			formData[e.name] = value;
			var newData = mini.encode(formData);			
			if (oldData == newData) {
				mini.get("btnOk").disable();
			} else {
		 		mini.get("btnOk").enable();
		 	}
		}

        // 保存
        function onOk() {
        	var pageType = mini.getbyName("pageType").getValue();
        	var current_operator = mini.getbyName("current_operator").getValue();
        	var next_operator_group = mini.getbyName("next_operator_group").getValue();
        	form.validate();
            if (form.isValid() == false) return;
				$.ajax({
					url: "<%=root%>/djb/getAffairsName",
           		    type: 'post',
           	        data: {id:mini.getbyName("id").getValue(),current_operator:current_operator,djbid:mini.getbyName("djbid").getValue()},
           	        cache: false,
           	        success: function (text) {
           	        	if(text!=0){
           	            	mini.alert("当前操作角色已存在。。。");
           	            }else{
           	            	saveData();
               	        }
           	        }
           	        ,error: function () {
           	        	 mini.alert("数据查询失败");
					}
				});
        }
        
        //把单选框变成多选框
        function flagValueChanged(e){
        	var flag = e.value;
        	if(flag == "true"){
        		mini.get("current_operator").setMultiSelect(true);
        	}else{
        		mini.get("current_operator").setMultiSelect(false);
        		mini.get("current_operator").setValue(null);
        		mini.get("current_operator").setText("");
        	}
        }
        
		//保存事务工作流维护信息
        function saveData() {
            
        	// 用保存信息
            var urlStr = "<%=root%>/djb/saveDjb";

            // 获取当前页面是编辑还是新增状态
            var pageType = mini.getbyName("pageType").getValue();
            // 表示为编辑状态
            if(pageType=="1"){
            	urlStr = "<%=root%>/djb/updateDjb";
            }
            var o = form.getData();            
            form.validate();
            if (form.isValid() == false) return;
            var json = mini.encode([o]);
            $.ajax({
                url: urlStr,
		        type: 'post',
                data: { data: json},
                cache: false,
                success: function (text) {
                	if(pageType=="1"){
                		mini.alert("修改成功!");
                	}else{
                		mini.alert("新增成功!");
                	}
                	CloseWindow("save");
                }
                ,error: function (jqXHR, textStatus, errorThrown) {
                    alert(jqXHR.responseText);
                    CloseWindow();
                }
            });
        }
       
        //标准方法接口定义
        function setData(data) {
        	data = mini.clone(data);
            action = data.action;
            if (data.action == "edit") {
            	//跨页面传递的数据对象，克隆后才可以安全使用
                mini.getbyName("pageType").setValue("1");
                $.ajax({
                    url: "<%=root%>/djb/getDetail",
                    data:{key:data.id},
                    cache: false,
                    success: function (text) {
                        var record = mini.decode(text).record;
                        mini.getbyName("id").setValue(record.id);
                        mini.getbyName("current_operator").setValue(record.current_operator);
                        mini.getbyName("next_operator_group").setValue(record.next_operator_group);
                        mini.getbyName("djbid").setValue(record.djbid);
                        mini.getbyName("zdmc").setValue(record.zdmc);
                        if(record.FLAG == "1"){
	                        mini.getbyName("flag").setValue(true);
                        }else{
                        	mini.getbyName("flag").setValue(false);
                        }
                        oldFormData = form.getData();
                    }
                });
            }else{
            	mini.getbyName("djbid").setValue(data.djbid);
            }
        }

        // 关闭窗口
        function CloseWindow(action) {        
            if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
            else window.close();            
        }
        
        //取消  
        function onCancel(e) {
            CloseWindow("cancel");
        }
    </script>
</body>
</html>

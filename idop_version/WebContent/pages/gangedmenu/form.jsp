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
	<div class="mini-fit">
		<form id="form1" method="post">
			<input name="id" class="mini-hidden" /> 
			<input name="bz" class="mini-hidden" /> 
			<input name="upid" class="mini-hidden" />
			<input name="type" class="mini-hidden" />
			<input name="vals" class="mini-hidden" />
			<div style="padding-left: 11px; padding-bottom: 5px;">
				<table style="table-layout: fixed;" align="center" >
				   <tr  style="height: 50px;">
						<td>父节点：</td>
						<td><input id="parentname" name="parentname" class="mini-textbox" style="width:200px;" maxlength="50"/>
						</td>
					</tr>
					<tr  style="height: 50px;">
						<td>编号：</td>
						<td><input id="val" name="val" class="mini-textbox" required="true"
							maxlength="10" style="width: 200px;" emptyText="字段编号" />
						</td>
					</tr>
					<tr style="height: 50px;">
						<td>名称：</td>
						<td><input id="remark" name="remark" class="mini-textbox"
							required="true" maxlength="50" style="width: 200px;"
							emptyText="字段名称" />
					   </td>
					</tr>
					<tr style="height: 50px;">
						<td>排序：</td>
						<td><input id="collate" name="collate" class="mini-textbox"
							required="true" maxlength="50" style="width: 200px;"
							emptyText="排序名称" />
					   </td>
					</tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a class="mini-button" onclick="onOk" iconCls="icon-ok"
			style="width: 60px; margin-right: 20px;">确定</a> <a
			class="mini-button" onclick="onCancel" iconCls="icon-close"
			style="width: 60px;">取消</a>
	</div>
	<script type="text/javascript">
		mini.parse();

		var form = new mini.Form("form1");

	     
		//标准方法接口定义
		function SetData(data) {
			if (data.action == "new") {
				
				// 跨页面传递的数据对象，克隆后才可以安全使用
				
				mini.getbyName("bz").setValue("add");
				mini.getbyName("upid").setValue(data.pid);
				mini.getbyName("type").setValue(data.type);
				mini.getbyName("id").setValue(data.pid);
				mini.getbyName("parentname").setValue(data.remark);
				//mini.getbyName("collate").setValue(data.collate);
				mini.getbyName("parentname").disable();
			} else if (data.action == "edit") {
				
				mini.getbyName("bz").setValue("edit");
				mini.getbyName("id").setValue(data.id);
				mini.getbyName("upid").setValue(data.pid);
				mini.getbyName("vals").setValue(data.val);
				mini.getbyName("parentname").setValue(data.remark);
			///mini.getbyName("collate").setValue(data.collate);
				mini.getbyName("parentname").disable();
				$.ajax({
					url : "<%=root%>/gangedmenu/getDetail",
					data : {
						id : data.id
					},
					cache : false,
					success : function(text) {
						var datas = mini.decode(text).datas;

						mini.getbyName("remark").setValue(datas.remark);
						mini.getbyName("val").setValue(datas.val);
						mini.getbyName("collate").setValue(datas.collate);
						//mini.getbyName("val").disable();\
					}
				});
			}
		}

		// 新增保存
		function SaveData() {
			var formData = form.getData();
			
			var collate=formData.collate;

			if(isNaN(collate)==true){
				mini.alert("排序号必须为数字型数据,请输入数字型数据");
				return;
			}
			
			form.validate();
			if (form.isValid() == false)
				return;
			
		
			var json = mini.encode([ formData ]);
			
			// 判断键值是否重复
			$.ajax({
				url : "<%=root%>/gangedmenu/checkZdwhNum",
				type : "post",
				data : {
					val : formData.val,
					upid : formData.upid
				},
				cache : false,
				success : function(text) {
					if (text.msg == "1") {
						mini.alert("键值已经存在！");
						return;
					} else if (text.msg == "0") {
						
						// 保存
						$.ajax({
							url : "<%=root%>/gangedmenu/save",
							type : 'post',
							data : {
								data : json
							},
							cache : false,
							success : function(text) {
								var flag = mini.decode(text).flag;
								if (flag == 1) {
									mini.alert("添加成功！", "消息",
											CloseWindow("save"));
								} else {
									mini.alert("添加失败！");
								}
							},
							error : function(jqXHR, textStatus, errorThrown) {
								mini.alert(jqXHR.responseText);
								CloseWindow();
							}
						});
					}
				}
			});
		}

		//修改保存(修改时键值不让修改！)
		function SaveEditData() {
			var formData = form.getData();
			
			var collate=formData.collate;
	
			if(isNaN(collate)==true){
				mini.alert("排序号必须为数字型数据,请输入数字型数据");
				return;
			}
			
			form.validate();
			if (form.isValid() == false)
				return;
			var json = mini.encode([ formData ]);
			var vals = mini.getbyName("vals").getValue();
						
			// 保存
			$.ajax({
				url : "<%=root%>/gangedmenu/update",
				type : 'post',
				data : {
					data : json
				},
				cache : false,
				success : function(text) {
					var flag = mini.decode(text).flag;
					if (flag == 1) {
						mini.alert("修改成功！", "消息",
								CloseWindow("save"));
					} else {
						mini.alert("修改失败！");
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					mini.alert(jqXHR.responseText);
					CloseWindow();
				}
			});
		}

		
		//确定
		function onOk(e) {
			var bz = mini.getbyName("bz").value;
			if (bz == "add") {
				SaveData();
			} else if (bz == "edit") {
				SaveEditData();
			}
		}
		//取消
		function onCancel(e) {
			CloseWindow("cancel");
		}

		// 关闭窗口
		function CloseWindow(action) {
			if (window.CloseOwnerWindow)
				return window.CloseOwnerWindow(action);
			else
				window.close();
		}
	</script>    
</body>
</html>
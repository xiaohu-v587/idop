<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html>
	<head>
		<title>附件</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div class="nui-fit">
			<div class="nui-toolbar" id="toolbar" style="border:0;padding:0px;height: 32px;">
				<form id="form1" method="post" action="<%=root%>/manualReport/excel" enctype="multipart/form-data">
					<table style="width:100%;">
						<tr>
							<td style="right">
								<input name="id" class="nui-hidden"/>
			      				<input id="file" name="file" class="mini-htmlfile"/>
			      				<a class="mini-button" iconCls="" id="uploadBtn" onclick="upload()">上传</a>
								<span class="separator"></span>
								<a class="mini-button" iconCls="" id="delBtn" onclick="delFile()">删除</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width:100%;margin:1% 0" 
				sortMode="client" showPager="false" showEmptyText="true" emptyText="没有文件" 
				url="<%=root%>/manualReport/getFileList" onselectionchanged="onSelectionChanged" onload="controlBtnState">
				<div property="columns">
					<div field="file_id" visible="false"><fmt:message key="ID"/></div>
					<div field="file_url" visible="false">路径</div>
					<div type="checkcolumn" name="checkCloumn"></div>  
					<div type="indexcolumn" headerAlign="center" align="center">序号</div>
			        <div field="file_name" allowSort="true" headerAlign="center" align="left" renderer="addLink">文件名</div>
				</div>
			</div>
		</div>
		<div class="mini-toolbar" style="text-align:center;padding:10px 0" borderStyle="border-left:0;border-bottom:0;border-right:0">
			<!-- <a id="okBtn" class="mini-button" iconCls="icon-ok" onclick="onOk">确定</a>
            <span class="separator"></span> -->
            <a id="closeBtn" class="mini-button" iconCls="" onclick="onCancel">关闭</a>
		</div>
   		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
		<script type="text/javascript">
			$G.parse();
			var grid = $G.get("datagrid1");
			var form = $G.get("form1");
			var file = $G.get("file");
			$G.get("uploadBtn").disable();

			function setData(data){
				data = $G.clone(data);
				$G.getbyName("id").setValue(data.id);
				if(data.view==1){
					$G.get("toolbar").hide();
				}
				grid.load({id:data.id});
			}
			
			file.on("fileselect",function(){
				if(file.value){
					$G.get("uploadBtn").enable();
				}
			});
			
			function upload(){
				var file = $("#file > input:file")[0];
				$.ajaxFileUpload({
		            url: "<%=root%>/manualReport/saveFile",
		            fileElementId: file,
		            data:{id:$G.getbyName("id").getValue()},
		            dataType: 'text',
		            success: function (data, status){
		            	grid.load({id:$G.getbyName("id").getValue()});
		    			$G.get("uploadBtn").disable();
		    			$G.alert("文件上传成功！");
		            },
		            error: function (data, status, e){
		            	$G.alert("文件获取失败!");
		            },
		            complete: function(){
		            	var jq = $("#file > input:file");
		            	jq.before(file);
		            	jq.remove();
		            }
				});
			};
			
			//控制按钮的状态 
			function controlBtnState() {
				$G.get("delBtn").disable();
			}
			
			//当选中grid中的行    
			function onSelectionChanged(e){
				var record = grid.getSelected();
				if (record) {
					$G.get("delBtn").enable();
				} else {
					$G.get("delBtn").disable();
				}
			}
			
			function addLink(e){
				var record = e.record;
				return "<a style='color:blue' href='<%=root%>/manualReport/download?id="+ record.file_id +"'>"+record.file_name+"</a>";
			}
			
			function delFile(){
				var record = grid.getSelected();
				$G.confirm("确认删除文件？若删除，文件将无法找回","操作提示",function(action){
					if(action=='ok'){
						var ajaxConf = new GcdsAjaxConf();
						ajaxConf.setIsShowProcessBar(true);
						ajaxConf.setIsShowSuccMsg(false);
					    ajaxConf.setSuccessFunc(function (data){
					    	grid.reload();
					    });
					    $G.postByAjax({id:record.file_id,filepath:record.file_url},"<%=root%>/manualReport/delFile",ajaxConf);
					}
				});
			}
			
			function onOk(e) {
				$G.closemodaldialog("ok");
		    }
			
			function onCancel(e) {
				$G.closemodaldialog("cancel");
		    }
			
		</script>
	</body>
</html>

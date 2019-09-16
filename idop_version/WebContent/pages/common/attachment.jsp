<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html>
	<head>
		<title>附件</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	 <style type="text/css">
        html, body
        {
            height: 100%;
            width: 100%;
            padding: 0;
            margin: 0;
            overflow: hidden;
        }
    </style>
	 <%
    	Object limitType = request.getAttribute("limitType");
    	Object ebavleType = request.getAttribute("ebavleType");
    	Object ebavleUrl = request.getAttribute("ebavleUrl");
    	Object limitSize = request.getAttribute("limitSize");
    	
    %>
    </head>
	<body>
		<div class="nui-fit">
			<div class="nui-toolbar" id="toolbar" style="border:0;padding:0px;height: 32px;">
				<form id="form1" method="post" action="<%=root%>/manualReport/excel" enctype="multipart/form-data">
					<table style="width:100%;">
						<tr>
							<td style="right">
								<input name="id" class="nui-hidden"/>
			      				<input id="file" name="file" class="mini-htmlfile"  style="width:280px"   ebavleType="<%=(ebavleType==null?"":ebavleType)%>" 
			      				limitSize="<%=(limitSize==null?"":limitSize)%>"    limitType=""   ebavleUrl="<%=root%>/<%=(ebavleUrl==null?"":ebavleUrl)%>"/>
			      				<a class="mini-button" iconCls="icon-upload" id="uploadBtn" onclick="upload()">上传</a>
								<span class="separator"></span>
								<a class="mini-button" iconCls="icon-remove" id="delBtn" onclick="delFile()">删除</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="datagrid1" dataField="datas" class="mini-datagrid" style="width:96%;height:220px;" sortMode="client" allowUnselect="false"
														     oncellclick="" onrowdblclick="" onselectionchanged=""  onload="" url="<%=root%>/common/queryFiles"
														    autoEscape="true" onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" 
														    ondrawcell="ondrawcell" pageSize="5">
										        <div property="columns">                    
													<div field="id" visible="false" ></div>
													<div field="pid" visible="false" >任务父id</div>
													<div field="filename" headerAlign="center" align="center" >附件名称</div>
										            <div field="createdatef" headerAlign="center" align="center" >上传日期</div>                
										            <div field="proc" headerAlign="center" align="center" >操作</div>
										        </div>
			</div> 
		</div>
		<div class="mini-toolbar" style="text-align:center;padding:10px 0" borderStyle="border-left:0;border-bottom:0;border-right:0">
			<!-- <a id="okBtn" class="mini-button" iconCls="icon-ok" onclick="onOk">确定</a>
            <span class="separator"></span> -->
            <a id="closeBtn" class="mini-button" iconCls="icon-cancel" onclick="onCancel">关闭</a>
		</div>
   		<script src="<%=request.getContextPath()%>/resource/js/ajaxfileupload.js" type="text/javascript"></script>
		<script type="text/javascript">
			$G.parse();
			var grid = $G.get("datagrid1");
			var form = $G.get("form1");
			var file = $G.get("file");
			$G.get("uploadBtn").disable();
			var pid = "<%=request.getParameter("pid")%>";
			grid.load({pid:pid});
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
		            url: "<%=root%>/common/uploadFile",
		            fileElementId: file,
		            data:{pid:pid},
		            dataType: 'text',
		            success: function (data, status){
		            	grid.load({pid:pid});
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
				return "<a style='color:yellow' href='<%=root%>/manualReport/download?id="+ record.file_id +"'>"+record.file_name+"</a>";
			}
			
			function delFile(){
				var record = grid.getSelected();
				top.okBtnPosition="left";
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
				CloseWindow("ok");
		    }
			
			function onCancel(e) {
				CloseWindow("cancel");
		    }
			function SetData(data) {
			 	//跨页面传递的数据对象，克隆后才可以安全使用
				data = $G.clone(data);
			}
			function ondrawcell(e){
				
				if(e.field == "proc") { // 操作
				   	e.cellStyle="text-align:center";
					e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\")'>删除</a>";

				 }
				
			}
			 function profile(flag,id,createdate) {
				 top.okBtnPosition="left";
					mini.confirm("是否确认删除?","删除确认",function(action){
				 		if(action == "ok"){
			           	 $.ajax({
			                   url: "<%=root%>/common/delFile",	//将请求提交至UserCtl的save方法
			    		        type: 'post',
			                    data:  {id:id,createdate:createdate},
			                    cache: false,
			                    success: function (text) {
			                    	var flag = mini.decode(text).flag;
			    		             if(flag == "1"){	
			    		            	 mini.alert("删除成功！");
			    		             	 grid.reload();
			    		             }else {	
			    		            	mini.alert("删除失败！");
			    		             }
			                    },
			                    error: function (jqXHR, textStatus, errorThrown) {
			                        alert(jqXHR.responseText);
			                        CloseWindow();
			                    }
			                });
				 		}});
				}
			 function CloseWindow(action) {
			        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
			        else window.close();
			    }
		</script>
	</body>
</html>

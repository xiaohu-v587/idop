<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
	<head>
		<title>文件类型任务反馈</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script src="<%=request.getContextPath()%>/resource/js/fileupload/jquery214.min.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/resource/js/fileupload/jquery.liteuploader.min.js" type="text/javascript"></script>
	</head>
<body>


<div class="mini-toolbar" style="padding: 2px; border-bottom: 0;">
		<!-- 操作区域 -->
		<table style="width: 100%;">
			<tr>
				<td align="left" cosplan="1">
					任务说明：
					<input id="mission_remark" name="mission_remark" class="mini-textarea" readonly="true" style="width:99%;height:100px;" /></td>
				</td>
			</tr>
			<tr>
				<td>
					<input type="file" name="fileUpload" id="fileUpload" class="fileUpload" multiple/>
					<a class="mini-button" iconCls="icon-ok" id="removeBtn" onclick="upload()" plain="false">上传</a>
				</td>
			</tr>
		</table>
		<form id="form1" method="post">
			<input name="pageType" class="nui-hidden"/>
			<input name="id" class="nui-hidden" />
	       	<input name="mission_issue_id" class="nui-hidden" />
	       	<input name="up_mission_id" class="nui-hidden" />
       	</form>
	</div>
	<!-- 表格绘制区域 -->
	<div class="mini-fit" id="hot_div">
		<div id="filelist" dataField="datas" class="mini-datagrid" style="width:99%;height:100%;margin-left: 5px;" sortMode="client" 
			allowUnselect="false" autoEscape="false" onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" 
			ondrawcell="filedraw_1" pageSize="10" url="<%=root%>/missionFileReport/getRetoactionInfo">
	        <div property="columns">                    
	            <div field="id" visible="false" ></div>
				<div field="pid" visible="false" >任务父id</div>
				<div field="filename" headerAlign="center" align="center" allowsort="true">附件名称</div>
				<div field="username" headerAlign="center" align="center" allowsort="true">上传人</div>
	            <div field="createdate" headerAlign="center" align="center" allowsort="true" renderer="renderTime">上传日期</div>                
	            <div field="yxptid"  visible="false"></div>                
	            <div field="proc" headerAlign="center" align="center" >操作</div>
	        </div>
		</div>	
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" onclick="feedBack">提交</a> 
		<a class="nui-button" onclick="onCancel">关闭</a>       
	</div>  
</body>

<script type="text/javascript">
	 $G.parse();
	 var form = $G.getForm("form1");
	 var status = "${status}";
	 
	 function setData(data){
		 
	 }
	 
	 //反馈 
	 function feedBack(){
	 	 mini.confirm("确定反馈该任务？", "提示", function(action) {
				if (action == "ok") {
					var data = {
						id:"<%=request.getAttribute("id") %>"
					}
					if("04" == status){
						 mini.prompt("请输入提交内容：", "请输入",function (action, value) {
			                if (action == "ok") {
			                    data.mission_remark = value;
			                    toTask(data);
			                } 
						 },true);
					}else{
						toTask(data)
					}
		 		}
		 	});
	 }
	 
	 function toTask(data){
    	//文件類型反饋保存 
    	var urlStr = "<%=root%>/missiondatareport/retoactionTask";
		var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag == '1'){
	    		$G.alert("提交成功","",function(action){
	    			$G.closemodaldialog("OK");	
	   			});
	    	}else if(text.flag == '-1'){
	    		$G.alert(text.msg);
	    	}else{
	    		$G.alert("提交失败");
	    	}
		});
	    $G.postByAjax(data, urlStr, ajaxConf);
    	
    }
	 
	 
	 
     
    var filegrid = $G.get("filelist");
 	var pid = "<%=request.getAttribute("id")%>";
 	var type = "<%=request.getAttribute("type")%>";
 	var pidtype = "3";
 	
 	filegrid.load({"id": pid,"pidtype":pidtype});
 	
 	var handle;
 	var errStr = "";
 	
 	//上传附件
 	function upload() {
     	if (document.getElementById('fileUpload').files.length == 0) {
     		$G.alert("请先选择文件！");
     		return;
     	}
     	
     	//校验文件格式和尺寸
     	if (!validFileType()) {
     		return;
     	} 
     	
     	$(".fileUpload").data("liteUploader").addParam("pid", pid);
     	$(".fileUpload").data("liteUploader").addParam("pidtype", pidtype);
     	$(".fileUpload").data("liteUploader").startUpload();
     	handle = mini.loading("上传中...", "文件上传");
       }

 	$(".fileUpload").liteUploader({
         script: "<%=root%>/common/uploadFile",
        	singleFileUploads: true
       })
       .on("lu:success", function (e, response) {	//成功时回调
     	 filegrid.load({'id':pid,"pidtype":pidtype});	//刷新附件列表
     	 mini.hideMessageBox(handle);	//释放遮罩
       });
       
       
       //校验文件格式和大小
       function validFileType() {
     	  var arr = document.getElementById('fileUpload').files;
     	  for(var i=0; i<arr.length; i++) {
 			var name = arr[i].name;
 			var start = name.lastIndexOf(".")+1;
 			var type = name.substring(start, name.length);
 			/* if (type != "jpg" && type != 'JPG' && type != 'png' && type != 'PNG'
 				 && type != 'jpeg' && type != 'JPEG' && type != 'gif' && type != 'GIF'
 					&& type != 'doc' && type != 'DOC' && type != 'docx' && type != 'DOCX'
 						&& type != 'xls' && type != 'XLS' && type != 'xlsx' && type != 'XLSX'
 							 && type != 'pdf' && type != 'PDF' && type != 'bmp' && type != 'BMP') {
 				$G.alert("不支持 " + type + " 格式的文件。请上传: jpg、png、jpeg、gif、bmp、doc、xls、pdf 格式");
 				return false;
 			} */
 			
 			var size = arr[i].size;
 			if (size > 15 * 1024 * 1024) {	//不超过15M
 				$G.alert("单个文件不能超过15M !");
 				return false;
 			}
     	  }
     	  
     	  return true;
       }
       
 	function filedraw_1(e){
   		var bz = "101";
       	if(e.field == "proc") { // 操作
       		e.cellStyle="text-align:center";
       		if(type == "see"){
       			e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>";
       		}else{
       			e.cellHtml = "<a href='<%=root%>/common/receiveFileByURL?fileType="+e.record.filetype+"&fileName="+encodeURI(encodeURI(e.record.filename))+"&filePath="+encodeURI(encodeURI(e.record.filepath))+"' target='_blank'>查看</a>&nbsp;<a href='javascript:profile(\"1\",\""+e.record.id+"\",\""+e.record.createdate+"\")'>删除</a>";
       		}
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
 	    		            	 filegrid.reload();
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
       
     function renderTime(e){
 	    	return renderTimes(e.value)
 	}
     
     function CloseWindow(action) {            
	     	
         if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
         else window.close();            
     }
     
     function onCancel(e) {
         CloseWindow("cancel");
     }
 </script>
 
 </html>
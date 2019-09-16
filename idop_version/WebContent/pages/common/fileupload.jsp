<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/common/jstlres.jsp"%>
<script src="<%=request.getContextPath()%>/resource/js/fileupload/jquery214.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/js/fileupload/jquery.liteuploader.min.js" type="text/javascript"></script>
	<div style="margin-top:5px;">
		<input type="file" name="fileUpload" id="fileUpload" class="fileUpload" multiple/>
		<a class="mini-button" iconCls="icon-ok" id="removeBtn" onclick="upload()" plain="false">上传</a>
		
		<div id="filelist" dataField="datas" class="mini-datagrid" style="width:100%;height:200px;" sortMode="client" 
			allowUnselect="false" autoEscape="false" onshowrowdetail="" showEmptyText="true" emptyText="没有对应的附件信息" 
			ondrawcell="filedraw_1" pageSize="5" url="<%=root %>/common/queryFiles">
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
<script type="text/javascript">
	$G.parse();
	var filegrid = $G.get("filelist");
	var pid = "<%=request.getParameter("pid")%>";
	var type = "<%=request.getParameter("type")%>";
	var pidtype = "<%=request.getParameter("pidtype")%>";
	if(type == "see"){
		$G.get("removeBtn").setEnabled(false);
	}
	
	filegrid.load({"pid": pid,"pidtype":pidtype});
	
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
       	/*, rules: {
          allowedFileTypes: null,//"image/jpeg,image/png,image/gif,text/plain",
          maxSize: null//2500000
        } */
      })
      .on("lu:success", function (e, response) {	//成功时回调
    	 filegrid.load({'pid':pid,"pidtype":pidtype});	//刷新附件列表
    	 mini.hideMessageBox(handle);	//释放遮罩
      });
      /* .on("lu:progress", function (e, percentage) {	//上传进度回调
        //console.log(percentage);
      })
      .on("lu:errors", function (e, errors) {	//发生错误回调
    	console.log(errors);
        if (errors.length > 0) {
        	
			/* isErrors = true; 
			$.each(errors, function (i, errorInfo) {
				if (errorInfo.errors.length > 0) {
					errStr += 'ERROR! File: ' + errorInfo.name + ' - Info: ' + JSON.stringify(errorInfo.errors)  + '<br />';
				}
				mini.hideMessageBox(handle);
			});
			//$G.alert("单个文件不能超过15M !");
		}
      }); */
      
      //校验文件格式和大小
      function validFileType() {
    	  var arr = document.getElementById('fileUpload').files;
    	  for(var i=0; i<arr.length; i++) {
			var name = arr[i].name;
			var start = name.lastIndexOf(".")+1;
			var type = name.substring(start, name.length);
			if (type != "jpg" && type != 'JPG' && type != 'png' && type != 'PNG'
				 && type != 'jpeg' && type != 'JPEG' && type != 'gif' && type != 'GIF'
					&& type != 'doc' && type != 'DOC' && type != 'docx' && type != 'DOCX'
						&& type != 'xls' && type != 'XLS' && type != 'xlsx' && type != 'XLSX'
							 && type != 'pdf' && type != 'PDF' && type != 'bmp' && type != 'BMP') {
				$G.alert("不支持 " + type + " 格式的文件。请上传: jpg、png、jpeg、gif、bmp、doc、xls、pdf 格式");
				return false;
			}
			
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
</script>
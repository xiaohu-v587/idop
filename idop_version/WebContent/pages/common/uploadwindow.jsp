<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>附件上传</title>
	<%@ include file="/common/nuires.jsp"%>
    <script src="<%=request.getContextPath()%>/resource/nui/fileupload/swfupload.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/resource/nui/fileupload/multiupload.js" type="text/javascript"></script>
    <link href="<%=request.getContextPath()%>/resource/nui/fileupload/multiupload.css" rel="stylesheet" type="text/css" />
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
    <div class="mini-panel" style="width: 100%; height: 100%" showfooter="true" bodystyle="padding:0" borderStyle="border:0"
        showheader="false">
        
        <div id="multiupload1" class="uc-multiupload" style="width: 100%; height: 100%" 
            flashurl="<%=root%>/resource/nui/fileupload/swfupload.swf"
            uploadurl="<%=root%>/common/uploadFile?pid=<%=request.getParameter("pid")%>" _autoupload="true" borderstyle="border:0" 
            onuploadsuccess="uploadsuccess" onuploaderror="uploaderror" limitType="<%=(limitType==null?"*":limitType)%>" 
            ebavleType="<%=(ebavleType==null?"":ebavleType)%>" limitSize="<%=(limitSize==null?"":limitSize)%>" 
            ebavleUrl="<%=root%>/<%=(ebavleUrl==null?"":ebavleUrl)%>" >
        </div>

        <div property="footer" style="padding:8px; text-align: center">
            <a class="mini-button" onclick="onOk" style="width: 80px" iconcls="">确定</a>
            <a class="mini-button" onclick="onCancel" style="width: 80px; margin-left: 50px"
                iconcls="">取消</a>
        </div>
    </div>
</body>
</html>
<script type="text/javascript">
    $G.parse();
    var grid = $G.get("multiupload1");
    
    var pid = "<%=request.getParameter("pid")%>";
    
    //页面自动加载附件信息
    //document.on

    function SaveData() {
        CloseWindow("ok");
    }
    function CloseWindow(action) {
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }
    function onOk(e) {
        SaveData();
    }
    function onCancel(e) {        
        CloseWindow("cancel");
   }

   /* function SetData(params) {
       grid.setPostParam(params);
   } */
   
   
	function SetData(data) {
	 	//跨页面传递的数据对象，克隆后才可以安全使用
		data = $G.clone(data);
	}

	function GetData() {
		var rows = grid.findRows(function(row) {
			if (row.status == 1) {
				return true;
			}
		})
		return rows;
	}

	function uploadsuccess(e) {
		//$G.alert("上传成功！" + e.serverData);
	}

	function uploaderror(e) {
		$G.alert("上传失败！" + e.file + " " + e.message);
	}
</script>

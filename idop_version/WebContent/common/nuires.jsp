<script src="<%=request.getContextPath()%>/resource/nui/nui.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/nui/locale/zh_CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/nui/gcds/message_zh_CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/gcds/nui-dict.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/gcds/gcdsui.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/gcds/common.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resource/gcds/nui-extension.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/resource/gcds/css/nui-extension.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/resource/gcds/css/gcdsuicss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var appContext = "<%= request.getContextPath() %>";
	/*
		处理ajax 等其他因回话超时未跳转到超时页面场景
		auther: 常显阳  2018/12/10
	*/
	var setting = $.ajax;
	$.ajax = function (a){
		var success = a.success;
		a.success = function(u,v,t){
			try{
				var data = mini.decode(u);
				if(data.isnosession && data.code == "9999"){
					window.location="<%=request.getAttribute("root")%>/nosession";
				}
			}catch(e){
				
			}
			success(u,v,t);
		}
		setting(a);		
	}
</script>

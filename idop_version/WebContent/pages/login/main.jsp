<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
 	<head>
		<title>多棱镜</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/common/nuires.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/index.css">
	</head>
<body>
	<div id="layout1" class="nui-layout" style="width:100%;height:100%;">
	    <div style="height:79px;overflow:hidden;background-image:url(resource/zxcss/zximage/top_bg.jpg);background-repeat:no-repeat;background-size:100% 100%;background-size:100% 100%;border-bottom:0;" region="north" class="header app-header " splitSize="0" height="auto" showSplit="false" showHeader="false">
		    <div style="height:79px;overflow:hidden;margin:0;padding:0;border:0;">	
		    	<div style="height:100%;position:absolute;overflow:hidden;">
		    		<span style="display:inline-block;height:100%;width:200px;background-image:url(resource/zxcss/zximage/duolenjing.png);background-repeat:no-repeat;background-size:200px 100%;"></span>
		    		<span style="display:inline-block;background-image: url(resource/zxcss/zximage/logo3.png);background-repeat:no-repeat;background-size:contain;width:400px;height:75px;margin:0 0 0 0;"></span>
		    	</div>
			     <div class="search_box" style="position:absolute;margin-left:40%;margin-top:26px;z-index:999">
	        		<div id="tb" name="tb"  class="mini-autocomplete" valueField="describe" textField="describe" url="<%=root%>/menu/menuSearch" 
	        		style="height:30px;width:90%;border:0;" onvaluechanged="onvalueChenaged" >
	        			<div property="columns">
	        				<div field="describe"  width="150" align="left"></div>
	        			</div>
	        		</div>
	                <a href="javascript:menuSearch()"><span class="search_btn"></span></a>
	            </div>
			    <div style="color:#ffffff; font-size:14px;position:absolute;line-height:79px;right:480px;margin-top:px;">
			    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好：${user.name}！
			    </div>
		        <div style="position:absolute;top:30px;right:30px;width:450px;">
		           <a href="javascript:alterUserInfo('0')"  style="color: #ffffff;text-decoration: none;" ><img border="0" width="20px;" height="20px;" src="resource/zxcss/zximage/mymenu.png" />&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#ffffff; font-size:14px;position:absolute;">我的</span>&nbsp;&nbsp;&nbsp;&nbsp;</a>
		          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		           <a href="javascript:alterUserRole()"  style="color: #ffffff;text-decoration: none;" ><img border="0" width="20px;" height="20px;" src="resource/zxcss/zximage/setinfo.png" />&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#ffffff; font-size:14px;position:absolute;">角色切换</span>&nbsp;&nbsp;&nbsp;&nbsp;</a>
		           <a href="javascript:switchOrg()"  style="color: #ffffff;text-decoration: none;" ><img border="0" width="20px;" height="20px;" style="margin-left:50px;" src="resource/zxcss/zximage/setinfo.png" />&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#ffffff; font-size:14px;position:absolute;">切换机构</span>&nbsp;&nbsp;&nbsp;&nbsp;</a>
		            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		           <!-- <a class="mini-button mini-button-iconTop" iconCls="icon-setinfo" onclick="alterUserRole()" plain="true" style="color: #FFFFFF;">角色切换 </a>
		           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
		           <a href="javascript:loginOut()" style="color: #ffffff;text-decoration: none;"><img border="0" width="20px;" height="20px;" src="resource/zxcss/zximage/return.png" />&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:#ffffff; font-size:14px;position:absolute;">退出</span>&nbsp;&nbsp;&nbsp;&nbsp;</a>
		           <!-- 	         <a class="nui-menubutton" plain="true" menu="#skinMenu" style="color:#ffffff;margin-top:0px;_margin-top:-2px;">&nbsp;选择皮肤</a>
						<ul id="skinMenu" class="nui-menu" style="display:none;">
							<li onclick="onSkinChanged(this,'default')">default</li>
					        <li onclick="onSkinChanged(this,'blue')">blue</li>
				            <li onclick="onSkinChanged(this,'blue2003')">blue2003</li>
				            <li onclick="onSkinChanged(this,'blue2010')">blue2010</li>
					        <li onclick="onSkinChanged(this,'gray')">gray</li>
				            <li onclick="onSkinChanged(this,'olive2003')">olive2003</li>
				            <li onclick="onSkinChanged(this,'bootstrap')">bootstrap</li>
				            <li onclick="onSkinChanged(this,'jqueryui-cupertino')">jqueryui-cupertino</li>
				            <li onclick="onSkinChanged(this,'jqueryui-excitebike')">jqueryui-excitebike</li>
				            <li onclick="onSkinChanged(this,'jqueryui-humanity')">jqueryui-humanity</li>
				            <li onclick="onSkinChanged(this,'jqueryui-uilightness')">jqueryui-uilightness</li>
						</ul>  -->
		        </div>
	        </div>
	       
	    </div>
    	<div showHeader="false" showProxyText="true" title="菜单列表" showSplitIcon="true" region="west" width="200" maxWidth="300" minWidth="100" style="background-image:url(resource/zxcss/zximage/nav_bg.jpg);background-repeat:no-repeat;background-size:100% 100%;background-size:100% 100%;height:100%;width:100%;border-top:0;" >
 			<div id="leftTree" class="nui-outlookmenu " url="<%=root%>/getmenu" onitemclick="menuClick" borderStyle="border:0"  
 				style="width:100%;height:auto;max-height:92%;overflow:auto;" parentField="pid" textField="name" idField="id" dataField="datas" iconField="icon">
        	</div>
        	<div style="position:absolute;bottom:10px;left:0;width:100%;height:35px;background-image: url(resource/images/logo.png);background-repeat:no-repeat;background-size:contain;background-position:center;"></div>
    	</div>
    
	    <div title="center" region="center" style="width:100%;height:100%;overflow:hidden;" bodyStyle="" >
	        <div id="mainTabs" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;overflow:hidden;border-left: 0;" plain="true" 
	        	onactivechanged="onActivechanged" contextMenu="#tabMenu">
				<div title="首页" url="<%=root%>/welcome"></div>
			 </div>
			 <ul id="tabMenu" class="mini-contextmenu"  onbeforeopen="onBeforeOpen">
			 	<li onclick="closeTab"><span style="color:#000000">关闭标签页</span></li>
			 	<li onclick="closeAllBut" ><span style="color:#000000">关闭其他标签页</span></li>
			 	<li onclick="closeAll"><span style="color:#000000">关闭所有标签页</span></li>
			 </ul>
	    </div>
	</div>
	
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var leftTree= $G.get("leftTree");
	findApplyRoleByUserId();
	
	// 菜单点击事件
    function menuClick(e) {
        var item = e.item;
            showTab(item);
    }
    
	//查询是否进行过角色申请
	function findApplyRoleByUserId(){
		$.ajax({
			url: "<%=root%>/findApplyRoleByUserId",
            success: function (text) {
            	if(text.flag){
            		alterUserInfo('1');
            	}
            }
		});
	}
	
	 // 切换机构
    function switchOrg(){
    	mini.open({
    		url : "<%=root%>/goSwitchOrg",
    		title : "切换机构",
    		width : 500,
    		height : 400,
    		showCloseButton: false,
    		onload : function() {
    			var iframe = this.getIFrameEl();
				
    		}
    	});
    }
	
	var currentTab = null; //当前选择的标签页
	
	//右键点击标签页
	function onBeforeOpen(e){
		var tabs = $G.get("mainTabs");
		currentTab = tabs.getTabByEvent(e.htmlEvent);
		if(!currentTab){
			e.cancel = true;
		}
	}
	
	//关闭当前页（非首页）
	function closeTab(){
		var tabs = $G.get("mainTabs");
		if(currentTab != tabs.getTabs()[0]){
			tabs.removeTab(currentTab);
		}
	}
	
	//关闭其他页面（过滤首页）
	function closeAllBut(){
		var tabs = $G.get("mainTabs");
		var tabarr = tabs.getTabs();
		var len = tabarr.length;
		if(tabs){
			for(var len;len>0;len--){
				if(tabarr[len] != undefined && tabarr[len].title != currentTab.title){
					tabs.removeTab(tabarr[len]);
				}
			}
		}
	}
	
	//关闭所有页面（过滤首页）
	function closeAll(){
		var tabs = $G.get("mainTabs");
		var tabarr = tabs.getTabs();
		var len = tabarr.length;
		if(tabs){
			for(var len;len>0;len--){
				if(tabarr[len] != undefined ){
					tabs.removeTab(tabarr[len]);
				}
			}
		}
	}
	
	function showTab(node) {
		
		var tabs = $G.get("mainTabs");
		var tabsNum = tabs['tabs'].length;
		var maxTabsNum = 9;
		var id = "tab$" + node.id;
		var tab = tabs.getTab(id);
		if(tabsNum && tabsNum >= maxTabsNum && !tab) {
			$G.GcdsConfirm("打开菜单超过9个，可能造成系统速度变慢，确定仍然打开？", message.common.prompt,
				function(action) {
					if(action == 'ok') {
						createTab();
					}
				}
			);
		} else {
			createTab();
		}
		totalclick(node);
	      
		function createTab() {
			var tabs = $G.get("mainTabs");
			var id = "tab$" + node.id;
			var tab = tabs.getTab(id);

			var activeTab = tabs.getActiveTab();
			if(id == activeTab.name){
				tabs.reloadTab(tab);
				return;
			}
			
			if (!tab) {
				tab = {};
				tab.name = id;
				tab.title = node.text;
				tab.showCloseButton = true;
				tab.showMenuButton = true;
				//这里拼接了url，实际项目，应该从后台直接获得完整的url地址
				var url = node.url;
		            if(url == null || url == '') {
		            	//getMenuChildren();/**增加左侧树(获取左侧树数据) zgq 2015年2月2日14:47:41 **/
		            	$G.GcdsAlert("未配置菜单URL");
		            	return;
		            } else if(url.indexOf("/") != 0) {
		            	url = "/" + url;
		            }
		            tab.url = "<%= request.getContextPath() %>" + url;
					
		            tabs.addTab(tab);
			}
			tabs.activeTab(tab);
		}
	}
	//菜单点击次数统计
	function totalclick(node){
		var ids=node.id;
		var name=node.text;
		var url=node.url;
		var strurl="<%=root%>/mycombusnet/gettotalclick?ids="+ids+"&name="+name+"&url="+url;
		
		$.ajax({
			url:strurl,
			data:null,
			cache: false,
			success: function (text) {
              
			}
		});
	}

	
	function createChialedTab(tab1) {
		var tabs = $G.get("mainTabs");
		
		var id = "tab$" + tab1.name;
		var tab = tabs.getTab(id);

		var activeTab = tabs.getActiveTab();
		if(id == activeTab.name){
			tabs.reloadTab(tab);
			return;
		}
		if(!tab){
			tab1.name = id;
			tab1["showCloseButton"] = true; 
			tab1["showMenuButton"] = true;
			//这里拼接了url，实际项目，应该从后台直接获得完整的url地址
	        tabs.addTab(tab1);
	        tabs.activeTab(tab1);
		}else{
			tabs.activeTab(tab);
		}
		
	}
	

	
	/*
	 *主题切换
	 */
	function onSkinChanged(e, thisValue){
		var value = thisValue;
		if(value) {
	    	$G.Cookie.set("nuiSkin",value);
		}
		window.location.reload();
	}

	/*
	 *退出
	 */
	function loginOut(){
		var urlStr = "<%=root%>/logout";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(true);
	    ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
	    	if( text.code == "0000"){
	    		window.close();
	    		//window.location.reload();
	    	}else{
	            $G.alert(text.desc);
			}
		});
		$G.postByAjax(null,urlStr,ajaxConf);
	}

	function onActivechanged(e){
		
	}
	
	// 修改密码
    function alterPwd(mmflag){
    	mini.open({
        	url : "<%=root%>/alterPwd?mmflag="+mmflag,
        	title : "修改密码",
        	width : 400,
        	height : 230,
        	showCloseButton: false,
        	onload : function() {
        		var iframe = this.getIFrameEl();
					
				var	data = {
							
				};
        		//关闭
        		iframe.contentWindow.SetData(data);
        	},
        	ondestroy : function(action) {
        		if(action =="cancel1"){
        			loginOut();
        		}
        	}    		
        });
    }
	
 	// 修改个人信息
    function alterUserInfo(isfirst){
    	mini.open({
        	url : "<%=root%>/zxindex/alterUserInfo",
        	title : "信息核对",
        	width : 700,
        	height : 550,
        	//showCloseButton: false,
        	onload : function() {
        		var iframe = this.getIFrameEl();
					
				var	data = {
						isfirst: isfirst		
				};
        		//关闭
        		iframe.contentWindow.SetData(data);
        	},
        	ondestroy : function(action) {
        		if(action =="cancel1"){
        			loginOut()
        		}
        	}    		
        });
    }
 	
	function onMain(e) {
		var tabs = mini.get("mainTabs");
		var tab = tabs.getTab(0);
		tabs.activeTab(tab);
		tabs.reloadTab(tab);
	}
	
 	function alterUserRole(){
 		mini.open({
        	url : "<%=root%>/zxindex/alterUserRole",
        	title : "修改默认角色",
        	width : 500,
        	height : 480,
        	//showCloseButton: false,
        	onload : function() {}, 
        	ondestroy : function(action) {
        		if(action =="ok"){
        			window.location = "<%=root%>/"+"?task=1";
        		}
        	}
        });
 	}
 	
	function onvalueChenaged(e){
		var address = e.selected.adress;
		var style1 = e.selected.style1;
		var key = e.selected.key;
		var menuname = e.selected.menuname;
		var tabs = $G.get("mainTabs");
		var id = "tab$" + key;
		var tab = tabs.getTab(id);

		var activeTab = tabs.getActiveTab();
		if(id == activeTab.name){
			tabs.reloadTab(tab);
			return;
		}
		
		if (!tab) {
			tab = {};
			tab.name = key;
			tab.title = menuname;
			tab.showCloseButton = true;
			tab.showMenuButton = true;
			//这里拼接了url，实际项目，应该从后台直接获得完整的url地址
            tab.url = "<%= request.getContextPath() %>"+address+"?key="+key+"&style="+style1;
            tabs.addTab(tab);
		}
		tabs.activeTab(tab);
		
	}
	
	//菜单查询
	function menuSearch(){
		var combox = $G.get("tb");
		combox.reload();
	}
	
 	
 	
</script>
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
    <title>登记簿目录</title>
	
</head>
<style type="text/css">
    body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
</style>
<body>
	<div  class="mini-toolbar" style="padding: 5px;height:30px;"  id="inner_querypanel">
          <form id="form1">
             <table id="table1">
                <tr>
                   <td >登记簿名称:</td>
		           <td >
		           		<input class="mini-textbox" name="djbmc"></input>
		           </td>
		           <!--<td style="width:90px;">机构:</td>
					<td style="width:300px;" colspan="3"><input  id="orgid" name="orgid" style="width:300px;"
						class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							 textfield="orgname" valuefield="id" parentfield="upid" dataField="datas"  
							 valueFromSelect="false" multiSelect="false"
							expandOnLoad="0" emptyText="机构" allowInput="false"
							showClose="true" oncloseclick="onCloseClick" 
							showRadioButton="true" showFolderCheckBox="false"
							popupWidth="286" popupHeight="470" popupMaxHeight="600" /></td>
              		<td >-->
              		<td >
	                	<a class="mini-button"  onclick="search()">查询</a>
	                	&nbsp;&nbsp;
	                	<a class="mini-button"  onclick="reset()">重置</a>
	                	&nbsp;&nbsp;
	                	<a id="btnOk" class="mini-button"  onclick="add()" >登记</a>
	                	&nbsp;&nbsp;
	                	<a id="btnOk1" class="mini-button"  onclick="add1()" >补登</a>
	                	&nbsp;&nbsp;
	                	<a id="btnOk1" class="mini-button"  onclick="onOk1()" >查看</a>
            		</td>
                </tr>
             </table>
             </form>
            </div>
	 <div class="mini-fit" style="overflow: hidden;">
	      <div id="datagrid1" dataField="data" class="mini-datagrid" style="float: left;width:100%;height:99%;" multiSelect="true"  allowUnselect="false"
		 	url="<%=root%>/djbHandler/getDjbmc"   pageSize="10" showPager="true" autoEscape="false" >
			<div property="columns" >
				<div type="indexcolumn"  width="40" align="center" headerAlign="center">序号 </div>
				<div field="id" visible="false"  align="center"  headerAlign="center" allowSort="true">ID</div>
				 <div field="djbmc"  headerAlign="center"  align="center"  width="200"  allowSort="true" >登记簿名称</div>
				  <div field="org_name" headerAlign="center"   width="150" allowSort="true"  align="center" >
              	创建机构
            	</div>
            	<div field="cjr"  headerAlign="center"  align="center"   width="150" allowSort="true" >创建人</div>
				<div field="cjrq"  headerAlign="center"  align="center"   width="150" allowSort="true" >创建日期</div>
				<div field="useflag"  headerAlign="center"  align="center"   width="150" allowSort="true" renderer="onqtyRenderer">状态</div>
				<div field="sfpz"  headerAlign="center"  align="center"   width="150" allowSort="true" renderer="onsfRenderer">是否需要复核</div>
				
				<div field="startusetime"  headerAlign="center"  align="center"   width="150" allowSort="true" >启用日期</div>
				<div field="endusetime"  headerAlign="center"  align="center"   width="150" allowSort="true" >停用日期</div>
				
				
				 <div field="orgname" headerAlign="center"   width="150" allowSort="true"  align="center" >
              	适用机构
            	</div>
            	 <div field="orgid" headerAlign="center"   width="150" allowSort="true"  align="center" visible="false" >
              
            	</div>
			</div>
		  </div> 
     </div>
     
</body>
<script type="text/javascript">
      mini.parse();
      var form = new mini.Form("form1");
      var grid = mini.get("datagrid1");
      grid.load();

    function CloseWindow(action) {        
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
  //查询
    function search() {
      grid.load(form.getData(false,true));//grid查询
    }

    //重置查询条件
    function reset() {
      form.reset();
    }

    //enter键触发查询
    function onKeyEnter(e) {
      search();
    }
    function onqtyRenderer(e){
            var value = e.value;
            if(value=="0"){
            	return "停用";
            }else{
            	return "启用";
            }
            
        }
     function onsfRenderer(e){
     	 var value = e.value;
            if(value=="0"){
            	return "否";
            }else{
            	return "是";
            }
     }   
    //新增TDjbBhzdy
    function add() {
    	var rows = grid.getSelected();
    	if(rows==null){
    		$G.alert("请选择一行进行添加");
    		return;
    	}
    	if(rows.useflag=="0"){
    		$G.alert("登记簿已停用，不允许新增！");
    		return;
    	}
    	$.ajax({
	            url: "<%=root%>/djbHandler/checkUserOrg",
	            data:{orgid:rows.orgid},
	            cache: false,
	            success: function (text) {
		            if(text.flag == "1"){
		            	var djbid = rows.id;
						var bizData = {pageType : "add",djbid:rows.id};//传入模态窗口页面的json数据
						//传入模态窗口页面的json数据
						mini.open({
	           				 title: "登记",
	            			 url: "<%=root %>/djbHandler/addDetail?djbid="+djbid,
	           				 width: 950,
	           				 height: 600,
	           				 allowResize: false,
	            			 onload: function () {
	                            var iframe = this.getIFrameEl();
	                			var data = bizData;  //模拟传递上传参数
	                			iframe.contentWindow.SetData(data);
	            			 },
	            			 ondestroy: function (action) {
	                			if (action == "save") {
	                    		var iframe = this.getIFrameEl();
	                    		//var data = iframe.contentWindow.GetData();
	                    		//data = mini.clone(data);
	                    		//var json = mini.encode(data);
	                    		//alert("已完成上传数据：\n" + json);
	                    		grid.reload();
	               				 }
	           				 }
	        			})
		            }else{
		            	mini.alert("非适用机构无法进行此操作","提示");
		            }
	            }
	     	});
    	

		//var url = "<%=root %>/djbHandler/addDetail?djbid="+djbid+"&pageType=add";
		    			//$G.showmodaldialog("新增信息模块",url,800,500,bizData,function(action){
		    			//	grid.reload();
		    			//});
	}
     //新增TDjbBhzdy
    function add1() {
    	var rows = grid.getSelected();
    	if(rows==null){
    		$G.alert("请选择一行进行添加");
    		return;
    	}
    	if(rows.useflag=="0"){
    		$G.alert("登记簿已停用，不允许新增！");
    		return;
    	}
    	$.ajax({
	            url: "<%=root%>/djbHandler/checkUserOrg",
	            data:{orgid:rows.orgid},
	            cache: false,
	            success: function (text) {
		            if(text.flag == "1"){
		            	var djbid = rows.id;
						var bizData = {pageType : "add",djbid:rows.id};//传入模态窗口页面的json数据
						//传入模态窗口页面的json数据
						mini.open({
	           				 title: "补登",
	            			 url: "<%=root %>/djbHandler/addDetail?djbid="+djbid+"&bd=1",
	           				 width: 950,
	           				 height: 600,
	           				 allowResize: false,
	            			 onload: function () {
	                            var iframe = this.getIFrameEl();
	                			var data = bizData;  //模拟传递上传参数
	                			iframe.contentWindow.SetData(data);
	            			 },
	            			 ondestroy: function (action) {
	                			if (action == "save") {
	                    		var iframe = this.getIFrameEl();
	                    		//var data = iframe.contentWindow.GetData();
	                    		//data = mini.clone(data);
	                    		//var json = mini.encode(data);
	                    		//alert("已完成上传数据：\n" + json);
	                    		grid.reload();
	               				 }
	           				 }
	        			})
		            }else{
		            	mini.alert("非适用机构无法进行此操作","提示");
		            }
	            }
	     	});
    	

		//var url = "<%=root %>/djbHandler/addDetail?djbid="+djbid+"&pageType=add";
		    			//$G.showmodaldialog("新增信息模块",url,800,500,bizData,function(action){
		    			//	grid.reload();
		    			//});
	}
    //打开按钮
    function onOk() {
    	var rows = grid.getSelected();
    	if(rows!=null){
    		var bizData = {
    		        djbid : rows.id,
    		        djbmc : rows.djbmc
    		};
        	//var djbmc = rows.djbmc;
        	$.ajax({
	            url: "<%=root%>/djbHandler/checkUserPower",
	            data:{djbid:rows.id},
	            cache: false,
	            success: function (text) {
		            if(text.flag == "1"){
		            	var url = "<%=root%>/djbHandler/list?djbid="+rows.id;
		    			$G.showmodaldialog("",url,1024,600,bizData,function(){
		    				grid.reload();
		    			});
		            }else{
		            	mini.alert("你没有申请权限，请联系管理员!","提示");
		            }
	            }
	     	});
        }else{
            mini.alert("请选择一条数据！");
        }
        
    }
    
    //打开按钮
    function onOk1() {
 
    	var rows = grid.getSelected();
    	if(rows!=null){
    	var useflag = rows.useflag;
    	var sfpz = rows.sfpz;
    		var bizData = {
    		        djbid : rows.id,
    		        djbmc : rows.djbmc,
    		        orgid : rows.orgid,
    		        sfpz:rows.sfpz,
    		        useflag:useflag,
    		        sfpz:sfpz
    		};
    		var url = "<%=root%>/djbHandler/list1?djbid="+rows.id+"&orgid="+rows.orgid+"&useflag="+useflag+"&sfpz="+sfpz;
			$G.showmodaldialog("查看",url,1024,600,bizData,function(){
				grid.reload();
			});
        }else{
            mini.alert("请选择一条数据！");
        }
        
    }
    
    //取消  
    function onCancel(e) {
        CloseWindow("new");
    }

</script>
</html>
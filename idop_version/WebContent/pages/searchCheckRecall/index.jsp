<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   		<script src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></script>
   		<script src="<%=request.getContextPath()%>/resource/js/panorama.js" type="text/javascript"></script>
  		<%@include file="/common/nuires.jsp" %>
  		<style type="text/css">
  		.mini-panel-body{
  			padding: 0px;
  		}
  		
  		</style>
	</head> 
	<body>
		<div size="100%" showCollapseButton="false" expanded="true" style="border-left:0px;">
			<div id="panel1" class="nui-toolbar" title="查询查复回复" style="width:100%;" showToolbar="false" showCollapseButton="false"
	    		 showFooter="false" allowResize="false" collapseOnTitleClick="false">
			   	<div style="overflow: auto;">
			   		<form id="form1">
			   		
			   		
						<div  class="search_box" width="100%">

				             
				             <ul>
			                   	<li class="search_li" align="right">业务模块：</li>
						        <li>
							        <input id="check_model" name="check_model" class="nui-combobox" url="<%=root%>/param/getDict?key=dop_ywtype"
							                 onvaluechanged="checkModelchange" textfield="remark" valuefield="val" emptyText="请选择..." style="width:165px;"/>
						        </li>
						     </ul>
						      
		               		 <ul>	
			          			<li class="search_li" align="right">预警名称：</li>
			          			<li>								
			          				<input class="mini-combobox" width="165px"  nullItemText="请选择..."  multiSelect="false" emptyText="请选择..."  url="" 
	           	 				id="check_warningname" name="check_warningname"    valueField="warning_code" textField="warning_name" popupWidth="220px" ></input>
			          			</li>
			          		</ul>

			          		
		               		
			          		<ul>	
			          			<li class="search_li" align="right">查询编号：</li>
								<li align="left">
									<input name="check_flownum" class="nui-textbox"  style="width:165px;"/>
								</li>
		               		</ul>
		               		
			          		<ul>
			                   	<li class="search_li" align="right">查询机构：</li>
								<li align="left">
									<input id="search_org" class="mini-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
										name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  
										valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
										allowInput="false" showClose="true" oncloseclick="onCloseClick" onvaluechanged=""
										showRadioButton="true" showFolderCheckBox="false" popupWidth="305"
										popupHeight="470" popupMaxHeight="600" style="width:165px;"/>
								</li>
							</ul>
							
		               		<ul style="width:500px;">
		               			<li class="search_li" align="right">查询日期：</li>
			          			<li colspan="1">
									<input id="start_time" name="start_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
									<span style="float:left;">至</span>
									<input id="end_time" name="end_time"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="float:left;width:165px;"/>
			          			</li>
			          		</ul>	

			          		
				   		</div>
			   		</form>  
			   		<div class="nui-toolbar" style="float:left;border:0;padding:0px;height: 32px;">
			        	<table style="width:20%;">
			        		<tr>
						        <td style="white-space:nowrap;text-align:center;">
						 			<a class="nui-button" iconCls="" onClick="search()" style="margin-right:50px;">查询</a>
						 			<a class="nui-button" iconCls="" onClick="reset()">重置</a>
						 		</td>
						    </tr>
			        	</table>
			    	</div> 
			   	</div>
			</div>
		</div>
			<div class="nui-fit">
		    	<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/searchCheckRecall/getList" style="width: 100%;height: 100%;" 
		    		multiSelect="false">
			        <div property="columns">   
			            <div field="check_flownum" width="60" headerAlign="center"  align="center" renderer="onRender">操作</div>     
			            <div field="check_model" width="80" allowSort="true" headerAlign="center" align="center" renderer="">业务模块</div>     
			            <div field="check_warningname" width="150" allowSort="true" headerAlign="center" align="center">预警名称</div>  
			            <div field="check_flownum" width="150"  allowSort="true" headerAlign="center" align="center">查询编号</div>  
			            <div field="search_name" width="80" allowSort="true" headerAlign="center" align="center" >查询发起人</div>
			            <div field="search_date" width="80" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">查询时间</div>                
                        <div field="check_enddate" width="80" dateFormat="yyyy-MM-dd" headerAlign="center" allowSort="true"  align="center">查复截止日期</div> 
			            <div field="search_check_status" width="60" allowSort="true" headerAlign="center" align="center" renderer="">查询查复状态</div>  
			            <div field="is_over_due" width="60" allowSort="true" headerAlign="center" align="center"  visible="false" renderer="">是否超时</div>        
			        </div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	$G.parse();
	var grid =$G.get("datagrid1");
	var form = $G.getForm("form1");
    var warning_code="";
     grid.load(form.getData());
	
   function checkModelchange(e){
	   var val=e.value;
	   var url="<%=root%>/searchCheckRecall/getWarningTypeList?val="+val;
		$G.getbyName("check_warningname").setUrl(url);
   }
	function onClose(e) {
		var lookup2 = mini.get(e);
		lookup2.hidePopup();
	}
	
	/**
	 * 清空检索条件时
	 */
	function onClearClick(e) {
		var lookup2 = mini.get(e);
		lookup2.deselectAll();
	}

	 // 机构下拉框清空
    function onCloseClick(e) {
        var obj = e.sender;
        obj.setText("");
        obj.setValue("");
    }

	//查询
	function search(){
		var data = form.getData();
		grid.load(data);
	}
	//重置查询
	function reset(){
		form.reset();
	}
	
	//查复
	function check(check_flownum){
		getWarningCode(check_flownum);
	

	}
	//添加操作链接
	function onRender(e){
		var row = grid.findRow(function(row){
			if(row.check_flownum==e.value){
				return true;
			}
		});
		var op = "";
		//超时行变红色
		if(row.is_over_due==0){
			e.rowStyle="color:red;";
		}

		op = '<a id="link_check" href="javascript:check(\''+e.value+'\')"><font color="blue">查复</font></a>';

		return op;
	}
	
	//获取预警编码
	function getWarningCode(check_flownum){
		var url= "<%=root%>/searchCheckRecall/getWarningCode";
		$.ajax({
			url:url,
			type:'post',
			data:{check_flownum:check_flownum},
			cache:false,
			success:function(text){
				warning_code = mini.decode(text).warning_code;
				var row = grid.findRow(function(row){
					if(row.check_flownum==check_flownum){
						return true;
					}
				})
					var urlstr = "<%=root%>/searchCheckRecall/check";
					var bizParams = {check_flownum:check_flownum,warning_code:warning_code};
					$G.showmodaldialog("查复",urlstr,1000,570,bizParams,function(){
						grid.reload();
					});
			}
		});
	}
</script>


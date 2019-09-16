<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 关键指标数据展现主界面
	*
	* @author 陆磊磊
	* @date 2018-11-19
-->
<head>
	<title>关键指标数据展现主界面</title>
  		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 		<%@include file="/common/nuires.jsp" %>
 		<script type="text/javascript" src="<%=root%>/resource/js/echarts.js"></script>
		<%-- <script type="text/javascript" src="<%=root%>/resource/echarts/jquery.min.js"></script> --%>
 		<script type="text/javascript" src="<%=root%>/resource/echarts/echarts.min.js"></script>
 	
	<style type="text/css">
		html,body {
			margin: 0;
			padding: 0;
			border: 0;
			width: 100%;
			height: 100%;
			overflow: hidden;
		}
	</style>
</head>
<body>
	<div class="mini-panel"  style="width: 100%;height:100%;"style="width:100%;height:200px;" showToolbar="false" showCollapseButton="false"
		showFooter="false" allowResize="false" collapseOnTitleClick="false">
    	<div id="form1" class="mini-form" align="left" style="width: 100%;height:15%;">
            <div  class="search_box" width="100%">
	            
            	<ul>
            		<li class="search_li" align="right">机构名称:</li>
            		<li align="left">
            			<input id="orgSelect2" width="200px" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							 			name="orgid" textfield="orgname" valuefield="orgnum" parentfield="upid"  
							 			valueFromSelect="false" multiSelect="false" expandOnLoad="0"
					 					allowInput="false" showClose="true" oncloseclick="onCloseClick" 
											showRadioButton="true" showFolderCheckBox="false" popupWidth="305" 
											popupHeight="470" popupMaxHeight="600" required="true"/>
            		</li>
            	</ul>
            	
            	<ul>
            		<li class="search_li" align="right">业务模块:</li>
            		<li align="left">
            			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="busi_module" name="busi_module" onvaluechanged="moduleChanged" valueField="val" textField="remark" required="true"></input>
            			
            		</li>
            	</ul>
            	<ul>
            		<li class="search_li" align="right">指标维度:</li>
            		<li align="left">
            			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=ZBWD" 
           	 				id="mark_dimension" name="mark_dimension"  value="0" valueField="val" textField="remark" readonly="true" required="true"></input>
            		</li>
            	</ul>
            	<ul>
            		<li class="search_li" align="right">业务类型:</li>
            		<li align="left">
            			<input name="sub_busi_code" class="nui-treeselect"  
                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
							allowInput="false" showClose="true" oncloseclick="onCloseClick" 
							showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
							popupHeight="350px" popupMaxHeight="600" style="width:200px;" required="true" onvaluechanged="busi_markTypeChanged"/>
            		</li>
            	</ul>
            	<ul>
            		<li class="search_li" align="right">指标名称:</li>
            		<li align="left">
            			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  
           	 				id="mark_code" name="mark_code"    valueField="mark_code" textField="mark_name" required="true" onvaluechanged="mark_codeChanged"></input>
            		</li>
            	</ul>
            	<ul>
            		<li class="search_li" align="right">开始时间:</li>
            		<li align="left">
            			<input class="nui-datepicker" name="startime" id="startime" width="200px" format="yyyyMMdd" required="true"></input>
            		</li>
            	</ul>
            	<ul>
            		<li class="search_li" align="right">结束时间:</li>
            		<li align="left">
            			<input class="nui-datepicker" name="endtime" id="endtime" width="200px" format="yyyyMMdd" required="true"></input>
            		</li>
            	</ul>
      		</div>
        </div>
        <div class="nui-toolbar" style="border-bottom:0;padding:1px;height: 32px;border-top:0;border-left:1;border-right:1;height:25%">
			<table style="width:100%;">
	    		<tr>
	    			<td style="white-space:nowrap;text-align:center;">
						<a class="nui-button"  onClick="search()" style="margin-right:50px;">查询</a>
						<a class="mini-button"  onclick="reset()" style="margin-right:50px;">重置</a>
						<a class="mini-button"  onclick="exportData()" style="margin-right:50px;">导出当前数据</a>
						<a class="mini-button"  onclick="exportDataWd()" style="margin-right:50px;">导出网点数据</a>
						<a id="checkData" class="mini-button"  onclick="checkData()" style="margin-right:50px;">查看一级明细</a>

	    			</td>
	    		</tr>
			</table>
		</div> 
        
        <div id="line" style="width:100%;height:55%">
        	
        </div>
        
        <div id="bar" style="width:100%;height:100%">
        	
        </div>

		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 100%;" sortMode="client" emptyText="没有对应的记录" pageSize="100"
			allowUnselect="false"  multiSelect="false" allowCellSelect="false" allowResize="true" autoEscape="false" showEmptyText="true" showPager="true">
			<div property="columns">
	            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
	            <div field="indexname" headerAlign="center"  visible="false" align="center" >指标编码</div>
	            <div field="begindate" headerAlign="center" width="150" allowSort="false"  renderer="onDateRender" align="center">时间</div>
	            <div field="deptname" headerAlign="center" width="150" allowSort="false"   align="center">机构</div>
	            <div field="module" headerAlign="center" width="100" renderer="onModuleRender"  allowSort="false"  align="center" >业务模块</div>
	            <div field="sub_busi_code" headerAlign="center"  width="100" allowSort="false"  align="center" >业务类型</div>
	            <div field="mark_name" headerAlign="center"   width="100" allowSort="false"  align="center" >指标名称</div>
	            <div field="value" headerAlign="center" width="100"  allowSort="false"  align="center" >指标值</div>
	            <div field="deptno" width="" headerAlign="center"  align="center" renderer="onRender">查看</div>     
			</div>
		</div>
    </div>
	<script type="text/javascript">
	mini.parse();
	
	var form =  mini.getForm("form1");
	var grid=mini.get("datagrid1");
	var barchart=echarts.init(document.getElementById("bar"));
	var linechart=echarts.init(document.getElementById("line"));
	var flagdata = "";
	var org = '${org}';
	$G.get("orgSelect2").setValue(org);
	
	

	$(function(){
		
		barchart.setOption({
			title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltips:{
				trigger:'axis',
				axisPointer:{type:'shadow'}
				},
			legend:{
				data:null
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			       		data:null
			       }
			      ],
			yAxis:[
			       {
			    	  type:'value', 
			    	  data:null
			       }
			       ],
			series:[{
				name:'',
				type:'bar',
				itemStyle:{normal:{color:'#0099cc'}},
				data:null
				}]
		});
		
		
		
		linechart.setOption({
			title:{
				text:'',
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltips:{
				trigger:'axis',
				axisPointer:{type:'shadow'}
				},
			legend:{
				data:null
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	   //boundaryGap:false,
			    	   data:null
			       }
			       
			      ],
				
				
			yAxis:[
			       {
			    	  type:'value', 
			    	  data:null
			       }
			       ],
				
			series:[
			    {
				name:null,
				type:'line',
				itemStyle:{normal:{color:'#0099cc'}},
			    data:null
				}
			]
		});
	});
	
	//业务模块改变
	function moduleChanged(e){
		var val=e.value;
		$G.getbyName("sub_busi_code").setValue("");
		var url="<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+val;
		$G.getbyName("sub_busi_code").setUrl(url);
		
		$G.getbyName("mark_code").setValue("");
		
		$G.getbyName("mark_code").setUrl("");
		
	}
	
	function busi_markTypeChanged(e){
		$G.getbyName("mark_code").setValue("");
		
		var sub_busi_code=$G.getbyName("sub_busi_code").getValue();  
		var mark_dimension=$G.getbyName("mark_dimension").getValue(); 
		if(sub_busi_code!=null&&mark_dimension!=null){
			var url="<%=root%>/quotaDataExhibit/getMarkParamList?sub_busi_code="+sub_busi_code+"&mark_dimension="+mark_dimension;
		    $G.getbyName("mark_code").setUrl(url);
		}
	}
	
	//业务模块中文显示
	function onModuleRender(e){
		var textVal = mini.getDictText("dop_ywtype",e.row.module);
		return textVal;
		
	}
	
	//日期渲染
	function onDateRender(e){
		var textVal = e.row.begindate+"至"+e.row.enddate;
		return textVal;
	}
	
	//查询
	function search(){
		form.validate();
		if (form.isValid() == false){
			return;
		}else{
			$G.mask({
					el:document.body,
					html:'加载中...',
					cls:'mini-mask-loading',
					
				
			})
			//grid.load(form.getData(true,true));
			$.ajax({
				url: "<%=root%>/quotaDataExhibit/getList",
				data:form.getData(true,true),
				cache: false,
				success: function (text) {
					var formd = mini.decode(text).data;
					grid.setData(formd);
					//szzb(grid);
					lineSetOption(mini.decode(text).val_line,mini.decode(text).date_bar,mini.decode(text).deptname_line,mini.decode(text).legend);
					barSetOption(mini.decode(text).val_bar,mini.decode(text).dept_bar,mini.decode(text).legend);
					searchdata();
					$G.unmask();
					
				},
				error:function(u,v,t){
					$G.unmask();
				}
			});
		}
	}
	function searchdata(){
		var indexname = $G.get("mark_code").getValue();
		$.ajax({
			url: "<%=root%>/quotaDataExhibit/getZbDetailList",
			data:{indexname:indexname},
			cache: false,
			success: function (text) {
				var data = mini.decode(text);
				flagdata = data.firstDetailCode;
				if(flagdata==""||flagdata==null||flagdata=="null"){
					grid.hideColumn(8);
				}else{
					grid.showColumn(8);
				}
			},
			error:function(u,v,t){
				$G.unmask();
			}
		})
	}
	
/* 	function szzb(gdata){
		var data=gdata.data;
		var len=data.length;
		if(len>0){
			for(var i=0;i<len;i++){
				var vals=data[i].value;
				if(vals==null){
					$G.getbyName("value").setValue("--");
				}
			}
		}
	} */
	//柱状图设置
	function barSetOption(val,dept,legend){
		var markvalue=val.split(",");
		var deptlist=dept.split(",");
		barchart.setOption({
			title:{
				text:legend,
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltips:{
				trigger:'axis',
				x:"center",
				axisPointer:{type:'shadow'}
				},
			legend:{
				data:[]
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'15%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	   axisLabel:{interval:0,rotate:40},
			       	   data:deptlist
			       }
			      ],
				
				
			yAxis:[
			       {
			    	  type:'value'
			       }
			       ],
			series:[{
				name:'指标',
				type:'bar',
				stack:'总量',
				itemStyle:{
					normal:{
						color:'#0099cc',
						label:{
							show:true,
							position:'top',
							textStyle:{
								color:'#0099cc'
								}
							}
						}
				},
				data:markvalue
				}]
		});
		
	}
	
	//条线图设置
	function lineSetOption(val,date,dept,legend){
		var markvalue=val.split(",");
		var datelist=date.split(",");
		var flag = false;
		for (var item in markvalue){
			if(item != 0){
				flag=true;
				break;
			}
		}
		if(flag == false){
			markvalue =null;
		}
		linechart.setOption({
			title:{
				text:legend,
				textStyle:{color:'#c23531',fontSize:14}
				},
			tooltips:{
				trigger:'axis',
				axisPointer:{type:'cross'}
				},
			legend:{
				data:dept
				},
			grid:{
				left:'3%',
				right:'4%',
				bottom:'3%',
				containLabel:true
				},
			xAxis:[
			       {
			    	   type:'category',
			    	   boundaryGap:false,
			    	   data:datelist
			       }
			       
			      ],
				
				
			yAxis:[
			       {
			    	  type:'value', 
			    	  data:markvalue
			       }
			       ],
				
			series:[
			    {
				name:"",
				type:'line',
				smooth:true,
				itemStyle:{
					normal:{
						color:'#0099cc',
						label:{
							show:true,
							position:'top',
							textStyle:{
							color:'#0099cc'
							}
						}
						
			    	}
			    },
			    data:markvalue
				}
				
				
			]
		});
		
	}
	
		//导出
		function exportData(){
			form.validate();
			if (form.isValid() == false){
				return;
			}else{
				var data=form.getData(true,true);
				var orgid = $G.getbyName("orgid").getValue();
				var busi_module = $G.getbyName("busi_module").getValue(); 
				var sub_busi_code = $G.getbyName("sub_busi_code").getValue();
				var mark_code = $G.getbyName("mark_code").getValue();
				var url = "<%=root%>/quotaDataExhibit/download?orgid="+orgid+"&busi_module="+busi_module+"&sub_busi_code="+sub_busi_code+"&mark_code="+mark_code+"&startime="+data.startime+"&endtime="+data.endtime;
				window.location=url;
			}
			
		}
		//导出网点
		function exportDataWd(){
			form.validate();
			if (form.isValid() == false){
				return;
			}else{
				var data=form.getData(true,true);
				var orgid = $G.getbyName("orgid").getValue();
				var busi_module = $G.getbyName("busi_module").getValue(); 
				var sub_busi_code = $G.getbyName("sub_busi_code").getValue();
				var mark_code = $G.getbyName("mark_code").getValue();
				var url = "<%=root%>/quotaDataExhibit/downloadWd?orgid="+orgid+"&busi_module="+busi_module+"&sub_busi_code="+sub_busi_code+"&mark_code="+mark_code+"&startime="+data.startime+"&endtime="+data.endtime;
				window.location=url;
			}
			
		}
		function mark_codeChanged(e){
			judgecondition();
		}
		

		//判断指标下是否存在一级指标
		function judgecondition(){
		var indexname = $G.get("mark_code").getValue();
		if(indexname==""||indexname==null||indexname=="null"){
			$G.get("checkData").hide();
		}else{
		
		$.ajax({
			url: "<%=root%>/quotaDataExhibit/getZbDetailList",
			data:{indexname:indexname},
			cache: false,
			success: function (text) {
				var data = mini.decode(text);
				flagdata = data.firstDetailCode;
				if(flagdata==""||flagdata==null||flagdata=="null"){
					$G.get("checkData").hide();
				}else{
					$G.get("checkData").show();
				}
			},
			error:function(u,v,t){
				$G.unmask();
			}
		})
		}
	}
		
		//导出网点
		function exportFirst(){
			form.validate();
			if (form.isValid() == false){
				return;
			}else{
				var data=form.getData(true,true);
				var orgid = $G.getbyName("orgid").getValue();
				var busi_module = $G.getbyName("busi_module").getValue(); 
				var sub_busi_code = $G.getbyName("sub_busi_code").getValue();
				var mark_code = $G.getbyName("mark_code").getValue();
				var pageSize = "999999";
				var url = "<%=root%>/quotaDataExhibit/downloadFirst?deptno="+orgid+"&busi_module="+busi_module+"&sub_busi_code="+sub_busi_code+"&indexname="+mark_code+"&starttime="+data.startime+"&endtime="+data.endtime;
				window.location=url;
			}
			
		}
	
	
	
	
	
		//重置
		function reset(){
			form.reset();
		}
		//添加操作链接
		function onRender(e){
			var row = grid.findRow(function(row){
				if(row.deptno==e.value){
					return true;
				}
			});
			var op = '<a id="link_check" href="javascript:view(\''+e.value+'\')"><font color="blue">明细指标</font></a>';
			return op;
		}
		//核查
		function view(deptno){
			var row = grid.findRow(function(row){
				if(row.deptno==deptno){
					return true;
				}
			});
			var indexname = row.indexname;
			var starttime = $G.get("startime").getValue();
			var endtime = $G.get("endtime").getValue();
			var url = "<%=root%>/quotaDataExhibit/view";
			var bizParams = {deptno:deptno,indexname:indexname,starttime:starttime,endtime:endtime};
			$G.showmodaldialog("明细指标",url,800,570,bizParams,function(){
				grid.reload();
			});
		}
		
		//查看一级明细
		function checkData(){
			    var orgid = $G.getbyName("orgid").getValue();//机构号
			  
				var indexname = $G.getbyName("mark_code").getValue();
			    var starttime = $G.get("startime").getValue();
				var endtime = $G.get("endtime").getValue();
				
				var url = "<%=root%>/quotaDataExhibit/checkDetail";
				var bizParams = {pageType:"checkDetail",deptno:orgid,indexname:indexname,starttime:starttime,endtime:endtime};
				$G.showmodaldialog("一级明细详情",url,1000,675,bizParams,function(){
				});
		}
	</script>
</body>
</html>
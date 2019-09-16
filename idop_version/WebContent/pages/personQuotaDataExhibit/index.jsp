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
            			<input  width="200px" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							 		id="orgid"	name="orgid" textfield="orgname" valuefield="orgnum" parentfield="upid"  
							 			valueFromSelect="false" multiSelect="false" expandOnLoad="0"
					 					allowInput="false" showClose="true" oncloseclick="onCloseClick" 
											showRadioButton="true" showFolderCheckBox="false" popupWidth="305" 
											popupHeight="470" popupMaxHeight="600" required="true"/>
            		</li>
            	</ul>
            	<ul>
	                <li class="search_li"  align="right">柜员名称:</li>
	                <li colspan="1"  align="left" >
						<input id="followed_teller"  name="followed_teller" class="mini-lookup"  textField="name"
							valueField="user_no" popupWidth="auto" popup="#gridPanel1" grid="#collarGrid" value="" text="" 
							onvalidation="" onvaluechanged="" />
						<div id="gridPanel1" class="mini-panel" title="header" iconCls="icon-add" style="width: 380px; height: 200px;"
							showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0">
							<div property="toolbar" style="width: 380px; padding: 5px; padding-left: 8px; text-align: center;">
								<div style="float: left; padding-bottom: 2px;">
									<span>员工号或姓名：</span> 
									<input id="keyText" class="mini-textbox" style="width: 120px;" emptyText="请输入" onenter="onSearchClick('collarGrid')"  maxlength="20" vtype="maxLength:20" />
									<a class="mini-button" onclick="onSearchClick('collarGrid')">查询</a>
									<a class="mini-button" onclick="onClearClick('followed_teller')">清除</a>
								</div>
								<div style="float: left; padding-bottom: 2px;"> &nbsp;&nbsp;<a class="mini-button" onclick="onCloseClick('followed_teller')">关闭</a>
								</div>
								<div style="clear: both;"></div>
							</div>
							<div id="collarGrid" class="mini-datagrid" style="width: 380px; height: 100%;" borderStyle="border:0"
								showPageSize="false" showPageIndex="false" showPager="false" sortMode="client" onrowclick="">
								<div property="columns">
									<div type="checkcolumn"></div>
									<div field="org_id" visible="false"></div>
									<div field="name" align="center" headerAlign="center" width="90px;" allowSort="true">用户名</div>
									<div field="user_no" align="center" headerAlign="center" width="50px;" allowSort="true">员工号</div>
									<div field="orgname" align="center" headerAlign="center" width="150px;" allowSort="true">机构名称</div>
								</div>
							</div>
						</div>
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
           	 				id="mark_dimension" name="mark_dimension"  value="1" valueField="val" textField="remark" readonly="true" required="true"></input>
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
						<a class="mini-button"  onclick="exportData()" style="margin-right:50px;">导出人员明细</a>
						<a id="checkData" class="mini-button"  onclick="checkData()" style="margin-right:50px;">查看一级明细</a>
					<!-- 	<a class="mini-button"  onclick="exportDataWd()">导出网点数据</a>  -->
	    			</td>
	    		</tr>
			</table>
		</div> 
        
        <div id="line" style="width:100%;height:55%">
        	
        </div>
        
        <div id="bar" style="width:100%;height:100%">
        	
        </div>
<!-- 
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 100%;" sortMode="client" emptyText="没有对应的记录" pageSize="100"
			allowUnselect="false"  multiSelect="false" allowCellSelect="false" allowResize="true" autoEscape="false" showEmptyText="true" showPager="false">
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
 -->
    </div>
	<script type="text/javascript">
	mini.parse();
	var keyText = mini.get("keyText"); 
	var form =  mini.getForm("form1");
	var grid=mini.get("datagrid1");
	var barchart=echarts.init(document.getElementById("bar"));
	var linechart=echarts.init(document.getElementById("line"));
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
		var url="<%=root%>/personQuotaDataExhibit/getSubbusicodeList?val="+val;
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
	
	function mark_codeChanged(e){
		searchdata();
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
				url: "<%=root%>/personQuotaDataExhibit/getList",
				data:form.getData(true,true),
				cache: false,
				success: function (text) {
					var formd = mini.decode(text).data;
					//grid.setData(formd);
					//szzb(grid);
					lineSetOption(mini.decode(text).val_line,mini.decode(text).val_line2,mini.decode(text).date_bar,mini.decode(text).deptname_line,mini.decode(text).legend);
					barSetOption(mini.decode(text).val_bar,mini.decode(text).dept_bar,mini.decode(text).legend);
					$G.unmask();
				},
				error:function(u,v,t){
					$G.unmask();
				}
			});
		}
	}
	
	
/* 	function szzb(gdata){
		debugger;
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
	function lineSetOption(val,val2,date,dept,legend){
		var markvalue=val.split(",");
		var markvalue2=val2.split(",");
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
		
		if(val2 == ""){
			linechart.clear();
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
					//data:dept
					
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
					name:dept,
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
		}else{
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
					//data:dept
					data:[dept,"当前人员平均值"]
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
					name:dept,
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
					},
					{
						name:"当前人员平均值",
						type:'line',
						smooth:true,
						itemStyle:{
							normal:{
								color:'#FF7F24',
								label:{
									show:true,
									position:'top',
									textStyle:{
									color:'#FF7F24'
									}
								}
								
					    	}
					    },
					    data:markvalue2
						}
					
					
				]
			});
		}

		
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
				var url = "<%=root%>/personQuotaDataExhibit/download?orgid="+orgid+"&busi_module="+busi_module+"&sub_busi_code="+sub_busi_code+"&mark_code="+mark_code+"&startime="+data.startime+"&endtime="+data.endtime;
				window.location=url;
			}
			
		}
		
		//查看一级明细
		function checkData(){
			    var orgid = $G.getbyName("orgid").getValue();//机构号
			  
				var indexname = $G.getbyName("mark_code").getValue();//机构号
			    var followed_teller = $G.getbyName("followed_teller").getValue();//柜员名称
			    var starttime = $G.get("startime").getValue();
				var endtime = $G.get("endtime").getValue();
				
				var url = "<%=root%>/personQuotaDataExhibit/checkDetail";
				var bizParams = {pageType:"checkDetail",followed_teller:followed_teller,deptno:orgid,indexname:indexname,starttime:starttime,endtime:endtime};
				$G.showmodaldialog("一级明细详情",url,1000,675,bizParams,function(){
				});
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
			var url = "<%=root%>/quotaDataExhibit/view";
			var bizParams = {deptno:deptno,indexname:indexname};
			$G.showmodaldialog("明细指标",url,800,570,bizParams,function(){
				grid.reload();
			});
		}
		
		/**
		 * 根据用户ID进行模糊查询
		 */
		function onSearchClick(e) {
			var org = $G.get("orgid").getValue();
			var grid1 = mini.get(e);
			grid1.setUrl("<%=root%>/myfocus/getAllUser?org="+org);
			grid1.load({ key : keyText.value});
		}
		
		//判断指标下是否存在一级指标
		function searchdata(){
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
		function onCloseClick(e) {
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
	</script>
</body>
</html>
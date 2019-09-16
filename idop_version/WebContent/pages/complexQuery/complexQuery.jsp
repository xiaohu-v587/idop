<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 组合查询主界面
	*
	* @author 张强强
	* @date 2018-11-26
-->
	<head>
		<title>组合查询</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
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
		<style type="text/css">
		.mini-grid-headerCell{
			text-align:center;
		}
  		</style>

</head>

<body > 
<form id = "form1" style="border: 1px #f0eff5 solid;width:100%" >
	<table style="width:96%">
            <tr >
              	<td style="width:9%">机构名称：</td>
                <td align="left">
						<input id="orgid"  class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							name="orgid" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="true"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="true"
       						popupWidth="305" popupHeight="250" popupMaxHeight="280" onvaluechanged="" style="width:96%" required="true"/>
					</td>
              </td>
              	<td class="form_label" style="padding-left:15px;width:9%">比较值:</td>
	                	<td align="left">
							<input name="compareValue" id="compareValue" multiSelect="true" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="COMPAREVALUE" value="" style="width:96%"/>
						</td> 
            </td>
           
            <td style="padding-left:15px;width:9%">业务模块:</td>
                <td align="left">
                	<input class="mini-combobox" width="99%"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="ywtype" name="ywtype"   onvaluechanged="moduleChanged" valueField="val" textField="remark"></input>
                </td>
               
        </tr>
        <tr >
        			<td class="form_label" >业务类型:</td>
	                	<td colspan="1">
		                	<input name="sub_busi_code" class="nui-treeselect"  id="sub_busi_code" 
	                       	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
	                       	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
								allowInput="false" showClose="true" oncloseclick="onCloseClick" 
								showRadioButton="true" showFolderCheckBox="false" popupWidth="305px"
								popupHeight="350px" popupMaxHeight="600"  onvaluechanged="loadMarkCode" style="width:96%"/>
	                	</td>
        			 <td class="form_label" style="padding-left:15px">指标维度:</td>
              			<td >
              				<!-- 2018/12/7  因人员维度无数据 暂时关闭人员维度选择  -->
                			<input class="mini-combobox" width="96%"    url="<%=root%>/param/getDict?key=ZBWD" 
           	 				id="mark_dimension" name="mark_dimension"   valueField="val" textField="remark" value="0" enabled="false"></input>
            			</td>
	                	<td class="form_label" style="padding-left:15px">指标名称:</td>
	                	<td colspan="1">
	                		<input class="mini-combobox" width="99%"  nullItemText="请选择..."  multiSelect="true" emptyText="请选择..."  url="" 
	           	 				id="mark_code" name="mark_code"    valueField="mark_code" textField="mark_name" popupWidth="350px" ></input>
	                	</td>
        
        </tr>  
        <tr >
        	<td>预警类型：</td>
        			<td>	
        				<input name="warning_type"  id="warning_type" class="nui-treeselect" url="<%=root%>/warning/getWarningTypeList" 
                     	    dataField="data" valuefield="id" textfield="remark" parentfield="upid" 
                     	    valueFromSelect="false" multiSelect="false"  expandOnLoad="0" emptyText="请选择..."
					allowInput="false" showClose="true" oncloseclick="onCloseClick" 
					showRadioButton="true" showFolderCheckBox="false" popupWidth="200"
					popupHeight="470" popupMaxHeight="600" style="width:96%" onvaluechanged="warnTypechange"/>
				</td>
				<td class="form_label" style="padding-left:15px">预警纬度:</td>
	                	<td align="left">
							<input name="warn_wd" id="warn_wd" class="nui-dictcombobox" textfield="dictName" valuefield="dictID" dictTypeId="warn_wd" value="0" style="width:96%"  enabled="false"/>
						</td>
				<td class="form_label" style="padding-left:15px">预警名称:</td>
           	 		<td colspan="1">
	                		<input class="mini-combobox" width="99%"  nullItemText="请选择..."  multiSelect="true" emptyText="请选择..."  url="" 
	           	 				id="warn_name" name="warn_name"    valueField="warning_code" textField="warning_name" popupWidth="350px" ></input>
	                	</td>
        </tr>
        <tr >
       		<td class="form_label">开始时间:</td>
            <td>
        		<input id="begindate" name="begindate" required="true"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:96%"/>
             </td>
                <td class="form_label" colspan="1" style="padding-left:15px">结束时间:</td>
                	 <td colspan="1">
        		<input id="enddate" name="enddate" required="true"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:96%"/>
             </td>
				<td></td>		
				<td  style="padding-left:15px">
					<a class="mini-button"  onclick="branch_add()">添加</a>
	               	 	&nbsp;&nbsp;
					<a class="mini-button"  onclick="branch_query()">查询</a>
	                    &nbsp;&nbsp;
	               	<a class="mini-button"  onclick="branch_exportData()">导出</a>
				</td>
        </tr>
	 </table>
<!--    <h:hidden property="query_out" id="query_out"/>
 -->  </form>
 	<div id="addhtml" style="height:100px;border-bottom:1px #f0eff5 solid;"></div>
    <div class="mini-fit" >
    	<span id = "info1" style="display:block;font-size:14px;">单机构查询</span>
		<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 97%;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/complexQuery/queryData" multiSelect="true" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true" allowAlternating="true">
			<div property="columns">
	            <!-- <div type="checkcolumn"></div>
	            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div> -->
	            <!-- <div type="indexcolumn"  width="40" headerAlign="center">序号</div> -->
	            <div field="deptno"  name="deptno" headerAlign="center"  allowSort="true"  align="center" >机构名称</div>
	            <div header="指标/预警">·
	            	<div property="columns">
	            		<div field="module_name"  name="module_name" width="120">业务模块</div>
	            		<div field="ywlx" name="ywlx" width="100">业务类型</div>
	            		<div field="indexname" name="indexname" width="100">名称</div>
	            	</div>
	            </div>
	            
	            <!-- <div field="module_name" headerAlign="center" renderer="onYwTypeRender" width="150" allowSort="true"  align="center" >业务模块</div>
	            <div field="ywlx" headerAlign="center"  width="150" allowSort="true"  align="center" >业务类型</div>
	            <div field="indexcode" headerAlign="center"  width="150" allowSort="true"  align="center" >指标预警编码</div>
	            <div field="indexname" headerAlign="center" renderer="onAssTypeRender"   width="150" allowSort="true"  align="center" >指标预警名称</div> -->
	            <div field="flag" headerAlign="center"  width="150" allowSort="true"  align="center"  renderer="onTypeRender">类型</div>
	            <div field="value0" headerAlign="center"  width="150" allowSort="true"  align="center" renderer="onNullRender">值</div>
	            <div field="value1" headerAlign="center"  width="150" allowSort="true"  align="center" renderer="onNullRender">上日比较值</div>
	            <div field="value2" headerAlign="center"  width="150" allowSort="true"  align="center" renderer="onNullRender">上月末比较值</div>
	          	<div field="value3" headerAlign="center"  width="150" allowSort="true"  align="center" renderer="onNullRender">上年末比较值</div>
	          	<div field="value4" headerAlign="center"  width="150" allowSort="true"  align="center" renderer="onNullRender">同比比较值</div>
	          	<!-- <div field="describtion" headerAlign="center"  width="250" allowSort="true"  align="center" >描述</div>
	          	<div field="byzd1" headerAlign="center"  visible="false" allowSort="true"  align="center" >备用字段一</div>
	          	<div field="byzd2" headerAlign="center"  visible="false" allowSort="true"  align="center" >备用字段二</div> -->
          </div>
        </div>
        <span id = "info2" style="display:none;font-size:14px;">多机构查询</span>
        <div id="datagrid2" dataField="data" class="mini-datagrid" style="width: 100%; height: 95%; display:none;" sortMode="client" emptyText="没有对应的记录"
			allowUnselect="false" url="<%=root%>/complexQuery/queryData?type=1" multiSelect="true" allowCellSelect="false" allowResize="true" autoEscape="false"
			  showEmptyText="true" allowAlternating="true" allowHeaderWrap="true" showPager="false">
		
        </div>
      </div>
</body>
<script type="text/javascript">
$G.parse();
var lablejson ={};
var org = '${org}';
if(org != null){
	$G.get("orgid").setValue(org);
}
var grid = $G.get("datagrid1");
var grid1=$G.get("datagrid2");

var lablejsons={};
var zbstr="";
var yjstr="";


function nextNumberFunc(){
	var i= 0;
	return function(){return ++i;};
}
var nextNumber = nextNumberFunc();
//业务模块改变
function moduleChanged(e){
	var val=e.value;
		$G.getbyName("sub_busi_code").setValue("");
		var url="<%=root%>/quotaDataExhibit/getSubbusicodeList?val="+val;
		$G.getbyName("sub_busi_code").setUrl(url);
		$G.getbyName("warning_type").setValue("");
		var url="<%=root%>/warning/getWarningTypeList?val="+e.value;
		$G.getbyName("warning_type").setUrl(url);
}
function branch_add(e){
	var arr =["ywtype","sub_busi_code","mark_code","warning_type","warn_wd",  "warn_name","compareValue"];
	/* var arr1 = ["warning_type","warn_wd",  "warn_name"]; */
	var cs = "";
	if(!lablejsons) lablejsons = {};
	var warn_name = $G.get("warn_name").getValue();
	var mark_code = $G.get("mark_code").getValue();
	
	var warn_code = $G.get("warn_name").getText();
	var mark_name = $G.get("mark_code").getText();
	
	var data = $G.getForm("form1").getData();
	if(warn_name ==""&&mark_code!=""){
		
		for(var i=0;i<arr.length;i++){
			var text = $G.get(arr[i]).getText();
			if(text!=null && text!="" && text!="null" &&cs!=""){
				cs = cs+"-"+text;
				
			}else if(text!=null && text!="" && text!="null" &&cs==""){
				cs +=text;
				
			}
		}
		if(!lablejsons.hasOwnProperty(mark_code)){
			
			lablejsons[mark_code]= cs;
			var idNumber = nextNumber();
			$("#addhtml").append('<div id="'+idNumber+'" style="color:#ffffff;margin-left:10px;float:left;border-radius:20px;padding:0 10px 0 10px;margin-top:6px;background-color:#44afee;">'+mark_name+'<span>&nbsp;<a style="float:right;display:block;color:#dfe4e9;margin-left:6px;text-decoration:none;" href="javascript:delcondition('+idNumber+',\''+mark_code+'\')">x</a></span></div>');
			
		}
	}else if(warn_name !=""&&mark_code==""){
		
		for(var i=0;i<arr.length;i++){
			var text = $G.get(arr[i]).getText();
			if(text!=null && text!="" && text!="null" &&cs!=""){
				cs = cs+"-"+text;
			}else if(text!=null && text!="" && text!="null" &&cs==""){
				cs +=text;
			}
		}

		
		if(!lablejson.hasOwnProperty(warn_name)){
			lablejson[warn_name]= cs;
			var idNumber = nextNumber();
			$("#addhtml").append('<div id="'+idNumber+'" style="color:#ffffff;margin-left:10px;float:left;border-radius:20px;padding:0 10px 0 10px;margin-top:6px;background-color:#44afee;">'+warn_code+'<span>&nbsp;<a style="float:right;display:block;color:#dfe4e9;margin-left:6px;text-decoration:none;" href="javascript:delcondition('+idNumber+',\''+warn_name+'\')">x</a></span></div>');
			
		}
	}else if(warn_name !=""&&mark_code!=""){
		$G.alert("指标或预警只能选择一种");
	}else{
		$G.alert("请选择具体的指标或预警");
	}
}
//机构下拉框清空
function onCloseClick(e) {
    var obj = e.sender;
    obj.setText("");
    obj.setValue("");
    if(e.sender.id=="sub_busi_code"){
    	$G.get("mark_code").setText("");
    	$G.get("mark_code").setValue("");
    };
    if(e.sender.id=="warning_type"){
    	$G.get("warn_name").setText("");
    	$G.get("warn_name").setValue("");
    };
}
function branch_query(e){
	var orgid = $G.get("orgid").getValue();
	var begindate = $G.get("begindate").getFormValue("yyyy-MM-dd");
	var enddate = $G.get("enddate").getFormValue("yyyy-MM-dd");
	var comparevalue = $G.get("compareValue").getValue();
	var arryj = [];	
	for (var str in lablejson){
		arryj =arryj.concat(str); 
	}
	yjstr=arryj.join();
	var arrzb = [];
	for (var str in lablejsons){
		arrzb =arrzb.concat(str); 
	}
	zbstr=arrzb.join();
	if(yjstr==""&&zbstr==""){
		$G.alert("请先添加标签");
		return;
	}
	if(orgid==""||begindate==""||enddate==""){
		$G.alert("机构名称,开始时间和结束时间必填");
		return;
	}
	$G.mask({
			el:document.body,
			html:'加载中...',
			cls:'mini-mask-loading',
			
		
	})
	<%-- $.ajax({
 		url : "<%=root%>/complexQuery/judgeCondition",
 		data:{orgid:orgid},
 		async:false,
 		success : function(text) {
 			var record = $G.decode(text).data;
 			if(record==false){
 				$G.alert("请选择级别一致的机构");
 				$G.get("orgid").setValue("");
 				return;
 			}
 			
 		},
 		error:function(u,v,x){
 		}
 		}) --%>
	var data = {orgid:orgid,begindate:begindate,enddate:enddate,comparevalue:comparevalue,yjstr:yjstr,zbstr:zbstr};
	if(orgid.indexOf(",")!=-1){//此时为多机构
		document.getElementById("datagrid1").style.display='none';
		document.getElementById("datagrid2").style.display='block';//info1
		document.getElementById("info1").style.display='none';
		document.getElementById("info2").style.display='block';
		var grid1 = mini.get("datagrid2");
		
		
		$.ajax({
			url:"<%=root%>/complexQuery/queryData",
			data:data,
			success : function(text) {
	 			grid1.set({columns:text.headers});
	 			//grid1.load();
	 			grid1.setData(text.data);
	 			/* grid1.on("beforeload",function(e){
	 				e.cancel= false;
	 			}); */
				$G.unmask();
	 		},
	 		error:function(u,v,x){
	 			$G.unmask();
	 		}
		})
		  
		//columnControl(comparevalue);
		/* grid1.load(data);
		grid1.on("load",function(){
			grid1.mergeColumns(["module_name","ywlx"]);
			$G.unmask();
		})  */
		
	}else{
		document.getElementById("datagrid1").style.display='block';
		document.getElementById("datagrid2").style.display='none';
		document.getElementById("info1").style.display='block';
		document.getElementById("info2").style.display='none';
		var grid1 = mini.get("datagrid1");
		grid1.load(data);
		columnControl(comparevalue);
		grid1.on("load",function(){
			grid1.mergeColumns(["deptno","module_name","ywlx"]);
			$G.unmask();
		})
		
	}
	
	
}
function columnControl(comparevalue){
	if(comparevalue.indexOf('1')=="-1"){
		$G.get("datagrid1").hideColumn(6);
	}else{
		$G.get("datagrid1").showColumn(6);
	}
	if(comparevalue.indexOf('2')=="-1"){
		$G.get("datagrid1").hideColumn(7);
	}else{
		$G.get("datagrid1").showColumn(7);
		
	}
	if(comparevalue.indexOf('3')=="-1"){
		$G.get("datagrid1").hideColumn(8);
	}else{
		$G.get("datagrid1").showColumn(8);
	}
	if(comparevalue.indexOf('4')=="-1"){
		$G.get("datagrid1").hideColumn(9);
	}else{
		$G.get("datagrid1").showColumn(9);
	}
	
}
function delcondition(id,e){
	$("#"+id).remove();
	if(lablejson.hasOwnProperty(e)){
		delete lablejson[e];
	}else{
		delete lablejsons[e];
	}
	
}
function onTypeRender(e){
	if(e.value=="0"){
		return "指标";
	}else{
		return "预警";
	}
}
function onNullRender(e){
	if(e.value===""||e.value==="null"||e.value===null||e.value==="undefined"||e.value===undefined){
		return "--";
	}else{
		return e.value;
	}
}
function loadMarkCode(e){
	var val=e.value;
	var mark_dimension=$G.get("mark_dimension").getValue();
	var ywtype=$G.get("ywtype").getValue();
	var sub_busi_code=$G.get("sub_busi_code").getValue();
	$G.getbyName("mark_code").setValue("");
	var url="<%=root%>/complexQuery/getMarkParamList?sub_busi_code="+sub_busi_code+"&mark_dimension="+mark_dimension;
	$G.getbyName("mark_code").setUrl(url);
}
function warnTypechange(e){
	var warning_type = $G.get("warning_type").getValue();
	var warn_wd = $G.get("warn_wd").getValue();
	$G.get("warn_name").setValue("");
	var url = "<%=root%>/complexQuery/getWarnNameList?val="+warning_type+"&warn_wd="+warn_wd;
	$G.get("warn_name").setUrl(url);
	
}	
//导出
function branch_exportData(e){
	var orgid = $G.get("orgid").getValue();
	var begindate = $G.get("begindate").getFormValue("yyyy-MM-dd");
	var enddate = $G.get("enddate").getFormValue("yyyy-MM-dd");
	var comparevalue = $G.get("compareValue").getValue();
	var arryj = [];	
	for (var str in lablejson){
		arryj =arryj.concat(str); 
	}
	yjstr=arryj.join();
	var arrzb = [];
	for (var str in lablejsons){
		arrzb =arrzb.concat(str); 
	}
	zbstr=arrzb.join();
	if(yjstr==""&&zbstr==""){
		$G.alert("请先添加标签");
		return;
	}
	if(orgid==""||begindate==""||enddate==""){
		$G.alert("机构名称,开始时间和结束时间必填");
		return;
	}
/* 	if(comparevalue==""){
		$G.alert("请先选择比较值类型");
		return;
	} */

	var url="<%=root%>/complexQuery/exportJg?orgid="+orgid+"&begindate="+begindate+"&enddate="+enddate+"&comparevalue="+comparevalue+"&yjstr="+yjstr+"&zbstr="+zbstr;
	window.location=url;
/* 	var data = {orgid:orgid,begindate:begindate,enddate:enddate,comparevalue:comparevalue,yjstr:yjstr,zbstr:zbstr};
	if(orgid.indexOf(",")!=-1){//此时为多机构
		document.getElementById("datagrid1").style.display='none';
		document.getElementById("datagrid2").style.display='block';//info1
		document.getElementById("info1").style.display='none';
		document.getElementById("info2").style.display='block';
		var grid1 = mini.get("datagrid2");
		
	}else{
		document.getElementById("datagrid1").style.display='block';
		document.getElementById("datagrid2").style.display='none';
		document.getElementById("info1").style.display='block';
		document.getElementById("info2").style.display='none';
		var grid1 = mini.get("datagrid1");

		
	} */
	
}
</script>
</html>




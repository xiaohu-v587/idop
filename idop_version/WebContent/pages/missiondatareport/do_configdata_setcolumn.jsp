<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<body>
    	<div class="nui-fit">
    		<input name="pageType" class="nui-hidden"/>
			<form id="form1" method="post">
	        	<input name="id" class="nui-hidden" />
	        	<input name="col_data_type" class="nui-hidden" />
	        	<input name="col_name" class="nui-hidden" />
	        	<input name="id" class="nui-hidden" />
	       		<div style="padding-left:1px;padding-bottom:5px;">
					<table style="width:100%" align="center">
						<colgroup>
							<col width="5%"/>
					       	<col width="20%"/>
					       	<col width="75%"/>
						</colgroup>
						<tr style="width:100%">
							<td colspan="3">
								<fieldset style="height:1px;border:none;border-top:1px solid #555555;">
								    <legend >格式设置</legend>
								</fieldset>
							</td>
		              	</tr>
		              	<tr style="height:10px;"><td colspan="3"></td></tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td>
		              			列类型：
		              		</td>
							<td style="align:left">
								<input name="col_type" id="col_type" class="nui-combobox"  data="[{id:'dropdown',text:'下拉框'},{id:'text',text:'文本'},{id:'numeric',text:'数字'},{id:'date',text:'日期'}]" value="text" style="width:165px;"
								onvaluechanged="coltypechange"
								/>
							</td>
		              	</tr>
		              	<tr style="width:100%" id="col_binddata_tr">
		              		<td></td>
		              		<td>
		              			设置绑定数据源：
		              		</td>
							<td style="align:left">
								<input id="col_binddata" name="col_binddata" class="nui-combobox" style="width:165px;" textfield="name" valuefield="key" emptyText="字典名称"
								url="<%=root%>/param/getCombox"  allowInput="true" showNullItem="true" nullItemText="请选择..."/>
							</td>
		              	</tr>
		              	<tr style="width:100%" id="col_data_upcol_tr">
		              		<td></td>
		              		<td>
		              			设置关联上级列：
		              		</td>
							<td style="align:left">
								<input name="col_data_upcol" id="col_data_upcol" class="nui-combobox" showNullItem="true" textfield="text" valuefield="id"   value="" style="width:165px;"/>
							</td>
		              	</tr>
		              	<tr style="width:100%">
							<td colspan="3">
								<fieldset style="height:1px;border:none;border-top:1px solid #555555;">
								    <legend>验证设置</legend>
								</fieldset>
							</td>
		              	</tr>
		              	<tr style="height:10px;"><td colspan="3"></td></tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td style="white-space: nowrap;">
		              			<div id="col_v_isnull" name="col_v_isnull" class="nui-checkbox" readOnly="false" text="允许该列填空"  ></div>
		              		</td>
							<td style="align:left">
							</td>
		              	</tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_edit" name="col_is_edit" class="nui-checkbox" readOnly="false" text="设置该列可编辑"  ></div>
							</td>
		              	</tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td>
		              			<div id="col_valid" name="col_valid" class="nui-checkbox"  text="设置列验证格式"  onvaluechanged="colValidChange"></div>
		              		</td>
							<td style="align:left">
								<input name="col_v_type_com" id="col_v_type_com" class="nui-combobox" data="[{id:0,text:'身份证格式'},{id:1,text:'标准金额'},{id:2,text:'手机或固话'}]" 
								value="" showNullItem="true" style="width:165px;" readOnly="true" />
							</td>
		              	</tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_length" name="col_is_length" class="nui-checkbox" readOnly="false" text="设置输入长度限制," onvaluechanged="colValidChange" ></div>
		              			最小
								<input name="col_min_length" id="col_min_length" class="nui-textbox"  maxlength="10" vtype="range:0,24;int" style="width:52px" emptyText="" readOnly="true"/>
		                   		最大	
		                   		<input name="col_max_length" id="col_max_length" class="nui-textbox"  maxlength="10" vtype="range:0,60;int" style="width:52px" emptyText="" readOnly="true"/>
							</td>
		              	</tr>
		              	<tr style="width:100%">
							<td colspan="3">
								<fieldset style="height:1px;border:none;border-top:1px solid #555555;">
								    <legend>数据抽取设置</legend>
								</fieldset>
							</td>
		              	</tr>
		              	<tr style="height:10px;"><td colspan="3"></td></tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_break" name="col_is_break" class="nui-checkbox" readOnly="true" text="该列作为拆分数据列" ></div>
		              			绑定拆分方式
								<input id="col_break_type" name="col_break_type" class="nui-combobox" showNullItem="true"  data="[{id:0,text:'机构号'},{id:1,text:'角色'},{id:1,text:'员工号'}]" value="1" style="width:165px;" readOnly="true"/>
							</td>
		              	</tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_data" name="col_is_data" class="nui-checkbox" readOnly="true" text="该列作为数据抽取列" ></div>
		              			数据来源&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="col_data_sou" name="col_data_sou" class="nui-combobox" showNullItem="true"   data="[{id:0,text:'机构号'},{id:1,text:'角色'},{id:2,text:'员工号'},{id:3,text:'物品大类'},{id:4,text:'物品子类'}]" value="1" style="width:165px;"  readOnly="true"/>
							</td>
		              	</tr>
		              	<tr style="width:100%">
							<td colspan="3">
								<fieldset style="height:1px;border:none;border-top:1px solid #555555;">
								    <legend>数据汇总统计设置</legend>
								</fieldset>
							</td>
		              	</tr>
		              	<tr style="height:10px;"><td colspan="3"></td></tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_keyword" name="col_is_keyword" class="nui-checkbox" readOnly="false" text="该列作为汇总关键字 "  onvaluechanged="colValidChange" ></div>
							</td>
		              	</tr>
		              	<tr style="width:100%">
		              		<td></td>
		              		<td colspan="2">
		              			<div id="col_is_summary" name="col_is_summary" class="nui-checkbox" readOnly="false" text="允许该列进行计算"   onvaluechanged="colValidChange"></div>
		              			&nbsp;&nbsp;&nbsp;&nbsp;统计方式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="col_formart_type" name="col_formart_type" class="nui-combobox" showNullItem="true"  data="[{id:'SUM',text:'SUM'},{id:'MAX',text:'MAX'},{id:'WM_CONCAT',text:'WM_CONCAT'}]" value="" style="width:165px;" readOnly="true"/>
							</td>
		              	</tr>
					</table>
				</div> 
			</form>
		</div>  
		<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
			<a class="nui-button" iconCls="" onclick="save">确定</a> 
			<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
		</div>  
</body>
</html>
 <script type="text/javascript">
 	$G.parse();
	var form = $G.getForm("form1");
	$("#hide_day_tr").hide();
	var orgData;
 	function setData(data){
 		var infos = $G.clone(data);
 		var copyflag = infos.copyflag;
		var pageType=infos.pageType;
		var colheader = infos.colheader;
		var headers = infos.headers;
		var olddata = infos.olddata;
		
		//去除指定元素
		headers.splice(headers.indexOf(colheader),1);
		var col_data_upcol_data = [];
		for(var i=0,len = headers.length;i<len;i++ ){
			col_data_upcol_data.push({id:i,text:headers[i]})
		}
		$G.get("col_data_upcol").setData(col_data_upcol_data);
		$G.getbyName("pageType").setValue(pageType);
		 // console.log($.isEmptyObject(olddata))
		if(!$.isEmptyObject(olddata)){
			//对数据进行转化 checkbox 只识别 true 和 false ，需要对设置时的值进行转义 qxzbzwzm , j
			//找到对应元素名称,判定元素类型
	/* 		.set("col_data_type", "0")		//数据源配置方式  0-字典1-sql2-文本
			.set("col_v_isnull", "0")		//允许该列为空 0-允许，1-不允许
			.set("col_name", "DATA_COL_"+(i+1)) //列字段名称
			.set("col_type", "text") 		//列类型 
			.set("col_valid", "1")			//允许列验证格式 0-允许，1-不允许
			.set("col_v_type", "0")			//数据验证方式 0-字典
			.set("col_is_length", "1")		//允许字段长度限制 0-允许，1-不允许
			.set("col_is_break", "1")		//该列作为拆分列0-允许，1-不允许
			.set("col_is_data", "1")		//该列作为数据抽取列 0-允许，1-不允许
			.set("col_is_keyword", "1")		//是否关键字列 0-允许，1-不允许
			.set("col_is_summary", "1")		//是否数据汇总列（该列进行计算）0-允许，1-不允许
			.set("col_is_edit", "0");		//是否可编辑0-允许，1-不允许 */
			
			var emarray = ["col_v_isnull","col_valid","col_is_length","col_is_break","col_is_data","col_is_keyword","col_is_summary","col_is_edit"]
			for(var i=0,len=emarray.length;i<len;i++){
				olddata[emarray[i]] = olddata[emarray[i]] == '0' ? 'true' :'false';
			}
			form.setData(olddata);
			//$G.get("col_v_isnull").setValue('true');
			//$G.get("col_is_edit").setValue('true');
			//console.log($G.getbyName("col_v_isnull").getValue())
		}else{
			$G.get("col_v_isnull").setValue('true');
			$G.get("col_is_edit").setValue('true');
		}
		if(pageType=="edit"){
			
			
		}else if(pageType=="add"){
			
		}
		coltypechange({value:$G.get("col_type").getValue()})
 	}
	
	//任务类型值发生改变时	
 	function missiontypeChaged(e){
 		if(e.value == "1"){
 			$("#hide_day_tr").hide();
 		}else{
 			$("#hide_day_tr").show();
 		}
 	}
 	
	
	function coltypechange(e){
		if(e.value != 'dropdown'){
			$("#col_binddata_tr").hide();
			$("#col_data_upcol_tr").hide();
			$G.get("col_binddata").hide();
			$G.get("col_data_upcol").hide();
		}else{
			$("#col_binddata_tr").show();
			$("#col_data_upcol_tr").show();
			$G.get("col_binddata").show();
			$G.get("col_data_upcol").show();
		}
	}
	
    function getData(){
    	var formData = form.getData();
		form.validate();
		if (form.isValid() == false) {
			mini.alert("请完善必输项。");
			return;
		}
		//转化数据格式  trueValue 和  falseValue 修改时值不变动，采用此方案转换值
		var emarray = ["col_v_isnull","col_valid","col_is_length","col_is_break","col_is_data","col_is_keyword","col_is_summary","col_is_edit"]
		for(var i=0,len=emarray.length;i<len;i++){
			formData[emarray[i]] = formData[emarray[i]] == 'true' ? '0' :'1';
		}
		return formData;
		
    }
    
    //设置列属性验证规则
    function colValidChange(){
    	//获取列验证格式的值 如果选中，其后面的下拉框才可以选，否则默认只读 
    	var colValid = $G.get("col_valid").getValue();
    	if(colValid == "true"){
    		$G.get("col_v_type_com").setReadOnly(false);
    	} else {
    		$G.get("col_v_type_com").setReadOnly(true);
    	}
    	
    	//获取输入长度的限制  如果选中，其后面的最大最小才可以输入，否则默认只读
    	var colLength = $G.get("col_is_length").getValue();
    	if(colLength == "true"){
    		$G.get("col_min_length").setReadOnly(false);
    		$G.get("col_max_length").setReadOnly(false);
    	} else {
    		$G.get("col_min_length").setReadOnly(true);
    		$G.get("col_max_length").setReadOnly(true);
    	}
    	
    	//获取列数据汇总统计设置 如果选择了关键字，则不能进行计算 反之
    	var colkeyword = $G.get("col_is_keyword").getValue();
    	var colsummary = $G.get("col_is_summary").getValue();
    	if(colkeyword == "true" && colsummary != "true"){
    		$G.get("col_is_summary").setReadOnly(true);
    		$G.get("col_formart_type").setReadOnly(true);
    	}  else {
    		$G.get("col_is_summary").setReadOnly(false);
    		$G.get("col_formart_type").setReadOnly(false);
    	}
    	
    	
    	if(colsummary == "true" && colkeyword != "true"){
    		$G.get("col_is_keyword").setReadOnly(true);
    		$G.get("col_formart_type").setReadOnly(false);
    	} else {
    		$G.get("col_formart_type").setReadOnly(true);
    	}
    }
    
    
    function save(){
    	$G.closemodaldialog("ok");
    }
	
 	function onCancel(e) {
		$G.closemodaldialog("cancel");
    } 
 </script>
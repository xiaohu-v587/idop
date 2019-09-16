<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>新增</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>    
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
        	<input name="id" class="nui-hidden"/>
        	<input name="conjudges" class="nui-hidden" value="0"/><!-- 条件判断值的个数 -->
        	<input name="cons" class="nui-hidden" value="0"/><!-- 条件的个数 -->
        	<input name="ratios" class="nui-hidden" value="0"/><!-- 系数的个数 -->
        	<input name="conendjudges" class="nui-hidden" value="0"/><!-- 总条件判断值的个数 -->
        	<input name="totalSteps" class="nui-hidden" value="0"/><!-- 步数 -->
        	<input name="removeConjudges" class="nui-hidden" value="0"/><!-- 删除的条件判断值的个数 -->
        	<input name="removeCons" class="nui-hidden" value="0"/><!-- 删除的条件的个数 -->
        	<input name="removeRatios" class="nui-hidden" value="0"/><!-- 删除的系数的个数 -->
        	<input name="removeConendjudges" class="nui-hidden" value="0"/><!-- 删除的总条件判断值的个数 -->
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="18%"/>
				       	<col width="15%"/>
				       	<col width="18%"/>
				       	<col width="15%"/>
				       	<col width="19%"/>
				</colgroup>
               	<tr>
                   	<td align="right">数据源：</td>
                   	<td align="left">
                   		<input name="standard_flag" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=DATA_TYPE"
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" onvaluechanged="onDtChanged" required="true"/></td>   
                   	<td align="right">标准：</td>
                    <td align="left">
                    	<input name="data_flag" class="nui-combobox" textfield="name" valuefield="val" url=""
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" required="true"/></td>   
					<td class="addProperty"align="left">
						<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="addCon()" plain="true">增加条件</a>
					</td>
					<td class="addProperty">
               			<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="addConJudge()" plain="true">增加条件判断值</a>
               		</td>
               	</tr>
               	<tr>
               		<td align="right">描述：</td>
               		<td align="left">
               			<input name="standard_remark" class="nui-textbox" maxlength="600"/>
               		</td>
               		<td align="right">二级机构：</td>
               		<td><input name="second_org_num" class="nui-combobox" textfield="orgname" valuefield="orgnum" url="<%=root%>/pccm_standard_info/getSecondOrg"
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" onvaluechanged="onOrgChanged"/></td>
               		<td class="addProperty">
               			 <a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="addRatio()" plain="true">增加系数</a>
               		</td>
					<td class="addProperty" align="left">
						<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="addConEndJudge()" plain="true">增加总条件判断值</a>
					</td>
				</tr>
				<tr>
					<td align="right">三级机构：</td>
               		<td><input name="third_org_num" class="nui-combobox" textfield="orgname" valuefield="orgnum" url=""  
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" /></td>
				</tr>
				<tr>
					<td colspan='6'><hr style="width:775px;"/></td>
				</tr>
				<tr class="addInfo"><td colspan='6'></td></tr>
				<tr><td colspan='6' class="expressionInfo" style="color:red;padding-left:9.4%;"></td></tr>
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="icon-save" onclick="save">确定</a> 
		<a class="nui-button" iconCls="icon-close" onclick="onCancel">关闭</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	var consObj=$G.getbyName("cons");
	var conjudgesObj=$G.getbyName("conjudges");
	var ratiosObj=$G.getbyName("ratios");
	var conendjudgesObj=$G.getbyName("conendjudges");
	function setData(data){
		var infos = $G.clone(data);
		var pageType=infos.pageType;
		$G.getbyName("pageType").setValue(pageType);
		if(pageType=="edit"){
			$(".addProperty").remove();
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
				$(".addInfo").after("<hr style='margin-left:42%;width:775px;'/>");
				$(".expressionInfo").html("得到表达式："+text.msg);
				var data_flag_val=text.record.data_flag;
				var standard_flag=$G.getbyName('standard_flag');
				var data_flag=$G.getbyName('data_flag');
				var flag=standard_flag.getValue();
				data_flag.setValue("");
				var url="<%=root%>/pccm_standard_info/getDataFlag?flag="+flag+"&key=DATA_TYPE&subkey=STANDARD_TYPE";
				data_flag.setUrl(url);
				data_flag.setValue(data_flag_val);
				
				var third_orgnum=text.record.third_org_num;
				var second_org_num=$G.getbyName('second_org_num');
				var third_org_num=$G.getbyName('third_org_num');
				var orgnum=second_org_num.getValue();
				third_org_num.setValue("");
				var url="<%=root%>/pccm_standard_info/getThridOrg?orgnum="+orgnum;
				third_org_num.setUrl(url);
				third_org_num.setValue(third_orgnum);
				
				var arr=text.records;
				for(var i=1;i<=arr.length;i++){
					var obj=arr[i-1];
					if(obj.param_flag==1){
						var cons_val=consObj.getValue();
						if(cons_val==''){
							cons_val=0;
						}
						cons_val=cons_val+1;
						consObj.setValue(cons_val);
						if(i==0){
							$(".addInfo").before("<tr><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+cons_val+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+cons_val+"' value='"+i+"'></td></tr>");
							$G.parse();
						}else{
							if(obj.rel_symbol==null||obj.rel_symbol==''){
								if(obj.symbol==null||obj.symbol==''){
									$(".addInfo").before("<tr><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+cons_val+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+cons_val+"' value='"+i+"'></td></tr>");
									$G.parse();
								}else{
									$(".addInfo").before("<tr><td align='right'>运算比较符：</td><td align='left'><input name='symbolCons"+cons_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+cons_val+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+cons_val+"' value='"+i+"'></td></tr>");
									$G.parse();
									$G.getbyName("symbolCons"+cons_val).setValue(obj.symbol);
								}
							}else{
								$G.parse();
								if(obj.symbol==null||obj.symbol==''){
									$(".addInfo").before("<tr><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+cons_val+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolCons"+cons_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+cons_val+"' value='"+i+"'></td></tr>");
								}else{
									$(".addInfo").before("<tr><td align='right'>运算比较符：</td><td align='left'><input name='symbolCons"+cons_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+cons_val+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolCons"+cons_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+cons_val+"' value='"+i+"'></td></tr>");
									$G.getbyName("symbolCons"+cons_val).setValue(obj.symbol);
								}
								$G.getbyName("rel_symbolCons"+cons_val).setValue(obj.rel_symbol);
							}
						}
						$G.getbyName("condition"+cons_val).setValue(obj.condition);
					}else if(obj.param_flag==2){
						var conjudges_val=conjudgesObj.getValue();
						if(conjudges_val==''){
							 conjudges_val=0;
						}
						conjudges_val=conjudges_val+1;
						conjudgesObj.setValue(conjudges_val);
						if(obj.rel_symbol==null||obj.rel_symbol==''){
							$(".addInfo").before("<tr><td align='right'>比较符：</td><td align='left'><input name='symbolConJudge"+conjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件判断值：</td><td align='left'><input class='nui-textbox' value='"+obj.condition_val+"' name='condition_val"+conjudges_val+"'/><input class='nui-hidden' name='stepConJudge"+conjudges_val+"' value='"+i+"'></td></tr>");							
							$G.parse();
						}else{
							$(".addInfo").before("<tr><td align='right'>比较符：</td><td align='left'><input name='symbolConJudge"+conjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件判断值：</td><td align='left'><input class='nui-textbox' value='"+obj.condition_val+"' name='condition_val"+conjudges_val+"'/></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolConJudge"+conjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepConJudge"+conjudges_val+"' value='"+i+"'></td></tr>");						
							$G.parse();
							$G.getbyName("rel_symbolConJudge"+conjudges_val).setValue(obj.rel_symbol);
						}
						$G.getbyName("symbolConJudge"+conjudges_val).setValue(obj.symbol);
					}else if(obj.param_flag==3){
						var ratios_val=ratiosObj.getValue();
						if(ratios_val==''){
							ratios_val=0;
						}
						ratios_val=ratios_val+1;
						ratiosObj.setValue(ratios_val);
						$(".addInfo").before("<tr><td align='right'>运算符比较符：</td><td align='left'><input name='symbolRatio"+ratios_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>系数名：</td><td align='left'><input class='nui-textbox' value='"+obj.ratio_name+"' name='ratio_name"+ratios_val+"'/></td><td align='right'>系数值：</td><td align='left'><input class='nui-textbox' value='"+obj.ratio_val+"' name='ratio_val"+ratios_val+"'/><input class='nui-hidden' name='stepRatio"+ratios_val+"' value='"+i+"'></td></tr>");
						$G.parse();
						$G.getbyName("symbolRatio"+ratios_val).setValue(obj.symbol);
					}else if(obj.param_flag==4){
						var conendjudges_val=conendjudgesObj.getValue();
						if(conendjudges_val==''){
							conendjudges_val=0;
						}
						conendjudges_val=conendjudges_val+1;
						conendjudgesObj.setValue(conendjudges_val);
						if(obj.rel_symbol==null||obj.rel_symbol==''){
							$(".addInfo").before("<tr><td align='right'>比较符：</td><td align='left'><input name='symbolConEndJudge"+conendjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>总条件判断值：</td><td align='left'><input class='nui-textbox' value='"+obj.condition_end_val+"' name='condition_end_val"+conendjudges_val+"'/><input class='nui-hidden' name='stepEndVal"+conendjudges_val+"' value='"+i+"'></td></tr>");
							$G.parse();
						}else{
							$(".addInfo").before("<tr><td align='right'>比较符：</td><td align='left'><input name='symbolConEndJudge"+conendjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>总条件判断值：</td><td align='left'><input class='nui-textbox' value='"+obj.condition_end_val+"' name='condition_end_val"+conendjudges_val+"'/></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolConEndJudge"+conendjudges_val+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepEndVal"+conendjudges_val+"' value='"+i+"'></td></tr>");
							$G.parse();
							$G.getbyName("rel_symbolConEndJudge"+conendjudges_val).setValue(obj.rel_symbol);
						}
						$G.getbyName("symbolConEndJudge"+conendjudges_val).setValue(obj.symbol);
					}
				}
			});
			$G.postByAjax({id:infos.id},"<%=root%>/pccm_standard_info/getDetail",ajaxConf);
		}
	}
	function onDtChanged(e){
		var standard_flag=$G.getbyName('standard_flag');
		var data_flag=$G.getbyName('data_flag');
		var flag=standard_flag.getValue();
		data_flag.setValue("");
		var url="<%=root%>/pccm_standard_info/getDataFlag?flag="+flag+"&key=DATA_TYPE&subkey=STANDARD_TYPE";
		data_flag.setUrl(url);
		data_flag.select("");
	}
	function onOrgChanged(e){
		var second_org_num=$G.getbyName('second_org_num');
		var third_org_num=$G.getbyName('third_org_num');
		var orgnum=second_org_num.getValue();
		third_org_num.setValue("");
		var url="<%=root%>/pccm_standard_info/getThridOrg?orgnum="+orgnum;
		third_org_num.setUrl(url);
		third_org_num.select("");
		
	}
	function addCon(){
		var standard_flag_val=$G.getbyName('standard_flag').getValue();
		if(standard_flag_val==''){
			$G.alert("请选择数据源");
			return false;
		}
		var data_flag_val=$G.getbyName('data_flag').getValue();
		if(data_flag_val==''){
			$G.alert("请选择标准");
			return false;
		}
		var cons=$G.getbyName("cons").getValue();
		if(cons==''){
			$G.getbyName("cons").setValue(0);
		}
		var num=parseInt($G.getbyName("cons").getValue());
		num=num+1;
		var total=parseInt($G.getbyName("totalSteps").getValue());
		var step=total+1;
		$G.getbyName("totalSteps").setValue(step);
		if(num==1){
			$(".addInfo").before("<tr class='consRow"+num+"'><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+num+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+num+"' value='"+step+"'></td></tr>");
		}else{
			$(".addInfo").before("<tr class='consRow"+num+"'><td align='right'>运算比较符（可选）：</td><td align='left'><input name='symbolCons"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件：</td><td align='left'><input class='nui-combobox' name='condition"+num+"' textfield='remark' valuefield='val' url='<%=root%>/pccm_standard_info/getStandardFields?data_flag_val="+data_flag_val+"&key=STANDARD_TYPE&subkey=STANDARD_TABLE&thirdsubkey=STANDARD_FIELD"+"' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolCons"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepCons"+num+"' value='"+step+"'><a class='mini-button' iconCls='icon-remove' id='removeBtn' onclick='delCons("+num+")' plain='true'></a></td></tr>");
		}
		$G.getbyName("cons").setValue(num);
		$G.parse();
	}
	function delCons(i){
		var cons=$G.getbyName("cons").getValue();
		$(".consRow"+i).remove();
		$G.getbyName("cons").setValue(parseInt(cons)-1);
		var total=parseInt($G.getbyName("totalSteps").getValue());
		$G.getbyName("totalSteps").setValue(total-1);
		var removeCons=parseInt($G.getbyName("removeCons").getValue());
		$G.getbyName("removeCons").setValue(removeCons+1);
	}
	function addConJudge(){
		var cons_val=$G.getbyName("cons").getValue();
		if(cons_val==''||cons_val==0){
			$G.alert("请先添加条件作为依据");
			return false;
		}
		var conjudges=$G.getbyName("conjudges").getValue();
		if(conjudges==''){
			$G.getbyName("conjudges").setValue(0);
		}
		var num=parseInt($G.getbyName("conjudges").getValue());
		num=num+1;
		var total=parseInt($G.getbyName("totalSteps").getValue());
		var step=total+1;
		$G.getbyName("totalSteps").setValue(step);
		$(".addInfo").before("<tr class='conjudgesRow"+num+"'><td align='right'>比较符：</td><td align='left'><input name='symbolConJudge"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>条件判断值：</td><td align='left'><input class='nui-textbox' name='condition_val"+num+"'/></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolConJudge"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepConJudge"+num+"' value='"+step+"'><a class='mini-button' iconCls='icon-remove' id='removeBtn' onclick='delConJudge("+num+")' plain='true'></a></td></tr>");
		$G.getbyName("conjudges").setValue(num);
		$G.parse();
	}
	function delConJudge(i){
		var conjudges=$G.getbyName("conjudges").getValue();
		$(".conjudgesRow"+i).remove();
		$G.getbyName("conjudges").setValue(parseInt(conjudges)-1);
		var total=parseInt($G.getbyName("totalSteps").getValue());
		$G.getbyName("totalSteps").setValue(total-1);
		var removeConjudges=parseInt($G.getbyName("removeConjudges").getValue());
		$G.getbyName("removeConjudges").setValue(removeConjudges+1);
	}
	function addRatio(){
		var cons_val=$G.getbyName("cons").getValue();
		if(cons_val==''||cons_val==0){
			$G.alert("请先添加条件作为依据");
			return false;
		}
		var ratios=$G.getbyName("ratios").getValue();
		if(ratios==''){
			$G.getbyName("ratios").setValue(0);
		}
		var num=parseInt($G.getbyName("ratios").getValue());
		num=num+1;
		var total=parseInt($G.getbyName("totalSteps").getValue());
		var step=total+1;
		$G.getbyName("totalSteps").setValue(step);
		$(".addInfo").before("<tr class='ratiosRow"+num+"'><td align='right'>运算符比较符：</td><td align='left'><input name='symbolRatio"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>系数名：</td><td align='left'><input class='nui-textbox' name='ratio_name"+num+"'/></td><td align='right'>系数值：</td><td align='left'><input class='nui-textbox' name='ratio_val"+num+"'/><input class='nui-hidden' name='stepRatio"+num+"' value='"+step+"'><a class='mini-button' iconCls='icon-remove' id='removeBtn' onclick='delRatios("+num+")' plain='true'></a></td></tr>");
		$G.getbyName("ratios").setValue(num);
		$G.parse();
	}
	function delRatios(i){
		var ratios=$G.getbyName("ratios").getValue();
		$(".ratiosRow"+i).remove();
		$G.getbyName("ratios").setValue(parseInt(ratios)-1);
		var total=parseInt($G.getbyName("totalSteps").getValue());
		$G.getbyName("totalSteps").setValue(total-1);
		var removeRatios=parseInt($G.getbyName("removeRatios").getValue());
		$G.getbyName("removeRatios").setValue(removeRatios+1);
	}
	function addConEndJudge(){
		var cons_val=$G.getbyName("cons").getValue();
		if(cons_val==''||cons_val==0){
			$G.alert("请先添加条件作为依据");
			return false;
		}
		var conendjudges=$G.getbyName("conendjudges").getValue();
		if(conendjudges==''){
			$G.getbyName("conendjudges").setValue(0);
		}
		var num=parseInt($G.getbyName("conendjudges").getValue());
		num=num+1;
		var total=parseInt($G.getbyName("totalSteps").getValue());
		var step=total+1;
		$G.getbyName("totalSteps").setValue(step);
		$(".addInfo").before("<tr class='conendjudgesRow"+num+"'><td align='right'>比较符：</td><td align='left'><input name='symbolConEndJudge"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/pccm_standard_info/getDict?key=MATH_SYMBOL&type=0' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td align='right'>总条件判断值：</td><td align='left'><input class='nui-textbox' name='condition_end_val"+num+"'/></td><td align='right'>关系符（可选）：</td><td align='left'><input name='rel_symbolConEndJudge"+num+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=RELATION_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /><input class='nui-hidden' name='stepEndVal"+num+"' value='"+step+"'><a class='mini-button' iconCls='icon-remove' id='removeBtn' onclick='delConendjudges("+num+")' plain='true'></a></td></tr>");
		$G.getbyName("conendjudges").setValue(num);
		$G.parse();
	}
	function delConendjudges(i){
		var conendjudges=$G.getbyName("conendjudges").getValue();
		$(".conendjudgesRow"+i).remove();
		$G.getbyName("conendjudges").setValue(parseInt(conendjudges)-1);
		var total=parseInt($G.getbyName("totalSteps").getValue());
		$G.getbyName("totalSteps").setValue(total-1);
		var removeConendjudges=parseInt($G.getbyName("removeConendjudges").getValue());
		$G.getbyName("removeConendjudges").setValue(removeConendjudges+1);
	}

	/*
	 *保存数据
	 */
	function save(){
		var cons=$G.getbyName("cons").getValue();
		if(cons==''||cons==0){
			$G.alert("请增加条件");
			return false;
		}
    	var urlStr = "<%=root%>/pccm_standard_info/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag==1){
	    		$G.closemodaldialog("ok");
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}

	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>
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
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="18%"/>
				       	<col width="12%"/>
				       	<col width="12%"/>
				       	<col width="12%"/>
				       	<col width="12%"/>
				       	<col width="20%"/>
				</colgroup>
               	<tr>
                   	<td align="right">标准：</td>
                   	<td align="left">
                   		<input name="standard_flag" class="nui-combobox" textfield="remark" valuefield="name" url="<%=root%>/zxparam/getDict?key=STANDARD_TYPE"
                       		showNullItem="true" nullItemText="请选择" emptyText="请选择" onvaluechanged="onDtChanged" required="true"/></td>   
                      
					<td align="right">二级机构：</td>
               		<td><input name="second_org_num" class="nui-combobox" textfield="orgname" valuefield="orgnum" url="<%=root%>/pccm_standard/getSecondOrg"
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" onvaluechanged="onOrgChanged"/></td>

               	</tr>
               	<tr>
					<td align="right">三级机构：</td>
               		<td><input name="third_org_num" class="nui-combobox" textfield="orgname" valuefield="orgnum" url=""  
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" /></td>
				</tr>
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
				var third_orgnum=text.record.third_org_num;
				var second_org_num=$G.getbyName('second_org_num');
				var third_org_num=$G.getbyName('third_org_num');
				var orgnum=second_org_num.getValue();
				third_org_num.setValue("");
				var url="<%=root%>/pccm_standard/getThridOrg?orgnum="+orgnum;
				third_org_num.setUrl(url);
				third_org_num.setValue(third_orgnum);
				var arr=text.records;
				var standard_flag=text.record.standard_flag;
				var dw="(单位:万元)";
				for(var i=0;i<arr.length;i++){
					var obj=arr[i];
					var title_name="";
					if(obj.condition_flag=='tran_count'){
						title_name="交易笔数";
					}else if(obj.condition_flag=='tran_amount'){
						title_name="交易金额"+dw;
					}else if(obj.condition_flag=='deposit_financial'){
						title_name="日均存款及理财合计"+dw;
					}else if(obj.condition_flag=='second_weekday'){
						title_name="工作日";
					}else if(obj.condition_flag=='third_weekday'){
						title_name="工作日";
					}else if(obj.condition_flag=='personal_deposit'){
						title_name="个人日均存款"+dw;
					}else if(obj.condition_flag=='borrow_income'){
						title_name="借款人月收入"+dw;
					}else if(obj.condition_flag=='family_income'){
						title_name="家庭月收入"+dw;
					}else if(obj.condition_flag=='deposit_financial_potential'){
						title_name="日均存款及理财合计"+dw;
					}else if(obj.condition_flag=='deposit_financial_critical'){
						title_name="日均存款及理财合计"+dw;
					}else if(obj.condition_flag=='deposit_financial_absolute'){
						title_name="日均存款及理财合计"+dw;
					}else if(obj.condition_flag=='pa_loan'){
						title_name="PA贷款年日均"+dw;
					}else if(obj.condition_flag=='pa_deposit'){
						title_name="PA存款年日均"+dw;
					}else if(obj.condition_flag=='group_cust'){
						title_name="集团客户中成员";
					}else if(obj.condition_flag=='cust_income'){
						title_name="客户年收益"+dw;
					}else if(obj.condition_flag=='isworth'){
						title_name="价值客户";
					}
					var val2=obj.condition_val2;
					if(val2==null||val2==''||val2=='null'){
						val2="";
					}
					var standard_remark=obj.standard_remark;
					if(standard_remark==null||standard_remark==''||standard_remark=='null'){
						standard_remark="";
					}
					if(standard_flag=='code1'||standard_flag=='code3'||standard_flag=='code4'||standard_flag=='code5'||standard_flag=='code6'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"'  maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}else if(standard_flag=='code2'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>五层分类：<input name='result_value_"+i+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=CLAS_FIVE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"'  maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("result_value_"+i).setValue(obj.result_value);
					}else if(standard_flag=='code7'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td align='left'><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>分层系数：<input class='nui-textbox' name='result_value_"+i+"' value='"+obj.result_value+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}else if(standard_flag=='code8-potential'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>潜在值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-potential&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("result_value_"+i).setValue(obj.result_value);
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}else if(standard_flag=='code8-critical'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>临界值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-critical&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("result_value_"+i).setValue(obj.result_value);
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}else if(standard_flag=='code8-absolute'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' value='"+val2+"' vtype='float'/></td><td>绝对值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-absolute&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("result_value_"+i).setValue(obj.result_value);
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}else if(standard_flag='code9'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>非价值客户：<input class='nui-hidden' value='isworth' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input name='condition_val1_"+i+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=CLAS_FIVE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("condition_val1_"+i).setValue(obj.condition_val1);
					}else if(standard_flag=='major_company_kpi'||standard_flag=='minor_enterprise_kpi'||standard_flag=='financial_institution_kpi'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>"+title_name+"：<input class='nui-hidden' name='condition_flag_"+i+"' value='"+obj.condition_flag+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td>"
								+"<td><input class='nui-textbox' name='condition_val1_"+i+"' value='"+obj.condition_val1+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' readonly='true'/></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float' value='"+val2+"'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' value='"+obj.result_value+"' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' value='"+standard_remark+"' maxlength='150'/></td></tr>");
						$G.parse();
						$G.getbyName("result_value_"+i).setValue(obj.result_value);
						$G.getbyName("symbol2_"+i).setValue(obj.symbol2);
					}
					$G.parse();
					$G.getbyName("symbol1_"+i).setValue(obj.symbol1);
				
				}
				$("#detailTable").append("<tr class='"+standard_flag+"'><td><input class='nui-hidden' value='"+arr.length+"' name='total'></td></tr>");
				$G.parse();
			});
			$G.postByAjax({id:infos.id},"<%=root%>/pccm_standard/getDetail",ajaxConf);
		}
	}
	function onDtChanged(e){
		var standard_flag=$G.getbyName('standard_flag').getValue();
		var dw="(单位:万元)";
		if(standard_flag=='code1'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>交易笔数：<input class='nui-hidden' value='tran_count' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择'/></td>"
					+"<td><input class='nui-textbox' name='condition_val1_0' vtype='float' required='true'/></td><td><input name='symbol2_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_0' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td align='right'>交易金额"+dw+"：<input class='nui-hidden' value='tran_amount' name='condition_flag_1'></td><td><input name='symbol1_1' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_1' vtype='float' required='true'/></td><td><input name='symbol2_1' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_1' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_1' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td><input class='nui-hidden' value='2' name='total'></td></tr>");
		}else if(standard_flag=='code2'){
			$(".addRow").remove();
			for(var i=0;i<5;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>日均存款及理财合计"+dw+"：<input class='nui-hidden' value='deposit_financial' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float'/></td><td>五层分类：<input name='result_value_"+i+"' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=CLAS_FIVE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='5' name='total'></td></tr>");
		}else if(standard_flag=='code3'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>工作日：<input class='nui-hidden' value='second_weekday' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_0' vtype='float' required='true'/></td><td><input name='symbol2_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_0' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td><input class='nui-hidden' value='1' name='total'></td></tr>");
		}else if(standard_flag=='code4'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>工作日：<input class='nui-hidden' value='third_weekday' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_0' vtype='float' required='true'/></td><td><input name='symbol2_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_0' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td><input class='nui-hidden' value='1' name='total'></td></tr>");
		}else if(standard_flag=='code5'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>个人日均存款"+dw+"：<input class='nui-hidden' value='personal_deposit' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_0' vtype='float' required='true'/></td><td><input name='symbol2_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_0' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td><input class='nui-hidden' value='1' name='total'></td></tr>");
		}else if(standard_flag=='code6'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>借款人月收入"+dw+"：<input class='nui-hidden' value='borrow_income' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_0' vtype='float'required='true'/></td><td><input name='symbol2_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_0' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
							+"<tr class='addRow'><td align='right'>家庭月收入"+dw+"：<input class='nui-hidden' value='family_income' name='condition_flag_1'/></td><td><input name='symbol1_1' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
							+"<td><input class='nui-textbox' name='condition_val1_1' vtype='float' required='true'/></td><td><input name='symbol2_1' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_1' vtype='float'/></td><td>描述：<input class='nui-textbox' name='standard_remark_1' maxlength='150'/></td></tr>"
							+"<tr class='addRow'><td><input class='nui-hidden' value='2' name='total'></td></tr>");
		}else if(standard_flag=='code7'){
			$(".addRow").remove();
			for(var i=0;i<5;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>日均存款及理财合计"+dw+"：<input class='nui-hidden' value='deposit_financial' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td align='left'><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float'/></td><td>分层系数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='5' name='total'></td></tr>");
		}else if(standard_flag=='code8-potential'){
			$(".addRow").remove();
			for(var i=0;i<5;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>日均存款及理财合计"+dw+"：<input class='nui-hidden' value='deposit_financial_potential' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float'/></td><td>潜在值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-potential&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='5' name='total'></td></tr>");
		}else if(standard_flag=='code8-critical'){
			$(".addRow").remove();
			for(var i=0;i<5;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>日均存款及理财合计"+dw+"：<input class='nui-hidden' value='deposit_financial_critical' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float'/></td><td>临界值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-critical&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='5' name='total'></td></tr>");
		}else if(standard_flag=='code8-absolute'){
			$(".addRow").remove();
			for(var i=0;i<5;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>日均存款及理财合计"+dw+"：<input class='nui-hidden' value='deposit_financial_absolute' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='float'/></td><td>绝对值：<input name='result_value_"+i+"' class='nui-combobox' textfield='remark' valuefield='name' url='<%=root%>/zxparam/getDictByCode?key=CTYPE&code=code8-absolute&parentKey=STANDARD_TYPE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='5' name='total'></td></tr>");
		}else if(standard_flag=='code9'){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>非价值客户：<input class='nui-hidden' value='isworth' name='condition_flag_0'/></td><td><input name='symbol1_0' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input name='condition_val1_0' class='nui-combobox' textfield='name' valuefield='val' url='<%=root%>/zxparam/getDict?key=CLAS_FIVE' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_0' maxlength='150'/></td></tr>"
					+"<tr class='addRow'><td><input class='nui-hidden' value='1' name='total'></td></tr>");
		}else if(standard_flag=='major_company_kpi'){
			$(".addRow").remove();
			for(var i=0;i<4;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>PA贷款年日均"+dw+"：<input class='nui-hidden' value='pa_loan' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			for(var i=4;i<8;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>PA存款年日均"+dw+"：<input class='nui-hidden' value='pa_deposit' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			for(var i=8;i<13;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>集团客户中成员：<input class='nui-hidden' value='group_cust' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='13' name='total'></td></tr>");
		}else if(standard_flag=='minor_enterprise_kpi'){
			$(".addRow").remove();
			for(var i=0;i<4;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>PA贷款年日均"+dw+"：<input class='nui-hidden' value='pa_loan' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			for(var i=4;i<8;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>PA存款年日均"+dw+"：<input class='nui-hidden' value='pa_deposit' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
						+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='8' name='total'></td></tr>");
		}else if(standard_flag=='financial_institution_kpi'){
			$(".addRow").remove();
			for(var i=0;i<3;i++){
				$("#detailTable").append("<tr class='addRow'><td align='right'>客户年收益"+dw+"：<input class='nui-hidden' value='cust_income' name='condition_flag_"+i+"'/></td><td><input name='symbol1_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' required='true' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td>"
					+"<td><input class='nui-textbox' name='condition_val1_"+i+"' vtype='float' required='true'/></td><td><input name='symbol2_"+i+"' class='nui-combobox' textfield='name' valuefield='name' url='<%=root%>/zxparam/getDict?key=MATH_SYMBOL' allowInput='true' showNullItem='true' nullItemText='请选择' emptyText='请选择' /></td><td><input class='nui-textbox' name='condition_val2_"+i+"' vtype='int'/></td><td>客户数：<input class='nui-textbox' name='result_value_"+i+"' vtype='float' required='true'/></td><td>描述：<input class='nui-textbox' name='standard_remark_"+i+"' maxlength='150'/></td></tr>");
			}
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' value='3' name='total'></td></tr>");
		}
		$G.parse();
	}
	function onOrgChanged(e){
		var second_org_num=$G.getbyName('second_org_num');
		var third_org_num=$G.getbyName('third_org_num');
		var orgnum=second_org_num.getValue();
		third_org_num.setValue("");
		var url="<%=root%>/pccm_standard/getThridOrg?orgnum="+orgnum;
		third_org_num.setUrl(url);
		third_org_num.select("");
		
	}
	

	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/pccm_standard/save";
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
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
<!--         	<input name="ss" class="nui-hidden"/> -->
<!--         	<input name="removeKpi" class="nui-hidden"/> -->
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="22%"/>
				       	<col width="28%"/>
				       	<col width="11%"/>
				       	<col width="39%"/>
				</colgroup>
               	<tr>
                   	<td align="right">客户经理类型：</td>
                   	<td align="left">    
                     	<input name="khjl_type" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=KHJL_TYPE" required="true"  
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择" onvaluechanged="onKhChanged"/>
                   	</td>
                   	<td align="right">职级：</td>
                    <td align="left">    
                       	<input name="zj" class="nui-combobox" textfield="name" valuefield="val" url="<%=root%>/zxparam/getDict?key=ZJ" required="true"  
                       		allowInput="true" showNullItem="true" nullItemText="请选择" emptyText="请选择"/>
                   	</td>
               	</tr>
<!--                	<tr class="zb_first"> -->
<!--                    	<td align="right">指标名称：</td> -->
<!--                    	<td align="left">     -->
<!-- 						  	<input name="zbmcs1" class="nui-textbox" required="true"/> -->
<!-- 					</td> -->
<!--                     <td align="right">指标值：</td> -->
<!-- 					<td align="left">     -->
<!--                        	  	<input name="vals1" class="nui-textbox" required="true"/>	<a class="mini-button" iconCls="icon-addnew" id="addBtn" onclick="addKpi()" plain="true" style="width:19px;"></a> -->
<!--                    	</td> -->
<!-- 				</tr> -->
<!--                	<tr id="zz"> -->
<!--                		<td align="right">公式：</td> -->
<!--                		<td align="left"> -->
<!--                			<textarea class="mini-textarea" name="gs" emptyText="请输入公式" style="width:373px;height:130px;" required="true"></textarea> -->
<!--                		</td> -->
<!--                	</tr> -->
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
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.record);
				var arr=text.records;
				for(var i=0;i<arr.length;i++){
					var obj=arr[i];
					if(obj.zb_flag='busine_income'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag"+i+"' class='nui-hidden' value='"+obj.zb_flag+"'/><input name='zbmcs"+i+"' class='nui-textbox' required='true' readonly='true' value='"+obj.zbmc_name+"'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+i+"' class='nui-textbox' required='true' value='"+obj.zbmc_val+"' vtype='float'/></td></tr>");
					}else if(obj.zb_flag='cust_num'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag"+i+"' class='nui-hidden' value='"+obj.zb_flag+"'/><input name='zbmcs"+i+"' class='nui-textbox' required='true' readonly='true' value='"+obj.zbmc_name+"'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+i+"' class='nui-textbox' required='true' value='"+obj.zbmc_val+"' vtype='float'/></td></tr>");
					}else if(obj.zb_flag='cust_effect'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag"+i+"' class='nui-hidden' value='"+obj.zb_flag+"'/><input name='zbmcs"+i+"' class='nui-textbox' required='true' readonly='true' value='"+obj.zbmc_name+"'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+i+"' class='nui-textbox' required='true' value='"+obj.zbmc_val+"' vtype='float'/></td></tr>");
					}else if(obj.zb_flag='pro_effect'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag"+i+"' class='nui-hidden' value='"+obj.zb_flag+"'/><input name='zbmcs1' class='nui-textbox' required='true' readonly='true' value='"+obj.zbmc_name+"'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+i+"' class='nui-textbox' required='true' value='"+obj.zbmc_val+"' vtype='float'/></td></tr>");
					}else if(obj.zb_flag='deposite'){
						$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag"+i+"' class='nui-hidden' value='"+obj.zb_flag+"'/><input name='zbmcs1' class='nui-textbox' required='true' readonly='true' value='"+obj.zbmc_name+"'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+i+"' class='nui-textbox' required='true' value='"+obj.zbmc_val+"' vtype='float'/></td></tr>");
					}
				}
				$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='"+arr.length+"'></td></tr>");
				$G.parse();
			});
			$G.postByAjax({id:infos.id},"<%=root%>/pccm_kpi_param/getDetail",ajaxConf);
		}
	}
	function onKhChanged(e){
		if(e.value==1){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag0' class='nui-hidden' value='busine_income'/><input name='zbmcs0' class='nui-textbox' required='true' value='营业收入' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals0' class='nui-textbox' required='true' vtype='float'/></td></tr>")
			.append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag1' class='nui-hidden' value='cust_num'/><input name='zbmcs1' class='nui-textbox' required='true' value='客户数' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals1' class='nui-textbox' required='true' vtype='float'/></td></tr>");
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='2'></td></tr>");
		}else if(e.value==2){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag0' class='nui-hidden' value='busine_income'/><input name='zbmcs0' class='nui-textbox' required='true' value='营业收入' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals0' class='nui-textbox' required='true' vtype='float'/></td></tr>")
			.append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag1' class='nui-hidden' value='cust_num'/><input name='zbmcs1' class='nui-textbox' required='true' value='客户数' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals1' class='nui-textbox' required='true' vtype='float'/></td></tr>");
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='2'></td></tr>");
		}else if(e.value==3){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag0' class='nui-hidden' value='cust_effect'/><input name='zbmcs0' class='nui-textbox' required='true' value='客户效能' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals0' class='nui-textbox' required='true' vtype='float'/></td></tr>")
			.append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag1' class='nui-hidden' value='pro_effect'/><input name='zbmcs1' class='nui-textbox' required='true' value='产品效能' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals1' class='nui-textbox' required='true' vtype='float'/></td></tr>");
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='2'></td></tr>");
		}else if(e.value==4){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag0' class='nui-hidden' value='busine_income'/><input name='zbmcs0' class='nui-textbox' required='true' value='营业收入' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals0' class='nui-textbox' required='true' vtype='float'/></td></tr>")
			.append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag1' class='nui-hidden' value='cust_num'/><input name='zbmcs1' class='nui-textbox' required='true' value='客户数' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals1' class='nui-textbox' required='true' vtype='float'/></td></tr>");
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='2'></td></tr>");
		}else if(e.value==5){
			$(".addRow").remove();
			$("#detailTable").append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag0' class='nui-hidden' value='cust_num'/><input name='zbmcs0' class='nui-textbox' required='true' value='客户数' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals0' class='nui-textbox' required='true' vtype='float'/></td></tr>")
			.append("<tr class='addRow'><td align='right'>指标名称：</td><td align='left'><input name='zb_flag1' class='nui-hidden' value='deposite'/><input name='zbmcs1' class='nui-textbox' required='true' value='客户日均存款' readonly='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals1' class='nui-textbox' required='true' vtype='float'/></td></tr>");
			$("#detailTable").append("<tr class='addRow'><td><input class='nui-hidden' name='total' value='2'></td></tr>");
		}
		$G.parse();
	}
// 	function delKpi(i){
// 		var ss=	$G.getbyName("ss").getValue();
// 		$(".addRow"+i).remove();
// 		$G.getbyName("ss").setValue(parseInt(ss)-1);
// 		var removeKpis=$G.getbyName("removeKpi").getValue();
// 		if(removeKpis==''){
// 			$G.getbyName("removeKpi").setValue(i);
// 		}else{
// 			$G.getbyName("removeKpi").setValue(removeKpis+","+i);
// 		}
		
// 	}
// 	function addKpi(){
// 		var ss=$G.getbyName("ss").getValue();
// 		if(ss==''){
// 			$G.getbyName("ss").setValue(1);
// 		}
// 		var num=parseInt($G.getbyName("ss").getValue());
// 		num=num+1;
// 		$("#zz").before("<tr class='addRow"+num+"'><td align='right'>指标名称：</td><td align='left'><input name='zbmcs"+num+"' class='nui-textbox' required='true'/></td><td align='right'>指标值：</td><td align='left'><input name='vals"+num+"' class='nui-textbox' required='true'/><a class='mini-button' iconCls='icon-addnew' id='addBtn' onclick='addKpi()' plain='true' style='width:20px;'></a><a class='mini-button' iconCls='icon-remove' id='removeBtn' onclick='delKpi("+num+")' plain='true'></a></td></tr>");
// 		$G.getbyName("ss").setValue(num);
// 		$G.parse();
// 	}

	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/pccm_kpi_param/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(true);
	    ajaxConf.setSuccessFunc(function (text){
	    	if(text.flag==1){
	    		$G.closemodaldialog("ok");
	    	}else{
	    		ajaxConf.setIsShowSuccMsg(false);
	    		$G.alert(text.msg);
	    	}
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}

	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
</script>
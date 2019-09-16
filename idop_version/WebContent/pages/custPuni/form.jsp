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
	<div class="mini-fit">
		<input name="pageType" class="nui-hidden"/>      
		<form id="form1" method="post">
        	<input name="id" class="nui-hidden"/>
        	<input id ="cust_type" name="cust_type" class="nui-hidden"/>
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
				<tr>
                   	<td align="right">机构：</td>
                   	<td align="left">
						<input id="org_id" required="true" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
       						popupWidth="205" popupHeight="150" popupMaxHeight="180" onvaluechanged="onOrgChanged" />
					</td>
                    <td align="right">客户经理：</td>
					<td align="left">
                       	<input id="cust_id" name="cust_id" class="nui-combobox" popupHeight="150" popupMaxHeight="180" textfield="name" valuefield="id" required="true" onvaluechanged="onCustChanged"/>
                   		<input id="cust_name" name="cust_name" class="nui-hidden" />
                   	</td>
				</tr>
				<tr>
                   	<td align="right">风险类型：</td>
					<td align="left">
						<input id="punish_type" class="nui-combobox"  name="punish_type" allowInput="false" 
						showClose="true" oncloseclick="onCloseClick" textfield="ptext" required="true" valuefield="pid"
						data="[{'pid':'0','ptext':'经济处理'},{'pid':'1','ptext':'纪律处分'}]" onvaluechanged="onTypeChanged"/>
					</td>
                   	<td align="right">扣分：</td>
					<td align="left">
						<input id="deduction" name="deduction" class="nui-textbox" allowInput="false" />
					</td>
               	</tr>
               	<tr>
                   	<td align="right">备注：</td>
					<td align="left" colspan="3">
						<input id="remark" name="remark" class="nui-textarea" width="374px"  height="65px" />
					</td>
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
	var bigFina = "${bigFina}";
	var bigPuni = "${bigPuni}";
	var midFina = "${midFina}";
	var midPuni = "${midPuni}";
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
				$G.get("org_id").disable();
	            $G.get("cust_id").disable();
				onOrgChanged();
				onCustChanged();
				
				$G.get("punish_type").setValue(text.record.punish_type);
				$G.get("deduction").setValue(text.record.deduction);
				//
			});
			$G.postByAjax({id:infos.id},"<%=root%>/custPuni/getDetail",ajaxConf);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/custPuni/save";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setIsShowProcessBar(false);
		ajaxConf.setIsShowSuccMsg(false);
	    ajaxConf.setSuccessFunc(function (text){
    		$G.alert("操作成功");
    		$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
	
	function onOrgChanged(){
		var org_id = $G.get("org_id").getValue();
		if(org_id){
			var url="<%=root%>/custPuni/getListByOrgId?org_id="+org_id;
			$G.get("cust_id").setUrl(url);
		}
		reset();
	}
	
	function onTypeChanged(){
		
		var punish_type = $G.get("punish_type").getValue();
		var cust_type = $G.get("cust_type").getValue();
		if($G.get("cust_id").getValue()){
			if(punish_type&&cust_type){
				setDeDuct(punish_type,cust_type);
			}
		}else{
			$G.alert("请选择客户经理");
		}
		
	}
	
	function onCustChanged(){
		$G.get("cust_name").setValue($G.get("cust_id").getText());
		if($G.get("cust_name").getValue()){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (data){
		    	if(data&&data.type){
		    		$G.get("cust_type").setValue(data.type);
		    	}
			});
			$G.postByAjax({id:$G.get("cust_id").getValue()},"<%=root%>/custPuni/getMgrType",ajaxConf);
		}
		reset();
		//onTypeChanged();
	}
	
	function setDeDuct(punish_type,cust_type){
		var deduction="";
		if("1"==cust_type){
			if("0"==punish_type){
				deduction = bigFina;
			} else if("1"==punish_type){
				deduction = bigPuni;
			}
		}else if("2"==cust_type){
			if("0"==punish_type){
				deduction = midFina;
			} else if("1"==punish_type){
				deduction = midPuni;
			}
		}
		$G.get("deduction").setValue(deduction);
	}
	
	function reset(){
		$G.get("punish_type").setValue("");
		$G.get("deduction").setValue("");
	}
	
</script>
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
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<colgroup>
				       	<col width="15%"/>
				       	<col width="40%"/>
				       	<col width="15%"/>
				       	<col width="40%"/>
				</colgroup>
               	<tr>
                   	<td align="right">岗位名称：</td>
                   	<td align="left">    
                       	<input name="name" class="nui-textbox" required="true" style="width:80%"/>
                   	</td>
                   	<td align="right">任期天数：</td>
                    <td align="left">    
                        <input name="term_days" class="nui-textbox" required="true"  style="width:80%"/>
                    </td>
               	</tr>
               	<tr>
                   	<td align="right">岗位描述：</td>
                   	<td align="left" colspan="3">
                   	  	<textarea class="nui-textarea" name="describe" emptyText="请输入" style="width:92%;height:80px"></textarea>
                   	</td>
               	</tr>
               	<tr>
                   	<td align="right">岗位职责：</td>
                    <td align="left"  colspan="3">    
                    	<textarea class="nui-textarea" name="duty" emptyText="请输入" style="width:92%;height:80px"></textarea>
                    </td>
               	</tr>
				<tr>
                   	<td align="right">岗位任期：</td>
                   	<td align="left">    
                   		<input id="term" name="term" class="nui-datepicker" allowInput="false"  ondrawdate="onDrawDate" style="width:80%"/>
                   	</td>
                   	<td align="right">机构：</td>
                   	<td align="left">    
						<input id="orgid" required="true" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							name="orgnum" textfield="orgname" valuefield="orgnum" parentfield="upid"  valueFromSelect="false" multiSelect="false"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
       						popupWidth="205" popupHeight="150" popupMaxHeight="180" style="width:80%" required="true"/>
					</td>
               	</tr>
               	<tr>
                   	<td align="right">岗位类型：</td>
                    <td align="left" >    
                    	<input id="type" name="type" class="mini-combobox"  textfield="remark" valuefield="val" emptyText="岗位类型"
								url="<%=root%>/param/getKeyList?key=1006"  allowInput="false" showNullItem="true" nullItemText="请选择..." style="width:80%"/>
                    </td>
                    <td align="right">岗位ID</td>
	                <td align="left" >    
	                	 <input id = "position_no" name="position_no" class="nui-textbox" required="true"  style="width:80%"/>
	                </td>
               	</tr>
               	
                   
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" iconCls="" onclick="save">确定</a> 
		<a class="nui-button" iconCls="" onclick="onCancel">关闭</a>       
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
		var orgnum=infos.orgnum;
		$G.getbyName("orgnum").setValue(orgnum);
		if(pageType=="edit"){
			var ajaxConf = new GcdsAjaxConf();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
				form.setData(text.datas);
				$G.get("position_no").disable();
				
			});	
			$G.postByAjax({id:infos.id},"<%=root%>/org/getPositionDetail",ajaxConf);
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
    	var urlStr = "<%=root%>/org/savePosition";
	    var ajaxConf = new GcdsAjaxConf();
	    ajaxConf.setSuccessFunc(function (){
			$G.closemodaldialog("ok");
		});
	    $G.submitForm("form1", urlStr, ajaxConf);
	}
	
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }   
	
	function onDrawDate(e) {
        var date = e.date;
        var d = new Date();
        var t = d.getTime()-1000*60*60*24;
        if (date.getTime() < t) {
            e.allowSelect = false;
        }
    }
</script>

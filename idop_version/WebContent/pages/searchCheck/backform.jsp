<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title></title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
	<style type="text/css">
		.labelname{
			width:100px;
		}
	</style>
<body>    
	<div class="mini-fit">

		<form id="form1" method="post">
			<input name="check_flownum" id="check_flownum" class="nui-hidden" />
		
           	<table style="table-layout: fixed;" id="detailTable"  width="100%">
				<tr>
					<td align="right" class="labelname">查复期限：</td>
                    <td align="left">    
                        <input name="check_deadline" id="check_deadline" class="nui-spinner"  style="width:100%;" onvaluechanged="onDeadlineChange"/>
                    </td>
                   	<td align="right" class="labelname">查复截止日期：</td>
                   	<td align="left">    
						<input name="check_enddate" class="nui-textbox"   style="width:90%;" allowInput="false" />
					</td>
				</tr>		
				<tr >
					<td align="right" valign="top" >退回说明：</td>
					<td align="left">
						<input name="remarks" id="remarks" class="nui-textarea" style="width:250%;height:80px;"  maxlength="500" emptyText="最多输入500汉字" onvaluechanged="datachanege"/>
					</td>
				</tr>		
			</table>
		</form>
	</div>
	<div class="nui-toolbar"  style="text-align: center;padding-top: 10px;padding-bottom: 10px;margin-bottom: -5px;" borderStyle="border-left:0;border-bottom:0;border-right:0;">               
		<a class="nui-button" onclick="save">确定</a> 
		<a class="nui-button" onclick="onCancel">取消</a>       
	</div>        
</body>
</html>
<script type="text/javascript">
	$G.parse();
	var form = $G.getForm("form1");
	
	function datachanege(){
		var data =$("#remarks").val();
		if(data.length>499){
			alert("最多输入500个字");
		}
		
	}
	function setData(data){
		var infos = $G.clone(data);
			
		    var flownum=infos.check_flownum;
			var ajaxConf = new GcdsAjaxConf();
			var e= $G.getbyName("check_deadline").getValue();
			ajaxConf.setIsShowProcessBar(true);
			ajaxConf.setIsShowSuccMsg(false);
		    ajaxConf.setSuccessFunc(function (text){
		    	var check_flownum=text.check_flownum;
		        $G.getbyName("check_flownum").setValue(check_flownum);		      
			});
			$G.postByAjax({flownum:infos.check_flownum},"<%=root%>/searchCheck/getCheckDate",ajaxConf);
			
			onDeadlineChange1(e);		
	}
	
	function getData(data){
		var data =form.getData();
		return data;
		
	}
	
	/*
	 *保存数据
	 */
	function save(){	
		var remarks=$G.get("remarks").getValue();
		if(remarks==""||remarks==null){
			$G.alert("请填写退回说明");
			return;
		}else{
 	    var check_flownum= $G.getbyName("check_flownum").getValue();
		var check_deadline=$G.getbyName("check_deadline").getValue();
		var check_enddate=$G.getbyName("check_enddate").getValue();
	    var remarks=$G.getbyName("remarks").getValue();
	      $.ajax({
	  
					url:"<%=root%>/searchCheck/backSave",
					data:{check_flownum:check_flownum,check_enddate:check_enddate,check_deadline:check_deadline,remarks:remarks},
					success:function(text){
						$G.closemodaldialog("ok");
					},
					error:function(a,b,c){
					$G.alert("修改失败！");
					}
				});
		}
	}
	function onCancel(e) {
		$G.closemodaldialog("cancel");
    }
    
    function onDeadlineChange1(e){
		var val=e;
		$.ajax({
			url:"<%=root%>/searchCheck/getCheckEndDate",
			data:{val:val},
			success:function(text){
				var ch_enddate=text.data;
				 $G.getbyName("check_enddate").setValue(ch_enddate);
			}
		});
	}
	
    //查复期限变更
	function onDeadlineChange(e){
		var val=e.value;
		$.ajax({
			url:"<%=root%>/searchCheck/getCheckEndDate",
			data:{val:val},
			success:function(text){
				var ch_enddate=text.data;
				 $G.getbyName("check_enddate").setValue(ch_enddate);
			}
		});
	}

</script>
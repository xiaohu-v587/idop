<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 算法管理主界面
	*
	* @author Liu Dongyuan
	* @date 2018-11-09
-->
	<head>
		<title>重点监测报表主界面</title>
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
<script src="<%=request.getContextPath()%>/common/timeTransportByBb.js" type="text/javascript"></script>

<script type="text/javascript">
var record;
var sondata;
var str = '<%=request.getParameter("str")%>';
var ipaddress = '<%=request.getAttribute("ipaddress")%>';
//解决中文参数问题
	function cjkEncode(text) {                                                                            
      if (text == null) {         
        return "";         
      }         
      var newText = "";         
      for (var i = 0; i < text.length; i++) {         
        var code = text.charCodeAt (i);          
        if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".         
          newText += "[" + code.toString(16) + "]";         
        } else {         
          newText += text.charAt(i);         
        }         
      }         
      return newText;         
    }  
//页面直接刷新数据
function branch_choice()
	{
	 // var id=document.getElementById("id").value;
	  var begindatestr = $G.get("begindate").getValue();
	  var enddatestr = $G.get("enddate").getValue();
	  
	  var begindates = begindatestr.substr(0,10);
	  var enddates = enddatestr.substr(0,10);
	  var begindate = begindates.split('-').join("");
	  var enddate = enddates.split('-').join("");
	 // var  = $G.get("branch_no").getValue();
	  var orgid = $G.get("org_id").getValue();
	  querydataBb(orgid);
	  querydataBybm(str);
	  //var branch_no = text.id;
	  var branch_no = record.id;//后期修改为id
	  var brno = record.br_no;
	  //var branch_no = $G.get("branch_no").getValue(); 
	  var querystr2="http://"+ipaddress+"/WebReport/ReportServer?reportlet=/idop/"+sondata+"&__bypagesize__=false&begindate="+begindate+"&enddate="+enddate+"&branch_no="+branch_no+"&brno="+brno;
	  var chartFrame = document.getElementById('chartFrame');
      chartFrame.src =cjkEncode(querystr2);
      chartFrame.removeAttribute("hidden");
	  
	  //拼接参数      
	  //var querystr2="http://localhost:8075/WebReport/ReportServer?reportlet=do_cn_opatlas_teller.cpt";
	}
	//查询参数
	function querydataBb(orgid){
		$.ajax({
			url : "<%=root%>/importantBbView/queryData",
			data:{orgid:orgid},
			async:false,
			success : function(text) {
	          	 record = $G.decode(text).data;
			}
		})
	}
	//查询参数
	function querydataBybm(str){
		$.ajax({
			url : "<%=root%>/importantBbView/querySonDataBybm",
			data:{str:str},
			async:false,
			success : function(text) {
	          	 sondata = $G.decode(text).data.spare1;
			}
		})
	}
</script>
</head>

<body > 
<form >
	<table>
            <tr>
            <td class="form_label" colspan="1">
                	开始时间:
            </td>
           	 <td colspan="1">
        		<input id="begindate" name="begindate"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:165px;"/>
             </td>
              <td class="form_label">
                	结束时间:
            </td>
           	 <td colspan="1">
        		<input id="enddate" name="enddate"  class="mini-datepicker" format="yyyy-MM-dd" allowInput="false" style="width:165px;"/>
              </td>
              	<td align="right">机构名称：</td>
                <td align="left">
						<input id="org_id"  class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							name="org_id" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="onCloseClick" showRadioButton="true" showFolderCheckBox="false"
       						popupWidth="205" popupHeight="150" popupMaxHeight="180" onvaluechanged="onOrgChanged" />
					</td>
					<td colspan="2">
	              <input class="mini-hidden"  name="branch_no"  id="branch_no" value="102"/>
	              <input type="button" value="查询" class="button" onclick="branch_choice();"/>
              </td>
        </tr>      
	 </table>
<!--    <h:hidden property="query_out" id="query_out"/>
 -->  </form>
 <iframe id="chartFrame" frameborder="0" name="chartFrame" width="100%" height="82%" hidden="true"></iframe>
</body>
</html>




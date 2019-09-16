<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@ include file="/common/jstlres.jsp" %>
<HTML>
<HEAD>
	<title>定时任务管理</title>
	<%@ include file="/common/nuires.jsp" %>
	<SCRIPT src="<%= request.getContextPath() %>/resource/js/jquery.min.js"></SCRIPT>
	<SCRIPT src="<%= request.getContextPath() %>/resource/js/jquery.easyui.min.js"></SCRIPT>
    <link href="<%= request.getContextPath() %>/resource/css/schedule/style.css" rel="stylesheet" type="text/css" />  
	<link href="<%= request.getContextPath() %>/resource/css/schedule/easyui.css" rel="stylesheet" type="text/css" /> 
	
</HEAD>
<BODY>

<DIV id="cronContainer">
<DIV id=tt>

<DIV id=cChoice style="margin-top: -15px">
<DIV style="WIDTH: 700px; HEIGHT: 250px" class=easyui-tabs>
<DIV 
style="PADDING-BOTTOM: 10px; PADDING-LEFT: 10px; PADDING-RIGHT: 10px; PADDING-TOP: 10px" 
title="分钟" closeWhenDblClick="false">
<UL>
  <LI><INPUT value=* CHECKED type=radio name=rMin>周期  从 <INPUT 
  style="WIDTH: 80px" id=minStart class="easyui-numberspinner" value=0 
  data-options="min:0,max:60">分钟开始，每<INPUT style="WIDTH: 80px" id=minEnd 
  class=easyui-numberspinner value=1 
  data-options="min:1,max:60"></INPUT>分钟执行一次</LI>
  <LI><INPUT value=c type=radio name=rMin>指定	
  <DIV id=minPanel ></DIV></LI></UL></DIV>
<DIV 
style="PADDING-BOTTOM: 10px; PADDING-LEFT: 10px; PADDING-RIGHT: 10px; PADDING-TOP: 10px" 
title="小时" closeWhenDblClick="false">
<UL>
  <LI><INPUT id=hourId value=* CHECKED type=radio name=rHour>每小时 
  <LI><INPUT id=hourId2 value=c type=radio name=rHour >指定
  <DIV id=hourPanel ></DIV></LI></UL>
  </DIV>
<DIV 
style="PADDING-BOTTOM: 10px; PADDING-LEFT: 10px; PADDING-RIGHT: 10px; PADDING-TOP: 10px" 
title="日" closeWhenDblClick="false">
<div style="width: 50%;float: left;">
<UL>
  <LI><INPUT id=dayId value=* CHECKED type=radio name=rDay>每日(按日期)
  <LI><INPUT id=dayId2 value=c type=radio name=rDay>指定
  <DIV id=dayPanel ></DIV></LI></UL>
  </div>
  
   <div style="width: 40%;float: left;margin-left: 5%;">
  <UL>
  <LI><INPUT id="weekId" value=? CHECKED type=radio name=rWeek>每日(按星期)
  <LI><INPUT id="weekId2" value=c type=radio name=rWeek>指定
  <DIV id="weekPanel" ></DIV></LI></UL></div > 
   </DIV> 
<DIV 
style="PADDING-BOTTOM: 10px; PADDING-LEFT: 10px; PADDING-RIGHT: 10px; PADDING-TOP: 10px" 
title="月" closeWhenDblClick="false">
<UL>
  <LI><INPUT id=monId value=? CHECKED type=radio name=rMon>每月
  <LI><INPUT id=monId2 value=c type=radio name=rMon>指定
  <DIV id=monPanel ></DIV></LI></UL></DIV>
  </DIV></DIV>
<DIV id="Cres">
<FIELDSET><LEGEND>表达式字段</LEGEND>
<DIV>
<TABLE>
  <TBODY>
   <TR>
    <TD></TD>
    <TD>秒</TD>
    <TD>分钟</TD>
    <TD>小时</TD>
    <TD>日</TD>
    <TD>月</TD>
    <TD>星期</TD>
  </TR>
  <TR>
    <TD>表达式</TD>
    <TD><INPUT id="exSec"></TD>
    <TD><INPUT id="exMin"></TD>
    <TD><INPUT id="exHour"></TD>
    <TD><INPUT id="exDay"></TD>
    <TD><INPUT id="exMon"></TD>
    <TD><INPUT id="exWeek"></TD></TR>
  <TD>Cron表达式</TD> 
    <TD colSpan=5><INPUT style="WIDTH: 100%" id=rwqdsj name="rwqdsj" field="rwqdsj" type="text"></TD>
    <TD></TD></TR></TBODY></TABLE></DIV></FIELDSET> 
</DIV>
</div>
</DIV>
<div class="nui-toolbar" style="text-align:center;padding-top:8px;padding-bottom:8px;" 
      borderStyle="border-left:0;border-bottom:0;border-right:0;">
      	<a id="btnCancel" class="nui-button " iconCls="icon-close"style="margin-right:20px;">
      		取消
		</a>
		<a id="btnSc" class="nui-button" iconCls="icon-reload"  style="margin-right:20px;">
      		生成
		</a>
		<a id="btnOk" class="nui-button" iconCls="icon-ok" onclick="onOk()" enabled="false">
			确定
		</a> 
	</div>

<script type="text/javascript">

$G.parse();
var grid = $G.get("rwqdsj");
function getData() {
	var rwqdsj=document.getElementById("rwqdsj").value;
	return rwqdsj;
}
   var mi= ""; 
  for(var i=0;i<60;i++){
	var a = i+1;
	if(a==60){a=0;}
	mi+="<input type='checkbox' value='"+a+"' name='minchk' disabled='disabled'><span>"+a+"</span>";
	if(a!=0&&a%10==0){
		mi+="<br>";
	}
}  
  $("#minPanel").html(mi);   
 

var daygg = "";
for(var i=0;i<31;i++){
	var a = i+1;
	daygg+="<input type='checkbox' value='"+a+"' name='daychk' disabled='disabled'><span>"+a+"</span>";
	if(a!=0&&a%6==0){
		daygg+="<br>";
	}
}
$("#dayPanel").html(daygg);

var mon = "";
for(var i=0;i<12;i++){
	var a = i+1;
	mon+="<input type='checkbox' value='"+a+"' name='monchk' disabled='disabled'><span>"+a+"</span>";
	if(a!=0&&a%10==0){
		mon+="<br>";
	}
}
$("#monPanel").html(mon);

var week = "";
for(var i=0;i<7;i++){
	var a = i+1;
	week+="<input type='checkbox' value='"+a+"' name='weekchk' disabled='disabled'><span>"+a+"</span>";
	if(a!=0&&a%5==0){
		week+="<br>";
	}
}
$("#weekPanel").html(week);

var hour = "AM:";
for(var i=0;i<24;i++){
	var a = i+1;
	if(a==24)a=0;
	hour+="<input type='checkbox' value='"+a+"' name='hourchk' disabled='disabled'><span>"+a+"</span>";
	if(a!=0&&a%12==0&&a!=24){
		hour+="<br>PM:";
	}
}
$("#hourPanel").html(hour);


function getMin(){
	var minR = 	$("input[name='rMin']:checked").val();
	if(minR == 'c'){
		var cks = $("input[name='minchk']:checked");
		if(cks.length==1){
			return 	cks[0].value;
		}else{
			var vs = "";
			for(var i=0;i<cks.length;i++){
				vs+=cks[i].value;
				if(i<cks.length-1){
					vs+=","	
				}
			}
			return vs;
		}
	}else{
		return $("#minStart").val()+"/"+$("#minEnd").val();
	}
}

function getHour(){
	var HourR = 	$("input[name='rHour']:checked").val();
	if(HourR == 'c'){
		var cks = $("input[name='hourchk']:checked");
		if(cks.length==1){
			return 	cks[0].value;
		}else{
			var vs = "";
			for(var i=0;i<cks.length;i++){
				vs+=cks[i].value;
				if(i<cks.length-1){
					vs+=","	
				}
			}
			return vs;
		}
	}
	return "*";
}

function getWeek(){
	var WeekR = 	$("input[name='rWeek']:checked").val();
	if(WeekR == 'c'){
		var chk_value=[];
		$("input[name='weekchk']:checked").each(function(){
			chk_value.push($(this).val());
		})
		return chk_value;
		
	}
	return "?";
}

function getDay1(){
	var DayR = 	$("input[name='rDay']:checked").val();
	if(DayR == 'c'){
		var chk_value=[];
		$("input[name='daychk']:checked").each(function(){
			chk_value.push($(this).val());
		})
		return chk_value;
		
	}
	return "*";
}

function getMon(){
	var MonR = 	$("input[name='rMon']:checked").val();
	if(MonR == 'c'){
		var chk_value=[];
		$("input[name='monchk']:checked").each(function(){
			chk_value.push($(this).val());
		})
		return chk_value;
	}
	return "*";
}

function initChecks(){
	$("input[name='rMin']").click(function(){						  
		var v = $(this).val();
		if("c"==v){
				$("input[name='minchk']").removeAttr("disabled");
		}else{
			$("input[name='minchk']").attr("disabled","disabled");
		}
	});	 
	$("input[name='rHour']").click(function(){						  
		var v = $(this).val();
		if("c"==v){
				$("input[name='hourchk']").removeAttr("disabled");
		}else{
			$("input[name='hourchk']").attr("disabled","disabled");
		}
	});	
	$("input[name='rDay']").click(function(){						  
		var v = $(this).val();
		if("c"==v){
				$("input[name='daychk']").removeAttr("disabled");
		}else{
			$("input[name='daychk']").attr("disabled","disabled");
		}
	});	
	$("input[name='rMon']").click(function(){						  
		var v = $(this).val();
		if("c"==v){
				$("input[name='monchk']").removeAttr("disabled");
		}else{
			$("input[name='monchk']").attr("disabled","disabled");
		}
	});	
	$("input[name='rWeek']").click(function(){						  
		var v = $(this).val();
		if("c"==v){
				$("input[name='weekchk']").removeAttr("disabled");
		}else{
			$("input[name='weekchk']").attr("disabled","disabled");
		}
	});	
}
function fanMin(txt){
	$("#exMin").val(txt);
	if(txt.indexOf('/')!=-1){
		var ts = txt.split("/");
		$("#minStart").val(ts[0]);
		$("#minEnd").val(ts[1]);
	}else{
		var mincks = $("input[name='minchk']");
		var mins = txt.split(",");
		for(var i=0;i<mins.length;i++){
			mincks[i+1].checked = "checked";
		}
		for(var i=0;i<mincks.length;i++){
			mincks[i].disabled = null;
		}
	}
}

function fanHour(txt){
	$("#exHour").val(txt);
	if(txt=="*"){
		$("#hourId").attr("checked","checked");
	}else{
		$("#hourId2").attr("checked","checked");
		$("input[name='hourchk']").each(function(){
				if($(this).val()==txt){
					$(this)	.attr("checked","checked");
				}	
				$(this).removeAttr("disabled");
		})
	}
}

function fanObj(txt,idTxt,exSuf){
	$("#ex"+exSuf).val(txt);
	if(txt=="*"){
		$("#"+idTxt+"Id").attr("checked","checked");
	}else{
		$("#"+idTxt+"Id2").attr("checked","checked");
		$("input[name='"+idTxt+"chk']").each(function(){
				if($(this).val()==txt){
					$(this)	.attr("checked","checked");
				}	
				$(this).removeAttr("disabled");
		})
	}
}

function fan(txt){
	var regs = txt.split(' ');
	$("#exSec").val(regs[0]);
	fanMin(regs[1]);
	fanHour(regs[2]);
	fanObj(regs[3],"daygg","Daygg");
	fanObj(regs[4],"mon","Mon");
	fanObj(regs[5],"week","Week");
}

function changeByEx(){
	var html = "";
	var exLen = $("input[id^='ex']").size();
	$("input[id^='ex']").each(function(i){
		html+=$(this).val();	
		if(i<exLen-1){
			html+=" ";
		}
	})
	$("#rwqdsj").val(html);
}
							
$(function(){
	 initChecks(); 
		   
	$("#btnSc").click(function(){
		$("#exSec").val(0);
		var mi = getMin();
		$("#exMin").val(mi);
		var hour = getHour();
		$("#exHour").val(hour);
		var daygg = getDay1();
		$("#exDay").val(daygg);
		var mon = getMon();
		$("#exMon").val(mon);
		var week = getWeek();
		$("#exWeek").val(week);
		var str = "0 "+mi+" "+hour+" "+daygg+" "+mon+" "+week;
		$("#rwqdsj").val(str);
		$G.get("btnOk").enable();
	});
	
	$("input[id^='ex']").keyup(function(){
		changeByEx();									
	})
	
	$("#btnCancel").click(function(){
		$G.closemodaldialog("cancel");
	});
});


function onOk(){
	$G.closemodaldialog("ok");
	
}


function setData(data){
        data = $G.clone(data);
        var rwqdsj=data.rwqdsj;
        $("#rwqdsj").val(rwqdsj);
        var corn=rwqdsj.split(" ");
        $("#exSec").val(corn[0]);
        $("#exMin").val(corn[1]);
        $("#exHour").val(corn[2]);
        $("#exDay").val(corn[3]);
        $("#exMon").val(corn[4]);
        $("#exWeek").val(corn[5]);
}

</script> 
</BODY></HTML>
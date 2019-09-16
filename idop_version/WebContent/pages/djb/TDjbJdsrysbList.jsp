<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>要素配置</title>
<style type="text/css">
	html, body{
	        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    }
	#fieldset1 {
		weight: 100%;
	}
	
	#fieldset2 {
		weight: 100%;
	}
	
	fieldset {
		overflow: auto;
	}
	
	img {
		height: 15px
	}
</style>
</head>
<body>
	<div class="mini-fit">
		<input name="pageType" class="mini-hidden" />
		<form id="dataform1"  method="post" >
			<input class="mini-hidden" name="djbid" />
			<fieldset id="fieldset2">
				<legend>
					登记表样式&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:AddElementDiv1('elem1')"><img border="0"
						src="../resource/image/add.jpg" /></a>
				</legend>
				<div style="padding: 0px; margin: 0px;" id="parentpoint1"></div>
			</fieldset>
		</form>
	</div>
	<div class="mini-toolbar" id="toolbardiv"
		style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		 <a class="mini-button" onclick="onOk" > 确定 </a>
		<a class="mini-button" onclick="onCancel" style="margin-right: 20px;" > 取消 </a>
	</div>
	<script type="text/javascript">
    mini.parse();
    var grid;
    var updateBtn;
    var node1 = 1;
    var form =  new mini.Form("dataform1");//将普通form转为mini的form
    $(function(){
    });
    
   //确定保存或更新
	function onOk(e) {
		saveJson();
	}
    
	//json方式保存数据
	function saveJson() {
		var urlStr = "<%=root%>/djb/saveSyb?node="+(node1-1);
		var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
		var o = form.getData();            
		form.validate();
		if (form.isValid() == false)
		return;
        var json = mini.encode([o]);
        $.ajax({
             url: urlStr,
		     type: 'post',
             data: { data: json},
             cache: false,
             success: function (text) {
             	if(pageType=="1"){
             		mini.alert("修改成功!");
             	}else{
             		mini.alert("新增成功!");
             	}
             	CloseWindow("save");
             }
             ,error: function (jqXHR, textStatus, errorThrown) {
                 alert(jqXHR.responseText);
                 CloseWindow();
             }
         });
	}
    
    
    var node = 1;
    
	var createElem1 = function(strid){
		var tr = $("<tr/>");
		var form1 = $("<form/>");
		var table1 = $("<table/>");
		var input1 = $("<input/>");
		var input2 = $("<input/>");
		var input3 = $("<input/>");
		var input6 = $("<input/>");
		var input10 = $("<input/>");
		var input11 = $("<input/>");
		var input16= $("<input/>");
		var addbtn = $("<a/>");
		var delbtn = $("<a/>");
		var mblxbtn = $("<input/>");
		var packe = function (e) {
			return "node_"+strid+"_"+e+"_"+node1;
		}; 
		
		addbtn.attr({id:"addnode"+node1,href:"javascript:AddElementDiv1('elem1')"}).append("<img border='0' src='../resource/image/add.jpg' />");
		
		delbtn.attr({id:"delnode"+node1,href:"javascript:DeleteElementDiv1('nodeDiv1"+node1+"')"}).append("<img border='0' src='../resource/image/delete.gif' />");
		
		mblxbtn.attr({id:"mblx"+node1,name:packe("mblx"),vtype:"maxLength:18",required:true,style:"width:100px;",valueField:"key",textField:"name",url:"<%=root%>/param/getDjbKey",allowInput:true}).addClass("mini-combobox");
		
		input1.attr({id:packe("yshy"),name:packe("yshy"),required:true,vtype:"maxLength:18",style:"width:100px;"}).addClass("mini-textbox");
		
		input2.attr({id:packe("zdlx"),name:packe("zdlx"),valueField:"val",vtype:"maxLength:18",style:"width:136px;",textField:"remark",required:true,url:"<%=root%>/param/getKeyList?key=500003",onvaluechanged:"opennewid('"+packe("zdlx")+"','"+node1+"')"}).addClass("mini-combobox");
		
		input3.attr({id:packe("cdxz"),name:packe("cdxz"),vtype:"maxLength:8",required:true,style:"width:50px;"}).addClass("mini-textbox");
		
		input6.attr({id:packe("ysm"),name:packe("ysm"),vtype:"maxLength:18",value:packe("ysm")}).addClass("mini-hidden");
		
		input10.attr({id:packe("zdpx"),name:packe("zdpx"),vtype:"maxLength:18",required:true,style:"width:50px;"}).addClass("mini-textbox");
		
		input11.attr({id:packe("zdsm"),name:packe("zdsm"),vtype:"maxLength:40",style:"width:100px;"}).addClass("mini-textarea");
		input16.attr({id:packe("sfym"),name:packe("sfym"),border:1,vtype:"maxLength:4",style:"width:70px;",valueField:"val",textField:"remark",required:true,value:"0",url:"<%=root%>/param/getKeyList?key=528"}).addClass("mini-combobox");
		
	    tr.append($("<td/>").append("字段"+node1+"名称:").attr({style:"width:75px;"}), $("<td/>").append(input1))
	      .append($("<td/>").append("类型:").attr({width:"30px;"}), $("<td/>").append(input2))
	      .append($("<td/>").append("下拉框属性:").attr({width:"70px;"}),$("<td/>").append(mblxbtn))
	      .append($("<td/>").append("字段长度限制:").attr({width:"90px;"}),$("<td/>").append(input3))
	      .append($("<td/>").append(input6).attr({width:"50px;",style : "display:none"}))
	      .append($("<td/>").append("字段排序:").attr({width:"60px;"}),$("<td/>").append(input10))
	      .append($("<td/>").append("字段说明:").attr({width:"60px;"}),$("<td/>").append(input11))
	      .append($("<td/>").append("是否验密:").attr({width:"60px;"}),$("<td/>").append(input16))
	      
	      .append($("<td/>").append(delbtn).attr({width:"30px;"}))
	      .append($("<tr/>"));
		table1.append(tr);
		form1.append(table1);
		form1.attr({id:"nodeDiv1"+node1});
		
		var chiledelem = null;
		var parentpoint = $("#parentpoint1");
		if(chiledelem){
			chiledelem.after(form1);
		}else{
			parentpoint.append(form1);
		}
		node1++;
	};
	

	//获取父页面传递来的json数据
	function setData(data) {
		//跨页面传递的数据对象，克隆后才可以安全使用
		var infos = mini.clone(data);
		//保存list页面传递过来的页面类型：add表示新增、edit表示编辑
		mini.getbyName("pageType").setValue(infos.pageType);
		if(infos!=null){
			mini.getbyName("djbid").setValue(infos.id);
		}
		//如果是点击编辑类型页面
		if (infos.pageType == "edit") {
			//编辑页面根据主键加载业务信息
			$.ajax({
		        url: "<%=root%>/djb/findAll",
		        data:{djbid:infos.id},
		        cache: false,
		        success: function (text) {
		            var d =  text.data;
					var total = text.total;
					createNodes(d,total);
		       	}
		    });
		}
	}
		
	var createNodes = function(data,total){
		if(total>0){
			for( var i=0;i<total;i++){
				createBjNode('elem1',data[i]);
			}
			mini.parse();
		}
	};
	
	var createBjNode = function(strid,e){
		var tr = $("<tr/>");
		var form1 = $("<form/>");
		var table = $("<table/>");
		var input1 = $("<input/>");
		var input2 = $("<input/>");
		var input3 = $("<input/>");
		var input6 = $("<input/>");
		var input10 = $("<input/>");
		var input11= $("<input/>");
		var input13= $("<input/>");
		var input14= $("<input/>");
		var input15= $("<input/>");
		var input16= $("<input/>");
		var addbtn = $("<a/>");
		var delbtn = $("<a/>");
		var mblxbtn = $("<input/>");
		var packe = function (e) {
			return "node_"+strid+"_"+e+"_"+node1;
		}; 
		input2.attr({id:"zdlx"+node1,name:packe("zdlx"),vtype:"maxLength:18",style:"width:136px;",value:e.zdlx,valueField:"val",required:true,textField:"remark",url:"<%=root%>/param/getKeyList?key=500003",onvaluechanged:"opennewid('"+packe("zdlx")+"','"+node1+"')"}).addClass("mini-combobox");
		
		mblxbtn.attr({id:"mblx"+node1,name:packe("mblx"),vtype:"maxLength:18",style:"width:100px;",value:e.mblx,valueField:"key",required:true,textField:"name",url:"<%=root%>/param/getDjbKey",allowInput:true}).addClass("mini-combobox");
		
		addbtn.attr({id:"addnode"+node1,href:"javascript:AddElementDiv1('elem1')"}).append("<img border='0' src='../resource/image/add.jpg' />");
		
		delbtn.attr({id:"delnode"+node1,href:"javascript:DeleteElementDiv1('nodeDiv1"+node1+"')"}).append("<img border='0' src='../resource/image/delete.gif' />");
		
		input1.attr({id:packe("yshy"),name:packe("yshy"),border:1,value:e.yshy,vtype:"maxLength:18",style:"width:100px;",required:true}).addClass("mini-textbox");

		input3.attr({id:packe("cdxz"),name:packe("cdxz"),border:1,value:e.cdxz,vtype:"maxLength:8",style:"width:50px;",required:true}).addClass("mini-textbox");
		
		input6.attr({id:packe("ysm"),name:packe("ysm"),border:1,vtype:"maxLength:18",value:packe("ysm")}).addClass("mini-hidden");
		
		input10.attr({id:packe("zdpx"),name:packe("zdpx"),border:1,vtype:"maxLength:18",value:e.zdpx,style:"width:50px;",required:true}).addClass("mini-textbox");
		
		input15.attr({id:packe("zdsm"),name:packe("zdsm"),border:1,vtype:"maxLength:40",style:"width:100px;",value:e.zdsm}).addClass("mini-textarea");
		
		input16.attr({id:packe("sfym"),name:packe("sfym"),border:1,vtype:"maxLength:4",style:"width:80px;",value:e.sfym,valueField:"val",required:true,textField:"remark",url:"<%=root%>/param/getKeyList?key=528"}).addClass("mini-combobox");
		
		tr.append($("<td/>").append("字段"+node1+"名称:").attr({width:"75px;"}), $("<td/>").append(input1))
	      .append($("<td/>").append("类型:").attr({width:"30px;"}), $("<td/>").append(input2))
	      .append($("<td/>").append("下拉框属性:").attr({width:"70px;"}),$("<td/>").append(mblxbtn))
	      .append($("<td/>").append("字段长度限制:").attr({width:"90px;"}),$("<td/>").append(input3))
	      .append($("<td/>").append(input6).attr({width:"50px;",style : "display:none"}))
	      .append($("<td/>").append("字段排序:").attr({width:"60px;"}),$("<td/>").append(input10))
	      .append($("<td/>").append("字段说明:").attr({width:"60px;"}),$("<td/>").append(input15))
	      .append($("<td/>").append("是否验密:").attr({width:"60px;"}),$("<td/>").append(input16))
	      .append($("<td/>").append(delbtn).attr({width:"30px;"}))
	      .append($("<tr/>"));
		table.append(tr);
		form1.append(table);
		form1.attr({id:"nodeDiv1"+node1});
		
		var chiledelem = null;
		var parentpoint = $("#parentpoint1");
		if(chiledelem){
			chiledelem.after(form1);
		}else{
			parentpoint.append(form1);
		}
		node1++;
		mini.parse();
		onload1(e);
	};
	
	function mblxCilck(node){
		var bizData = {
        	pageType : "add",
        	node : node
        };//传入模态窗口页面的json数据
        Open("<%=root%>/djb/param","配置页面",600,400,bizData,function (data) {
        	var mblx = "node_elem1_mblx_"+(node);
        	mini.getbyName(mblx).setValue(data.key);
        });
	}
	
	function opennewid(sj,count) {
		var lxxz = mini.getbyName(sj).getValue();
		if (lxxz == 1 ||lxxz == 12 ||lxxz == 13) {
			mini.get("mblx"+count).enable();
		} else {
			mini.get("mblx"+count).disable();
		};
	}

 	function onload1(e) {
		var zdlxmc = e.ZDLX;
		var node11 = node1-1;
		if (zdlxmc == 1) {
			mini.get("mblx"+node11).enable();
		} else {
			mini.get("mblx"+node11).disable();
		};
	} 

	//生成div
	function AddElementDiv1(strid1) {
		createElem1(strid1);
		mini.parse();
		onload(); 
	}

	function onload() {
		mini.get("mblx"+(node1-1)).disable();
	} 

	function DeleteElementDiv1(id) {
		var my = document.getElementById(id);
		if (my != null) {
			my.parentNode.removeChild(my);
		}
		mini.parse();
	}

	function onCancel(e) {
		CloseWindow("cancel");
	}

	function CloseWindow(action) {        
		if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
		else window.close();            
	}
	</script>
</body>
</html>

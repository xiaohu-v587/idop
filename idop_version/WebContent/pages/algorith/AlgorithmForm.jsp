<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
	* 基本算法信息
	*
	* @author Liu Dongyuan
	* @date 2018-11-09
-->
<head>
	<%@ include file="/common/nuires.jsp"%>
		    <title>基本算法信息</title>
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
</head>
<body>
	<!-- 标识页面是查看(query)、修改(edit)、新增(add) -->
    <div class="mini-fit">
    	<input name="pageType" class="mini-hidden" />
        <form id="dataform1" method="post">
        	<div style="padding-bottom: 20px; padding-top: 20px;">
         		<input class="mini-hidden"  name="id"  id="id"/>
         		<input class="mini-hidden"  name="node"  id="node" value="0"/>
         		<input class="mini-hidden"  name="num"  id="num" value="0"/>
          		<table style="table-layout: fixed;" align="center" id="addtable">
            		<tr>
              			<td class="form_label">业务模块:</td>
              			<td >
                			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
           	 				id="ywtype" name="ywtype"   onvaluechanged="" valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">指标预警编号:</td>
              			<td >
                			<input class="mini-textbox" name="indexnum"   id="indexnum" width="200px" required="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">指标预警名称:</td>
              			<td >
                			<input class="mini-textbox" name="indexname"   id="indexname" width="200px" required="true"></input>
            			</td>
              			<td class="form_label">赋分方式:</td>
              			<td >
                			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_asstype" 
           	 				id="assigntype" name="assigntype"  onvaluechanged="OnassValueChange" valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">计算方式:</td>
              			<td >
                			<input class="mini-combobox" width="200px"   nullItemText="请选择..." emptyText="请选择..."  url="" 
           	 				id="caltype" name="caltype"   onvaluechanged=""  valueField="val" textField="remark" required="true"></input>
            			</td>
              			<td class="form_label">适用层级:</td>
              			<td >
                			<input class="mini-combobox" width="200px"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_apply" 
           	 				id="apply" name="apply"   onvaluechanged=""  valueField="val" textField="remark" required="true"></input>
            			</td>
        			</tr>
            		<tr>
              			<td class="form_label">满分:</td>
              			<td >
                			<input class="mini-textbox" name="marks"   id="marks" width="200px" required="true"></input>
            			</td>
              			<td class="form_label">权重:</td>
              			<td >
                			<input class="mini-textbox" name="weight"   id="weight" width="200px" required="true"></input>
            			</td>
        			</tr>
        			<tr>
        				<td class="form_label">描述:</td>
              			<td colspan="3">
                			<input class="mini-textarea"  name="describtion"   id="describtion" width="490px" required="true"></input>
            			</td>
        			</tr>
        			<tr>
        				<td>新增条件:</td>
          				<td colspan="1"><a href="javascript:addHtml()"><img border="0" width="15px;" height="15px;"
						src="<%=request.getContextPath()%>/resource/image/add.jpg" /></a></td>
        			</tr>
				</table>
			</div>
		</form>
	</div>
	<div class="mini-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
		<a id="btnOk" class="mini-button" onclick="saveJson()" style="margin-right: 20px;">确定</a> 
		<a id="btnCancle" class="mini-button"  onclick="onCancel()">取消</a>
	</div>
	<script type="text/javascript">
	  //changeButtonPosition($("#toolbardiv"));
	   //$G.parse();
	  var form = new mini.Form("dataform1");
	  //区间赋值条件字段标识
	  var node = 1;
	  //预警扣分条件字段标识
	  var num = 1;
	  
	  //保存或修改数据
	  function saveJson() {
	    //保存
	    var urlStr = "<%=root%>/algorith/save";
	    var pageType = mini.getbyName("pageType").getValue();//获取当前页面是编辑还是新增状态
	    //表示为编辑状态
	    if (pageType == "edit") {
	      urlStr = "<%=root%>/algorith/update";
	    }
		form.validate();
		if (form.isValid() == false){
			mini.alert("请完善表单信息。");
			mini.get("btnOk").enable();
			return;
		}else{
			/* $("#dataform1").attr('action',urlStr);
			$("#dataform1").submit();//提交表单 */
		    var ajaxConf = new GcdsAjaxConf();
		    ajaxConf.setSuccessFunc(function (){
				$G.closemodaldialog("ok");
			});
		    $G.submitForm("dataform1", urlStr, ajaxConf);
		}
	  }
	  
	  //拼接条件
	  function addHtml(){
		  var assigntype = mini.get("assigntype").getValue();
		  var caltype = mini.get("caltype").getValue();
		  if(assigntype != "" && assigntype != "null"){
			  if(assigntype == "01"){
				 //区间赋分
				 addInval(node);
			  }else{
				 //预警扣分
				 if(caltype != "" &&caltype =="01"){
					 //预警等级
					 addWarn(num,caltype);
					 $G.get("caltype").setReadOnly(true);
				 } else if(caltype != "" &&caltype =="02"){
					 //笔数
					 addWarn(num,caltype);
					 $G.get("caltype").setReadOnly(true);
				 }
			  }
			  $G.get("assigntype").setReadOnly(true);
		  }else{
			  mini.alert("请选择赋分方式！","提示");
		  }
	  }
	  
	  //新增区间赋分
	  function addInval(e){
		  var tab = document.getElementById("addtable");
		  var tr = document.createElement("tr");
		  tr.setAttribute("id","inval_"+e);
		  tr.setAttribute("display","block");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+e+":";
		  td.setAttribute("class","form_label");
		  var td1 = document.createElement("td");
		  td1.innerHTML=" <input class='mini-textbox' width='50px'  id='ele1_"+e+"' name='ele1_"+e+"'   required='true' ></input>" +
			" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px'  id='ope1_"+e+"' name='ope1_"+e+"'   required='true'></input>" +
				" &nbsp;&nbsp;" +
				" &nbsp;&nbsp;传入值" +
				" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px'  id='ope2_"+e+"' name='ope2_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px'  id='ele2_"+e+"' name='ele2_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;&nbsp;&nbsp;得分:" +
				" <input class='mini-textbox' width='50px'  id='inmarks_"+e+"' name='inmarks_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;" +
				" <a href='javascript:delInval("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>";
		  td1.setAttribute("colspan","3");
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tab.appendChild(tr);
		  $G.parse();
		  mini.get("node").setValue(node);
		  node++;
	  }
	  
	  //新增预警扣分
	  function addWarn(e,c){
		  var tab = document.getElementById("addtable");
		  var tr = document.createElement("tr");
		  tr.setAttribute("id","warn_"+e);
		  tr.setAttribute("display","block");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+e+":";
		  td.setAttribute("class","form_label");
		  var td1 = document.createElement("td");
		  if(c == "01"){
			  td1.innerHTML = "<input class='mini-combobox' width='150px' url='<%=root%>/param/getDict?key=dop_warning_lvl' " +
				"id='levels_"+e+"' name='levels_"+e+"'   valueField='val' textField='remark' required='true'></input>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;得分:" +
					" <input class='mini-textbox' width='50px'  id='wamarks_"+e+"' name='wamarks_"+e+"'   required='true' ></input>" +
					"<a href='javascript:delWarn("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>";
		  }else{
			  td1.innerHTML = "每一笔扣分:" +
					" <input class='mini-textbox' width='50px'  id='wamarks_"+e+"' name='wamarks_"+e+"'   required='true' ></input>" +
					"<a href='javascript:delWarn("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>";
		  }
		  td1.setAttribute("colspan","3");
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tab.appendChild(tr);
		  $G.parse();
		  mini.get("num").setValue(num);
		  num++;
	  }
	  
	  //加载预警扣分条件
	  function setWarn(e,c,d){
		  var tab = document.getElementById("addtable");
		  var tr = document.createElement("tr");
		  tr.setAttribute("id","warn_"+e);
		  tr.setAttribute("display","block");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+e+":";
		  td.setAttribute("class","form_label");
		  var td1 = document.createElement("td");
		  if(d == "01"){
			  td1.innerHTML = "<input class='mini-combobox' width='150px' url='<%=root%>/param/getDict?key=dop_warning_lvl' " +
				"id='levels_"+e+"' name='levels_"+e+"' value='"+c.levels+"'   valueField='val' textField='remark' required='true'></input>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;得分:" +
					" <input class='mini-textbox' width='50px'  id='wamarks_"+e+"' value='"+c.marks+"' name='wamarks_"+e+"'   required='true' ></input>" +
					"<a href='javascript:delWarn("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>" ;
		  }else{
			  td1.innerHTML = "每一笔扣分:" +
					" <input class='mini-textbox' width='50px'  id='wamarks_"+e+"' value='"+c.marks+"' name='wamarks_"+e+"'   required='true' ></input>" +
					"<a href='javascript:delWarn("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>" ;
		  }
		  td1.setAttribute("colspan","3");
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tab.appendChild(tr);
		  $G.parse();
		  mini.get("num").setValue(num);
		  num++;
	  }
	  
	  //加载区间赋分条件
	  function setInval(e,c){
		  var tab = document.getElementById("addtable");
		  var tr = document.createElement("tr");
		  tr.setAttribute("id","inval_"+e);
		  tr.setAttribute("display","block");
		  var td = document.createElement("td");
		  td.innerHTML="条件"+e+":";
		  td.setAttribute("class","form_label");
		  var td1 = document.createElement("td");
		  td1.innerHTML=" <input class='mini-textbox' width='50px' value='"+c.element1+"' id='ele1_"+e+"' name='ele1_"+e+"'   required='true' ></input>" +
			" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px' value='"+c.operator1+"' id='ope1_"+e+"' name='ope1_"+e+"'   required='true'></input>" +
				" &nbsp;&nbsp;" +
				" &nbsp;&nbsp;传入值" +
				" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px' value='"+c.operator2+"' id='ope2_"+e+"' name='ope2_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;" +
				" <input class='mini-textbox' width='50px' value='"+c.element2+"' id='ele2_"+e+"' name='ele2_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;&nbsp;&nbsp;得分:" +
				" <input class='mini-textbox' width='50px' value='"+c.marks+"' id='inmarks_"+e+"' name='inmarks_"+e+"'   required='true' ></input>" +
				" &nbsp;&nbsp;" +
				" <a href='javascript:delInval("+e+")'><img border='0' width='15px;' height='15px;' src='<%=request.getContextPath()%>/resource/image/delete.gif' /></a>";
		  td1.setAttribute("colspan","3");
		  tr.appendChild(td);
		  tr.appendChild(td1);
		  tab.appendChild(tr);
		  $G.parse();
		  mini.get("node").setValue(node);
		  node++;
	  }
	  
	  
	  //删除区间赋值条件
	  function delInval(a){
		  document.getElementById("inval_"+a).style.display="none";
		  mini.getbyName("ele1_"+a).setValue("");
		  mini.getbyName("ele2_"+a).setValue("");
		  mini.getbyName("ope1_"+a).setValue("");
		  mini.getbyName("ope2_"+a).setValue("");
		  mini.getbyName("inmarks_"+a).setValue("");
	  }
	  
	  //删除预警扣分条件
	  function delWarn(a){
			document.getElementById("warn_"+a).style.display="none";
			mini.get("levels_"+a).setValue("");
			mini.get("levels_"+a).setText("");
			mini.get("wamarks_"+a).setValue("");
	  }
	  
	  //获取父页面传递来的json数据
	  function setData(data) {
	    //跨页面传递的数据对象，克隆后才可以安全使用
	    var infos = mini.clone(data);
	    //保存list页面传递过来的页面类型：add表示新增、edit表示编辑
	    mini.getbyName("pageType").setValue(infos.action);
	    //如果是点击编辑类型页面
	    if (infos.action == "edit") {
	      //编辑页面根据主键加载业务信息
		    var id = infos.id;
			$.ajax({
				url: "<%=root%>/algorith/getAlgorithmById",
				data:{key:id},
				cache: false,
				success: function (text) {
					var formt = mini.decode(text).record;
		    		for (var key in formt){
		    			data[key.toLowerCase()]=formt[key];
		    		}
		    		//赋分方式级联计算方式
			        var assigntype = data.assigntype;
					var calcombox = $G.get("caltype");
					calcombox.setUrl("<%=root%>/param/getDict?key=dop_caltype"+assigntype);
			        form.setData(data);
			        var list = mini.decode(text).list;
			        var size = mini.decode(text).size;
			        var caltype = data.caltype;
			        if(assigntype == "01"){
			        	//拼接区间条件
			        	mini.get("node").setValue(size);
			        	mini.get("num").setValue(0);
			        	if(size > 0){
					        for(var i =1;i<=size;i++){
					        	setInval(i,list[i-1]);
					        }
			        	}
			        }else if(assigntype == "02"){
			        	mini.get("num").setValue(size);
			        	mini.get("node").setValue(0);
				        //拼接预警扣分条件
			        	if(caltype == "01"){
				        	if(size > 0){
						        for(var i =1;i<=size;i++){
						        	setWarn(i,list[i-1],caltype);
						        }
				        	}
			        	}else if(caltype == "02"){
				        	if(size > 0){
						        for(var i =1;i<=size;i++){
						        	setWarn(i,list[i-1],caltype);
						        }
				        	}
			        	}
			        }else{
			        	mini.get("num").setValue(0);
			        	mini.get("node").setValue(0);
			        }
			        $G.get("assigntype").setReadOnly(true);
			        $G.get("caltype").setReadOnly(true);
				}
			});
	    }
	  }
	  
	  //关闭窗口
	  function CloseWindow(action) {
			if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
			else window.close();
	  }
	
	  //取消
	  function onCancel() {
			CloseWindow("cancel");
	  }
	  
	  //
	  function OnassValueChange(e){
		  var val = e.value;
		  var calcombox = $G.get("caltype");
		  calcombox.setUrl("<%=root%>/param/getDict?key=dop_caltype"+val);
	  }
	</script>
</body>
</html>
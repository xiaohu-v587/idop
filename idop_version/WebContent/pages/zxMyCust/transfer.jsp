
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
	<head>
		<title>客户移交</title>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<%@include file="/common/nuires.jsp" %>
	</head>
<body>    
	<div style="width:100%;height:43%;overflow: hidden">
		<div style="width:100%;height:100%;overflow-y: scroll">
		<form id="form1" method="post">
        	<input id="clas" name="clas" class="nui-hidden"/>
        	<input id="claimids" name="claimids" class="nui-hidden"/>
        	<input name="mgr_ids" class="nui-hidden"/>
        	<input name="mgr_names" class="nui-hidden"/>
        	<input name="claim_props" class="nui-hidden"/>
           	<table style="table-layout: fixed;padding-top: 20px;" id="detailTable"  width="100%">
				<colgroup>
			       	<col width="17%"/>
			       	<col width="32%"/>
			       	<col width="17%"/>
			       	<col width="32%"/>
				</colgroup>
				<tr>
                   	<td style="padding-left: 40px;" colspan="4">批量可移交比例最大值：<span id="claim_prop_max"></span>%</td>
				</tr>
<!-- 				<tr> -->
<!--                    	<td align="right">客户经理：</td> -->
<!--                    	<td align="left">     -->
<!-- 						<input id="mgrId" name="mgrId" class="nui-text" required="true" allowInput="false" style="width:90px;"/> -->
<!--                    		<input id="mgrName" name="mgrName" class="nui-hidden" /> -->
<!--                    		<a class="nui-button" onclick="save">选中</a>  -->
<!-- 					</td> -->
<!-- 					<td align="right">移交比例：</td> -->
<!--                    	<td align="left">     -->
<!-- 						<input id="prop" name="prop" class="nui-text" required="true" style="width:107px;"/>% -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>                                                                                       
					<td align="right">                                                              
						<a class="nui-button" iconCls="icon-addnew" onclick="addtr">添加</a>       
					</td>
					<td align="left" colspan="3">
						<div id="tips" style="color:red;display:none;">错误提示：<span id="msg"></span></div>                                                         
					</td>                                                                           
	             </tr>                                                                    
	             <tbody id="tbody"></tbody>
			</table>
		</form>
		</div>
	</div>
	<div style="width:100%;height:47%; border-top: solid; border-color: #2fa2fd; border-width: 1px;">
	   	<div style="width:100%;height:15%;overflow: hidden">
	   		<form id="form2" method="post">
		   		<table style="table-layout: fixed;" width="100%">
					<colgroup>
				       	<col width="10%"/>
				       	<col width="20%"/>
				       	<col width="12%"/>
				       	<col width="20%"/>
				       	<col width="15%"/>
				       	<col width="15%"/>
					</colgroup>
					<tr>
		                <td align="right">EHR号</td>
						<td align="left">
							<input id="user_no" name="user_no" class="mini-textbox" style="width:100px"/>
						</td>
		                <td align="right">客户经理：</td>
						<td align="left">
							<input id="name" name="name" class="mini-textbox" style="width:100px"/>
						</td>
						<td align="center">
							<a class="mini-button" iconCls="icon-reload" onclick="reset()" >重置</a>
						</td>
						<td align="center">
							<a class="mini-button" iconCls="icon-search" onclick="search()" >查询</a>
						</td>
		            </tr>
		   		</table>
	   		</form>
	   	</div>
	   	<div style="width:100%;height:80%;">
			<div id="datagrid1" class="nui-datagrid" sortMode="client" url="<%=root%>/zxMyCust/getOrgUser" style="width: 100%;height: 100%;"
	    		multiSelect="false" showPager="true" onRowClick="onRowClick">
		        <div property="columns">
		        	<div type="checkcolumn" name="checkCloumn" width="6%"></div>
		        	<div headerAlign="center" width="10%" type="indexcolumn">序号</div>
		            <div field="orgname" width="24%" headerAlign="center" allowSort="true"  align="center">责任中心</div>                
		            <div field="name" width="20%" headerAlign="center" allowSort="true"  align="center">客户经理</div>                
		            <div field="id" width="20%" allowSort="true" headerAlign="center" align="center">EHR号</div> 
		            <div field="cnt" width="20%" allowSort="true" headerAlign="center" align="center">名下客户数</div>
		        </div>
			</div>
		</div>
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
	var form2 = $G.getForm("form2");
	var grid1 =$G.get("datagrid1");
	grid1.load();
	var array=[];
	var toProp=0;//待分配比例
	var propAll=0;//分配比例之和
	var msg;//错误消息
	
	function setData(data){
		var infos = $G.clone(data);
		var org_id = infos.org_id;
		toProp = infos.claimPropMin;
		if(org_id){
			$G.get("clas").setValue(infos.clas);
			$G.get("claimids").setValue(infos.claimids);
			document.getElementById("claim_prop_max").innerHTML= infos.claimPropMin;
			addtr("1");
		}
	}
	
	function onCustChanged(){
		$G.get("mgrName").setValue($G.get("mgrId").getText());
	}
	
	//添加行
	function addtr(flag){
		var trid=radom();
		array.push(trid);
		var htmlstr="<tr id='"+trid+"'> "
				+"	<td align='right'>客户经理：</td> "
				+"	<td align='left'>     "
				+"		<input id='mgrName"+trid+ "' name='mgrName"+trid+ "' onvaluechanged='dataSelCha()' allowInput='false' class='nui-textbox' required='true' style='width:90px;'/> "
				+"		<input id='mgrId' name='mgr"+trid+ "' class='nui-hidden' /> "
				+"		<a class='nui-button' onclick='selRow(\""+trid+ "\")'>选中</a>  "
				+"	</td> "
				+"	<td align='right'>移交比例：</td> "
				+"	<td align='left'>     "
				+"		<input name='prop"+trid+ "' class='nui-textbox' required='true' vtype='int' maxlength='3' style='width:90px;' onvaluechanged='dataProCha()' />% ";	
		if("1"!=flag){
			htmlstr+="<a href='javascript:del(\""+trid+ "\")' class='nui-button' iconCls='icon-remove'>删除</a>";
		}
		htmlstr+="</td></tr>";
		$("#tbody").append(htmlstr);
		$G.parse();
	}
	
	//删除行
	function del(id){
		$("#"+id).remove();
		$G.parse();
		for(var i=0; i< array.length;i++){
			if(array[i]==id){
				array.splice(i,1);
				break;
			}
		}
		dataSelCha();
		dataProCha();
	}
	
	function selRow(trid){
		var row1 = grid1.getSelected();
		if (row1) {
			$G.getbyName("mgr"+trid).setValue(row1.id);
			$G.get("mgrName"+trid).setValue(row1.name);
		}else{
			$G.alert("请选中一条记录");
		}
	}
	
	//分层列表行点击事件
	function onRowClick(){
	}
	
	//查询
	function search(){
		grid1.load(form2.getData());
	}

	//重置
	function reset(){
		form2.reset();
	}
	
	//四位随机数
	function radom(){
		var num='';
		for(var i=0;i<4;i++){
			num+=Math.floor(Math.random()*10);
		}
		return num;
	}
	
// 	//数据校验
// 	function check(){
// 		$("#tips").css("display","none");
// 		if(array.length>0){
// 			for(var i=0; i< array.length;i++){
// 				if($G.getbyName("prop"+array[i]).getValue()){
// 					proAll += parseInt($G.getbyName("prop"+array[i]).getValue());
// 				}
// 			}
// 		}
		
		
// 	}
	
	//将选择的客户经理拼接成字符串
	function dataSelCha(){
		var mgr_names=[];
		var mgr_ids=[];
		msg = "";
		if(array.length>0){
			for(var i=0; i< array.length;i++){
				if($G.getbyName("mgr"+array[i]).getValue()){
					mgr_ids.push($G.getbyName("mgr"+array[i]).getValue());
					mgr_names.push($G.getbyName("mgrName"+array[i]).getValue());
				}
			}
			//根据登录名判断是否重复
			var mgr_idss = mgr_ids.sort();
			for(var j=0; j< mgr_idss.length;j++){
				if(mgr_idss[j]==mgr_idss[j+1]){
					msg= "客户经理选择重复！请重新选择！";
					break;
				}
			}
			$G.getbyName("mgr_ids").setValue(mgr_ids.join(","));
			$G.getbyName("mgr_names").setValue(mgr_names.join(","));
			msgIsShow(msg);
		}
	}
	
	//将填写的分配比例拼接成字符串
	function dataProCha(){
		var claim_props=[];
		msg = "";
		propAll=0;
		if(array.length>0){
			for(var i=0; i< array.length;i++){
				if($G.getbyName("prop"+array[i]).getValue()){
					claim_props.push($G.getbyName("prop"+array[i]).getValue());
					propAll += parseInt($G.getbyName("prop"+array[i]).getValue());
					if(parseInt($G.getbyName("prop"+array[i]).getValue())==0){
						msg= "移交比例不能为0！";
					}
				}
			}
			$G.getbyName("claim_props").setValue(claim_props.join(","));
		}
		if(propAll&&toProp&&parseInt(propAll)>parseInt(toProp)){
			msg= "移交比例之和不能大于批量可移交比例最大值"+toProp+"%！";
		}
		msgIsShow(msg);
	}
	
	//错误消息提示
	function msgIsShow(){
		if(msg){
			document.getElementById("msg").innerHTML=msg;
			$("#tips").css("display","block");
			return;
		}else{
			document.getElementById("msg").innerHTML=msg;
			$("#tips").css("display","none");
		}
	}
	
	/*
	 *保存数据
	 */
	function save(){
		dataSelCha();
		msgIsShow();
		if(msg){
			return;
		}
    	var urlStr = "<%=root%>/zxMyCust/transave";
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
</script>
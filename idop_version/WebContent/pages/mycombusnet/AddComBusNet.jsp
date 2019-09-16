<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 常用业务网址界面
	*
	* @author Liu Dongyuan
	* @date 2018-11-21
-->
	<head>
		<title>常用业务网址界面</title>
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
	</head>
<body>
	<div class="mini-splitter" style="width:100%;height:100%;" vertical="true" borderStyle="border: 0;">
    	<div size="100px;" showCollapseButton="true" style="border: 0;">
        	<div  title="查询条件" style="width: 100%;height: 100%;align:center;">
          		<div id="form1" class="mini-form" align="left">
            		<table id="table1" class="table">
              	<%-- 		<tr>
		                   	<td class="form_label">机构：</td>
		                   	<td align="left">
								<input id="followed_org" required="true" width="283px;" class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
									name="followed_org" textfield="orgname" valuefield="id" parentfield="upid"  valueFromSelect="false" multiSelect="false"
		    					 	expandOnLoad="0" allowInput="false" showClose="true" oncloseclick="" showRadioButton="true" showFolderCheckBox="false"
		       						popupWidth="305" popupHeight="470" popupMaxHeight="600" onvaluechanged="onOrgChange"/>
							</td>
			        
			                <td class="form_label">业务模块:</td>
			                <td align="left" colspan="1">
			                	<input class="mini-combobox"  width="283px;"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
			           	 				id="busi_module" name="busi_module"   onvaluechanged="onBusiModule" valueField="val" textField="remark"></input>
			                </td>
              			</tr> --%>
              			
                        <tr>
		                   	<td class="form_label">网站名称：</td>
		                   	<td align="left">
								<input width="283px;"  class="mini-textbox"  id="networkname" name="networkname" required="true" ></input>
							</td>
			              	<td class="form_label">网址：</td>
		                   	<td align="left">
								<input width="283px;"  class="mini-textbox"  id="webaddress" name="webaddress"  required="true" ></input>
							</td>
              			</tr>

              			
			            <tr height="50px">
			                <td colspan="8" align="center">
			                	<a id="add"  class="mini-button"  onclick="add()">添加</a>
			                </td>
			            </tr>
      				</table>
        		</div>
      		</div>
    	</div>
    	<div showCollapseButton="false" style="border: 0;">
      		<div class="mini-toolbar" style="border-bottom: 0; padding: 0px;">
        		<table style="width: 100%;">
          			<tr>
            			<td style="width: 100%;">
			              	<a id="remove" class="mini-button"  onclick="remove()">删除</a>
            			</td>
          			</tr>
        		</table>
      		</div>
      	<div class="mini-fit">
			<div id="datagrid1" dataField="data" class="mini-datagrid" style="width: 100%; height: 98%;" sortMode="client" emptyText="没有对应的记录"
				allowUnselect="false" url="<%=root%>/mycombusnet/getList" multiSelect="true" allowCellSelect="false"  autoEscape="false"
				  showEmptyText="true">
				<div property="columns">
		            <div type="checkcolumn" ></div>
		            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
		            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
		            <div field="netname" headerAlign="center" width="200" allowSort="true"  align="center" >网站名称</div>
		            <div field="webadd" headerAlign="center" width="350" allowSort="true"  align="center" >网址</div>
	          </div>
	        </div>
      	</div>
    </div>
  </div>
  <script type="text/javascript">
	mini.parse();
  	
  	var grid = mini.get("datagrid1");
  	var form = new mini.Form("form1");
	var keyText = mini.get("keyText"); 

  	grid.load();
  	
  	
  	//业务模块发生变化时操作
  //	function onBusiModule(e){
  		//$G.get("networkname").setValue("");
  		//$G.get("webaddress").setValue("");
  	//}
	
	function onCloseClick(e) {
		var lookup2 = mini.get(e);
		lookup2.hidePopup();
	}
	
	/**
	 * 清空检索条件时
	 */
	function onClearClick(e) {
		var lookup2 = mini.get(e);
		lookup2.deselectAll();
	}
  	


  	//删除算法
  	function remove(){
		var rows = grid.getSelecteds();
  	  	if (rows.length == 0) {
  	  		mini.alert("请至少选择一条数据进行操作！");
  	  		return;
  	  	}
  	  	if(rows.length > 0){
			mini.confirm("确定删除该网址？", "确定？", function(action) {
				if (action == "ok") {	
	   	    		var ids = [];
	                for (var i = 0, l = rows.length; i < l; i++) {
	                    var r = rows[i];
	                    ids.push(r.id); 
	                }
	                var id = ids.join(',');
	                grid.loading("操作中，请稍后......");
					$.ajax({
						url : "<%=root%>/mycombusnet/delete?key=" + id,
						success : function(text) {
				          	   var record = $G.decode(text).record;
				          	   if(record=="0"){
				         		 $G.alert("删除失败!");
				          	   }else{
				          	     $G.alert("删除成功！");
				          	   }
								grid.reload();
						}
					});
				}
			});
  	  	}
  	}
  	
	  //保存数据
	  function add() {
	    //保存
	    var urlStr = "<%=root%>/mycombusnet/save";
		form.validate();
		if (form.isValid() == false){
			mini.alert("请完善表单信息。");
			return;
		}else{
			$.ajax({
				url: urlStr,
				data:form.getData(),
				cache: false,
				success: function (text) {
					var flag = $G.decode(text).flag;
						if(flag == "0"){
							mini.alert("该用户下已经存在该网址,添加失败!!");
						}else{
							mini.alert("添加成功");
							grid.load();
						}
				}
			});
		}
	  }
  	
  	// 查询
  	function search(){
  		grid.load();
  	}
  	
  	// 重置
  	function reset(){
  		form.reset();
  	}
  	
  </script>
</body>
</html>
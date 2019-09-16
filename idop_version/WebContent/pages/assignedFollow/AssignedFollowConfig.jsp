<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<html>
<!--
	* 指定关注配置界面
	*
	* @author 陈佳争
	* @date 2019/05/07
-->
	<head>
		<title>指定关注配置</title>
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
        	<div  title="" style="width: 100%;height: 100%;align:center;">
          		<div id="form1" class="mini-form" align="left">
            		<table id="table1" class="table">
              			<tr>
							<td class="form_label">指定类型：</td>
							<td colspan="1">
			                	<input class="mini-combobox"  width="170px;"  nullItemText="请选择..." emptyText="请选择..."  url="" data="[{id: '1', text: '管理关注'},{id: '2', text: '条线关注'},{id: '3', text: '网点关注'}]"
			           	 				id="assigned_type" name="assigned_type"   onvaluechanged="" valueField="id" textField="text" required="true"></input>
			                </td>
			                <td class="form_label">业务模块:</td>
			                <td colspan="1">
			                	<input class="mini-combobox"  width="170px;"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_ywtype" 
			           	 				id="busi_module" name="busi_module"   onvaluechanged="onBusiModule" valueField="val" textField="remark" required="true" ></input>
			                </td>
              			</tr>
			            <tr>
			                <td class="form_label">关注信息:</td>
			                <td colspan="1">
			                	<input class="mini-combobox" width="170px;"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/param/getDict?key=dop_follow_type" 
			           	 				id="follow_type" name="follow_type"   onvaluechanged="onFollowType" valueField="val" textField="remark" required="true"></input>
			                </td>
			                <td class="form_label">预警子类:</td>
			                <td colspan="1">
			                	<input class="nui-treeselect" width="170px;"  nullItemText="请选择..." emptyText="请选择..."  url="<%=root%>/warning/getWarningTypeList" 
			           	 				id="sub_busi_code" name="sub_busi_code"   dataField="data" onvaluechanged="onSBCode" valueField="id" textField="remark"  parentfield="upid"></input>
			                </td>
			                <td class="form_label">关注名称:</td>
			                <td colspan="1">
			                	<input class="mini-combobox" width="170px;"  nullItemText="请选择..." emptyText="请选择..."  url="" 
			           	 				id="mark_code" name="mark_code"   onvaluechanged="" valueField="warning_code" textField="warning_name" ></input>
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
				allowUnselect="false" url="<%=root%>/assignedFollow/getList" multiSelect="true" allowCellSelect="false"  autoEscape="false"
				  showEmptyText="true">
				<div property="columns">
		            <div type="checkcolumn" ></div>
		            <div type="indexcolumn"  width="40" headerAlign="center">序号</div>
		            <div field="id" headerAlign="center" allowSort="true" visible="false"  align="center">id</div>
		            <div field="describe" headerAlign="center" width="550" allowSort="true"  align="center" >我的关注</div>
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
  	//默认子类选择框不能选择
  	$G.get("sub_busi_code").setReadOnly(true);
  	$G.get("mark_code").setReadOnly(true);
  	grid.load();
  	
  	//子类级联控制
	function onBusiModule(e){
		var val = e.value;
		var calcombox = $G.get("sub_busi_code");
		calcombox.setUrl("<%=root%>/warning/getWarningTypeList?val="+val);
	}
  	
  	//获取名称
  	function onSBCode(e){
  		var val = e.value;
  		var index="2";
//   		var followedteller=$G.get("followed_teller").getValue();
//   		if(followedteller =="" || followedteller == null){
//   			index="2";
//   		}
		var calcombox = $G.get("mark_code");
		calcombox.setUrl("<%=root%>/complexQuery/getWarnNameList?val="+val+"&index="+index);
  	}
  	
  	//关注信息控制子类是否可选
  	function onFollowType(e){
  		//当选择评分时，不允许选子类
  		if(e.value == "1"){
	  		$G.get("sub_busi_code").setReadOnly(true);
	  		$G.get("sub_busi_code").setValue("");
	  		$G.get("sub_busi_code").setText("");
	  		//预警名称是否可选
	  		$G.get("mark_code").setReadOnly(true);
	  		$G.get("mark_code").setValue("");
	  		$G.get("mark_code").setText("");
	  		$G.get("busi_module").setRequired(true);
	  		$G.get("sub_busi_code").setRequired(false);
  			$G.get("mark_code").setRequired(false);
  		}else{
  			$G.get("sub_busi_code").setReadOnly(false);
  			$G.get("mark_code").setReadOnly(false);
  			//选择预警时必填字段
  			$G.get("sub_busi_code").setRequired(true);
  			$G.get("mark_code").setRequired(true);
  		}
  	}

  	//删除指定关注项
  	function remove(){
		var rows = grid.getSelecteds();
  	  	if (rows.length == 0) {
  	  		mini.alert("请至少选择一条数据进行操作！");
  	  		return;
  	  	}
  	  	if(rows.length > 0){
			mini.confirm("确定删除该关注？", "确定？", function(action) {
				if (action == "ok") {	
	   	    		var ids = [];
	                for (var i = 0, l = rows.length; i < l; i++) {
	                    var r = rows[i];
	                    ids.push(r.id); 
	                }
	                var id = ids.join(',');
	                grid.loading("操作中，请稍后......");
					$.ajax({
						url : "<%=root%>/assignedFollow/delete?key=" + id,
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
	    var urlStr = "<%=root%>/assignedFollow/save";
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
						mini.alert("添加失败");
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
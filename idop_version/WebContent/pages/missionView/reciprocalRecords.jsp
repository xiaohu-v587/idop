<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@include file="/common/nuires.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看往来记录</title>
</head>
<body>
<div class="nui-fit">
		<div id="datagrid1" dataField="datas" class="nui-datagrid" style="width: 100%; height: 95%;" url="<%=root%>/missionView/getReciprocalRecordsList?id=${id}"
			autoEscape="false" showEmptyText="true" emptyText="没有对应的记录" pageSize="10" allowCellWrap="true" sortMode="client" allowUnselect="false"
			allowCellSelect="false" allowResize="true" allowSortColumn="true">
			<div property="columns">
				<div headerAlign="center" type="indexcolumn">序号</div>
				<div field="actiontype" width="30px" allowSort="true" headerAlign="center" align="center" >操作方式</div>
				<div field="username" width="40px" allowSort="true" headerAlign="center" align="center" >操作人</div>
				<div field="action_date" width="60px"  allowSort="true" headerAlign="center" align="center"  dateFormat="yyyy-MM-dd ">操作时间</div>
				<div field="action_remark" width="100px" allowSort="true" headerAlign="center" align="center" >操作说明</div>
				<div field="id" renderer="operateRenderer" headerAlign="center" align="center" header="操作"></div>
			</div>
		</div>
	</div>
	
	<div class="nui-toolbar" style="text-align: center; padding-top: 10px; padding-bottom: 10px; margin-bottom: -5px;"
		borderStyle="border-left:0;border-bottom:0;border-right:0;">
			<a id="btnCancle" class="nui-button"  onclick="onCancel()">关闭</a>
	</div>
</body>
</html>
<script type="text/javascript">
		/**
		 * 初始化处理
		 */
		$G.parse(); //将页面的标签转换为可miniui可处理的对象
		var grid = $G.get("datagrid1"); //获取datagrid1标签
		grid.load();
		
		/**
		 *  查看附件
		*/
		 //操作
		 function operateRenderer(e){
			var pid = "${id}";
			var pidtype;
			//判断查看的附件是反馈附件还是退回说明附件  0--反馈  1--退回 
			//模板附件--1   2--上传的说明附件   3--反馈的附件   4--退回附件
			if(e.record.action_type == "0"){
				pidtype = "3";
			} else {
				pidtype = "4";
			}
			var obj = " <a class=\"nui-button\" href='javascript:show(\""+pid+"\",\""+pidtype+"\")' style='text-decoration:none'>附件查看</a>";
			return obj;
		}
		
		 /**
		 * 打开查看页面
		 */
		 function show(pid,pidtype){
			var url = "<%=root%>/missionForUser/retoactionView?id="+pid+"&pidtype="+pidtype;
			var bizParams = {pid:pid,pidtype:pidtype};
			$G.showmodaldialog("查看反馈结果", url,"50%", "40%", bizParams, function(data){
		    });
		}
		
		
		/**
		 * 取消按钮按下时的事件
		 */
		function onCancel(e) {
			CloseWindow("cancel");
		}
		/**
		 * 关闭窗体的事件
		 */
		function CloseWindow(action) {
			if (window.CloseOwnerWindow)
				return window.CloseOwnerWindow(action);
			else
				window.close();
		}

</script>
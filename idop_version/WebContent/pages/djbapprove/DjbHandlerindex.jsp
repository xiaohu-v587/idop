<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ include file="/common/jstlres.jsp"%>
<%@ include file="/common/nuires.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
   * 信息模块配置管理
   *
   * Created on 
   * @author  
   * @reviewer 
-->
<head>
    <title>登记簿目录</title>
	
</head>
<style type="text/css">
    html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
</style>
<body>
	<div  class="mini-toolbar" style="padding: 5px;height:30px;"  id="inner_querypanel">
          <form id="form1">
             <table id="table1">
                <tr>
                   <td >登记簿名称:</td>
		           <td >
		           		<input class="mini-textbox" name="djbmc"></input>&nbsp;&nbsp;
		           </td>
		           <!-- <td style="width:90px;">机构:</td>
					<td style="width:300px;" colspan="3"><input  id="orgid" name="orgid" style="width:300px;"
						class="nui-treeselect" url="<%=root%>/org/getListByUser" dataField="datas" 
							 textfield="orgname" valuefield="id" parentfield="upid" dataField="datas"  
							 valueFromSelect="false" multiSelect="false"
							expandOnLoad="0" emptyText="机构" allowInput="false"
							showClose="true" oncloseclick="onCloseClick" 
							showRadioButton="true" showFolderCheckBox="false"
							popupWidth="286" popupHeight="470" popupMaxHeight="600" /></td>
              		<td > -->
              		 <td align="right" class="labelname">复核状态：</td>
										<td align="left">    
					                       	<input name="status" id="status" class="nui-combobox" url="" enabled="true" data="[{id:1,text:'未复核'},{id:2,text:'已复核'}]"
											                 onvaluechanged="" textfield="text" valuefield="id" emptyText="请选择..." style="width:90%;" value="1"/>
					                   	</td> 
              		<td >
	                	<a class="mini-button"  onclick="search()">查询</a>
	                	&nbsp;&nbsp;
	                	<a class="mini-button"  onclick="reset()">重置</a>
	                	&nbsp;&nbsp;
	                	<a id="btnOk" class="mini-button"  onclick="onOk()" >打开</a>
            		</td>
                </tr>
             </table>
             </form>
            </div>
	 <div class="mini-fit" style="overflow: hidden;">
	      <div id="datagrid1" dataField="data" class="mini-datagrid" style="float: left;width:100%;height:99%;" multiSelect="true"  allowUnselect="false"
		 	url="<%=root%>/djbapprove/getDjbmc"   pageSize="10" showPager="true" autoEscape="false" >
			<div property="columns" >
				<div type="indexcolumn"  width="40" align="center" headerAlign="center">序号 </div>
				<div field="id" visible="false"  align="center"  headerAlign="center" allowSort="true">ID</div>
				<div field="djbmc"  headerAlign="center"  align="center"   allowSort="true" >登记簿名称</div>
				<div field="cjr"  headerAlign="center"  align="center"   allowSort="true" >创建人</div>
				<div field="cjrq"  headerAlign="center"  align="center"   allowSort="true" >创建日期</div>
			</div>
		  </div> 
     </div>
     
</body>
<script type="text/javascript">
      mini.parse();
      var form = new mini.Form("form1");
      var grid = mini.get("datagrid1");
      var status = nui.get("status").getValue();
      grid.load(form.getData(false,true));


    function CloseWindow(action) {        
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();            
    }
    
  //查询
    function search() {
      grid.load(form.getData(false,true));//grid查询
    }

    //重置查询条件
    function reset() {
      form.reset();
    }

    //enter键触发查询
    function onKeyEnter(e) {
      search();
    }
    
    
    //打开按钮
    function onOk() {
    	var rows = grid.getSelected();
    	var status = nui.get("status").getValue();
    	if(rows!=null){
    		var bizData = {
    		        djbid : rows.id,
    		        djbmc : rows.djbmc
    		};
        	var djbmc = rows.djbmc;
        	$G.showmodaldialog("","<%=root%>/djbapprove/list?djbid="+rows.id+"&status="+status,1024, 600,bizData,function (action) {
    	        grid.reload(form.getData(false,true));
    	    });
        }else{
            mini.alert("请选择一条数据！");
        }
        
    }
    
    //取消  
    function onCancel(e) {
        CloseWindow("new");
    }

</script>
</html>
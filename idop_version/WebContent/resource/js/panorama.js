/**
 * 组合自定义表格 头部数据
 * @author 常显阳 20181203
 * @param id
 * @param data {a:b，c:d} 
 */	
function callHeadAndTextToData(id,data){
	var grid = $("#"+id);		
	var children = grid.children();
	var tr0 = $(children[0].children[0]);
	var header = tr0.children();
	var headers = [],columns=[];
	var trs = children[0].children;
	for(var i=0;i<header.length;i++){
			headers.push($(header[i]).attr("field"));
			columns.push($(header[i]).html());
	}
	data["execlheaders"] = headers.join(",");
	data["execlcolumns"] = columns.join(",");
}

/**
 * 组合自定义表格 头部数据
 * @author 常显阳 20181203
 * @param id
 * @param data {a:b，c:d} 
 */	
function callHeadAndTextToDataByDataGrid(grid,data){	
	var gridcolumns = grid.getBottomColumns();
	var headers = [],columns=[];
	for(var i=0;i<gridcolumns.length;i++){
			headers.push(gridcolumns[i].header);
			columns.push(gridcolumns[i].field);
	}
	data["execlheaders"] = headers.join(",");
	data["execlcolumns"] = columns.join(",");
}
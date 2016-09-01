<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>角色管理</title>
<%@ include file="../tag.jsp"%>
<%@ include file="../inc.jsp"%>
<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/editPage')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/delete')}">
	<script type="text/javascript">
		$.canDelete = true;
	</script>
</c:if>
<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/grantPage')}">
	<script type="text/javascript">
		$.canGrant = true;
	</script>
</c:if>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${baseUrl}/role/roleList',
			fit : true,
			fitColumns : false,
			border : false,
			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'name',
			sortOrder : 'asc',
			checkbox : true,
			rownumbers : true,
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect: true,
			//nowrap : false,
			frozenColumns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				//hidden : true,
				checkbox : true
			}, {
				field : 'name',
				title : '角色名称',
				width : 100,
				sortable : true
			} ] ],
			columns : [ [ {
				field : 'permissionIds',
				title : '拥有权限',
				width : 250,
				formatter : function(value, row) {
					if (value) {
						return row.permissionNames;
					}
					return '';
				}
			}, {
				field : 'permissionNames',
				title : '拥有权限名称',
				width : 80,
				hidden : true
			}, {
				field : 'seq',
				title : '排序',
				width : 30
			}, {
				field : 'description',
				title : '备注',
				width : 150
			}, {
				field : 'createdate',
				title : '创建时间',
				width : 150,
				sortable : true
			}, {
				field : 'modifydate',
				title : '最后修改时间',
				width : 150,
				sortable : true
			}, {
				field : 'action',
				title : '操作',
				width : 200,
				formatter : function(value, row, index) {
					var str = '';
					if ($.canEdit) {
						str += $.formatString('<img onclick="editFun(\'{0}\');" src="{1}" title="编辑"/>', row.id, '${baseUrl}/resources/style/images/extjs_icons/pencil.png');
					}
					str += '&nbsp;';
					if ($.canGrant) {
						str += $.formatString('<img onclick="grantFun(\'{0}\');" src="{1}" title="授权"/>', row.id, '${baseUrl}/resources/style/images/extjs_icons/key.png');
					}
					str += '&nbsp;';
					if ($.canDelete) {
						str += $.formatString('<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>', row.id, '${baseUrl}/resources/style/images/extjs_icons/cancel.png');
					}
					return str;
				}
			} ] ],
			toolbar : '#toolbar',
			onLoadSuccess : function() {
				parent.$.messager.progress('close');

				$(this).datagrid('tooltip');
			},
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll').datagrid('uncheckAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加角色',
			width : 500,
			height : 300,
			href : '${baseUrl}/role/addPage',
			buttons : [ {
				width : 72,
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}

	function deleteFun(id) {
		if(id == undefined) {
			var node = dataGrid.datagrid('getSelected');
			id = node.id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		
		parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
			if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post('${baseUrl}/role/delete', {
					id : id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload');
					}
					parent.$.messager.progress('close');
				}, 'JSON');
			}
		});
	}

	function editFun(id) {
		if(id == undefined) {
			var node = dataGrid.datagrid('getSelected');
			id = node.id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		
		parent.$.modalDialog({
			title : '编辑角色',
			width : 500,
			height : 300,
			href : '${baseUrl}/role/editPage?id=' + id,
			buttons : [ {
				width : 72,
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}
	
	function grantFun(id) {
		if(id == undefined) {
			var node = dataGrid.datagrid('getSelected');
			id = node.id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		
		parent.$.modalDialog({
			title : '角色授权',
			width : 500,
			height : 500,
			href : '${baseUrl}/role/grantPage?id=' + id,
			buttons : [ {
				width : 72,
				text : '授权',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#form');
					f.submit();
				}
			} ]
		});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/addPage')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'pencil_add'">添加</a>
		</c:if>
		<a onclick="dataGrid.datagrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'transmit'">刷新</a>
	</div>

	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/addPage')}">
			<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		</c:if>
		<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/delete')}">
			<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		</c:if>
		<c:if test="${rbac:hasPermission(activeUserInfo.id, '/role/editPage')}">
			<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
		</c:if>
	</div>
</body>
</html>
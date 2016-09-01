<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../tag.jsp"%>
<script type="text/javascript">
	var permissionTree;
	$(function() {
		permissionTree = $('#permissionTree').tree({
			url : '${baseUrl}/permission/treePermission',
			parentField : 'pid',
			//lines : true,
			checkbox : true,
			animate : true,
			onClick : function(node) {
			},
			onLoadSuccess : function(node, data) {
				var ids = $.stringToList('${role.permissionIds}');
				
				/* if (ids.length > 0) {
					for ( var i = 0; i < ids.length; i++) {
						var node = permissionTree.tree('find', ids[i]);
						var childnodes = permissionTree.tree('getChildren',node.target);
						if (node && (childnodes != null && childnodes.length == 0)) {//父节点不勾选，只勾选叶子节点
							permissionTree.tree('check', permissionTree.tree('find', ids[i]).target);
						}
					}
				} */
				if (ids.length > 0) {
					for ( var i = 0; i < ids.length; i++) {
						var tmpnode = permissionTree.tree('find', ids[i]);
						var tmpchildnodes = permissionTree.tree('getChildren',tmpnode.target);
						
						if (tmpnode && (tmpchildnodes != null && tmpchildnodes.length == 0)) {
							permissionTree.tree('check', tmpnode.target);
						}
					}
				}
				$('#roleGrantLayout').layout('panel', 'west').panel('setTitle', $.formatString('[{0}]角色可以访问的资源', '${role.name}'));
				parent.$.messager.progress('close');
			},
			cascadeCheck : true
		});

		$('#form').form({
			url : '${baseUrl}/role/grant',
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				var checknodes1 = permissionTree.tree('getChecked', 'indeterminate');//获取实心框的父节点
				var checknodes2 = permissionTree.tree('getChecked');//获取勾选的节点
				var checknodes = checknodes1.concat(checknodes2);
				
				var ids = [];
				if (checknodes && checknodes.length > 0) {
					for ( var i = 0; i < checknodes.length; i++) {
						ids.push(checknodes[i].id);
					}
				}
				$('#permissionIds').val(ids);
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为role.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				}
			}
		});
	});

	function checkAll() {
		var nodes = permissionTree.tree('getChecked', 'unchecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				permissionTree.tree('check', nodes[i].target);
			}
		}
	}
	function uncheckAll() {
		var nodes = permissionTree.tree('getChecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				permissionTree.tree('uncheck', nodes[i].target);
			}
		}
	}
	function checkInverse() {
		var unchecknodes = permissionTree.tree('getChecked', 'unchecked');
		var checknodes = permissionTree.tree('getChecked');
		if (unchecknodes && unchecknodes.length > 0) {
			for ( var i = 0; i < unchecknodes.length; i++) {
				permissionTree.tree('check', unchecknodes[i].target);
			}
		}
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				permissionTree.tree('uncheck', checknodes[i].target);
			}
		}
	}
</script>
<div id="roleGrantLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'west'" title="系统资源" style="width: 300px; padding: 1px;">
		<div class="well well-small">
			<form id="form" method="post">
				<input name="id" type="hidden" value="${role.id}" />
				<ul id="permissionTree"></ul>
				<input id="permissionIds" name="permissionIds" type="hidden" />
			</form>
		</div>
	</div>
	<div data-options="region:'center'" title="" style="overflow: hidden; padding: 10px;">
		<div class="well well-small">
			<span class="label label-success">${role.name}</span>
			<div>${role.description}</div>
		</div>
		<div class="well well-large">
			<button class="btn btn-success" onclick="checkAll();">全选</button>
			<br /> <br />
			<button class="btn btn-warning" onclick="checkInverse();">反选</button>
			<br /> <br />
			<button class="btn btn-inverse" onclick="uncheckAll();">取消</button>
		</div>
	</div>
</div>
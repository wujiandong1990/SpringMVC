<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		
		$('#roleIds').combobox({
			url : '<c:url value="/role/roleList" />',
            valueField : 'id',
            textField : 'name',
            value : $.stringToList('${user.roleIds}'),
            multiple : true,
            panelHeight : 'auto'
		});
		
		$('#form').form({
			url : '<c:url value="/user/grant" />',
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
					parent.$.modalDialog.openner_dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<input name="ids" type="hidden" value="${ids}" />
			<table class="table table-hover table-condensed">
				<tr>
					<%-- <th>编号</th>
					<td><input name="ids" type="text" class="span2" value="${ids}" readonly="readonly"></td> --%>
					<th>所属角色</th>
					<td><select id="roleIds" name="roleIds" style="width: 280px; height: 29px;"></select><img src="<c:url value='/resources/style/images/extjs_icons/cut_red.png' />" onclick="$('#roleIds').combobox('clear');" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
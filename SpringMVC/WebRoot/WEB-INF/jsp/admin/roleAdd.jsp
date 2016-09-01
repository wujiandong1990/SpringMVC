<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../tag.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#icon').combobox({
			data : $.iconData,
			formatter : function(v) {
				return $.formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			}
		});
		
		parent.$.messager.progress('close');
		
		$('#form').form({
			url : '${baseUrl}/role/add',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为role.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
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
			<table class="table table-hover table-condensed">
				<tr>
					<th>角色名称</th>
					<td><input name="name" type="text" placeholder="请输入角色名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
					<th>排序</th>
					<td><input name="seq" value="0" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false,min:0"></td>
				</tr>
				<tr>
					<th>菜单图标</th>
					<td><input id="icon" name="icon" style="width: 140px; height: 29px;" data-options="editable:false" /></td>
					<th>状态</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${statusTypes}" var="statusType">
								<option value="${statusType.value}">${statusType.text}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3"><textarea name="description" rows="" cols="" class="span5"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>
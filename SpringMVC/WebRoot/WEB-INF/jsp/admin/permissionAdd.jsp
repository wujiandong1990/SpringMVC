<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {

		$('#iconCls').combobox({
			data : $.iconData,
			formatter : function(v) {
				return $.formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			}
		});

		$('#pid').combotree({
			url : '<c:url value="/permission/treePermission" />',
			parentField : 'pid',
			lines : true,
			//panelHeight : 'auto',
			onLoadSuccess : function() {
				parent.$.messager.progress('close');
			}
		});

		$('#form').form({
			url : '<c:url value="/permission/add" />',
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
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为permission.jsp页面预定义好了
					parent.layout_west_tree.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
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
					<%-- <th>编号</th>
					<td><input name="id" type="text" class="span2" value="${permission.id}" readonly="readonly"></td> --%>
					<th>权限名称</th>
					<td><input name="name" type="text" placeholder="请输入权限名称" class="easyui-validatebox span2" data-options="required:true" value=""></td>
					<th>权限路径</th>
					<td><input name="url" type="text" placeholder="请输入权限路径" class="easyui-validatebox span2" value=""></td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input name="seq" value="0" class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="editable:false,min:0"></td>
					<th>上级权限</th>
					<td><select id="pid" name="pid" style="width: 140px; height: 29px;"></select><img src="<c:url value='/resources/style/images/extjs_icons/cut_red.png' />" onclick="$('#pid').combotree('clear');" /></td>
				</tr>
				<tr>
					<th>权限类型</th>
					<td><select name="type" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${resourceTypes}" var="resourceType">
								<option value="${resourceType.value}">${resourceType.text}</option>
							</c:forEach>
					</select></td>
					<th>状态</th>
					<td><select name="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${statusTypes}" var="statusType">
								<option value="${statusType.value}">${statusType.text}</option>
							</c:forEach>
						</select></td>
				</tr>
				<tr>
					<th>菜单图标</th>
					<td colspan="3"><input id="iconCls" name="iconCls" style="width: 375px; height: 29px;" data-options="editable:false" /></td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3"><textarea name="description" rows="" cols="" class="span5"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</div>
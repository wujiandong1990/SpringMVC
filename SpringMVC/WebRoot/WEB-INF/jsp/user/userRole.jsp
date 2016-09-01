<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../tag.jsp"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		var data = eval("(" + '${userRoles}' + ")");
		/* $('#userRoles').tree({
			data : data,
			parentField : 'pid',
			onLoadSuccess : function() {
				parent.$.messager.progress('close');
				if (data.length < 1) {
					$('#userRoles').append('<img src="${baseUrl}/resources/style/images/blue_face/bluefaces_35.png" alt="您没有角色" /><div>您没有角色</div>');
				}
			}
		}); */
		var title='';
		$.each($.stringToList(data.roleNames), function(i, rolename) {
			title += '<li>' + rolename + '</li>';
		});
		$('#userRoles').html(title);
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="">
		<c:if test="${activeUserInfo.username == null}">
			<img src="${baseUrl}/resources/style/images/blue_face/bluefaces_35.png" alt="" />
			<div>登录已超时，请重新登录，然后再刷新本功能！</div>
			<script type="text/javascript" charset="utf-8">
				try {
					parent.$.messager.progress('close');
				} catch (e) {
				}
			</script>
		</c:if>
		<c:if test="${activeUserInfo.username != null}">
			<ul id="userRoles" class="easyui-datalist" data-options="fit:true,lines:true,border:false"></ul>
		</c:if>
	</div>
</div>
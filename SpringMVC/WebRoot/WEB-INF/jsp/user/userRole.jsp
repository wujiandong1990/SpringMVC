<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					$('#userRoles').append('<img src="<c:url value='/resources/style/images/blue_face/bluefaces_35.png' />" alt="您没有角色" /><div>您没有角色</div>');
				}
			}
		}); */
		var title='';
		$.each($.stringToList(data.roleNames), function(i, rolename) {
			title += '<li>';
			title += '<p>' + rolename + '</p>';
			title += '</li>';
		});
		$('#userRoles').html(title);
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="">
		<c:if test="${activeUserInfo.username == null}">
			<img src="<c:url value='/resources/style/images/blue_face/bluefaces_35.png' />" alt="" />
			<div>登录已超时，请重新登录，然后再刷新本功能！</div>
			<script type="text/javascript" charset="utf-8">
				try {
					parent.$.messager.progress('close');
				} catch (e) {
				}
			</script>
		</c:if>
		<c:if test="${activeUserInfo.username != null}">
			<ul id="userRoles"></ul>
		</c:if>
	</div>
</div>
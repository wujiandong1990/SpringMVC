<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>SpringMVC</title>
<jsp:include page="inc.jsp" />
<script type="text/javascript" charset="utf-8">
	var loginDialog;
	$(function() {
		$('#form input').keyup(function(event) {
			if (event.keyCode == '13') {
				loginFun();
			}
		});
		
		$('#form').form({
			url : '<c:url value="/user/login" />',
			onSubmit : function() {
				$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					window.location.href = '<c:url value="/index" />';
				} else {
					$('#tip').html(result.msg);
					$('#tipline').show();
				}
			}
		});
		
	});
	
	function loginFun() {
		$('#form').submit();
	}
	
	function resetFun() {
		$('#form').form('reset');
		$('#tipline').hide();
	}
</script>
</head>
<body>
	<div class="easyui-window" title="用户登录" data-options="collapsible:false,minimizable:false,maximizable:false,closable:false,draggable:false,iconCls:'icon-save',footer:'#footer'" style="width:350px;height:290px;">
        <div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north'" style="height: 70px; overflow: hidden;font-size:20px;line-height:70px;text-align:center;">
				快速开发框架
			</div>
			<div data-options="region:'center'" style="width: 350px; height: 220px; overflow: hidden;">
				<div style="overflow: hidden; padding: 25px 20px;">
				<form id="form" method="post" class="form-horizontal">
				
					<table class="table-hover table-condensed">
						<tr>
							<th>用&nbsp;户&nbsp;名&nbsp;&nbsp;</th>
							<td><input name="username" type="text" placeholder="请输入用户名" class="easyui-validatebox" data-options="required:true" value="wjd"></td>
						</tr>
						<tr>
							<th>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码&nbsp;&nbsp;</th>
							<td><input name="password" type="password" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true" value="123456"></td>
						</tr>
						<tr>
							<td colspan="2" id="tipline" style="color:red;font-size:14px;text-align:center;display:none;"><span id="tip"></span></td>
						</tr>
					</table>
				</form>
				</div>
			</div>
			<div id="footer" style="text-align:right;padding:6px;">
                <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="javascript:resetFun()" style="width:80px">重置</a>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="javascript:loginFun()" style="width:80px">登录</a>
			</div>
		</div>
    </div>
</body>
</html>
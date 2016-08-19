<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="<c:url value="/resources/jslib/extBrowser.js"/>" charset="utf-8"></script>

<!-- 引入my97日期时间控件 -->
<script type="text/javascript" src="<c:url value="/resources/jslib/My97DatePicker4.8b3/My97DatePicker/WdatePicker.js"/>" charset="utf-8"></script>

<!-- 引入kindEditor插件 -->
<link rel="stylesheet" href="<c:url value="/resources/jslib/kindeditor-4.1.7/themes/default/default.css"/>" type="text/css">
<script type="text/javascript" src="<c:url value="/resources/jslib/kindeditor-4.1.7/kindeditor-all-min.js"/>" charset="utf-8"></script>

<!-- 引入jQuery -->
<script type="text/javascript" src="<c:url value="/resources/jslib/jquery-1.8.3.js"/>" charset="utf-8"></script>

<!-- 引入Highcharts -->
<script type="text/javascript" src="<c:url value="/resources/jslib/Highcharts-3.0.1/js/highcharts.js"/>" charset="utf-8"></script>

<!-- 引入bootstrap样式 -->
<%-- <link rel="stylesheet" href="<c:url value="/resources/jslib/bootstrap-2.3.1/css/bootstrap.min.css"/>" type="text/css" media="screen"> --%>
<!-- <script type="text/javascript" src="<c:url value="/resources/jslib/bootstrap-2.3.1/js/bootstrap.min.js"/>" charset="utf-8"></script> -->
<link rel="stylesheet" href="<c:url value="/resources/jslib/bootstrap-2.3.2/css/bootstrap.min.css"/>" type="text/css" media="screen">

<!-- 引入EasyUI -->
<%-- <link id="easyuiTheme" rel="stylesheet" href="<c:url value="/resources/jslib/jquery-easyui-1.3.3/themes/bootstrap/easyui.css"/>" type="text/css"> --%>
<%-- <link rel="stylesheet" href="<c:url value="/resources/jslib/jquery-easyui-1.3.3/themes/icon.css"/>" type="text/css"> 55555--%>
<%-- <script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"/>" charset="utf-8"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"/>" charset="utf-8"></script> --%>
<!-- 修复EasyUI1.3.3中layout组件的BUG -->
<%-- <script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-1.3.3/plugins/jquery.layout.js"/>" charset="utf-8"></script> --%>
<link id="easyuiTheme" rel="stylesheet" href="${pageContext.request.contextPath}/resources/jslib/jquery-easyui-1.5/themes/<c:out value="${cookie.easyuiThemeName.value}" default="bootstrap"/>/easyui.css" type="text/css">
<link rel="stylesheet" href="<c:url value="/resources/jslib/jquery-easyui-1.5/themes/icon.css"/>" type="text/css">
<script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-1.5/jquery.easyui.min.js"/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-1.5/locale/easyui-lang-zh_CN.js"/>" charset="utf-8"></script>

<!-- 引入EasyUI Portal插件 -->
<link rel="stylesheet" href="<c:url value="/resources/jslib/jquery-easyui-portal/portal.css"/>" type="text/css">
<script type="text/javascript" src="<c:url value="/resources/jslib/jquery-easyui-portal/jquery.portal.js"/>" charset="utf-8"></script>

<!-- 扩展EasyUI -->
<script type="text/javascript" src="<c:url value="/resources/jslib/extEasyUI.js"/>" charset="utf-8"></script>

<!-- 扩展EasyUI Icon -->
<link rel="stylesheet" href="<c:url value="/resources/style/extEasyUIIcon.css"/>" type="text/css">

<!-- 扩展jQuery -->
<script type="text/javascript" src="<c:url value="/resources/jslib/extJquery.js"/>" charset="utf-8"></script>
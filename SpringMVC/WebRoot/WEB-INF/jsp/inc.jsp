<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="tag.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="${baseUrl}/resources/jslib/extBrowser.js" charset="utf-8"></script>

<!-- 引入my97日期时间控件 -->
<script type="text/javascript" src="${baseUrl}/resources/jslib/My97DatePicker4.8b3/My97DatePicker/WdatePicker.js" charset="utf-8"></script>

<!-- 引入kindEditor插件 -->
<link rel="stylesheet" href="${baseUrl}/resources/jslib/kindeditor-4.1.7/themes/default/default.css" type="text/css">
<script type="text/javascript" src="${baseUrl}/resources/jslib/kindeditor-4.1.7/kindeditor-all-min.js" charset="utf-8"></script>

<!-- 引入jQuery -->
<script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-2.0.0.js" charset="utf-8"></script>

<!-- plupload文件上传插件 -->
<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/jslib/plupload-2.1.9/js/jquery-ui-1.10.2/jquery-ui.min.css" media="screen" />
<script type="text/javascript" src="${baseUrl}/resources/jslib/plupload-2.1.9/js/jquery-ui-1.10.2/jquery-ui.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${baseUrl}/resources/jslib/plupload-2.1.9/js/plupload.full.min.js" charset="UTF-8"></script>
<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/jslib/plupload-2.1.9/js/jquery.ui.plupload/css/jquery.ui.plupload.css" media="screen" />
<script type="text/javascript" src="${baseUrl}/resources/jslib/plupload-2.1.9/js/jquery.ui.plupload/jquery.ui.plupload.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/jslib/plupload-2.1.9/js/i18n/zh_CN.js"></script>

<!-- 引入Highcharts -->
<script type="text/javascript" src="${baseUrl}/resources/jslib/Highcharts-3.0.1/js/highcharts.js" charset="utf-8"></script>

<!-- 引入bootstrap样式 -->
<%-- <link rel="stylesheet" href="${baseUrl}/resources/jslib/bootstrap-2.3.1/css/bootstrap.min.css" type="text/css" media="screen"> --%>
<!-- <script type="text/javascript" src="${baseUrl}/resources/jslib/bootstrap-2.3.1/js/bootstrap.min.js" charset="utf-8"></script> -->
<link rel="stylesheet" href="${baseUrl}/resources/jslib/bootstrap-2.3.2/css/bootstrap.min.css" type="text/css" media="screen">

<!-- 引入EasyUI -->
<%-- <link id="easyuiTheme" rel="stylesheet" href="${baseUrl}/resources/jslib/jquery-easyui-1.3.3/themes/bootstrap/easyui.css" type="text/css"> --%>
<%-- <link rel="stylesheet" href="${baseUrl}/resources/jslib/jquery-easyui-1.3.3/themes/icon.css" type="text/css"> 55555--%>
<%-- <script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js" charset="utf-8"></script> --%>
<%-- <script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js" charset="utf-8"></script> --%>
<!-- 修复EasyUI1.3.3中layout组件的BUG -->
<%-- <script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-1.3.3/plugins/jquery.layout.js" charset="utf-8"></script> --%>
<link id="easyuiTheme" rel="stylesheet" href="${baseUrl}/resources/jslib/jquery-easyui-1.5/themes/<c:out value="${cookie.easyuiThemeName.value}" default="bootstrap"/>/easyui.css" type="text/css">
<link rel="stylesheet" href="${baseUrl}/resources/jslib/jquery-easyui-1.5/themes/icon.css" type="text/css">
<script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-1.5/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-1.5/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<!-- 引入EasyUI Portal插件 -->
<link rel="stylesheet" href="${baseUrl}/resources/jslib/jquery-easyui-portal/portal.css" type="text/css">
<script type="text/javascript" src="${baseUrl}/resources/jslib/jquery-easyui-portal/jquery.portal.js" charset="utf-8"></script>

<!-- 扩展EasyUI -->
<script type="text/javascript" src="${baseUrl}/resources/jslib/extEasyUI.js" charset="utf-8"></script>

<!-- 扩展EasyUI Icon -->
<link rel="stylesheet" href="${baseUrl}/resources/style/extEasyUIIcon.css" type="text/css">

<!-- 扩展jQuery -->
<script type="text/javascript" src="${baseUrl}/resources/jslib/extJquery.js" charset="utf-8"></script>
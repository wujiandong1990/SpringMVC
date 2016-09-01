<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通springmvc上传和plupload上传</title>
<%@ include file="../inc.jsp"%>
<script type="text/javascript">
$(function(){
	var uploader = new plupload.Uploader({
		runtimes : 'gears,html5,html4,silverlight,flash',//运行环境，按顺序优先加载
		browse_button : 'pickfiles', //打开文件浏览窗口
		container: 'container', //哪一个容器
		max_file_size : '1gb',//最大文件大小，100b, 10kb, 10mb, 1gb
		chunk_size : '100mb',//分块大小，小于这个大小的不分块
		url: '${baseUrl}/file/ajaxUploadFile',//上传地址
		flash_swf_url : '${baseUrl}/resources/jslib/plupload-2.1.9/js/Moxie.swf',//FLASH控件加载地址
		silverlight_xap_url : '${baseUrl}/resources/jslib/plupload-2.1.9/js/Moxie.xap',//Sliverlight控件加载地址
		//resize : { width : 320, height : 240, quality : 90 },//如果可以的话压缩图片
		multi_selection: true,//是否可以多选文件(多文件上传)
		filters : [
			{title : "文档文件(doc,docx,xls,xlsx,ppt,htm,html,txt)", extensions : "doc,docx,xls,xlsx,ppt,htm,html,txt"},
			{title : "图片文件(jpg,gif,png,jpeg)", extensions : "jpg,gif,png,jpeg"},
			{title : "压缩文件(zip,rar)", extensions : "zip,rar"}
		],//文件过滤器

		init: {
			PostInit: function(up) {//初始化时
				$('#filelist').html("已检测到支持的运行环境："+up.runtime);
				
				$('#uploadfiles').get(0).onclick = function() {//给上传按钮绑定点击事件
					uploader.start();
					return false;
				};
			},

			FilesAdded: function(up, files) {//选择文件后
				plupload.each(files, function(file) {
					$('#filelist').get(0).innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
				});
			},

			UploadProgress: function(up, file) {//上传文件时的进度
				$('#'+file.id).get(0).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
			},
			
			FileUploaded: function(up, file, info){//上传完成后执行服务器的返回信息
				var response = $.parseJSON(info.response);
				alert(response.msg);
			},

			Error: function(up, err) {//出错时
				$('#console').get(0).innerHTML += "\nError #" + err.code + ": " + err.message;
			}
		}
	});

	uploader.init();
});
</script>
</head>
<body>
    <h1>普通非AJAX上传文件</h1>
  	<form id="formMain" action="file/speedUploadFile" method="post" enctype="multipart/form-data">
  		选择文件：<input type="file" name="file"/>
  		<br/>
  		<input type="submit" value="上传"/>
  	</form>
  	<br/>
  	
  	<h1>AJAX上传文件之Plupload2.1.2 上传实例</h1>
	<div id="filelist">你的浏览器不支持上传文件</div>
	<br />
	
	<div id="container">
	    <button id="pickfiles">选择文件</button> 
	    <button id="uploadfiles">上传</button>
	</div>
	<br />
	
	<pre id="console"></pre>
</body>
</html>
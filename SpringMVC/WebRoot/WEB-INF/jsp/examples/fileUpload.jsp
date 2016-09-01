<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>文件上传下载</title>
<%@ include file="../inc.jsp"%>
<script type="text/javascript">
$(function() {
	$("#uploader").plupload({
        // General settings
        runtimes : 'html5,flash,silverlight,html4',
        url : '${baseUrl}/file/ajaxUploadFile',
 
        // Maximum file size
        max_file_size : '10mb',
 
        chunk_size: '5mb',
 
        // Specify what files to browse for
        filters : [
            {title : "文档文件(doc,docx,xls,xlsx,ppt,htm,html,txt)", extensions : "doc,docx,xls,xlsx,ppt,htm,html,txt"},
			{title : "图片文件(jpg,gif,png,jpeg)", extensions : "jpg,gif,png,jpeg"},
			{title : "压缩文件(zip,rar)", extensions : "zip,rar"}
        ],
        
        // Files not allow duplicates
        prevent_duplicates : true,
 
        // Rename files by clicking on their titles
        rename: true,
         
        // Sort files
        sortable: true,
 
        // Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
        dragdrop: true,
 
        // Views to activate
        views: {
            list: true,
            thumbs: true, // Show thumbs
            active: 'thumbs'
        },
 
        // Flash settings
       	flash_swf_url : '${baseUrl}/resources/jslib/plupload-2.1.9/js/Moxie.swf',
     	// Silverlight settings
   		silverlight_xap_url : '${baseUrl}/resources/jslib/plupload-2.1.9/js/Moxie.xap'
    });
	
});
</script>
</head>
<body>
	<div id="uploader">
		<p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>
	</div>
</body>
</html>
package com.gtjt.xxjss.springmvc.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.gtjt.xxjss.springmvc.annotation.CustomLog;
import com.gtjt.xxjss.springmvc.common.GlobalConstants;
import com.gtjt.xxjss.springmvc.common.util.ResourcesUtil;
import com.gtjt.xxjss.springmvc.model.UploadFile;
import com.gtjt.xxjss.springmvc.service.UploadFileServiceI;
import com.gtjt.xxjss.springmvc.web.vo.ActiveUserInfo;

/**
 * 文件控制器
 * 
 * @author Wjd
 * 
 */
@Controller
@RequestMapping("/file")
public class FileController {
	private static Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private UploadFileServiceI uploadFileService;
	
    @RequestMapping("upload")
    public void exec(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	response.setCharacterEncoding("UTF-8");
    	
    	String repositoryPath = FileUtils.getTempDirectoryPath();
    	String uploadPath = request.getSession().getServletContext().getRealPath("") + "/uploadPath/";
		File up = new File(uploadPath);
		if(!up.exists()){
			up.mkdirs();
		}
		Integer schunk = null;//分割块数
		Integer schunks = null;//总分割数
		String name = null;//文件名
		BufferedOutputStream outputStream=null; 
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024);
				factory.setRepository(new File(repositoryPath));//设置临时目录
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				upload.setSizeMax(5 * 1024 * 1024);//设置附近大小
				List<FileItem> items = upload.parseRequest(request);
				//生成新文件名
				String newFileName = null;
				for (FileItem item : items) {
					if (!item.isFormField()) {// 如果是文件类型
						name = item.getName();// 获得文件名
						newFileName = UUID.randomUUID().toString().replace("-","").concat(".").concat(FilenameUtils.getExtension(name));
						if (name != null) {
							String nFname = newFileName;
							if (schunk != null) {
								nFname = schunk + "_" + name;
							}
							File savedFile = new File(uploadPath, nFname);
							item.write(savedFile);
						}
					} else {
						//判断是否带分割信息
						if (item.getFieldName().equals("chunk")) {
							schunk = Integer.parseInt(item.getString());
						}
						if (item.getFieldName().equals("chunks")) {
							schunks = Integer.parseInt(item.getString());
						}
					}
				}
				
				if (schunk != null && schunk + 1 == schunks) {
					outputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath, newFileName)));
					//遍历文件合并
					for (int i = 0; i < schunks; i++) {
						File tempFile=new File(uploadPath,i+"_"+name);
						byte[] bytes=FileUtils.readFileToByteArray(tempFile);  
						outputStream.write(bytes);
						outputStream.flush();
						tempFile.delete();
					}
					outputStream.flush();
				}
				response.getWriter().write("{\"status\":true,\"newName\":\""+newFileName+"\"}");
			} catch (FileUploadException e) {
				e.printStackTrace();
				response.getWriter().write("{\"status\":false}");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("{\"status\":false}");
			}finally{  
	            try {  
	            	if(outputStream!=null)
	            		outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }   
		}
    }
	
	@RequestMapping(value = "/fileUploadPage")
	public String index(HttpServletRequest request) {
		return "/examples/fileUpload";
	}
	
	//使用AJAX上传文件，支持多文件上传，已与Plupload2.1.9插件测试通过
	@CustomLog
	@RequestMapping("/ajaxUploadFile")
	@ResponseBody 
	public Map<String, Object> ajaxLoadFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getServletContext());
		Map<String, Object> map = new HashMap<String, Object>();
		
		String oriFileName = null;//原始文件名
		String newFileName = null;//新文件名
		long fileSize = 0L;//文件大小
		String fileType = null;//文件类型
		
		//request中是否是Multipart类型数据，如果不是就不处理，是的话就转化成SPRINGMVC中的Mulitipart Request
		if(cmr.isMultipart(request)) {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
			//迭代所有文件
			Iterator<String> it = mreq.getFileNames();
			while(it.hasNext()) {
				//使用SpringMVC中的FILE
				MultipartFile file = mreq.getFile(it.next());
				if(!file.isEmpty()) {
					oriFileName = file.getOriginalFilename();
					fileSize = file.getSize();
					fileType = file.getContentType();
					newFileName = UUID.randomUUID().toString().replace("-","").concat(".").concat(FilenameUtils.getExtension(oriFileName));
					
					//检查文件目录，不存在则创建
					String relativePath = "/upload/";
					String savePath = request.getSession().getServletContext().getRealPath("") + relativePath;
					File folder = new File(savePath);
					if (!folder.exists()) {
						folder.mkdirs();
					}
					
					//文件路径+文件名 
					File localFile = new File(folder, newFileName);
					//使用SpringMVC中的Multipart File来存储文件
					file.transferTo(localFile);
					
					//保存上传文件信息
					ActiveUserInfo activeUserInfo = (ActiveUserInfo) request.getSession().getAttribute(GlobalConstants.ACTIVEUSERINFO);
					UploadFile uploadFile = new UploadFile();
					uploadFile.setOrifilename(oriFileName);
					uploadFile.setNewfilename(newFileName);
					uploadFile.setFilesize(new Integer((int) fileSize));
					uploadFile.setFiletype(fileType);
					uploadFile.setSaveurl(relativePath + newFileName);
					uploadFile.setCreatedate(new Date());
					uploadFile.setCreateuser(activeUserInfo.getUsername());
					uploadFileService.add(uploadFile);
					
					map.put("status", true);
					map.put("filename", oriFileName);
					
				}
			}
		}
		return map;
	}
	
	
	/**
	 * 下面两种方法是普通SpringMVC的文件上传
	 *
	 *
	 * 
	 * 
	 */
	//此种上传效率低
	@RequestMapping("/uploadFile")
	public ModelAndView uploadFile(@RequestParam("file") CommonsMultipartFile file) throws Exception{ //使用SPRINGMVC中的FILE类去接收，注意如果不用REQUEST需要添加参数注解指明
		System.out.println(file.getSize());
		System.out.println(file.getOriginalFilename());
		if(!file.isEmpty()){
			FileOutputStream fos = new FileOutputStream("D:/" + new Date().getTime() + "_" + file.getOriginalFilename());
			InputStream in = file.getInputStream();
			int	a = 0;
			while((a = in.read()) != -1){
				fos.write(a);
			}
			fos.flush();
			fos.close();
			in.close();
		}
		log.info ("上传成功");
		return new ModelAndView("/uploadsuccess");
	}
		
	//使用SpringMVC的Multipart上传套件来进行上传，效率很高
	@RequestMapping("/speedUploadFile")
	public String speedUploadFile(HttpServletRequest request) throws IllegalStateException, IOException{
		CommonsMultipartResolver cmr = new CommonsMultipartResolver(request.getServletContext());
		//request中是否是Multipart类型数据，如果不是就不处理，是的话就转化成SPRINGMVC中的Mulitipart Request
		if(cmr.isMultipart(request)){
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
			//迭代所有文件
			Iterator<String> it = mreq.getFileNames();
			while(it.hasNext()){
				//使用SpringMVC中的FILE
				MultipartFile file = mreq.getFile(it.next());
				if(!file.isEmpty()){
					System.out.println(file.getOriginalFilename());
					String fileName = "Upload_"+file.getOriginalFilename();
					String path = "D:/" + fileName;
					
					File localFile = new File(path);
					//使用SpringMVC中的Multipart File来存储文件
					file.transferTo(localFile);
				}
			}
		}
		return "/uploadsuccess";
	}

		
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 富文本编辑器上传资源
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * 浏览器服务器附件
	 * 
	 * @param response
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/fileManage")
	@ResponseBody
	public Map<String, Object> fileManage(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		Map<String, Object> m = new HashMap<String, Object>();

		// 根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = session.getServletContext().getRealPath("/") + "attached/";

		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl = request.getContextPath() + "/attached/";

		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		String dirName = request.getParameter("dir");
		if (dirName != null) {
			if (!Arrays.<String> asList(new String[] { "image", "flash", "media", "file" }).contains(dirName)) {
				// out.println("Invalid Directory name.");
				// return;
				try {
					response.getWriter().write("无效的目录名称！");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return m;
			}
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}

		// 根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		// 排序形式，name or size or type
		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			// out.println("Access is not allowed.");
			// return;
			try {
				response.getWriter().write("不允许访问！");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return m;
		}
		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			// out.println("Parameter is not valid.");
			// return;
			try {
				response.getWriter().write("参数无效！");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return m;
		}
		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			// out.println("Directory does not exist.");
			// return;
			try {
				response.getWriter().write("目录不存在！");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return m;
		}

		// 遍历目录取的文件信息
		List<Hashtable<String, Object>> fileList = new ArrayList<Hashtable<String, Object>>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object a, Object b) {
					Hashtable hashA = (Hashtable) a;
					Hashtable hashB = (Hashtable) b;
					if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
							return 1;
						} else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
							return -1;
						} else {
							return 0;
						}
					}
				}
				
			});
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object a, Object b) {
					Hashtable hashA = (Hashtable) a;
					Hashtable hashB = (Hashtable) b;
					if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
					}
				}
				
			});
		} else {
			Collections.sort(fileList, new Comparator() {

				@Override
				public int compare(Object a, Object b) {
					Hashtable hashA = (Hashtable) a;
					Hashtable hashB = (Hashtable) b;
					if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
						return -1;
					} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
						return 1;
					} else {
						return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
					}
				}
				
			});
		}
		m.put("moveup_dir_path", moveupDirPath);
		m.put("current_dir_path", currentDirPath);
		m.put("current_url", currentUrl);
		m.put("total_count", fileList.size());
		m.put("file_list", fileList);

		return m;
	}
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param session
	 * @return
	 */
//	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("error", 1);
		m.put("message", "上传失败！");
		// 文件保存目录路径
		String savePath = session.getServletContext().getRealPath("/") + "attached/";

		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/attached/";

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", ResourcesUtil.getValue("config", "image"));
		extMap.put("flash", ResourcesUtil.getValue("config", "flash"));
		extMap.put("media", ResourcesUtil.getValue("config", "media"));
		extMap.put("file", ResourcesUtil.getValue("config", "file"));

		long maxSize = Long.parseLong(ResourcesUtil.getValue("config", "maxFileSize")); // 允许上传最大文件大小(字节)

		if (!ServletFileUpload.isMultipartContent(request)) {
			m.put("error", 1);
			m.put("message", "请选择文件！");
			return m;
		}

		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			uploadDir.mkdirs();
		}

		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			m.put("error", 1);
			m.put("message", "上传目录没有写权限！");
			return m;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			m.put("error", 1);
			m.put("message", "目录名不正确！");
			return m;
		}

		// 创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat yearDf = new SimpleDateFormat("yyyy");
		SimpleDateFormat monthDf = new SimpleDateFormat("MM");
		SimpleDateFormat dateDf = new SimpleDateFormat("dd");
		Date date = new Date();
		String ymd = yearDf.format(date) + "/" + monthDf.format(date) + "/" + dateDf.format(date) + "/";
		savePath += ymd;
		saveUrl += ymd;
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		if (ServletFileUpload.isMultipartContent(request)) {// 判断表单是否存在enctype="multipart/form-data"
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					String fileName = item.getName();
					if (!item.isFormField()) {
						// 检查文件大小
						if (item.getSize() > maxSize) {
							m.put("error", 1);
							m.put("message", "上传文件大小超过限制！(允许最大[" + maxSize + "]字节，您上传了[" + item.getSize() + "]字节)");
							return m;
						}
						// 检查扩展名
						String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
						if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {
							m.put("error", 1);
							m.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式！");
							return m;
						}

						String newFileName = UUID.randomUUID().toString() + "." + fileExt;
						try {
							File uploadedFile = new File(savePath, newFileName);
							item.write(uploadedFile);
						} catch (Exception e) {
							m.put("error", 1);
							m.put("message", "上传文件失败！");
							return m;
						}

						m.put("error", 0);
						m.put("url", saveUrl + newFileName);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}

		return m;
	}

}

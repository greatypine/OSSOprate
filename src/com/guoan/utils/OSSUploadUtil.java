package com.guoan.utils;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
 
/**
 * 
 * @ClassName: OSSUploadUtil
 * @Description: 阿里云OSS文件上传工具类
 * @author AggerChen
 * @date 2016年11月3日 下午12:03:24
 */
public class OSSUploadUtil {
	
	private static OSSConfig config = null;
 
	/**
	 * 
	 * @MethodName: uploadFile
	 * @Description: OSS单文件上传
	 * @param file	
	 * @param fileType 文件后缀
	 * @param savePath 保存的地址
	 * @return String 文件地址
	 */
	public static String uploadFile	(File file,String fileType , String savePath){
		
//		String fileName = savePath +UUID.randomUUID().toString().toUpperCase().replace("-", "")+"."+fileType;	//文件名，根据UUID来
		String fileName = savePath +file.getName();
		return putObject(file,fileType,fileName);
	}
	
	
	
	/**
	 * 
	 * @MethodName: putObject
	 * @Description: 上传文件
	 * @param file
	 * @param fileType
	 * @param fileName
	 * @return String
	 */
	private static String putObject(File file,String fileType,String fileName){
		config = config==null?new OSSConfig():config;
		
		// 创建ClientConfiguration实例
		ClientConfiguration conf = new ClientConfiguration();
		// 配置代理为本地8080端口
		conf.setProxyHost("proxy.guoanshequ.com");
		conf.setProxyPort(3128);
		
		String url = null;		//默认null
		OSSClient ossClient = null;  
		try {
			ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret(),conf); 
			InputStream input = new FileInputStream(file);  
			ObjectMetadata meta = new ObjectMetadata();				// 创建上传Object的Metadata
			meta.setContentType(OSSUploadUtil.contentType(fileType));		// 设置上传内容类型
			meta.setCacheControl("no-cache");				// 被下载时网页的缓存行为
			PutObjectRequest request = new PutObjectRequest(config.getBucketName(), fileName,input,meta);			//创建上传请求
			//上传
			ossClient.putObject(request); 
			url = config.getEndpoint().replaceFirst("http://","http://"+config.getBucketName()+".")+"/"+fileName;		//上传成功再返回的文件路径
		} catch (OSSException oe) {
            oe.printStackTrace();
            return null;
        } catch (ClientException ce) {
        	ce.printStackTrace();
        	return null;
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	return null;
		} finally {
            ossClient.shutdown();
        }
		return url;
	}
	
	/**
	 * 
	 * @MethodName: contentType
	 * @Description: 获取文件类型
	 * @param FileType
	 * @return String
	 */
	private static String contentType(String fileType){
		fileType = fileType.toLowerCase();
		String contentType = "";
		switch (fileType) {
		case "bmp":	contentType = "image/bmp";
					break;
		case "gif":	contentType = "image/gif";
					break;
		case "png":	
		case "jpeg":	
		case "jpg":	contentType = "image/jpeg";
					break;
		case "html":contentType = "text/html";
					break;
		case "txt":	contentType = "text/plain";
					break;
		case "vsd":	contentType = "application/vnd.visio";
					break;
		case "ppt":	
		case "pptx":contentType = "application/vnd.ms-powerpoint";
					break;
		case "doc":	
		case "docx":contentType = "application/msword";
					break;
		case "xml":contentType = "text/xml";
					break;
		case "mp4":contentType = "video/mp4";
					break;
		default: contentType = "application/octet-stream";
					break;
		}
		return contentType;
     }  
	
	/**
	 * 
	 * @MethodName: getFileName
	 * @Description: 根据url获取fileName
	 * @param fileUrl 文件url
	 * @return String fileName
	 */
	private static String getFileName(String fileUrl){
		String str = "aliyuncs.com/";
		int beginIndex = fileUrl.indexOf(str);
		if(beginIndex==-1) return null;
		return fileUrl.substring(beginIndex+str.length());
	}
	
}
 

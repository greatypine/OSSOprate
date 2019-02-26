package com.guoan.run;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.guoan.utils.OSSUploadUtil;

public class Run {

	/*
	 * args[0] 文件绝对路径
	 * args[1] 获取文件类型(日报还是月报)
	 * args[2] 获取城市标志
	 */
	public static void main(String[] args) throws Exception {
		
		
		for(String str : args){
			System.out.println(str);
		}
		
		//文件绝对路径
		String filePath =args[0];	
		//获取文件扩展名
		String fileExtenSionName = filePath.split("\\.")[1];
		//获取文件类型(日报还是月报)
		String fileType = args[1];
		//获取城市标志
		String cityTag = args[2];
		
		String savePath = getSavePath(fileType,cityTag);
		
		File file = new File(filePath);
		System.out.println(file.getName());
		String url = OSSUploadUtil.uploadFile(file, fileExtenSionName , savePath);
		System.out.println(url);
		
	}
	
	/**
	  * Title: getSavePath
	  * Description: 获取存储名
	  * @param fileType
	  * @param cityTag
	  * @return
	 */
	public static String getSavePath(String fileType , String cityTag){
		
		SimpleDateFormat dailySdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");
		
		String savePath = "";
		//判断是日报还是月报
		if("daily".equals(fileType) || "report_daily".equals(fileType) || "report_week".equals(fileType) ){
			Date today  = new Date();
			String yesterday = dailySdf.format(new Date(today.getTime() - 86400000L));
			///root/microAccount/result/daily/2018-10-23/SJZ
			savePath = "daqWeb/microAccount/"+fileType+"/"+yesterday+"/"+cityTag+"/";
			
		}else if("month".equals(fileType) || "country".equals(fileType) || "report_month".equals(fileType)){
			Calendar calendar1 = Calendar.getInstance();
	        calendar1.add(Calendar.MONTH, -1);
	        String beforeMonth = monthSdf.format(new Date());
	        savePath = "daqWeb/microAccount/"+fileType+"/"+beforeMonth+"/"+cityTag+"/";
		}
	
		return savePath;
	}
	
	@Test
	public void test01(){
		
	}
	
	
}

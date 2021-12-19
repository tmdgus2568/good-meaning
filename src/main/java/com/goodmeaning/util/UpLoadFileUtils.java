package com.goodmeaning.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.FileCopyUtils;

public class UpLoadFileUtils {
	//썸네일 이미지 만드는 이유는..?
//		static final int THUMB_WIDTH = 300;
//		static final int THUMB_HEIGHT = 300;
	    
		//파일 업로드
		public static String fileUpload(String uploadPath, String fileName, byte[] fileData, String ymdPath) //byte[] fileData : 이미지 byte로 들어온다, 
				throws Exception {
			System.out.println("uploadpath : "+uploadPath);
			System.out.println("filename : "+fileName);
			//System.out.println("fileData : "+fileData);
			System.out.println("ymdPath : " + ymdPath);
			
			// 고유식별 파일이름 생성
			//UUID uid = UUID.randomUUID();
			//String newFileName = uid + "_" + fileName;
			
			//String fileName = uploadfile.getOriginalFilename();
			String newFileName = makeUniqueFileName(fileName);
			String imgPath = uploadPath + ymdPath;

			File target = new File(imgPath, newFileName); //어디로~ 
			
			FileCopyUtils.copy(fileData, target);  //toFile과 같다.

			//String thumbFileName = "s_" + newFileName;
			File image = new File(imgPath + File.separator + newFileName);
			
			
			//File thumbnail = new File(imgPath + File.separator + "s" + File.separator + thumbFileName);

			//if (image.exists()) {
			//	thumbnail.getParentFile().mkdirs();
			//	Thumbnails.of(image).size(THUMB_WIDTH, THUMB_HEIGHT).toFile(thumbnail);
			//}
			return newFileName;
		}
		
		// 고유 파일이름 생성
		public static String makeUniqueFileName(String fileName) {
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_hhmmssSSSS");
			SimpleDateFormat sdf2 = new SimpleDateFormat("hhmmssSSS");
			
			String ranStr = RandomStringUtils.randomAlphanumeric(6);
			String date = sdf1.format(new Date());
			//String time = sdf2.format(new Date());
			
			String uniqName = date + ranStr + "_" + fileName;
			
			return uniqName;
		}
		
		//폴더이름 및 폴더 생성
		public static String calcPath(String uploadPath) {
			Calendar cal = Calendar.getInstance();
			String yearPath = File.separator + cal.get(Calendar.YEAR);
			String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
			//String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
			//new DecimalFormat("00") > 빈자리는 0으로 채움 

			//makeDir(uploadPath, yearPath, monthPath, datePath);
			makeDir(uploadPath, yearPath, monthPath);
			//makeDir(uploadPath, yearPath, monthPath, datePath + "\\s");

			//return datePath;
			return monthPath;
		}
		
		//일자별로 폴더 생성(부를때마다 생성)
		private static void makeDir(String uploadPath, String... paths) { //yearPath, monthPath

			if (new File(paths[paths.length - 1]).exists()) { //월이 있으면..
				return;
			}

			for (String path : paths) { //연도도 있는지 확인
				File dirPath = new File(uploadPath + path);

				if (!dirPath.exists()) {
					dirPath.mkdir();
				}
			}
		}
}

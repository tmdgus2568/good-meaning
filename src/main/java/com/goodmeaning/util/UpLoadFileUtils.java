package com.goodmeaning.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.FileCopyUtils;

public class UpLoadFileUtils {

	
		//파일 업로드
		public static String fileUpload(String uploadPath, String fileName, byte[] fileData, String ymdPath)                //byte[] fileData : 이미지 byte로 들어온다, 
				throws Exception {
			
			String newFileName = makeUniqueFileName(fileName);
			String imgPath = uploadPath + File.separator + ymdPath;

			File target = new File(imgPath, newFileName); //어디로~ 
			
			FileCopyUtils.copy(fileData, target);  //toFile과 같다.

			return  newFileName;
		}
		
		// 고유 파일이름 생성
		public static String makeUniqueFileName(String fileName) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("MMdd_hhmmssSSSS");
			
			String ranStr = RandomStringUtils.randomAlphanumeric(6);
			String date = sdf.format(new Date());
			
			String uniqName = date + ranStr + "_" + fileName;
			
			return uniqName;
		}
		
		//폴더이름 및 폴더 생성
		public static String calcPath(String uploadPath) {
			Calendar cal = Calendar.getInstance();
			String yearPath = "/" + cal.get(Calendar.YEAR);
			String monthPath = yearPath + "/" + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
			makeDir(uploadPath, yearPath, monthPath);
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

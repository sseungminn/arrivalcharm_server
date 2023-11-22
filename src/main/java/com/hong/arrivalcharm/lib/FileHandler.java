package com.hong.arrivalcharm.lib;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	private String defaultDir;	// 저장 경로
	
	public FileHandler (String defaultDir) {
		this.defaultDir = defaultDir;
	}
	
	public String saveImage(MultipartFile file){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormat.format(new Date());
		String imagePath = "/image/" + today + "/";
	
		// 폴더 체크 후 없으면 생성
		File uploadFolder = new File(defaultDir + imagePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}

		// 확장자 추출
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 파일명 생성
        String saveName = UUID.randomUUID().toString() + extension;
        // 파일을 불러올 때 사용할 파일 경로
        String savePath = defaultDir + imagePath + saveName;

        try {
            // 파일 저장
    		file.transferTo(new File(savePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return imagePath + saveName;
	}
	
	public void deleteImage(String path) throws IOException{
		if(!path.equals("") && !path.equals("/image/default/profile.jpg")) {
			File file = new File(defaultDir + path);
			if(!file.exists()) {
				throw new IOException("파일이 존재하지 않습니다.");
			}
			file.delete();
		}
	}
}

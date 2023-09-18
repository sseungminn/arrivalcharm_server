package com.hong.arrivalcharm.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import com.hong.arrivalcharm.define.Const;


@SuppressWarnings("unused")
public class ImageChecker {

	private List<MultipartFile> files;
	private MultipartFile file;
	public ImageChecker(List<MultipartFile> files) {
		this.files = files;
	}
	public ImageChecker(MultipartFile file) {
		this.file = file;
	}
	
	public void checkImage(MultipartFile file) throws Exception{
		String fileName = file.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<String> extList = new ArrayList<>(Arrays.asList(Const.EXTENSIONS));
		if (!extList.contains(ext.toLowerCase())) {
			throw new FileUploadException("허용되지 않은 파일 형식 및 확장명입니다.");
		}
	}
	
	public void checkImageList(List<MultipartFile> files) throws Exception{
		for(MultipartFile file : files) {
			checkImage(file);
		}
	}
	
}

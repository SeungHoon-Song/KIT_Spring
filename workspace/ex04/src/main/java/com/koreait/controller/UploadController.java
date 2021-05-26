package com.koreait.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload Ajax");
	}
	
	@PostMapping("/uploadFormAction")
	//외부에서 여러 개의 파일이 전달될 수 있으므로 배열로 받는다.
	public void uploadFormPost(MultipartFile[] uploadFile) {
		//업로드 할 경로
		String uploadFolder = "C:\\upload";
		
		//각 multipart 객체를 순서대로 가져온 후
		for(MultipartFile multipartFile : uploadFile) {
			//원하는 데이터를 메소드로 가져온다.
			log.info("===========");
			log.info("업로드 파일 명 : " + multipartFile.getOriginalFilename());
			log.info("업로드 파일 크기 : " + multipartFile.getSize());
			
				//전체 경로를 File객체에 담아준다.
				File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
				try {
					//해당 경로에 파일을 업로드해준다.
					multipartFile.transferTo(saveFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxAction(MultipartFile[] uploadFile) {
		log.info("upload Ajax post...");
		
		String uploadFolder = "C:\\upload";
		//사용자가 업로드를 한 시간인 년, 월, 일을 디렉터리로 만드는 getFolder()를 사용한다.
		File uploadPath = new File(uploadFolder, getFolder());
		
		//만약 해당 디렉터리가 존재하지 않으면
		if(!uploadPath.exists()) {
			//만들어준다.
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("===========");
			log.info("업로드 파일 명 : " + multipartFile.getOriginalFilename());
			log.info("업로드 파일 크기 : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			//IE에서는 파일 이름만 가져오지 않고 전체 경로를 가져오기 때문에 마지막에 위치한 파일 이름만 가져오도록 한다.
			//IE 외 브라우저에서는 \\가 없기 때문에 -1 +1 로 연산되어 0번째	즉, 파일이름을 의미한다.
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			log.info("실제 파일명: " + uploadFileName);
			
			//랜던함 UUID를 담아놓는다.
			UUID uuid = UUID.randomUUID();
			//파일 이름이 중복되더라도 이름 앞에 UUID를 붙여주기 때문에 중복될 가능성이 희박하다.
			//덮어씌워지는 것을 방지한다.
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			File saveFile = new File(uploadPath, uploadFileName);
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());;
			}
		}
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		//현재 날짜에서 -를 \\로 변경해준다.
		return str.replace("-", File.separator);
	}
	
	private boolean checkImg(File file) throws IOException {
	 	//사용자가 업로드한 파일의 타입 중 앞부분이 image로 시작한다면 이미지 파일이다.
		return Files.probeContentType(file.toPath()).startsWith("image");
	}
}


















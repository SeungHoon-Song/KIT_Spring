package com.koreait.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreait.domain.AllFileDTO;
import com.koreait.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName : " + fileName);
		File file = new File("C:\\upload\\" + fileName);
		log.info("file : " + file);
		
		ResponseEntity<byte[]> result = null;
		
		HttpHeaders header = new HttpHeaders();
		
		try {
			//헤더에 적절한 파일의 타입을 probeContentType을 통하여 포함시킨다.
			header.add("Content-Type",  Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header,HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<AllFileDTO> uploadAjaxAction(MultipartFile[] uploadFile) {
		log.info("upload ajax post.........");
		
		String uploadFolder = "C:\\upload";
		//사용자가 업로드를 한 시간인 년, 월, 일을 디렉토리로 만드는 getFolder()를 사용한다.
		String uploadFolderPath = getFolder();
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		AllFileDTO allFile = new AllFileDTO();
		List<BoardAttachVO> succeedList = new ArrayList<>();
		List<BoardAttachVO> failureList = new ArrayList<>();
		//만약 해당 디렉토리가 존재하지 않으면
		if(!uploadPath.exists()) {
			//만들어준다.
			uploadPath.mkdirs();
		}
		for(MultipartFile multipartFile : uploadFile) {
			log.info("================");
			log.info("업로드 파일 명 : " + multipartFile.getOriginalFilename());
			log.info("업로드 파일 크기 : " + multipartFile.getSize());
			BoardAttachVO boardAttachVO = new BoardAttachVO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			//IE에서는 파일 이름만 가져오지 않고 전체 경로를 가져오기 때문에 마지막에 위치한 파일 이름만 가져오도록 한다.
			//IE 이외의 브라우저에서는 \\가 없기 때문에 -1 + 1로 연산되어 0번째 즉, 파일이름을 의미한다.
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			log.info("실제 파일 명 : " + uploadFileName);
			boardAttachVO.setFileName(uploadFileName);
			//랜덤한 UUID를 담아놓는다.
			UUID uuid = UUID.randomUUID();
			//파일 이름이 중복되더라도 이름 앞에 UUID를 붙여주기 때문에 중복될 가능성이 희박하다.
			//덮어씌워지는 것을 방지한다.
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			InputStream in = null;
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				//업로드
				multipartFile.transferTo(saveFile);
				//업로드 된 파일 읽어오기
				in = new FileInputStream(saveFile);
				
				boardAttachVO.setUuid(uuid.toString());
				boardAttachVO.setUploadPath(uploadFolderPath);
				
				if(checkImg(saveFile)) {
					boardAttachVO.setFileType(true);
					//Stream은 파일을 통신할 때 byte가 이동할 경로이다.
					//썸네일 파일 업로드
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					//사용자가 첨부한 파일은 multipartFile을 통해서 가져오고, 
					//원하는 w, h를 지정한 후 변경된 이미지 파일을 FileOutputStream객체를 통해서 업로드한다.
					//Thumbnailator는 중간관리의 역할을 한다.
					Thumbnailator.createThumbnail(in, thumbnail, 100, 100);
					thumbnail.close();
				}
				succeedList.add(boardAttachVO);
			} catch (Exception e) {
				failureList.add(boardAttachVO);
				log.error(e.getMessage());
			} finally {
				try {
					if(in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
		}
		allFile.setSucceedList(succeedList);
		allFile.setFailureList(failureList);
		
		return new ResponseEntity<AllFileDTO>(allFile, HttpStatus.OK);
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		//현재 날짜에서 -를 \\로 변경해준다.
		return str.replace("-", File.separator);
	}
	
	private boolean checkImg(File file) throws IOException{
		//사용자가 업로드한 파일의 타입 중 앞부분이 image로 시작한다면 이미지 파일이다.
		return Files.probeContentType(file.toPath()).startsWith("image");
	}
}

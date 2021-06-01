package com.koreait.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreait.domain.AllFileDTO;
import com.koreait.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	//브라우저 상에서 로컬 경로를 직접 작성하면 보안상의 이유로 파일을 가져올 수 없게 된다.
	//JAVA에 로컬 경로(절대경로)를 작성함으로써 브라우저 내에서 보이지 않도록 숨기는 작업.
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
					
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
		allFile.setSucceedList(succeedList);
		allFile.setFailureList(failureList);
		
		//(1) 어떤 것이 프로세스에서 사용중인 지 모를 때
		//가비지 컬렉터: 주인이 없는 필드를 직접 찾아서 메모리에서 해제해주는 객체
		System.gc();	//가비지 컬렉터 호출 - 찾아도 바로 메모리 해제를 하지 않고 세대를 붙여준다. 추후 세대별 메모리 해제는 일괄 처리된다.
		System.runFinalization();	//세대에 상관없이 필드 메모리 해제(finalization() 호출)
		return new ResponseEntity<AllFileDTO>(allFile, HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String fileType){
		log.info("deleteFile: " + fileName);
		
		File file = null;
		
		try {
			file = new File("C:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
		file.delete();
		
		if(fileType.equals("image")) {
			String imgFileName = file.getPath().replace("s_", "");
			log.info("imgFileName: " + imgFileName);
			file = new File(imgFileName);
			
			//가비지 컬렉터: 주인이 없는 필드를 직접 찾아서 메모리에서 해제해주는 객체
			System.gc();	//가비지 컬렉터 호출 - 찾아도 바로 메모리 해제를 하지 않고 세대를 붙여준다. 추후 세대별 메모리 해제는 일괄 처리된다.
			System.runFinalization();	//세대에 상관없이 필드 메모리 해제(finalization() 호출)
			file.delete();
		}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("delete", HttpStatus.OK);
	}
	
	@GetMapping(value="/download", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName, @RequestHeader("User-Agent") String userAgent) {
		log.info("download file: " + fileName);
		Resource resource = new FileSystemResource("C:\\upload\\" + fileName);
		log.info("resource: " + resource);
		
		String resourceName = resource.getFilename();
		String originalName = resourceName.substring(resourceName.indexOf("_") +1);
		HttpHeaders headers = new HttpHeaders();
		//다운로드 시 저장되는 이름 : Content-Disposition
		try {
			String downloadName = null;
			//Trident : MSIE
			if(userAgent.contains("Trident")) {
				log.info("IE Browser로");
//				downloadName= URLEncoder.encode(resourceName, "UTF-8").replaceAll("\\", "");
				downloadName= URLEncoder.encode(originalName, "UTF-8");
			}else if(userAgent.contains("Edg")) {
			//Edg : 엣지
				log.info("Edg로");
				downloadName= URLEncoder.encode(originalName, "UTF-8");
			}else {
			//그 외(크롬)
				log.info("Chrome Browser");
				downloadName = new String(originalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			//new String(byte[], charset) : 해당 바이트배열을 charset으로 설정한다.
			//getBytes(charset) : 해당 문자열을 charset으로 변경하기 위해 byte배열로 리턴한다.
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
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

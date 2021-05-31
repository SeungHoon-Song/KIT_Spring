<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Board</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link rel="stylesheet" href="/resources/assets/css/main.css" />
		<style>
			.uploadResult{
				width:100%;
			}
			.uploadResult ul{
				display:flex;
				justify-content:center;
			}
			
			.uploadResult ul li{
				list-style:none;
				padding:10px;
			}
		</style>
	</head>
	<body class="is-preload">
		<!-- Main -->
			<div id="main">
				<div class="wrapper">
					<div class="inner">

						<!-- Elements -->
							<header class="major">
								<h1>Board</h1>
								<p>게시글 등록</p>
							</header>
									<!-- Table -->
										<h3><a href="/board/list${cri.getListLink()}" class="button small">목록 보기</a></h3>
						<div class="content">
							<div class="form">
								<form role="form" action="/board/register" method="post" id="regForm">
									<div class="fields">
										<div class="field">
											<h4>제목</h4>
											<input name="title" placeholder="Title" type="text" />
										</div>
										<div class="field">
											<h4>내용</h4>
											<textarea name="content" rows="6" placeholder="Content" style="resize:none"></textarea>
										</div>
										<div class="field">
											<h4>작성자</h4>
											<input name="writer" placeholder="Writer" type="text" />
										</div>
										<div class="field uploadDiv">
											<h4>첨부파일</h4>
											<input type="file" name="uploadFile" multiple>
										</div>
										<div class="field">
											<div class="uploadResult">
												<ul></ul>
											</div>
										</div>
									</div>
									<ul class="actions special">
										<li><input type="submit" class="button" value="등록" /></li>
									</ul>
								</form>
							</div>
										</div>
								</div>
							</div>
						</div> 
		<!-- Scripts -->
			<script src="/resources/assets/js/jquery.min.js"></script>
			<script src="/resources/assets/js/jquery.dropotron.min.js"></script>
			<script src="/resources/assets/js/browser.min.js"></script>
			<script src="/resources/assets/js/breakpoints.min.js"></script>
			<script src="/resources/assets/js/util.js"></script>
			<script src="/resources/assets/js/main.js"></script>
	</body>
	<script>
		$(document).ready(function(){
			var uploadResult = $(".uploadResult ul");
			var cloneObj = $(".uploadDiv").clone();
			var contextPath = "${pageContext.request.contextPath}";
			var regex = new RegExp("(.*/)\.(exe|sh|zip|alz)");
			var maxSize = 1024 * 1024 * 5; //5MB
			
			function checkExtension(fileName, fileSize){
				if(regex.test(fileName)){
					alert("업로드 할 수 없는 확장자입니다.");
					return false;
				}
				
				if(fileSize >= maxSize){
					alert("파일 사이즈 초과");
					return false;
				}
				return true;
			}
			
			$("input[type='submit']").on("click", function(e){
				e.preventDefault();
				console.log("submit clicked");
				var form = $("form[role='form']");
				var str = "";
				
				$(".uploadResult ul li").each(function(i, obj){
					str += "<input type='hidden' name='attachList[" + i +"].uploadPath' value='"+ $(obj).data("path") +"'>";
					str += "<input type='hidden' name='attachList[" + i +"].uuid' value='"+ $(obj).data("uuid") +"'>";
					str += "<input type='hidden' name='attachList[" + i +"].fileName' value='"+ $(obj).data("filename") +"'>";	//브라우저 filename 소문자
					str += "<input type='hidden' name='attachList[" + i +"].fileType' value='"+ $(obj).data("type") +"'>";
				});			
				form.append(str).submit();
				//obj는 JS이다.
			});
			
			function showUploadResult(uploadResults){
				var str = "";
				$(uploadResults).each(function(i, obj){
					if(!obj.fileType){
						//일반 파일
						str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType +"'>";
						str += "<div><img src='/resources/images/attach.png' width=100 height=100>";
						str += "<br>" + obj.fileName;
						str += "<span><br>x</span></div></li>";
					}else{
						//이미지 파일
						var thumbPath = encodeURIComponent(obj.uploadPath + "\\s_" + obj.uuid + "_" + obj.fileName);
						str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType +"'>";
						str += "<div><img src='/display?fileName=" + thumbPath + "' width=100 height=100>";
						str += "<br>" + obj.fileName;
						str += "<span><br>x</span></div></li>";
					}
				});
				uploadResult.append(str);
			}
			
			$("input[type='file']").on("change", function(e){
				var formData = new FormData();
				var inputFile = $("input[name='uploadFile']");
				var files = inputFile[0].files;
				
				for(let i=0; i<files.length; i++){
					if(!checkExtension(files[i].name, files[i].size)){
						return false;
					}
					formData.append("uploadFile", files[i]);
				}
				
				$.ajax({
					url: '/uploadAjaxAction',
					processData: false,
					contentType: false,
					data: formData,
					type: "post",
					dataType: "json",
					success: function(result){
						console.log(result);
						if(result.failureList.length != 0){
							var str = "";
							for(let i=0; i<result.failureList.length; i++){
								str += result.failureList[i].fileName + "\n";
							}
							alert("지원하지 않는 파일의 형식은 제외되었습니다.\n" + str);
						}
						
						//썸네일 실행
						showUploadResult(result.succeedList);
					}
				});
			});
		});
	</script>
</html>
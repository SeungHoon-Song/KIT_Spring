<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Upload - Ajax</title>
</head>
<body>
	<h1>File Upload - Ajax</h1>
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	<button id="uploadBtn">upload</button>
</body>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>
	$(document).ready(function(){
		var contextPath = "${pageContext.request.contextPath}";
		
		function check(fileName, fileSize){
			var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			var maxSize = 5242880;	//5MB
			if(regex.test(fileName)){
				alert("업로드 할 수 없는 파일의 형식입니다.");
				return false;
			}
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			return true;
		}
		
		$("#uploadBtn").on("click", function(){
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			
			console.log(files);
			
			for(let i=0; i<files.length; i++){
				if(!check(files[i].name, files[i].size)){
					return false;
				}
				formData.append("uploadFile", files[i]);
			}
			
			$.ajax({
				url: contextPath + "/uploadAjaxAction",
				processData: false,
				contentType: false,
				data: formData,
				type: "post",
				success: function(result){
					alert("Uploaded!");
				}
			});
		});
	});
</script>
</html>
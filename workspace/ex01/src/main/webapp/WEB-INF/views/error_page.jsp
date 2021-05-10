<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>500</title>
</head>
<body>
	<h4><c:out value="${exception.getMessage()}"/></h4>
	
	<ul>
		<c:forEach var="stack" items="${exception.getStackTrace()}">
			<li><c:out value="${stack}"/></li>
		</c:forEach>
	</ul>
</body>
</html>
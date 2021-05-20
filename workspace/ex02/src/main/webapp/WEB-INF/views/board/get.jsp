<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script>
   //테스트용
//      $(document).ready(function(){
         //console.log(replyService.name);
         
/*          console.log("==========");
         console.log("JS TEST"); */
         
         /* replyService.get(11, function(result){
         console.log(result);
     	}) */

/*          replyService.modify({rno : 11, reply : "Modified Reply"}, function(result){
            alert("MODIFY : " + result);
         });
          */
/*          replyService.remove(41, function(result){
            if(result == "success"){
               alert("REMOVED");
            }
         },function(err){
            alert("ERROR...");
      }) */
         
         /* replyService.getList({bno:"${board.bno}", page: 1},
               function(data){
               console.log(data);
         }); */
         
         /* replyService.add({reply:"JS TEST", replyer:"tester", bno:"${board.bno}"}, 
               function(result){
                  alert("RESULT : " + result);
         }); */
         
         /* replyService.get(21, function(result){
            console.log(result);
         }) */
         
//      })
   </script>
<html>
   <head>
      <title>Board</title>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
      <meta name="description" content="" />
      <meta name="keywords" content="" />
      <link rel="stylesheet" href="/resources/assets/css/main.css" />
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
                     <h3><a href="/board/list${cri.getListLink()}" class="button small">목록 보기</a></h3>
                  <div class="content">
                     <div class="form">
                        <form action="/board/remove">	<!-- GET방식 생략 -->
                        	<input type="hidden" name="pageNum" value="${cri.pageNum}">
                            <input type="hidden" name="amount" value="${cri.amount}">
                            <input type="hidden" name="keyword" value="${cri.keyword}">
                            <input type="hidden" name="type" value="${cri.type}">
                           <div class="fields">
                              <div class="field">
                                 <h4>번호</h4>
                                 <input name="bno" type="text" value="${board.bno}" readonly/>
                              </div>
                              <div class="field">
                                 <h4>제목</h4>
                                 <input name="title" type="text" value="${board.title}" readonly/>
                              </div>
                              <div class="field">
                                 <h4>내용</h4>
                                 <textarea name="content" rows="6" style="resize:none" readonly>${board.content}</textarea>
                              </div>
                              <div class="field">
                                 <h4>작성자</h4>
                                 <input name="writer" type="text" value="${board.writer}" readonly/>
                              </div>
                           </div>
                           <ul class="actions special">
                              <li>
                                 <input type="button" class="button" value="수정" onclick="location.href='/board/modify${cri.getListLink()}&bno=${board.bno}'"/>
                                 <input type="submit" class="button" value="삭제"/>
                              </li>
                           </ul>
                           <ul class="icons">
                           	<li>
                           		<span class="icon solid fa-envelope"></span>
                           		<strong>댓글</strong>
                           	</li>
                           </ul>
                           <ul class="replies">
                           
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
         <script src="/resources/assets/js/reply.js"></script>
   </body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>BBS List</title>
	<!-- Bootstrap -->
	<link href="bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
	<style>
	#container {
		width: 70%;
		margin: 0 auto; /* 가로로 중앙에 배치 */
		padding-top: 5%; /* 테두리와 내용 사이의 패딩 여백 */
		text-align: center;
	}
	
	#list {
		text-align: center;
	}
	
	#write {
		text-align: right;
	}
	
	/* Bootstrap 수정 */
	/* thead :  표에서 속성 */
	.table > thead {
		background-color: #b3c6ff;
	}/* 표 속성 */
	
	.table th{
		text-align: center;
		background-color: #b3c6ff;
	}
	.table > thead > tr > th {
		text-align: center;
	} /* tbody : 테이블에 각행에 대해서만 가능 열은 불가능 인스턴스 */
	.table-hover > tbody > tr:hover {
		background-color: #e6ecff;
	}
	
	.table > tbody > tr > td {
		text-align: center;
	}
	
	.table > tbody > tr > #title {
		text-align: left;
	}
	
	div > #paging {
		text-align: center;
	}
	
	.hit {
		animation-name: blink; /*애니메이션의 이름*/
		animation-duration: 1.5s; /*애니메이션의 동작 시간('1s','0.5s'와 같은 형식으로 지정)*/
		animation-timing-function: ease; /*애니메이션의 타이밍 : 시작과 종료를 부드럽게*/
		animation-iteration-count: infinite;
		/* 애니메이션의 동작 회수('infinite'를 지정하면 무한 반복)*/
		/* 위 속성들을 한 줄로 표기하기 */
		/* -webkit-animation: blink 1.5s ease infinite; */
	}
	
	/* 애니메이션 지점 설정하기 */
	/* 익스플로러 10 이상, 최신 모던 브라우저에서 지원 */
	@
	keyframes blink {
		from {color: white;
	}
	
	30%{color:yellow;}
	to {color: red; font-weight: bold;}
	/* 0% {color:white;}
	      30% {color: yellow;}
	      100% {color:red; font-weight: bold;} */
	}
	</style>
</head>

<body>
<center>
	<c:if test="${msg==0 }">
				<script type="text/javascript">
		alert("수정시 비밀번호가 일치하지 않습니다.")
	</script>
			</c:if>
	
			<c:if test="${msg==1 }">
				<script type="text/javascript">
		alert("삭제시 비밀번호가 일치하지 않습니다.")
	</script>
			</c:if>
		
	<div id="container">
		<div align="right">
			<!--  Login 검증 -->
			<!--  jstl의 if문은 else가 없어서 따로 검증해야함. -->
			<c:if test="${id != null }">
				<%-- <%@include file="loginOk.jsp --%>
			</c:if>
			<c:if test="${id == null }">
				<%-- <%@include file="login.jsp --%>
			</c:if>
		</div>

		<div id="list">
			<b>게시판 (전체 글 : ${count })</b>
		</div>
	

	<!-- #3-1 -->
		<div id="write">
			<!-- <button onclick="location.href='BoardWriteFome.jsp'">글쓰기</button> -->
		<%-- 	<a href="/bbs/writeForm.bbs?pageNum=${pageNum }">글쓰기</a> --%>
			<a href="BoardWriteFome.jsp" style="text-decoration:none">글쓰기</a>
		</div>
		
		
		<!-- <table> 태그에 .table-hover 클래스를 추가하면 테이블에 마우스를 올렸을 때 마우스 커서가 있는 행이 다른 색으로 변함 -->
   		<!-- <table> 태그에 .table-bordered 선택자를 추가하면 모든 셀에 테두리가 만들어 집니다. -->
    	<!-- <table> 태그에 .table-stripe 선택자를 추가하면 한 줄 건너 배경색이 달라지는 스트라이프 형태의 테이블이 됩니다. --> 

		<!--  <table width="700" border="1" bordercolor="skyblue">-->
		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr height="40">
					<td width="150" align="center">번호</td>
					<td width="500" align="center">제목</td>
					<td width="150" align="center">작성자</td>
					<td width="150" align="center">작성일</td>
					<td width="150" align="center">조회수</td>
				</tr>
			</thead>
			<tbody>
			<c:set var="number" value="${number }" />
			<!-- Vector에 있는 모든 리스트 가져오기 -->
			<c:forEach var="bean" items="${v }">
				<tr height="40">
					<th width="50" align="left">${number }</th>
					<td width="320" align="left">
						<!-- 댓글 중에서 처음 댓글은 들여쓰기가 필요없으므로 step 2 부터 들여쓰기 구현  --> 
						<c:if test="${bean.re_step > 1 }">
							<!-- 댓글이 하나일 때는 변화가 없다가 2개이상 부터 들여쓰기 -->
					<c:forEach var="j" begin="1" end="${(bean.re_step-1)*5 }">
					&nbsp;
					</c:forEach>
		</c:if> <!-- 세부검색시 링크될 페이지  --> 
		<a href="BoardInfoControl.do?num=${bean.num }" style="text-decoration: none">${bean.subject}</a>
					</td>
					<td width="100" align="center">${bean.writer }</td>
					<td width="150" align="center">${bean.reg_date }</td>
					<td width="80" align="center">${bean.readcount }</td>
				</tr>
				<!-- 글 카운터 최신글로 인해서 하나씩 감소 -->
				<c:set var="number" value="${number-1 }" />
			</c:forEach>
			</tbody>
			
		</table>
	</div>
	<!-- -----------------------------------------------------  -->
	<p>
		<!-- 페이지 카운터링 소스 구현 -->
		<c:if test="${count>0 }">
			<!-- 전체글이(count) 10일 경우 10/10+(10%10==0)=1페이지  -->
			<!-- 전체글이(count) 34일 경우 34/10+(34%10==1)= 3+1= 4페이지  -->
			<c:set var="pageCount"
				value="${count/pageSize+(count%pageSize==0 ? 0 : 1)}" />
			<!-- 시작페이지 숫자값 지정 -->
			<c:set var="startPage" value="${1 }" />

			<c:if test="${currentPage%10 != 0 }">
				<!-- 1~9 / 11~19  -->
				<fmt:parseNumber var="result" value="${currentPage/10 }"
					integerOnly="true" />
				<c:set var="startPage" value="${result*10+1 }" />
			</c:if>
			<%-- <c:if test="${currentPage%10 == 0 }">
				<c:set var="startPage" value="${(result-1)*10+1 }" />
			</c:if> --%>
		
			<c:if test="${currentPage%10 == 0 }">
				<!-- 10,20,30 페이지 -->
				<c:set var="startPage" value="${(result-1)*10+1 }" />
			</c:if>
			
			<!-- 화면에 보여질 페이지 처리 숫자를 표현 -->
			<c:set var="pageBlock" value="${10 }" />
			<!-- 첫번째 페이지 : 1+10-1=10 -->
			<c:set var="endPage" value="${startPage+pageBlock-1 }" />

			<!-- 페이지 예외 -->
			<c:if test="${endPage>pageCount }">
				<c:set var="endPage" value="${pageCount }" />
			</c:if>

			<!-- 이전 링크를 파악 -->
			<c:if test="${startPage>10 }">
				<a href="BoardListCon.do?pageNum=${startPage-10 }"
					style="text-decoration: none">[이전]</a>
			</c:if>

			<!-- 페이징 처리 -->
			<c:forEach var="i" begin="${startPage }" end="${endPage }">
				<a href="BoardListCon.do?pageNum=${i}" style="text-decoration: none">[${i}]</a>
			</c:forEach>

			<!-- 다음페이지 -->
			<c:if test="${endPage<pageCount }">
				<a href="BoardListCon.do?pageNum=${startPage+10 }"
					style="text-decoration: none">[다음]</a>
			</c:if>
		</c:if>

</center>	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" http://www.w3.org/TR/html4/loose.dtd">
<html>
<link type="text/css" rel="stylesheet" href="css/qnaboard.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>BBS WriteFome</title>
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
		background-color: #8671c9;
	}
	
	.table > thead > tr > th {
		text-align: center;
	} /* tbody : 테이블에 각행에 대해서만 가능 열은 불가능 인스턴스 */
	.table-hover > tbody > tr:hover {
		background-color: #e6ecff;
	}
	
	.table > tbody > tr > td {
		text-align: center;
		background-color: #e6ecff;
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

<div id="container">
	<b>답변 게시글 입력하기</b>
	<p>
	<form action="BoardwriteProcCon.do" method="post">
	<center>
		<!-- <table width="600" border="1" bordercolor="grey"> -->
		<table class="table table-striped table-bordered table-hover">
		<tbody>
			
			<tr height="40">
				<th width="150" align="center">작성자</th>
				<td width="450" align="left"><input type="text" name="writer" size="60" style="border:none; background: transparent;"></td>
			</tr>	
			<tr height="40">
				<th width="150" align="center">제목</th>
				<td width="450" align="left"><input type="text" name="subject" size="60" style="border:none; background: transparent;"></td>
			</tr>	
			<tr height="40">
				<th width="150" align="center">이메일</th>
				<td width="450" align="left"><input type="text" name="email" size="60" style="border:none; background: transparent;"></td>
			</tr>	
			<tr height="40">
				<th width="150" align="center">비밀번호</th>
				<td width="450" align="left"><input type="text" name="password" size="60" style="border:none; background: transparent;"></td>
			</tr>	
			<tr height="40">
				<th width="150" align="center">글내용</th>
				<td width="450" align="left"><textarea rows="10" cols="60" name="contents"></textarea></td>
			</tr>	
			
			<tr height="40">
				<th align="center" colspan="2">
					<input type="submit" value="글쓰기">&nbsp;&nbsp;
					<input type="reset" value="다시작성">&nbsp;&nbsp;
					<input type="button" onclick="location.href='BoardListCon.do'" value="전체 게시글 보기"></th>	
			</tbody>
		</table>
	</center>
	</form>
</div>	

</body>
</html>
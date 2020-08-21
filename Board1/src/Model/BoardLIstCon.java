package Model;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/BoardListCon.do")
public class BoardLIstCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		reqPro(req, res);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		reqPro(req, res);
		
	}
	
	protected void reqPro(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		// #1
		// 한 화면에 게시글이 보여질 숫자를 정함 = 10개씩 끊어보기
		int pageSize = 10;
		// 해당 페이지에 대한 정보를 읽어드림
		String pageNum = req.getParameter("pageNum"); //[1]페이지 [2]페이지 몇번쨰 페이지인지 파악
		// pageNum null처리
		if(pageNum==null) {
			pageNum="1"; // 페이지 초기값으로 1을 부여
		}
		// 총 게시글의 숫자를 저장할 변수
		int count = 0;
		// 화면에 보여질 글 번호 숫자를 저장하는 변수
		int number = 0;
		// 현재 보여지고 있는 페이지번호인 pageNum를 숫자로 변환
		int currentPage = Integer.parseInt(pageNum);
		
		
		// 데이터베이스의 객체 생성
		BoardDAO bdao = new BoardDAO();
		
		// 총 게시글의 값을 얻어오는 메소드 만들기
		count = bdao.getAllcount();
		// --------------------------------
		
		// #.2
		// 현재 보여질 페이지의 시작번호
		// 1페이지의 경우 (1-1)*10+1=1
		// 2페이지의 경우 (2-1)*10+1=11
		// 3페이지의 경우 (3-1)*10+1=21
		int startRow = (currentPage -1)*pageSize+1; // DB에서 가져오는 시작번호
		
		// 현재 보여질 페이지의 끝 번호
		// 1페이지의 경우 1*10=10
		// 2페이지의 경우 2*10=20
		int endRow = currentPage*pageSize; //DB에서 가져오는 끝 번호
		
		// 최신글 10개를 기준으로 게시글을 리턴 받아주는 메소드 호출
		Vector<BoardBean> v = bdao.getAllBoard(startRow, endRow);
		// --------------------------------------------------
		// #.3
		 // 전체글을 9(count)로 봤을 때 9-(1-1)*10=9
		 // 전체글이 23(count)로 봤을 때 23-(3-1)*10=3
		number = count-(currentPage-1)*pageSize;
		
		// 수정 삭제시 비번 오류
		String msg = (String)req.getAttribute("msg");  
		
		// 화면에 보이는 페이지에서 사용할 값을 request에 붙여서 jsp에 넘겨줌
		req.setAttribute("v", v);
		req.setAttribute("number", number); 
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("count", count);
		req.setAttribute("currentPage", currentPage);
		
		RequestDispatcher dis = req.getRequestDispatcher("BoardList.jsp");
		dis.forward(req, res);
		
	}

}

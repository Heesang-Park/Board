package Model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/BoardUpdateProcCon.do")
public class BoardUpdateProcCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
		
	}
	protected void reqPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		int num = Integer.parseInt(request.getParameter("num"));
		String password = request.getParameter("password"); // 사용자가 입력하는 패스워드
		String pass = request.getParameter("pass"); //DB에 있는 내용
		String subject = request.getParameter("subject");
		String contents = request.getParameter("contents");
		
		//password와 pass 비교
		if(password.equals(pass)) {
			BoardDAO bdao = new BoardDAO();
			bdao.UpdateBoard(num, subject, contents);
			// 수정이 완료되었다면 전체 게시글에서 확인
			RequestDispatcher dis = request.getRequestDispatcher("BoardListCon.do");
			dis.forward(request, response);
			
		}else {// 비밀번호가 틀렸다면 이전페이지
			request.setAttribute("msg", "0");
			RequestDispatcher dis = request.getRequestDispatcher("BoardListCon.do");
			dis.forward(request, response);
		}
		
		
		
	}

}

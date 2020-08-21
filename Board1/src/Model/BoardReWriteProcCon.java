package Model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/BoardReWriteProcCon.do")
public class BoardReWriteProcCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
		
	}

	protected void reqPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 빈에 넘어온 데이터를 저장
		BoardBean bean = new BoardBean();
		request.setCharacterEncoding("UTF-8");
		
		bean.setWriter(request.getParameter("writer"));
		bean.setSubject(request.getParameter("subject"));
		bean.setEmail(request.getParameter("email"));
		bean.setPassword(request.getParameter("password"));
		bean.setContents(request.getParameter("contents"));
		
		bean.setRef(Integer.parseInt(request.getParameter("ref").trim()));
		bean.setRe_step(Integer.parseInt(request.getParameter("re_step").trim()));
		bean.setRe_level(Integer.parseInt(request.getParameter("re_level").trim()));
		
		// 데이터베이스에 저장
		BoardDAO mbdao = new BoardDAO();
		
		//답변글을 저장해주는 메소드
		mbdao.reInsertBoard(bean);
		
		
		RequestDispatcher dis = request.getRequestDispatcher("BoardListCon.do");
		dis.forward(request, response);
	}
}

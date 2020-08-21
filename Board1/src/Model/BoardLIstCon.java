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
		// �� ȭ�鿡 �Խñ��� ������ ���ڸ� ���� = 10���� �����
		int pageSize = 10;
		// �ش� �������� ���� ������ �о�帲
		String pageNum = req.getParameter("pageNum"); //[1]������ [2]������ ����� ���������� �ľ�
		// pageNum nulló��
		if(pageNum==null) {
			pageNum="1"; // ������ �ʱⰪ���� 1�� �ο�
		}
		// �� �Խñ��� ���ڸ� ������ ����
		int count = 0;
		// ȭ�鿡 ������ �� ��ȣ ���ڸ� �����ϴ� ����
		int number = 0;
		// ���� �������� �ִ� ��������ȣ�� pageNum�� ���ڷ� ��ȯ
		int currentPage = Integer.parseInt(pageNum);
		
		
		// �����ͺ��̽��� ��ü ����
		BoardDAO bdao = new BoardDAO();
		
		// �� �Խñ��� ���� ������ �޼ҵ� �����
		count = bdao.getAllcount();
		// --------------------------------
		
		// #.2
		// ���� ������ �������� ���۹�ȣ
		// 1�������� ��� (1-1)*10+1=1
		// 2�������� ��� (2-1)*10+1=11
		// 3�������� ��� (3-1)*10+1=21
		int startRow = (currentPage -1)*pageSize+1; // DB���� �������� ���۹�ȣ
		
		// ���� ������ �������� �� ��ȣ
		// 1�������� ��� 1*10=10
		// 2�������� ��� 2*10=20
		int endRow = currentPage*pageSize; //DB���� �������� �� ��ȣ
		
		// �ֽű� 10���� �������� �Խñ��� ���� �޾��ִ� �޼ҵ� ȣ��
		Vector<BoardBean> v = bdao.getAllBoard(startRow, endRow);
		// --------------------------------------------------
		// #.3
		 // ��ü���� 9(count)�� ���� �� 9-(1-1)*10=9
		 // ��ü���� 23(count)�� ���� �� 23-(3-1)*10=3
		number = count-(currentPage-1)*pageSize;
		
		// ���� ������ ��� ����
		String msg = (String)req.getAttribute("msg");  
		
		// ȭ�鿡 ���̴� ���������� ����� ���� request�� �ٿ��� jsp�� �Ѱ���
		req.setAttribute("v", v);
		req.setAttribute("number", number); 
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("count", count);
		req.setAttribute("currentPage", currentPage);
		
		RequestDispatcher dis = req.getRequestDispatcher("BoardList.jsp");
		dis.forward(req, res);
		
	}

}

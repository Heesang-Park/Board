package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	Connection con ;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public void getCon() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/orcl");
			con = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// #1-1-----------------------------
	// 전체 게시글 갯수를 리턴하는 메소드
	public int getAllcount() {
		getCon();
		// 게시글 갯수를 저장하는 변수
		int count = 0;
		try {
			// 쿼리 작성
			String sql = "select count(*) from board";
			pstmt = con.prepareStatement(sql); //쿼리실행
			// 쿼리 실행 결과 받기
			rs = pstmt.executeQuery();
			if(rs.next()) { // 데이터가 있다면
				count = rs.getInt(1); // 전체 게시글 수
				
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	//#2-1-----------------------------------------
	// 화면에 보여질 데이터를 10개씩 추출해서 리턴하는 메소드
	public Vector<BoardBean> getAllBoard(int startRow, int endRow){
		getCon();
		Vector<BoardBean> v = new Vector<>();
		
		try {
			// 쿼리작성
			// 인라인 질의(최신글 10개씩 가져오기) (Rownum : 오라클 전용->예: 상위 30%에 해당하는 데이터 추출)
			String sql = "SELECT * FROM (SELECT A.* ,Rownum Rnum FROM (SELECT * FROM board ORDER BY ref desc, re_step asc) A) WHERE Rnum >= ? and Rnum <= ?";
			pstmt = con.prepareStatement(sql);
			// ?값 대입
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// 데이터 패키징(BoardBean과 매칭)
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContents(rs.getString(11));
				// 패키징한 데이터를 벡터에 저장
				v.add(bean);
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	// --------------------------------------------------
	//#4-1
	// 하나의 게시글을 저장하는 메소드
	public void insertBoard(BoardBean bean) {
		getCon();
		// Bean클래스에 넘어오지 않았던 데이터 초기화
		int ref=0; //쿼리를 실행시켜서 가장 큰 ref값을 가져온 후 항상 +1을 해준다
		int re_step=1;
		int re_level=1;
		
		try {
			// 최신글을 계산 글 그룹 생성
			String refsql = "select max(ref) from board";
			pstmt = con.prepareStatement(refsql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ref = rs.getInt(1)+1; // 이렇게 해야 최신글이 된다.
			}
			// 실제로 게시글 테이블에 저장
			String sql = "insert into board values(BOARD1_SEQ.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			// 쿼리실행
			pstmt = con.prepareStatement(sql);
			// ?에 값을 대입
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContents());
			
			// execute
			pstmt.executeUpdate();
			// 자원반납
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	// 하나의 게시글을 읽어드리는 메소드 작성
	public BoardBean getOneBoard(int num) {
		getCon();
		BoardBean bean=null;
		
		try {
			// 하나의 게시글을 읽었다는 조회수 증가
			String countsql = "update board set readcount = readcount+1 where num=?";
			// 쿼리 실행할 객체 선언
			pstmt = con.prepareStatement(countsql);
			// ?에 값 대입
			pstmt.setInt(1, num);
			//쿼리 실행
			pstmt.executeUpdate();
			//-----------------상세정보-----------------
			// 하나의 게시글 리턴
			//쿼리 준비
			String sql = "select * from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리실행후 저장
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				 bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContents(rs.getString(11));
				
			}
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void reInsertBoard(BoardBean bean) {
		
		getCon();
		int ref = bean.getRef();
		int re_step = bean.getRe_step();
		int re_level = bean.getRe_level();
		
		try {
			// 쿼리작성
			String levelsql = "update board set re_level = re_level+1 where ref=? and re_level > ?";
			
			pstmt = con.prepareStatement(levelsql);
			// ?에 값을 대입
			pstmt.setInt(1, ref); // 부모글 그룹
			pstmt.setInt(2, re_level);// 부모글 레벨
			//쿼리실행
			pstmt.executeUpdate();
			
			//핵심 코드 
			String insertsql = "insert into board values(BOARD1_SEQ.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = con.prepareStatement(insertsql);
			// ?에 값을 대입
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step+1);
			pstmt.setInt(7, re_level+1);
			pstmt.setString(8, bean.getContents());
			
			// 쿼리실행
			pstmt.executeUpdate();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BoardBean getoneUpdateBoard(int num) {
		getCon();
		BoardBean bean = null;
		
		try {
			
			// 하나의 게시글 리턴
			//쿼리 준비
			String sql = "select * from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// 쿼리실행후 저장
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				 bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContents(rs.getString(11));
				
			}
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	// 업데이트 메소드	
	public void UpdateBoard(int num, String subject, String contents) {
		getCon();
		
		try {
			String sql = "update board set subject=?, contents=? where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setInt(3, num);
			
			// 쿼리실행
			pstmt.executeUpdate();
			con.close();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 게시글 삭제하는 메소드
	public void deletBoard(int num) {
		getCon();
		try {
			String sql = "delete from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			pstmt.executeUpdate();
			con.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
}

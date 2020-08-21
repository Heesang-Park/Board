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
	// ��ü �Խñ� ������ �����ϴ� �޼ҵ�
	public int getAllcount() {
		getCon();
		// �Խñ� ������ �����ϴ� ����
		int count = 0;
		try {
			// ���� �ۼ�
			String sql = "select count(*) from board";
			pstmt = con.prepareStatement(sql); //��������
			// ���� ���� ��� �ޱ�
			rs = pstmt.executeQuery();
			if(rs.next()) { // �����Ͱ� �ִٸ�
				count = rs.getInt(1); // ��ü �Խñ� ��
				
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	//#2-1-----------------------------------------
	// ȭ�鿡 ������ �����͸� 10���� �����ؼ� �����ϴ� �޼ҵ�
	public Vector<BoardBean> getAllBoard(int startRow, int endRow){
		getCon();
		Vector<BoardBean> v = new Vector<>();
		
		try {
			// �����ۼ�
			// �ζ��� ����(�ֽű� 10���� ��������) (Rownum : ����Ŭ ����->��: ���� 30%�� �ش��ϴ� ������ ����)
			String sql = "SELECT * FROM (SELECT A.* ,Rownum Rnum FROM (SELECT * FROM board ORDER BY ref desc, re_step asc) A) WHERE Rnum >= ? and Rnum <= ?";
			pstmt = con.prepareStatement(sql);
			// ?�� ����
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// ������ ��Ű¡(BoardBean�� ��Ī)
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
				// ��Ű¡�� �����͸� ���Ϳ� ����
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
	// �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	public void insertBoard(BoardBean bean) {
		getCon();
		// BeanŬ������ �Ѿ���� �ʾҴ� ������ �ʱ�ȭ
		int ref=0; //������ ������Ѽ� ���� ū ref���� ������ �� �׻� +1�� ���ش�
		int re_step=1;
		int re_level=1;
		
		try {
			// �ֽű��� ��� �� �׷� ����
			String refsql = "select max(ref) from board";
			pstmt = con.prepareStatement(refsql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ref = rs.getInt(1)+1; // �̷��� �ؾ� �ֽű��� �ȴ�.
			}
			// ������ �Խñ� ���̺� ����
			String sql = "insert into board values(BOARD1_SEQ.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			// ��������
			pstmt = con.prepareStatement(sql);
			// ?�� ���� ����
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
			// �ڿ��ݳ�
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	// �ϳ��� �Խñ��� �о�帮�� �޼ҵ� �ۼ�
	public BoardBean getOneBoard(int num) {
		getCon();
		BoardBean bean=null;
		
		try {
			// �ϳ��� �Խñ��� �о��ٴ� ��ȸ�� ����
			String countsql = "update board set readcount = readcount+1 where num=?";
			// ���� ������ ��ü ����
			pstmt = con.prepareStatement(countsql);
			// ?�� �� ����
			pstmt.setInt(1, num);
			//���� ����
			pstmt.executeUpdate();
			//-----------------������-----------------
			// �ϳ��� �Խñ� ����
			//���� �غ�
			String sql = "select * from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���������� ����
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
			// �����ۼ�
			String levelsql = "update board set re_level = re_level+1 where ref=? and re_level > ?";
			
			pstmt = con.prepareStatement(levelsql);
			// ?�� ���� ����
			pstmt.setInt(1, ref); // �θ�� �׷�
			pstmt.setInt(2, re_level);// �θ�� ����
			//��������
			pstmt.executeUpdate();
			
			//�ٽ� �ڵ� 
			String insertsql = "insert into board values(BOARD1_SEQ.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = con.prepareStatement(insertsql);
			// ?�� ���� ����
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step+1);
			pstmt.setInt(7, re_level+1);
			pstmt.setString(8, bean.getContents());
			
			// ��������
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
			
			// �ϳ��� �Խñ� ����
			//���� �غ�
			String sql = "select * from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			// ���������� ����
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
	// ������Ʈ �޼ҵ�	
	public void UpdateBoard(int num, String subject, String contents) {
		getCon();
		
		try {
			String sql = "update board set subject=?, contents=? where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setInt(3, num);
			
			// ��������
			pstmt.executeUpdate();
			con.close();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �Խñ� �����ϴ� �޼ҵ�
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

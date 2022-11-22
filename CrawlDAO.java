package com.kh.crawl.model.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrawlDAO {

	public int insertCrawl(Map<String, String> category, Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query = "INSERT INTO CATEGORY_TBL VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,DEFAULT)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(category.get("pensionId")));
			pstmt.setString(2, category.get("SWMMING"));
			pstmt.setString(3, category.get("BARBECUE"));
			pstmt.setString(4, category.get("SING"));
			pstmt.setString(5, category.get("PARK"));
			pstmt.setString(6, category.get("CONVENIENCE_STORE"));
			pstmt.setString(7, category.get("SMOKING_ROOM"));
			pstmt.setString(8, category.get("DOG"));
			pstmt.setString(9, category.get("HANDICAP_PERSON"));
			pstmt.setString(10, category.get("COOK"));
			pstmt.setString(11, category.get("BREAKFAST"));
			pstmt.setString(12, category.get("WIFI"));
			pstmt.setString(13, category.get("AIRCON"));
			pstmt.setString(14, category.get("REFRIGERATOR"));
			pstmt.setString(15, category.get("BATHTUB"));
			pstmt.setString(16, category.get("DRY"));
			pstmt.setString(17, category.get("TV"));
			result = pstmt.executeUpdate();
			if(result > 0) {
				System.out.println("삽입 성공!");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int insertImage(String no, String img, int j, Connection conn) throws IOException {			
/** jpg 파일이 아닐 경우 dump 이미지 저장 */
		if(!img.contains(".jpg")) {
			img = "사진없음";
		}
		
		PreparedStatement pstmt = null;
		int result = 0;
		String query = "INSERT INTO PENSION_ATTACH VALUES(?,1,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, img);
			pstmt.setInt(2, Integer.parseInt(no));
			result = pstmt.executeUpdate();
			if(result > 0) {
				System.out.println(no + "번 삽입 성공!");
			}
			pstmt.close();
		}catch(Exception e) {
			
		}finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

	public int insertDB(Map<String, String> map, Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		int result = 0;
		String query = "INSERT INTO PENSION_TBL VALUES(?, NULL, ?, ?, ?, ?, NULL, NULL, DEFAULT, NULL, NULL, ?, DEFAULT, NULL, NULL, NULL, NULL)";	
		pstmt = conn.prepareStatement(query);	
		pstmt.setInt(1, Integer.parseInt(map.get("pensionNo")));
		pstmt.setString(2, map.get("pensionName"));
		pstmt.setString(3, map.get("pensionAddr"));
		pstmt.setString(4, map.get("pensionComments"));
		pstmt.setString(5, map.get("price"));	
		pstmt.setString(6, map.get("img"));
		System.out.println(map);
		result = pstmt.executeUpdate();
		System.out.println(result);
		pstmt.close();
		return result;
	}

	public long insertData(Map<String, String> map, Map<String, Integer> map2, Connection conn) throws SQLException {
		Long s = 0L;
		PreparedStatement pstmt = null;
		String sql = "SELECT SEQ_ROOM_NO.NEXTVAL FROM DUAL";
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		s = rs.getLong(1);
		System.out.println("됬나?");
		
		pstmt = conn.prepareStatement("INSERT INTO ROOM_TBL VALUES(?, NULL, NULL, DEFAULT, ?, ?, DEFAULT, ?, NULL)");
		pstmt.setLong(1, s);
		pstmt.setInt(2, map2.get("PRICE"));
		pstmt.setString(3, map.get("ROOMNAME"));
		pstmt.setString(4, map.get("PENSIONID"));
		pstmt.executeUpdate();
		pstmt.close();
		return s;
	}

	public int saveFile(Connection conn, long r_no, String imgSrc, int ORD) throws IOException, SQLException {
/** jpg 파일이 아닐 경우 dump 이미지 저장 */
		if(!imgSrc.contains(".jpg")) {
			imgSrc = "사진없음";
		} 
		int result = 0;
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ROOM_ATTACH VALUES(?,DEFAULT,?,?)");
		pstmt.setString(1, imgSrc);
		pstmt.setInt(2, ORD);
		pstmt.setLong(3, r_no);
		result = pstmt.executeUpdate();
		if(result > 0) {
			System.out.println("추가 성공!");
		}
		pstmt.close();
		
		return result;
	}

	public long saveData(Connection conn, Map<String, String> map) throws SQLException {
		Long s = 0L;
		String sql = "SELECT SEQ_REPLY.NEXTVAL FROM DUAL";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		s = rs.getLong(1);
		
		pstmt = conn.prepareStatement("INSERT INTO REVIEW_TBL VALUES(?,NULL,?,NULL,?,?,?)");
		System.out.println("실행중2");
		pstmt.setLong(1, s);
		pstmt.setString(2, map.get("regDate"));
		pstmt.setString(3, map.get("content"));
		pstmt.setString(4, map.get("count"));
		pstmt.setInt(5, Integer.parseInt(map.get("PENSIONID")));
		pstmt.executeUpdate();
		pstmt.close();
		return s;
	}

	public int selectNo(Connection conn, String no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "SELECT COUNT(*) AS C_COUNT FROM PENSION_TBL WHERE PENSION_NO = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(no));
			rset = pstmt.executeQuery();
			if(rset.next()) {
				result = rset.getInt("C_COUNT");
			}
			System.out.println("다오 넘버 : " + result);
		}catch(Exception e) {

		}finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int selectPensionNo(Connection conn, String no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "SELECT COUNT(*) AS C_COUNT FROM PENSION_TBL WHERE PENSION_NO = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(no));
			rset = pstmt.executeQuery();
			if(rset.next()) {
				result = rset.getInt("C_COUNT");
			}
			System.out.println("다오 넘버 : " + result);
		}catch(Exception e) {

		}finally {
			rset.close();
			pstmt.close();
		}
		return result;
	}

}

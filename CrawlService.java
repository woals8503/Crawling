package com.kh.crawl.model.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.common.JDBCTemplate;
import com.kh.crawl.model.dao.CrawlDAO;

public class CrawlService {
	private JDBCTemplate jdbcTemplate;
	private CrawlDAO cDao;
	public CrawlService() {
		jdbcTemplate = JDBCTemplate.getInstance();
		cDao = new CrawlDAO();
	}
	public int insertCrawl(Map<String, String> category) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.insertCrawl(category, conn);
			if(result > 0) {
				JDBCTemplate.commit();
			}
		}catch(Exception e) {
			
		}
		return result;
	}
	public int insertImage(String no, String img, int j) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.insertImage(no, img, j, conn);
			if(result > 0) {
				JDBCTemplate.commit();
			}
		}catch(Exception e) {
			
		}
		return result;
	}
	
	public int saveDB(Map<String, String> map) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.insertDB(map, conn);
			if(result > 0) {
				JDBCTemplate.commit();
				System.out.println("커밋완료");
			}
		}catch(Exception e) {
			
		}
		return result;
	}
	public long saveData(Map<String, String> map, Map<String, Integer> map2) {
		Connection conn = null;
		long result = 0L;
		System.out.println("여기까지는 올까?");
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.insertData(map, map2, conn);
			if(result > 0) {
				JDBCTemplate.commit();
				System.out.println("커밋 완료");
			}else {
				JDBCTemplate.rollback();
			}
		
		}catch(Exception e) {
			JDBCTemplate.rollback();
		}finally {
			JDBCTemplate.close();
		}
		return result;
	}
	public int saveFile(long r_no, String img, int k) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.saveFile(conn, r_no, img, k);
			if(result > 0) {
				JDBCTemplate.commit();
			}else {
				JDBCTemplate.rollback();
			}
			System.out.println("커밋!");
		}catch (Exception e) {
			JDBCTemplate.rollback();
		}finally {
			JDBCTemplate.close();
		}
		return result;
	}
	public long saveReply(Map<String, String> map) {
		Connection conn = null;
		long result = 0L;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.saveData(conn, map);		
			System.out.println("됬나?");
			JDBCTemplate.commit();		
			System.out.println("커밋!");
		}catch (Exception e) {
			JDBCTemplate.rollback();
		}finally {
			JDBCTemplate.close();
		}
		return result;
	}
	public int selectNo(String no) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.selectNo(conn, no);
			if(result > 0) {
				System.out.println("번호 조회 성공");
			}
		}catch (Exception e) {
			JDBCTemplate.rollback();
		}finally {
			JDBCTemplate.close();
		}
		return result;
	}
	public int selectPensionNo(String no) {
		Connection conn = null;
		int result = 0;
		try {
			conn = jdbcTemplate.createConnection();
			result = cDao.selectPensionNo(conn, no);
			if(result > 0) {
				System.out.println("번호 있음@@@");
			}
		}catch (Exception e) {
			
		}
		return result;
	}

}

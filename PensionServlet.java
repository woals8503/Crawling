package com.kh.crawl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kh.crawl.model.service.CrawlService;

/**
 * Servlet implementation class PensionServlet
 */
@WebServlet("/pension")
public class PensionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
				try {
					CrawlService cService = new CrawlService();
					
					Connection conn = Jsoup.connect("https://www.goodchoice.kr/product/result?sort=ACCURACY&keyword=%EC%A0%9C%EC%A3%BC%EB%8F%84&type=&sel_date=2022-10-18&sel_date2=2022-10-19&adcno%5B%5D=3&min_price=&max_price=");
				
					Document doc = conn.get();
					
					Elements elements = doc.select(".list_4");
					
					for(int i=0; i<elements.size(); i++) {
						Element el = elements.get(i);
						
						//고유 펜션번호
						String no = el.getElementsByTag("a").attr("data-ano");
						
						
							String link = el.getElementsByTag("a").attr("href");
							
							Document doc2 = Jsoup.connect(link).get();
							
							//펜션 이름
							String title = doc2.select(".info").select("h2").text();
							
							//펜션 가격
							String price= el.select(".map_html").select("b").html();
						
							//펜션 주소
							String address = doc2.select(".address").text();
							
							// 펜션 내용
							String comment = doc2.select(".comment div").text();
							
							/** 각각의 펜션 이미지들을 지정할 Elements 객체 생성 */
							Elements el3 = doc2.select(".swiper-slide");
							
					Element eximg = el3.select(".swiper-lazy").get(i);

					String img = "http:" + eximg.attr("data-src");
					
					Map<String, String> map = new HashMap<>();
					map.put("pensionNo", no);
					map.put("pensionName", title);
					map.put("pensionAddr", address);
					map.put("pensionComments", comment);
					map.put("price", price);
					map.put("img", img);
					int result1 = cService.saveDB(map);
					if(result1 > 0) {
						System.out.println("성공@@@@@@@@@@@@@@@@@");
					}
					System.out.println(i + "번째 작업중");
					System.out.println(no + "추가 성공");
					
					//펜션별 이미지들을 String 배열로 저장
					
//					boolean check = true;
					
					//펜션별 이미지들을 한 개씩 지정할 Element 객체 생성
						//펜션별 이미지들을 링크 형태로 img 라는 String 타입 변수에 저장
					
						// 이미지 파일 누락 및 손상을 확인하여 배열에 추가
						
//						if(imgs.size() == 0) {
//							imgs.add(img);
//						}else {
//							for(String s : imgs) {
//								if(!img.equals(s)) {
//									imgs.add(img);
//									break;
//								}else {
//									check = false;
//								}
//							}
//						}
//						if(!check) {
//							break;
//						}
//						imgs.add(img);
					}
				
				
				}catch(Exception e) {
					System.out.println("아웃바운드 오류!!!!!!!!!!!!!!!!!!!!!!! 추가불가");
				}
				
			
	}
			
			
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

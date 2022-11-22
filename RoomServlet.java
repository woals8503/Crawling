package com.kh.crawl.controller;

import java.io.IOException;
import java.util.HashMap;
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
 * Servlet implementation class RoomServlet
 */
@WebServlet("/room")
public class RoomServlet extends HttpServlet {
	
	CrawlService cService = new CrawlService();
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 여기어때 페이지 연결	*/
		Connection conn = Jsoup.connect("https://www.goodchoice.kr/product/result?sort=ACCURACY&keyword=%EC%A0%9C%EC%A3%BC%EB%8F%84&type=&sel_date=2022-10-18&sel_date2=2022-10-19&adcno%5B%5D=3&min_price=&max_price=");
		
		/** HTML 요소 접근을 위한 Document 객체 생성	*/
		Document doc = conn.get();
		
		/** 펜션목록을 받아올 Elements 객체 생성 */
		Elements elements = doc.select(".list_4");
		
		/** 펜션목록을 한 개씩 지정할 Element 객체 생성	*/
		for(int i = 0; i < elements.size() ; i++) {
			Element el = elements.get(i);
			
		/** 고유펜션번호를 no라는 String 타입 변수에 저장	*/
			String no = el.getElementsByTag("a").attr("data-ano");
			
		/** 각각의 펜션 링크를 link라는 String 타입 변수에 저장	*/
			String link = el.getElementsByTag("a").attr("href");
			
		/** 각각의 펜션 링크의 HTML 요소 접근을 위한 Document 객체 생성	*/
			Document doc2 = Jsoup.connect(link).get();
		
		/** 각각의 펜션 숙소별 정보를 받아올 Elements 객체 생성 */	
			Elements roomInfo = doc2.select(".room");
			
			int result = cService.selectPensionNo(no);
			
			if(result == 1) {
			System.out.println("리설트 1 반복문 실행");
		/** 각각의 펜션 숙소별 정보를 한 개씩 지정할 Element 객체 생성 */
			for (int j = 0; j < roomInfo.size(); j++) {
				Element el2= roomInfo.get(j);
					
		/**	각각의 펜션 숙소별 가격을 price 라는 int 타입 변수에 저장 
		 *	.equals("") ? "0" : el2.select(".price p b").text().replace(",", "").replace("원", "")) 는 가격정보에 결함이 있을 경우를 위함 
		 */			
				int price = Integer.parseInt(el2.select(".price p b").text().equals("") ? "0" : el2.select(".price p b").text().replace(",", "").replace("원", ""));
					
		/**	각각의 펜션 숙소별 숙소명을 roomName 이라는 String 타입 변수에 저장 */
				String roomName= roomInfo.select(".title").get(j).text();
				
				
					/** TBL_ROOM 테이블에 정보를 저장하기 위한 Map 객체 생성 후 put을 이용한 저장 */		
					Map<String, String> map = new HashMap<>();
					map.put("PENSIONID",no);
					map.put("ROOMNAME", roomName);
					Map<String, Integer> map2 = new HashMap<>();
					map2.put("PRICE",price);
			/** 이미지 파일 저장을 위한 folderName 이라는 long 타입 변수를 선언 */
//					long folderName = saveDB(map, map2);
					long r_no = cService.saveData(map, map2);
					System.out.println("룸 넘버" + r_no);
			/** 각각의 펜션 숙소별 이미지들을 받아오기 위한 Elements 객체 생성 */
					Elements el3= el2.select(".item");
					
			/** 각각의 펜션 숙소별 이미지 정보를 한 개씩 지정할 Element 객체 생성 */		
					for (int k = 0; k < el3.size(); k++) {
						Element eximg2 = el3.get(k);
			/** 각각의 펜션 숙소별 이미지 링크들을 img 라는 String 타입 변수에 저장 */			
						String img = "https:"+ eximg2.select("img").attr("data-src");
//						saveFile(no, folderName, img,k);
						int result1 = cService.saveFile(r_no, img, k);
						if(result1 > 0) {
							System.out.println("마지막 성공!");
						}
					}
					
				}
				System.out.printf(no + "번 작업 완료" +"\n"+"\n");
				}
				
		
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

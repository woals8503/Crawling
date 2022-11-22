package com.kh.crawl.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kh.crawl.model.service.CrawlService;

/**
 * Servlet implementation class ReplyServlet
 */
@WebServlet("/reply")
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CrawlService cService = new CrawlService();

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
			
			int result = cService.selectNo(no);
			
			if(result == 1) {
				/** TBL_REPLY 테이블에 정보를 저장하기 위한 Map 객체 생성 후 put을 이용한 저장 */
				Map<String, String> map = new HashMap<>();
				map.put("PENSIONID",no);
				
			/** 각각의 펜션 링크를 originLink라는 String 타입 변수에 저장 */
				String originLink = el.getElementsByTag("a").attr("href");
				
			/** 펜션별 JSON 데이터 링크를 headLink라는 String 타입 변수에 저장 */
				String headLink = originLink.replaceAll("detail", "get_review_non");
				
			/** 펜션별 JSON 데이터 링크의 요소 접근을 위한 Document 객체 생성	*/	
				Document doc2 = Jsoup.connect(headLink).get();
			
			/** 펜션별 JSON 데이터를 문자열로 변환해 jsonStr 이라는 String 타입 변수에 저장 */	
				String jsonStr = doc2.getElementsByTag("body").get(0).text();
				
			/** JSON 문자열을 java 객체로 변환하기 위한 Gson 객체를 생성 */	
				Gson gson = new Gson();
				
			/** 컴파일 에러가 나지 않기 위한 Type 객체 생성 */
				Type type = new TypeToken<Map<String, Object>>(){}.getType();
				
			/** JSON 문자열을 저장하기 위한 Map 객체 생성 */	
				Map<String, Object> myMap = gson.fromJson(jsonStr, type);
				
			/**	펜션별 댓글 개수를 count 라는 String 타입 변수에 저장 */
				String count= String.valueOf(((Map)myMap.get("result")).get("count"));
				
				
			/** 댓글이 존재하지 않을 시엔 동작을 생략함 */	
				if (count=="0") {
					continue;
				}
				
			/**	댓글이 존재할 경우 동작을 실행함  */
				else {
					map.put("count", count);
			/** 댓글 페이지 수를 totalPageCnt 라는 String 타입 변수에 저장 */
					String totalPageCnt = String.valueOf(((Map)myMap.get("result")).get("total_page_cnt"));
			
			/** 댓글 페이지별 JSON 데이터 요소 접근을 위한 Document 객체 생성 */
					for (int j = 1; j <= Float.parseFloat(totalPageCnt); j++) {
						Document doc3 =Jsoup.connect(headLink+"&page="+j).get();
			
			/** 댓글 페이지별 JSON 데이터를 문자열로 변환해 jsonStr2 라는 String 타입 변수에 저장 */			
						String jsonStr2= doc3.getElementsByTag("body").get(0).text();
			
			/** JSON 문자열을 저장하기 위한 Map 객체 생성 */				
						Map<String, Object> myMap2 =gson.fromJson(jsonStr2, type);
						
			/** 댓글 정보 저장을 위한 배열 생성 후 for문을 이용 */			
						List<Object> items =  (List<Object>) ((Map)myMap2.get("result")) .get("items");
						for(Object item : items) {
							
							
			/** 댓글별 내용을 content 라는 String 타입 변수에 저장 */				
							String content= String.valueOf(((Map)item).get("aepcont"));
							
			/** TBL_REPLY 테이블에 정보를 저장하기 위해 Map 객체에 put을 이용한 저장 */
							map.put("content", content);
			/** 댓글별 등록일을 저장하기 위해 epoch 타임을 변환 */
							Long epochDate=(long)((Double)((Map)item).get("aepreg")).doubleValue();
							Instant instant = Instant.ofEpochSecond(epochDate);
							ZonedDateTime regDateTime= (ZonedDateTime.ofInstant(instant, ZoneOffset.UTC));
							
			/** 댓글별 등록일을 regDate 라는 String 타입 변수에 저장  */				
							String regDate= regDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
							
			/** TBL_REPLY 테이블에 정보를 저장하기 위해 Map 객체에 put을 이용한 저장 */				
							map.put("regDate", regDate);
							System.out.println(map);
							long folderName = cService.saveReply(map);
							System.out.println("최종성공!" + j + "번째 진행");
							
							}
						}
					}
				}
			}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

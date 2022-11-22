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
 * Servlet implementation class CrawlServlet
 */
@WebServlet("/crawl/insert")
public class CrawlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CrawlService cService = new CrawlService();
		
		
		Connection conn = Jsoup.connect("https://www.goodchoice.kr/product/result?sort=ACCURACY&keyword=%EC%A0%9C%EC%A3%BC%EB%8F%84&type=&sel_date=2022-10-17&sel_date2=2022-10-18&adcno%5B%5D=3&min_price=&max_price=");

		// html요소 접근을 위한 doc 객체 생성
		Document doc = conn.get();

		// 펜션목록을 받아올 elements 객체 생성
		Elements elements = doc.select(".list_4");
		
		System.out.println(elements.size());
		// 펜션 목록을 한개씩 지정할 element 객체 생성
		for(int i = 0; i < elements.size(); i++) {
			System.out.println("실행중" + i);
			
			Element el = elements.get(i);

			//고유 펜션번호를 no라는 String 타입 변수에 저장
			String no = el.getElementsByTag("a").attr("data-ano");

			//각각 펜션 링크를 link라는 String 타입 변수에 저장
			String link = el.getElementsByTag("a").attr("href");

			//각각의 펜션 링크의 html 요소 접근을 위한 document 객체 생성
			Document doc2 = Jsoup.connect(link).get();

			//편의시설 및 서비스 정보를 받아올 Elements 객체 생성
			Elements el2 = doc2.select(".service");
			

			//.equals("") ? "" : "1" 은 편의시설 및 서비스 정보를 0 또는 1로 저장하기 위함

			// 픽업 가능 여부
			//완료
			String SWMMING = el2.select(".theme_56").text().equals("")? "" : "1";
			//완료
			String BARBECUE = el2.select(".theme_148").text().equals("")? "" : "1";

			//완료
			String SING = el2.select(".theme_59").text().equals("")? "" : "1";

			//완료
			String PARK = el2.select(".theme_221").text().equals("")? "" : "1";

			//완료
			String CONVENIENCE_STORE = el2.select(".theme_215").text().equals("")? "" : "1";

			//완료
			String SMOKING_ROOM = el2.select(".theme_233").text().equals("")? "" : "1";

			//완료
			String DOG = el2.select(".theme_236").text().equals("")? "" : "1";

			//완료
			String HANDICAP_PERSON = el2.select(".theme_337").text().equals("")? "" : "1";

			//완료
			String COOK = el2.select(".theme_237").text().equals("")? "" : "1";

			//완료
			String BREAKFAST = el2.select(".theme_208").text().equals("")? "" : "1";

			//완료
			String WIFI = el2.select("theme_60").text().equals("")? "" : "1";

			//완료
			String AIRCON = el2.select(".theme_227").text().equals("")? "" : "1";

			//완료
			String REFRIGERATOR = el2.select(".theme_228").text().equals("")? "" : "1";

			//완료
			String BATHTUB = el2.select(".theme_230").text().equals("")? "" : "1";

			//완료
			String DRY = el2.select("theme_231").text().equals("")? "" : "1";

			//완료
			String TV = el2.select(".theme_223").text().equals("")? "" : "1";

			Map<String, String> category = new HashMap<>();
			category.put("SWMMING", SWMMING);
			category.put("BARBECUE", BARBECUE);
			category.put("SING", SING);
			category.put("PARK", PARK);
			category.put("CONVENIENCE_STORE", CONVENIENCE_STORE);
			category.put("SMOKING_ROOM", SMOKING_ROOM);
			category.put("DOG", DOG);
			category.put("HANDICAP_PERSON", HANDICAP_PERSON);
			category.put("COOK", COOK);
			category.put("BREAKFAST", BREAKFAST);
			category.put("WIFI", WIFI);
			category.put("AIRCON", AIRCON);
			category.put("REFRIGERATOR", REFRIGERATOR);
			category.put("BATHTUB", BATHTUB);
			category.put("DRY", DRY);
			category.put("TV", TV);
			category.put("pensionId", no);
			
			System.out.println("카테고리 사이즈" + category.size());
			
			
			int result = cService.insertCrawl(category);
			if(result > 0) {
				System.out.println(i + "번째 추가성공!");
			}
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

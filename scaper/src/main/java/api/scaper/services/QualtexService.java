package api.scaper.services;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import api.scaper.model.Info;

public class QualtexService {

	Info info = null;
	
	public QualtexService(){
		info = new Info();
	}
	
	public Info getInfo(String partno) throws IOException, InterruptedException {
		
		/**
		 * Create instance of a Headless Browser that emulates 
		 * Google Chrome with javascript enabled
		 */
		
		WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		
		/**
		 * Use Jsoup to remotely log into the site
		 */
		
        Response res = Jsoup.connect("http://www.qualtexuk.com/login/default.aspx")
                .data("username", "9996", "password", "Scampbell301")
                .method(Method.POST)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .execute();
        
        /**
         * Store login cookies
         */
        
        Map<String, String> loginCookies = res.cookies();
        
        /**
         * At this point we switch from Jsoup to Selenium as the price and stock 
         * data is fed to the site via AJAX, this means we must wait for a short
         * amount of time before passing the DOM back to Jsoup
         * 
         * Open site in headless browser
         */
        
        driver.get("http://www.qualtexuk.com");
        
        /**
         * Pass login cookies to headless browser
         */
        
        for (Map.Entry<String, String> entry : loginCookies.entrySet()) {	
        	driver.manage().addCookie(new Cookie(entry.getKey(), entry.getValue()));   
        }
        
        /**
         * Open desired page in headless browser
         * 
         * This URL is the same as doing a search on the page, in some instances 
         * the response will be a single part page or a list of parts
         */
        
        driver.get("http://www.qualtexuk.com/trade/search/default.aspx?search="+partno);
        
        /**
         * Wait n second for AJAX to load
         */
        
        Thread.sleep(2000);
        
        /**
         * Pass DOM with AJAX content back to Jsoup
         */
        
        Document doc = Jsoup.parse(driver.getPageSource());
        
        /**
         * Pass all possible CSS selectors to extract price 
         */
        
        Elements tradeprice = 
        	doc.select("#dealerprice0:not(:has(table)), "
        			 + "#dealerprice:not(:has(table)), "
        			 + "#dealerprice0 tr:eq(0) > td:eq(1), "
        			 + "#dealerprice tr:eq(0) > td:eq(1)");
        
        System.out.println(tradeprice.text().replaceAll("\u00A0",""));
        
        /**
         * Stock
         */
        
        Element img = 
        		doc.select(".prodstock img").first() != null ? 
                doc.select(".prodstock img").first() : null;
 
        if(img != null){
        	String stock = img.attr("src").replaceAll(".*/|\\..*", "");
        	info.setStock(stock);
        	info.setPrice(tradeprice.text().replaceAll("\u00A0",""));
        }
 		
		return info;
	}

}

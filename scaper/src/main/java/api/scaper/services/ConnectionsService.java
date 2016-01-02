package api.scaper.services;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import api.scaper.model.Info;

public class ConnectionsService {

	Info info = null;
	
	public ConnectionsService(){
		info = new Info();
	}
	
	public Info getInfo(String partno) throws IOException, InterruptedException{
		
		/**
		 * Use Jsoup to remotely log into the site
		 */
        
		Connection.Response res = Jsoup.connect("https://connections.connect-distribution.co.uk/cv6/login.pl")
			.validateTLSCertificates(false)
            .data("username", "glen@c1279", "password", "StanleyG303", "login", "1")
            .method(Method.POST)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .execute();
            

        //This will get you cookies
        Map<String, String> loginCookies = res.cookies();

        //Here you parse the page that you want. Put the url that you see when you have logged in
        Document doc1 = Jsoup.connect("https://connections.connect-distribution.co.uk/cv6/search.pl?query="+partno)
          .cookies(loginCookies)
          .get();
        
        Elements title = doc1.select(".price > p");
        int pos = title.text().indexOf("£");
        System.out.println(title.text().substring(pos+1));
        
        Elements stock = doc1.select(".alignRight > span");
        System.out.println(stock.text());
        
        info.setPrice(title.text().substring(pos+1));
        info.setStock(stock.text());
 		
		return info;
	}
}

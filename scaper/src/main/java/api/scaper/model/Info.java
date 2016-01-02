package api.scaper.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Info {

	private String stock = "No Stock Information";
	private String price = "No Price Information";
	
	public Info(){}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}	
}

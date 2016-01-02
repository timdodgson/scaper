package api.scaper.services;

import api.scaper.model.Info;

public class BuySparesService {

	Info info = null;
	
	public BuySparesService(){
		info = new Info();
	}
	
	public Info getInfo() {
		return info;
	}

}

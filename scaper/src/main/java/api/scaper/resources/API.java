package api.scaper.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import api.scaper.model.Info;
import api.scaper.services.BuySparesService;
import api.scaper.services.ConnectionsService;
import api.scaper.services.QualtexService;



@Path("/")
public class API {
	
	QualtexService qualtexService = new QualtexService();
	ConnectionsService connectionsService = new ConnectionsService();
	BuySparesService buySparesService = new BuySparesService();

	@Path("qualtex/{partno}")
	@GET
	@Produces(value = {MediaType.APPLICATION_JSON,MediaType.TEXT_XML})
	public Info getQualtexInfo(@PathParam("partno") String partno) throws SQLException, IOException, InterruptedException{
		return qualtexService.getInfo(partno);
	}
	
	@Path("connections/{partno}")
	@GET
	@Produces(value = {MediaType.APPLICATION_JSON,MediaType.TEXT_XML})
	public Info getConnectionsInfo(@PathParam("partno") String partno) throws SQLException, IOException, InterruptedException{
		return connectionsService.getInfo(partno);
	}
	
	@Path("buyspares")
	@GET
	@Produces(value = {MediaType.APPLICATION_JSON,MediaType.TEXT_XML,})
	public Info getBuySparesInfo() throws SQLException{
		return buySparesService.getInfo();
	}
}


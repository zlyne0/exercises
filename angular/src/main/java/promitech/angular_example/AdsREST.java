package promitech.angular_example;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import promitech.angular_example.json.Ad;

@Path("/rest")
public class AdsREST {

    private static AdsRepository adsRepository = new AdsRepository();
    
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ad> list() {
        return adsRepository.getAll();
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Ad ad) {
        System.out.println("create " + ad);
        ad.setId(null);
        adsRepository.addNew(ad);
    }
}

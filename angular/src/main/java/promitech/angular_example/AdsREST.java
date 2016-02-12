package promitech.angular_example;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import promitech.angular_example.json.Ad;

@Path("/rest")
public class AdsREST {

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ad> list() {
        List<Ad> ads = new ArrayList<>();
        ads.add(new Ad(1L, "one", "description one"));
        ads.add(new Ad(2L, "two", "description two"));
        ads.add(new Ad(3L, "three", "description three"));
        return ads;
    }
}

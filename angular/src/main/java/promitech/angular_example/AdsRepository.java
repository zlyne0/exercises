package promitech.angular_example;

import java.util.ArrayList;
import java.util.List;

import promitech.angular_example.json.Ad;

public class AdsRepository {

    private List<Ad> ads = new ArrayList<>();
    {
        ads.add(new Ad(1L, "one", "description one"));
        ads.add(new Ad(2L, "two", "description two"));
        ads.add(new Ad(3L, "three", "description three"));
    }
    
    public List<Ad> getAll() {
        return ads;
    }
    
    public void addNew(Ad ad) {
        ad.setId(Long.valueOf(ads.size() + 1));
        ads.add(ad);
    }
}

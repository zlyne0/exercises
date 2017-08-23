package promitech.pact_example.client;

import org.springframework.web.client.RestTemplate;

public class ProductDetailsClient {

    private String addr;
    
    public ProductDetailsClient(String addr) {
        this.addr = addr;
    }
    
    public ProductDetails getProductDetails(Long id) {
        return new RestTemplate().getForObject(addr + "/product/" + id, ProductDetails.class);
        
    }
    
    public static void main(String[] args) {
        ProductDetailsClient productDetailsClient = new ProductDetailsClient("http://localhost:9000");
        System.out.println("productDetails = " + productDetailsClient.getProductDetails(100L));
    }

}

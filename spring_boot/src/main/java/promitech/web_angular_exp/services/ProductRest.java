package promitech.web_angular_exp.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import promitech.web_angular_exp.model.Product;

@RestController
@RequestMapping("/rest")
public class ProductRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRest.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @RequestMapping("/products")
    public List<Product> products() {
        List<Product> products = new ArrayList<Product>();
        
        System.out.println("products = " + products.size());
        
        for (Product product : productRepository.findAll()) {
            products.add(product);
        }
        return products;
    }
    
}

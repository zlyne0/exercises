package promitech.web_angular_exp.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import promitech.web_angular_exp.json.ProductJSON;
import promitech.web_angular_exp.model.Product;

@RestController
@RequestMapping("/rest")
public class ProductRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRest.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @RequestMapping("/product/list")
    public List<ProductJSON> products() {
        List<ProductJSON> products = new ArrayList<ProductJSON>();
        for (Product product : productRepository.findAll()) {
            products.add(new ProductJSON(product));
        }
        return products;
    }
    
}

package promitech.pact_example.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductDetailsController {

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ProductDetails getProductDetails(@PathVariable final Long id) {
        if (id == 0) {
            throw new IllegalStateException("can not find product");
        }
        return new ProductDetails(id, "code" + id);
    }
    
}

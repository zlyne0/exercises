package promitech.web_angular_exp.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import promitech.web_angular_exp.json.ProductJSON;
import promitech.web_angular_exp.json.ProductParameterJSON;
import promitech.web_angular_exp.json.ProductParameterTypeJSON;
import promitech.web_angular_exp.model.Product;
import promitech.web_angular_exp.model.ProductParameter;
import promitech.web_angular_exp.model.ProductParameterType;

@RestController
@RequestMapping("/rest")
public class ProductRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRest.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductParameterTypeRepository productParameterTypeRepository;
    
    @Autowired
    private ProductParameterRepository productParameterRepository;
    
    @RequestMapping("/product/list")
    public List<ProductJSON> products() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(ProductJSON::new)
                .collect(Collectors.toList());
    }
    
    @RequestMapping("/product/{productId}/parameters")
    public List<ProductParameterJSON> productParameters(@PathVariable Long productId) {
        return productRepository.findParametersByProductId(productId)
            .stream()
            .map(ProductParameterJSON::new)
            .collect(Collectors.toList());
    }
    
    @RequestMapping("/parameterType/list")
    public List<ProductParameterTypeJSON> parameterTypes() {
        return StreamSupport.stream(productParameterTypeRepository.findAll().spliterator(), false)
            .map(ProductParameterTypeJSON::new)
            .collect(Collectors.toList());
    }
    
    @RequestMapping(path = "/product/{productId}/addParam", method = RequestMethod.POST)
    public ResponseEntity<?> addProductParam(@PathVariable Long productId, @RequestBody ProductParameterJSON param) {
        Product product = productRepository.findOne(productId);
        if (product == null) {
            return ResponseEntity.badRequest().body("product not found by id " + productId);
        }
        ProductParameterType paramType = productParameterTypeRepository.findOne(param.getType().getId());
        if (paramType == null) {
            return ResponseEntity.badRequest().body("product not found by id " + param.getType().getId());
        }

        ProductParameter pp = new ProductParameter();
        pp.setBigValue(param.getBigValue());
        pp.setProduct(product);
        pp.setType(paramType);
        pp.setValue(param.getValue());
        
        pp = productParameterRepository.save(pp);
        ProductParameterJSON jsonObj = new ProductParameterJSON(pp);
        return new ResponseEntity<ProductParameterJSON>(jsonObj, HttpStatus.OK);
    }
}

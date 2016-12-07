package promitech.web_angular_exp.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import promitech.web_angular_exp.model.Product;
import promitech.web_angular_exp.model.ProductParameter;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByName(String name);
    
    @Query("select param from ProductParameter param where param.product.id = :id")
    List<ProductParameter> findParametersByProductId(@Param("id") Long productId);
}

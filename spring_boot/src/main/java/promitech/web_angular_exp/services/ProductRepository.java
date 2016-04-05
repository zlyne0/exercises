package promitech.web_angular_exp.services;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import promitech.web_angular_exp.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByName(String name);    
}

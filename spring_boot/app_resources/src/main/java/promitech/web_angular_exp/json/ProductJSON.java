package promitech.web_angular_exp.json;

import promitech.web_angular_exp.model.Product;

public class ProductJSON {
    private Long id;
    private String name;
    
    public ProductJSON(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

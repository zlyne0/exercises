package promitech.web_angular_exp.json;

import promitech.web_angular_exp.model.ProductParameterType;

public class ProductParameterTypeJSON {

    private final Long id;
    private final String name;
    
    public ProductParameterTypeJSON(ProductParameterType type) {
        this.id = type.getId();
        this.name = type.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

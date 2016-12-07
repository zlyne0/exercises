package promitech.web_angular_exp.json;

import promitech.web_angular_exp.model.ProductParameterType;

public class ProductParameterTypeJSON {

    private Long id;
    private String name;

    public ProductParameterTypeJSON() {
    }
    
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductParameterTypeJSON [id=" + id + ", name=" + name + "]";
    }
}

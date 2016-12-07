package promitech.web_angular_exp.json;

import promitech.web_angular_exp.model.ProductParameter;

public class ProductParameterJSON {

    private Long id;
    private Long productId;
    private ProductParameterTypeJSON type;
    private String value;
    private String bigValue;
    
    public ProductParameterJSON() {
    }
    
    public ProductParameterJSON(ProductParameter param) {
        id = param.getId();
        productId = param.getProduct().getId();
        type = new ProductParameterTypeJSON(param.getType());
        value = param.getValue();
        bigValue = param.getBigValue();
    }

    public Long getProductId() {
        return productId;
    }

    public ProductParameterTypeJSON getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getBigValue() {
        return bigValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setType(ProductParameterTypeJSON type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBigValue(String bigValue) {
        this.bigValue = bigValue;
    }
    
    @Override
    public String toString() {
        return "ProductParameterJSON [id=" + id + ", productId=" + productId + ", type=" + type + ", value=" + value + ", bigValue=" + bigValue + "]";
    }
}

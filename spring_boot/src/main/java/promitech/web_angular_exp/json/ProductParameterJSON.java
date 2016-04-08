package promitech.web_angular_exp.json;

import promitech.web_angular_exp.model.ProductParameter;

public class ProductParameterJSON {

    private final Long productId;
    private final ProductParameterTypeJSON type;
    private final String value;
    private final String bigValue;
    
    public ProductParameterJSON(ProductParameter param) {
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
}

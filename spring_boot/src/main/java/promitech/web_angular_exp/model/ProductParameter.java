package promitech.web_angular_exp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="product_parameter")
@NamedQuery(
    name=ProductParameter.NQ_FIND_PARAMS_BY_PRODUCT_ID, 
    query="select param "
        + " from ProductParameter param "
        + " where param.product.id = :id")
public class ProductParameter {

    public static final String NQ_FIND_PARAMS_BY_PRODUCT_ID = "ProductParameter.findParamsByProductId";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="type_id")
    private ProductParameterType type;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    
    @Column(name = "value")
    private String value;
    
    @Column(name = "big_value")
    private String bigValue;

    public Long getId() {
        return id;
    }

    public ProductParameterType getType() {
        return type;
    }

    public Product getProduct() {
        return product;
    }

    public String getValue() {
        return value;
    }

    public String getBigValue() {
        return bigValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(ProductParameterType type) {
        this.type = type;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBigValue(String bigValue) {
        this.bigValue = bigValue;
    }
}

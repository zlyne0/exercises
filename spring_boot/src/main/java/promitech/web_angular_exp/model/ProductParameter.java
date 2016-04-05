package promitech.web_angular_exp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProductParameter {

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
}

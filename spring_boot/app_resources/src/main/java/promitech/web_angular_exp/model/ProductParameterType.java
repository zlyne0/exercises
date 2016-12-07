package promitech.web_angular_exp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_parameter_type")
public class ProductParameterType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="code")
    private String code;
    
    @Column(name="description")
    private String description;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

package promitech.pact_example.service;

public class ProductDetails {
    private final Long id;
    private final String code;

    public ProductDetails(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
    
}

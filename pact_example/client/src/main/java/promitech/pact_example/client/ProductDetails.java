package promitech.pact_example.client;

public class ProductDetails {

    private Long id;
    private String code;

    public ProductDetails() {
    }
    
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

    @Override
    public String toString() {
        return "ProductDetails [id=" + id + ", code=" + code + "]";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

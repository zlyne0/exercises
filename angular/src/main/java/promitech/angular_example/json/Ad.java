package promitech.angular_example.json;

public class Ad {

    private Long id;
    private String name;
    private String description;

    public Ad() {
    }
    
    public Ad(Long l, String name, String description) {
        this.id = l;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Ad [id=" + id + ", name=" + name + ", description=" + description + "]";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

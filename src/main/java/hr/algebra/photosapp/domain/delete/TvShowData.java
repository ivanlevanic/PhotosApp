//package hr.algebra.photosapp.domain.delete;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Size;
//
//@Entity
//public class TvShowData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotEmpty(message = "The TV show's name has not been entered")
//    @Size(min = 2, max = 100, message = "The name should be between 2 and 100 characters long")
//    private String name;
//
//    @Min(value = 0, message = "Price must be positive")
//    private float price;
//
//    @Size(min = 2, max = 100, message = "The name should be between 2 and 100 characters long")
//    private String streamingservice;
//
//    @Min(value = 1, message = "At least 1 season needs to be available")
//    private Integer seasons;
//
//    //@NotEmpty(message = "The TV show's full name has not been entered")
//    //@Size(min = 2, max = 100, message = "The full name should be between 2 and 100 characters long")
//    //private String fullname;
//
//    public TvShowData() {}
//    public TvShowData(Long id, String name, float price, String streamingservice, Integer seasons) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.streamingservice = streamingservice;
//        this.seasons = seasons;
//    }
//
//    public String getName() { return this.name; }
//    public float getPrice() { return this.price; }
//    public String getStreamingservice() { return this.streamingservice; }
//    public Integer getSeasons() { return this.seasons; }
//    public Long getId() {
//        return id;
//    }
//    //public String getFullName() { return this.fullname; }
//
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    public void setPrice(float price) { this.price = price; }
//    public void setStreamingservice(String streamingservice) { this.streamingservice = streamingservice; }
//    public void setSeasons(Integer seasons) { this.seasons = seasons; }
//    public void setId(Long id) { this.id = id; }
//    //public void setFullName(String fullname) {
//    //    this.fullname = fullname;
//    //}
//}

package hr.algebra.photosapp.domain;

import javax.persistence.*;

@Entity
@Table(name = "package")
public class PackagePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan")
    private String plan;

    @Column(name = "price")
    private float price;

    @Column(name = "uploadSize")
    private long uploadSize;

    @Column(name = "dailyUploadLimit")
    private int dailyUploadLimit;

    public PackagePlan() {
    }

    public PackagePlan(String plan, float price, long uploadSize, int dailyUploadLimit) {
        this.plan = plan;
        this.price = price;
        this.uploadSize = uploadSize;
        this.dailyUploadLimit = dailyUploadLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getUploadSize() {
        return uploadSize;
    }

    public void setUploadSize(long uploadSize) {
        this.uploadSize = uploadSize;
    }

    public int getDailyUploadLimit() {
        return dailyUploadLimit;
    }

    public void setDailyUploadLimit(int dailyUploadLimit) {
        this.dailyUploadLimit = dailyUploadLimit;
    }
}

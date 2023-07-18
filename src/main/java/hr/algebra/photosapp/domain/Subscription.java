package hr.algebra.photosapp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profileId", nullable = false)
    private Long profileId;

    @Column(name = "packagePlan", nullable = false, length = 20)
    private String packagePlan;

    @Column(name = "lastRefreshDate")
    private Date lastRefreshDate;

    @Column(name = "dateOfLastChange")
    private Date dateOfLastChange;

    @Column(name = "newPackage", length = 20)
    private String newPackage;

    public Subscription() {
    }

    public Subscription(Long profileId, String packagePlan, Date lastRefreshDate, Date dateOfLastChange, String newPackage) {
        this.profileId = profileId;
        this.packagePlan = packagePlan;
        this.lastRefreshDate = lastRefreshDate;
        this.dateOfLastChange = dateOfLastChange;
        this.newPackage = newPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getPackagePlan() {
        return packagePlan;
    }

    public void setPackagePlan(String packagePlan) {
        this.packagePlan = packagePlan;
    }

    public Date getLastRefreshDate() { return lastRefreshDate; }

    public void setLastRefreshDate(Date lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

    public Date getDateOfLastChange() { return dateOfLastChange; }

    public void setDateOfLastChange(Date dateOfLastChange) {
        this.dateOfLastChange = dateOfLastChange;
    }

    public String getNewPackage() { return newPackage; }

    public void setNewPackage(String newPackage) {
        this.newPackage = newPackage;
    }
}

package hr.algebra.photosapp.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consumption")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profileId", nullable = false)
    private Long profileId;

    @Column(name = "dayOfTheMonth")
    private LocalDate dayOfTheMonth;

    @Column(name = "numberOfUploadedPhotos")
    private Integer numberOfUploadedPhotos;

    public Consumption() {}

    public Consumption(Long profileId, LocalDate dayOfTheMonth, Integer numberOfUploadedPhotos) {
        this.profileId = profileId;
        this.dayOfTheMonth = dayOfTheMonth;
        this.numberOfUploadedPhotos = numberOfUploadedPhotos;
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

    public LocalDate getDayOfTheMonth() {
        return dayOfTheMonth;
    }
    public void setDayOfTheMonth(LocalDate dayOfTheMonth) {
        this.dayOfTheMonth = dayOfTheMonth;
    }

    public Integer getNumberOfUploadedPhotos() {
        return numberOfUploadedPhotos;
    }
    public void setNumberOfUploadedPhotos(Integer numberOfUploadedPhotos) {
        this.numberOfUploadedPhotos = numberOfUploadedPhotos;
    }
}

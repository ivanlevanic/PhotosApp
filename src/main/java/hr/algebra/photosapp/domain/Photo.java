package hr.algebra.photosapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo", nullable = false)
    private String photo;

    @Column(name = "profileId", nullable = false)
    private Long profileId;

    @NotEmpty(message = "Description is required")
    @Size(max = 250, message = "Description should not exceed 250 characters")
    private String description;

    @Column(name = "uploadTime")
    private LocalDateTime uploadTime;

    @NotNull(message = "Size is required")
    private long size;

    @Column(name = "hashtags")
    @Size(max = 250)
    private String hashtags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

    public Photo() {
    }

    public Photo(Long profileId, String photo, String description, LocalDateTime uploadTime, long size, String hashtags) {
        this.profileId = profileId;
        this.photo = photo;
        this.description = description;
        this.uploadTime = uploadTime;
        this.size = size;
        this.hashtags = checkHashtagSyntax(hashtags);
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

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public String getHashtags() { return hashtags; }
    public void setHashtags(String hashtags) { this.hashtags = hashtags; }
    public void setUnparsedHashtags(String hashtags) { this.hashtags =  checkHashtagSyntax(hashtags);}

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    private String checkHashtagSyntax(String hashtags) {
        hashtags = hashtags.trim();
        hashtags = hashtags.replaceAll("\\s+", " ");

        String[] words = hashtags.split(" ");

        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                word = word.replaceAll("^#|#$", "");

                if (!word.isEmpty()) {
                    sb.append("#").append(word).append(" ");
                }
            }
        }
        hashtags = sb.toString().trim();
        hashtags = hashtags.replaceAll("#+", " #");

        return hashtags;
    }
}

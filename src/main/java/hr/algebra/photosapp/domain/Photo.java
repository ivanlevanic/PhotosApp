package hr.algebra.photosapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Lob
    @Column(name = "photo", nullable = false)
    private String photo;
    //private byte[] photo;

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

    private String checkHashtagSyntax(String hashtags) {
        // Remove leading and trailing whitespaces
        hashtags = hashtags.trim();

        // Replace multiple spaces with a single space
        hashtags = hashtags.replaceAll("\\s+", " ");

        // Split the string into individual words
        String[] words = hashtags.split(" ");

        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                // Remove leading and trailing hash characters from the word
                word = word.replaceAll("^#|#$", "");

                if (!word.isEmpty()) {
                    // Prepend a hash character to the word and append a space
                    sb.append("#").append(word).append(" ");
                }
            }
        }
        hashtags = sb.toString().trim();

        // Replace consecutive hash characters with a single hash character and space
        hashtags = hashtags.replaceAll("#+", " #");

        return hashtags;
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
    //public byte[] getPhoto() {return photo;}

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    //    public void setPhoto(byte[] photo) {
    //        this.photo = photo;
    //    }

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

    private void setUnparsedHashtags (String hashtags) { this.hashtags =  checkHashtagSyntax(hashtags);}
}

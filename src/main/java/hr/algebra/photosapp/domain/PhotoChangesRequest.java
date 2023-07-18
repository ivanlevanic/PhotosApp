package hr.algebra.photosapp.domain;

public class PhotoChangesRequest {
    private String hashtags;
    private String description;

    public String getHashtags() { return this.hashtags; }
    public void setHashtags(String hashtags) { this.hashtags = hashtags; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}

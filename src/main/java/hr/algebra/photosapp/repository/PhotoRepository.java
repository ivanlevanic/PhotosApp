package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.domain.Photo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository {
    public void save(Photo photo);
    //public List<Photo> getAllPhotos();
    public Photo getPhotoByName(String fileName);
    public Photo getPhotoById(Long id);
    public List<Photo> getAllPhotosByProfileId(Long profileId);
    public List<Photo> getPhotosByDescription(String description);
    public void updatePhoto(Long photoId, String description, String hashtags);
    public List<Photo> getAllPhotosWithUsername();
}

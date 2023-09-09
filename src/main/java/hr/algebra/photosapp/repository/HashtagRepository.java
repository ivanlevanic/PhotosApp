package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Hashtag;

import java.util.List;

public interface HashtagRepository {

    public void save (Hashtag hashtag);

    public List<Hashtag> getHashtagsForPhoto(Long photoId);

    public List<Long> getPhotoIdsByHashtag (String hashtag);

    public void deleteAllTheHashtags(Long photoId);
}

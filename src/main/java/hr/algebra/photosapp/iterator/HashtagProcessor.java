package hr.algebra.photosapp.iterator;

import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.repository.HashtagRepository;

public class HashtagProcessor {
    private final HashtagRepository hashtagRepository; // Declare the repository as an instance variable

    public HashtagProcessor(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository; // Initialize the repository through a constructor
    }

    public void processHashtags(String[] hashtagArray, long photoId) {
        // Step 3: Refactor the loop using the iterator
        HashtagIterator iterator = new ArrayHashtagIterator(hashtagArray);
        while (iterator.hasNext()) {
            String hashtag = iterator.next();
            if (!hashtag.isEmpty()) {
                Hashtag newHashtag = new Hashtag(photoId, hashtag);
                hashtagRepository.save(newHashtag);
            }
        }
    }
}
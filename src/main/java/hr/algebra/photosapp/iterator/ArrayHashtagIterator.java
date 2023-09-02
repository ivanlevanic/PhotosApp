package hr.algebra.photosapp.iterator;

// Step 2: Implement the iterator
public class ArrayHashtagIterator implements HashtagIterator {
    private String[] hashtagArray;
    private int currentIndex;

    public ArrayHashtagIterator(String[] hashtagArray) {
        this.hashtagArray = hashtagArray;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < hashtagArray.length;
    }

    @Override
    public String next() {
        if (hasNext()) {
            String hashtag = hashtagArray[currentIndex++];
            hashtag = hashtag.replace("#", ""); // Remove the '#' sign
            return hashtag.isEmpty() ? next() : hashtag;
        }
        return null;
    }
}
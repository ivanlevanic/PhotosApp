package hr.algebra.photosapp.repository.delete;

import hr.algebra.photosapp.domain.delete.TvShowData;

import java.util.List;
import java.util.Optional;

public interface TvShowRepository {

    List<Optional<TvShowData>> getCheapestStreamingService(String name);

}

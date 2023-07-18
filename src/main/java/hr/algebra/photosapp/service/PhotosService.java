package hr.algebra.photosapp.service;

import hr.algebra.photosapp.repository.*;
import org.springframework.stereotype.Service;

@Service
public class PhotosService {

    private PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PackageRepository packageRepository;
    private final ConsumptionRepository consumptionRepository;

    public PhotosService(  UserRepository userRepository,
                           PhotoRepository photoRepository,
                           ProfileRepository profileRepository,
                           SubscriptionRepository subscriptionRepository,
                           PackageRepository packageRepository,
                           ConsumptionRepository consumptionRepository) {
        this.profileRepository = profileRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.packageRepository = packageRepository;
        this.consumptionRepository = consumptionRepository;
    }
}

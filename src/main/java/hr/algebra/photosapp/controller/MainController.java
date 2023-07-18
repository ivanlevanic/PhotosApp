package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.domain.Photo;
import hr.algebra.photosapp.domain.Profile;
import hr.algebra.photosapp.domain.Subscription;
import hr.algebra.photosapp.repository.HashtagRepository;
import hr.algebra.photosapp.repository.PhotoRepository;
import hr.algebra.photosapp.repository.ProfileRepository;
import hr.algebra.photosapp.repository.SubscriptionRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class MainController {
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PhotoRepository photoRepository;
    private final HashtagRepository hashtagRepository;
    public MainController(ProfileRepository profileRepository,
                          SubscriptionRepository subscriptionRepository,
                          PhotoRepository photoRepository,
                          HashtagRepository hashtagRepository) {
        this.profileRepository = profileRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.photoRepository = photoRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @GetMapping("")
    public String redirectToHome() {
        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String homepage(Model model, @RequestParam(value = "searchText", required = false) String searchText,
                           @RequestParam(value = "searchOption", required = false) String searchOption) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Profile profile = profileRepository.getProfileByUsername(username);
            Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profile.getId());

            if (subscription.getPackagePlan() != null && subscription.getNewPackage() != null && !subscription.getPackagePlan().equals(subscription.getNewPackage())) {
                Date lastRefreshDate = subscription.getLastRefreshDate();
                Date dateOfLastChange = subscription.getDateOfLastChange();

                if (dateOfLastChange != null && !isSameDay(lastRefreshDate, dateOfLastChange)) {
                    subscription.setLastRefreshDate(Calendar.getInstance().getTime());
                    subscription.setDateOfLastChange(Calendar.getInstance().getTime());
                    subscription.setPackagePlan(subscription.getNewPackage());
                    subscriptionRepository.saveAfterPlanChange(subscription);
                }
            } //postaviti ta 2 datuma na danasnji prilikom registracije
            model.addAttribute("subscription", subscription);
        }
        model.addAttribute("username", authentication.getName());
        List<Photo> photos = photoRepository.getAllPhotos();

        if (searchText != null && searchOption != null) {
            if (searchOption.equals("user")) {
                try {
                    Long profileId = profileRepository.getProfileIdByUsername(searchText.toLowerCase());
                    photos = photoRepository.getAllPhotosByProfileId(profileId);
                } catch (Exception e) {
                    photos = new ArrayList<>();
                }
            } else if (searchOption.equals("description")) {
                photos = photoRepository.getPhotosByDescription(searchText);
            } else if (searchOption.equals("hashtag")) {
                photos = new ArrayList<>();
                List<Long> photoIds = hashtagRepository.getPhotoIdsByHashtag(searchText);
                for(Long photoId: photoIds) {
                    Photo photo = photoRepository.getPhotoById(photoId);
                    if (photo != null) {
                        photos.add(photo);
                    }
                }
            }
        }

        model.addAttribute("photos", photos);
        return "pages/homepage";
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}

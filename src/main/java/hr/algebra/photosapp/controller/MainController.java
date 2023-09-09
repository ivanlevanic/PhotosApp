package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
import hr.algebra.photosapp.domain.LoggingSystemFactory;
import hr.algebra.photosapp.repository.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;
import java.time.LocalDate;


//@FunctionalInterface
//interface DateComparator {
//    boolean compareDates(Date date1, Date date2);
//}
@Controller
@RequestMapping("")
public class MainController {
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PhotoRepository photoRepository;
    private final HashtagRepository hashtagRepository;
    private final LoggingSystemRepository loggingSystemRepository;
    private final AuthorityRepository authorityRepository;

    public MainController(ProfileRepository profileRepository,
                          SubscriptionRepository subscriptionRepository,
                          PhotoRepository photoRepository,
                          HashtagRepository hashtagRepository,
                          LoggingSystemRepository loggingSystemRepository,
                          AuthorityRepository authorityRepository) {
        this.profileRepository = profileRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.photoRepository = photoRepository;
        this.hashtagRepository = hashtagRepository;
        this.loggingSystemRepository = loggingSystemRepository;
        this.authorityRepository = authorityRepository;
    }
    LoggingSystemFactory factory = new LoggingSystemFactory();

    @GetMapping("")
    public String redirectToHome() {
        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String homepage(Model model, @RequestParam(value = "searchText", required = false) String searchText,
                           @RequestParam(value = "searchOption", required = false) String searchOption) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean isUserAdmin = false;
        if (authentication.getName() != "anonymousUser") {
            isUserAdmin = getAuthorityByUsername(authentication.getName());
        }
        if(!isUserAdmin) {
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                String username = authentication.getName();
                Profile profile = getProfileByUsername(username);
                Subscription subscription = getSubscriptionByProfileId(profile.getId());

                if (subscription.getPackagePlan() != null && subscription.getNewPackage() != null && !subscription.getPackagePlan().equals(subscription.getNewPackage())) {
                    Date lastRefreshDate = subscription.getLastRefreshDate();
                    Date dateOfLastChange = subscription.getDateOfLastChange();

                    if (dateOfLastChange != null && isSameDayPredicate.test(lastRefreshDate, dateOfLastChange)) { //!isSameDay.compareDates(lastRefreshDate, dateOfLastChange)) {
                        //Date changeDate = new Date();
                        LocalDate localDateOfLastChange = dateOfLastChange.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate nextDay = localDateOfLastChange.plusDays(1);
                        Date nextDate = Date.from(nextDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        subscription.setLastRefreshDate(nextDate);
                        subscription.setDateOfLastChange(nextDate);
                        subscription.setPackagePlan(subscription.getNewPackage());
                        saveAfterPlanChange(subscription);
                    }
                }
                model.addAttribute("subscription", subscription);
            }
        }
        model.addAttribute("username", authentication.getName());
        List<Photo> photos = getAllPhotosWithUsername();
        if(searchOption != null && searchText != null) {
            photos = search(searchText, searchOption);
            logTheAction(authentication.getName(), "", "search");
        }

        logTheAction(authentication.getName(), "", "homepage");
        model.addAttribute("photos", photos);
        return "pages/homepage";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/activity/statistics")
    public String usage(Model model, Principal principal) {
        logTheAction(principal.getName(), "", "statistics");
        List<LoggingSystem> loggingSystem = getAllActivity();
        model.addAttribute("loggingSystem", loggingSystem);
        return "pages/activity";
    }

    private Boolean getAuthorityByUsername (String username) {
        return authorityRepository.getAuthorityByUsername(username);
    }
    private Profile getProfileByUsername(String username) {
        return profileRepository.getProfileByUsername(username);
    }
    private Subscription getSubscriptionByProfileId(Long profileId) {
        return subscriptionRepository.getSubscriptionByProfileId(profileId);
    }
    private void saveAfterPlanChange(Subscription subscription) {
        subscriptionRepository.saveAfterPlanChange(subscription);
    }
    private Long getProfileIdByUsername(String username) {
        return profileRepository.getProfileIdByUsername(username);
    }
    private List<Photo> getAllPhotosByProfileId(Long profileId) {
        return photoRepository.getAllPhotosByProfileId(profileId);
    }
    private List<Photo> getAllPhotosWithUsername() {
        return photoRepository.getAllPhotosWithUsername();
    }
    private List<Photo> getPhotosByDescription(String description) {
        return photoRepository.getPhotosByDescription(description);
    }
    private List<LoggingSystem> getAllActivity() {
        return loggingSystemRepository.getAllActivity();
    }
    private List<Long> getPhotoIdsByHashtag(String hashtag) {
        return hashtagRepository.getPhotoIdsByHashtag(hashtag);
    }
    private Photo getPhotoById(Long photoId) {
        return photoRepository.getPhotoById(photoId);
    }
    public LoggingSystemProxy createLoggingSystemProxy(String username, String username2, String action) {
        LoggingSystemProxy proxy = factory.createLoggingSystem(username, username2, action);
        return proxy;
    }
    private void logTheAction(String username1, String username2, String action) {
        LoggingSystemProxy loggingSystem = createLoggingSystemProxy(username1, username2, action);
        saveLoggingSystem(loggingSystem.getUser(), loggingSystem.getAction(), loggingSystem.getTime());
    }
    private void saveLoggingSystem(String username, String action, LocalDateTime time) {
        loggingSystemRepository.save(username, action, time);
    }

    private List<Photo> search (String searchText, String searchOption) {
        List<Photo> photos = null;
        if (searchText != null && searchOption != null) {
            if (searchOption.equals("user")) {
                try {
                    Long profileId = getProfileIdByUsername(searchText.toLowerCase());
                    photos = getAllPhotosByProfileId(profileId);
                } catch (Exception e) {
                    photos = new ArrayList<>();
                }
            } else if (searchOption.equals("description")) {
                photos = getPhotosByDescription(searchText);
            } else if (searchOption.equals("hashtag")) {
                photos = new ArrayList<>();
                List<Long> photoIds = getPhotoIdsByHashtag(searchText);
                for(Long photoId: photoIds) {
                    Photo photo = getPhotoById(photoId);
                    if (photo != null) {
                        photos.add(photo);
                    }
                }
            }
        }
        return photos;
    }

//    private boolean isSameDay(Date date1, Date date2) {
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(date1);
//
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(date2);
//
//        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
//                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
//    }

//    private DateComparator isSameDay = (date1, date2) -> {
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(date1);
//
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(date2);
//
//        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
//                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
//    };

    BiPredicate<Date, Date> isSameDayPredicate = (dateA, dateB) -> {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dateA);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateB);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    };
}

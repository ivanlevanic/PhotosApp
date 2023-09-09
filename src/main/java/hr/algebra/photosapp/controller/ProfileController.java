package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
import hr.algebra.photosapp.domain.LoggingSystemFactory;
import hr.algebra.photosapp.iterator.ArrayHashtagIterator;
import hr.algebra.photosapp.iterator.HashtagIterator;
import hr.algebra.photosapp.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    public String uploadDirectory = "/Users/ivan/PhotosApp-main/src/main/resources/static/images/";
    @Autowired
    private PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PackageRepository packageRepository;
    private final ConsumptionRepository consumptionRepository;
    private final HashtagRepository hashtagRepository;
    private final LoggingSystemRepository loggingSystemRepository;

    public ProfileController(UserRepository userRepository,
                             ProfileRepository profileRepository,
                             SubscriptionRepository subscriptionRepository,
                             PackageRepository packageRepository,
                             ConsumptionRepository consumptionRepository,
                             HashtagRepository hashtagRepository,
                             LoggingSystemRepository loggingSystemRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.packageRepository = packageRepository;
        this.consumptionRepository = consumptionRepository;
        this.hashtagRepository = hashtagRepository;
        this.loggingSystemRepository = loggingSystemRepository;
    }

    LoggingSystemFactory factory = new LoggingSystemFactory();

    @Secured("ROLE_USER")
    @GetMapping("/feed")
    public String profileFeed(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long profileId = getProfileIdByUsername(authentication.getName());
        List<Photo> photos = getAllPhotosByProfileId(profileId);
        model.addAttribute("photos", photos);
        logTheAction(authentication.getName(), "", "profile feed");
        return "pages/profilefeed";
    }
    @Secured("ROLE_USER")
    @GetMapping("/data")
    public String profileData(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = getUserByUsername(username);
        Profile profile = getProfileByUsername(username);
        Subscription subscription = getSubscriptionByProfileId(profile.getId());
        Consumption consumption = new Consumption(profile.getId(), LocalDate.now(), 0);
        try {
            consumption = getConsumptionByProfileId(profile.getId());
        } catch (Exception e) {}
        PackagePlan packagePlan = getPackageByPlan(subscription.getPackagePlan()); //

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("subscription", subscription);
        model.addAttribute("consumption", consumption);
        model.addAttribute("packagePlan", packagePlan);

        logTheAction(authentication.getName(), "", "profile data page");
        return "pages/profiledata";
    }

    @Secured("ROLE_USER")
    @PostMapping("/data/change-plan")
    public String changeDataPlan(@RequestParam("plan") String plan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Profile profile = getProfileByUsername(username);
        Subscription subscription = getSubscriptionByProfileId(profile.getId());
        subscription.setNewPackage(plan);
        subscription.setDateOfLastChange(Calendar.getInstance().getTime());
        subscriptionRepository.savePlanChangeRequest(subscription);
        logTheAction(authentication.getName(), "", "package plan change");
        return "redirect:/homepage";
    }



    @Secured("ROLE_USER")
    @GetMapping("/post")
    public String postAPicture( Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logTheAction(authentication.getName(), "", "posting pictures");
        return "pages/postapicture";
    }

    @Secured("ROLE_USER")
    @PostMapping("/post/success")
    public String savePicture(@RequestParam("image") MultipartFile image,
                              @RequestParam("description") String description,
                              @RequestParam("hashtags") String hashtags,
                              Principal principal,
                              RedirectAttributes redirectAttributes) throws IOException {

        String username = principal.getName();
        Long profileId = getProfileIdByUsername(username);
        Consumption consumption = new Consumption();
        Subscription subscription = getSubscriptionByProfileId(profileId);
        PackagePlan chosenPlan = getPackageByPlan(subscription.getPackagePlan());
        Long uploadSizeLimit = chosenPlan.getUploadSize();

        if (!image.isEmpty()) {
            if (image.getSize() > uploadSizeLimit) {
                logTheAction(username, "", "picture post fail");
                redirectAttributes.addFlashAttribute("error", "Image size exceeds the upload limit. Please resize the image or upgrade your plan.");
                return "redirect:/profile/post";            }

            try {
                consumption = getConsumptionByProfileId(profileId);
            } catch (Exception e) {
                Consumption consumptionToInsert = new Consumption(profileId, LocalDate.now(),0);
                consumptionRepository.save(consumptionToInsert);
                consumption = getConsumptionByProfileId(profileId);
            }

            if(consumption.getNumberOfUploadedPhotos() >= (chosenPlan.getDailyUploadLimit() - 1)) {
                logTheAction(username, "", "picture post fail");
                redirectAttributes.addFlashAttribute("error", "You already reached your daily upload limit. Please try again tomorrow or upgrade your plan.");
                return "redirect:/profile/post";            }

            String originalFileName = image.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFileName);
            String uniqueFileName = generateUniqueFileName() + "." + fileExtension;
            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            Photo photo = new Photo(profileId, uniqueFileName, description, LocalDateTime.now(), image.getSize(), hashtags);
            photoRepository.save(photo);
            Photo photo1 = photoRepository.getPhotoByName(uniqueFileName);

            parseHashtags(photo1.getId(), hashtags);
            int uploadedPhotos = consumption.getNumberOfUploadedPhotos() + 1;
            consumptionRepository.updateNumberOfUploadedPhotos(consumption.getId(), uploadedPhotos);
            logTheAction(username, "", "picture post");
        }
        return "redirect:/profile/post";
    }

    @PostMapping("/save-photo-changes/{photoId}")
    public ResponseEntity<String> savePhotoChanges(@PathVariable String photoId, @RequestBody PhotoChangesRequest request, Principal principal) {
        Long id = Long.parseLong(photoId);
        String hashtags = request.getHashtags();
        String description = request.getDescription();

        photoRepository.updatePhoto(id, description, hashtags);
        hashtagRepository.deleteAllTheHashtags(id);
        parseHashtags(id, hashtags);
        logTheAction(principal.getName(), "", "description update");
        return ResponseEntity.ok("Changes saved successfully.");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-view/{username}")
    public String showAdminViewOfAUser(@PathVariable String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", username);
        User user = getUserByUsername(username);
        Profile profile = getProfileByUsername(username);
        Subscription subscription = getSubscriptionByProfileId(profile.getId());
        Consumption consumption = new Consumption(profile.getId(), LocalDate.now(), 0);
        try {
            consumption = getConsumptionByProfileId(profile.getId());
        } catch (Exception e) {}
        PackagePlan packagePlan = getPackageByPlan(subscription.getPackagePlan()); //
        List<Photo> photos = getAllPhotosByProfileId(profile.getId());
        logTheAction(authentication.getName(), username, "admin view");

        model.addAttribute("username", username);
        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("subscription", subscription);
        model.addAttribute("consumption", consumption);
        model.addAttribute("packagePlan", packagePlan);
        model.addAttribute("photos", photos);
        return "pages/adminview";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin-view/{username}/change-plan")
    public String adminChangesDataPlan(@PathVariable String username, @RequestParam("plan") String plan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Profile profile = getProfileByUsername(username);
        Subscription subscription = getSubscriptionByProfileId(profile.getId());
        subscription.setNewPackage(plan);
        subscription.setDateOfLastChange(Calendar.getInstance().getTime());
        subscriptionRepository.savePlanChangeRequest(subscription);
        logTheAction(authentication.getName(), username, "package change");
        return "redirect:/profile/admin-view/" + username;
    }

    private User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
    private Profile getProfileByUsername(String username) {
        return profileRepository.getProfileByUsername(username);
    }
    private PackagePlan getPackageByPlan (String plan) {
        return packageRepository.getPackageByPlan(plan);
    }
    private Subscription getSubscriptionByProfileId(Long profileId) {
        return subscriptionRepository.getSubscriptionByProfileId(profileId);
    }
    private Long getProfileIdByUsername(String username) {
        return profileRepository.getProfileIdByUsername(username);
    }
    private Consumption getConsumptionByProfileId(Long profileId) {
        return consumptionRepository.getConsumptionByProfileId(profileId, LocalDate.now());
    }
    private List<Photo> getAllPhotosByProfileId (Long profileId) {
        return photoRepository.getAllPhotosByProfileId(profileId);
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
    private String generateUniqueFileName() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID.replaceAll("-", ""); // Remove dashes from the UUID
    }
    public void parseHashtags(Long photoId, String hashtags) {
        String[] hashtagArray = hashtags.trim().split("(?<=\\s|\\#)(?=\\S)");

        HashtagIterator iterator = new ArrayHashtagIterator(hashtagArray);

        while (iterator.hasNext()) {
            String hashtag = iterator.next();
            Hashtag newHashtag = new Hashtag(photoId, hashtag);
            hashtagRepository.save(newHashtag);
        }
    }

//    public void parseHashtags2(Long photoId, String hashtags) {
//        String[] hashtagArray = hashtags.trim().split("(?<=\\s|\\#)(?=\\S)");
//        for (String hashtag : hashtagArray) {
//            hashtag = hashtag.replace("#", "");
//            if (!hashtag.isEmpty()) {
//                Hashtag newHashtag = new Hashtag(photoId, hashtag);
//                hashtagRepository.save(newHashtag);
//            }
//        }
//    }
}

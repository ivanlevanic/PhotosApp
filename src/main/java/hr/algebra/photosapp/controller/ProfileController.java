package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
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
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

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

    @Secured("ROLE_USER")
    @GetMapping("/feed")
    public String profileFeed(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long profileId = profileRepository.getProfileIdByUsername(authentication.getName());
        List<Photo> photos = photoRepository.getAllPhotosByProfileId(profileId);
        model.addAttribute("photos", photos);
        LoggingSystem loggingSystem = new LoggingSystem(authentication.getName(), "opened the profile feed page", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        return "pages/profilefeed";
    }
    @Secured("ROLE_USER")
    @GetMapping("/data")
    public String profileData(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.getUserByUsername(username);
        Profile profile = profileRepository.getProfileByUsername(username);
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profile.getId());
        Consumption consumption = new Consumption(profile.getId(), LocalDate.now(), 0);
        try {
            consumption = consumptionRepository.getConsumptionByProfileId(profile.getId(), LocalDate.now());
        } catch (Exception e) {}
        PackagePlan packagePlan = packageRepository.getPackageByPlan(subscription.getPackagePlan()); //

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("subscription", subscription);
        model.addAttribute("consumption", consumption);
        model.addAttribute("packagePlan", packagePlan);
        LoggingSystem loggingSystem = new LoggingSystem(authentication.getName(), "opened the profile data page", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);

        return "pages/profiledata";
    }

    @Secured("ROLE_USER")
    @PostMapping("/data/change-plan")
    public String changeDataPlan(@RequestParam("plan") String plan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Profile profile = profileRepository.getProfileByUsername(username);
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profile.getId());
        subscription.setNewPackage(plan);
        subscription.setDateOfLastChange(Calendar.getInstance().getTime());
        subscriptionRepository.savePlanChangeRequest(subscription);
        LoggingSystem loggingSystem = new LoggingSystem(authentication.getName(), "changed the package plan", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        return "redirect:/homepage";
    }



    @Secured("ROLE_USER")
    @GetMapping("/post")
    public String postAPicture( Model model) {
        //model.addAttribute("tvShowData", tvShowData);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggingSystem loggingSystem = new LoggingSystem(authentication.getName(), "opened the page for posting pictures", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
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
        Long profileId = profileRepository.getProfileIdByUsername(username);
        Consumption consumption = new Consumption();
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profileId);
        PackagePlan chosenPlan = packageRepository.getPackageByPlan(subscription.getPackagePlan());
        Long uploadSizeLimit = chosenPlan.getUploadSize();

        if (!image.isEmpty()) {
            if (image.getSize() > uploadSizeLimit) {
                LoggingSystem loggingSystem = new LoggingSystem(username, "tried to post a picture but failed", LocalDateTime.now());
                loggingSystemRepository.save(loggingSystem);
                redirectAttributes.addFlashAttribute("error", "Image size exceeds the upload limit. Please resize the image or upgrade your plan.");
                return "redirect:/profile/post";            }

            try {
                consumption = consumptionRepository.getConsumptionByProfileId(profileId, LocalDate.now());
            } catch (Exception e) {
                Consumption consumptionToInsert = new Consumption(profileId, LocalDate.now(),0);
                consumptionRepository.save(consumptionToInsert);
                consumption = consumptionRepository.getConsumptionByProfileId(profileId, LocalDate.now());
            }

            if(consumption.getNumberOfUploadedPhotos() >= (chosenPlan.getDailyUploadLimit() - 1)) {
                LoggingSystem loggingSystem = new LoggingSystem(username, "tried to post a picture but failed", LocalDateTime.now());
                loggingSystemRepository.save(loggingSystem);
                redirectAttributes.addFlashAttribute("error", "You already reached your daily upload limit. Please try again tomorrow or upgrade your plan.");
                return "redirect:/profile/post";            }

            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            Photo photo = new Photo(profileId, fileName, description, LocalDateTime.now(), image.getSize(), hashtags);
            photoRepository.save(photo);
            Photo photo1 = photoRepository.getPhotoByName(fileName);

            parseHashtags(photo1.getId(), hashtags);
            int uploadedPhotos = consumption.getNumberOfUploadedPhotos() + 1;
            consumptionRepository.updateNumberOfUploadedPhotos(consumption.getId(), uploadedPhotos);
            LoggingSystem loggingSystem = new LoggingSystem(username, "posted a picture", LocalDateTime.now());
            loggingSystemRepository.save(loggingSystem);
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
        LoggingSystem loggingSystem = new LoggingSystem(principal.getName(), "updated photo description and hashtags", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        return ResponseEntity.ok("Changes saved successfully.");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-view/{username}")
    public String showAdminViewOfAUser(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        User user = userRepository.getUserByUsername(username);
        Profile profile = profileRepository.getProfileByUsername(username);
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profile.getId());
        Consumption consumption = new Consumption(profile.getId(), LocalDate.now(), 0);
        try {
            consumption = consumptionRepository.getConsumptionByProfileId(profile.getId(), LocalDate.now());
        } catch (Exception e) {}
        PackagePlan packagePlan = packageRepository.getPackageByPlan(subscription.getPackagePlan()); //
        List<Photo> photos = photoRepository.getAllPhotosByProfileId(profile.getId());

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

        Profile profile = profileRepository.getProfileByUsername(username);
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profile.getId());
        subscription.setNewPackage(plan);
        subscription.setDateOfLastChange(Calendar.getInstance().getTime());
        subscriptionRepository.savePlanChangeRequest(subscription);
        LoggingSystem loggingSystem = new LoggingSystem(authentication.getName(), "changed the package plan of a user " + username, LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        return "redirect:/profile/admin-view/" + username;
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

    public void parseHashtags(Long photoId, String hashtags) {
        String[] hashtagArray = hashtags.trim().split("(?<=\\s|\\#)(?=\\S)");

        // Create an iterator
        HashtagIterator iterator = new ArrayHashtagIterator(hashtagArray);

        while (iterator.hasNext()) {
            String hashtag = iterator.next();
            Hashtag newHashtag = new Hashtag(photoId, hashtag);
            hashtagRepository.save(newHashtag);
        }
    }

}

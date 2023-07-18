package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
import hr.algebra.photosapp.domain.delete.TvShowData;
import hr.algebra.photosapp.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
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

    public ProfileController(UserRepository userRepository,
                             ProfileRepository profileRepository,
                             SubscriptionRepository subscriptionRepository,
                             PackageRepository packageRepository,
                             ConsumptionRepository consumptionRepository,
                             HashtagRepository hashtagRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.packageRepository = packageRepository;
        this.consumptionRepository = consumptionRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @Secured("ROLE_USER")
    @GetMapping("/feed")
    public String profileFeed(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long profileId = profileRepository.getProfileIdByUsername(authentication.getName());
        List<Photo> photos = photoRepository.getAllPhotosByProfileId(profileId);
        model.addAttribute("photos", photos);
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
        Consumption consumption = consumptionRepository.getConsumptionByProfileId(profile.getId());
        PackagePlan packagePlan = packageRepository.getPackageByPlan(subscription.getPackagePlan()); //

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("subscription", subscription);
        model.addAttribute("consumption", consumption);
        model.addAttribute("packagePlan", packagePlan);

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

        return "redirect:/homepage";
    }



    @Secured("ROLE_USER")
    @GetMapping("/post")
    public String postAPicture(TvShowData tvShowData, Model model) {
        model.addAttribute("tvShowData", tvShowData);
        return "pages/postapicture";
    }

    @Secured("ROLE_USER")
    @PostMapping("/post/success")
    public String savePicture(@RequestParam("image") MultipartFile image,
                              @RequestParam("description") String description,
                              @RequestParam("hashtags") String hashtags,
                              Principal principal) throws IOException {

        String username = principal.getName();
        Long profileId = profileRepository.getProfileIdByUsername(username);
        Subscription subscription = subscriptionRepository.getSubscriptionByProfileId(profileId);
        PackagePlan chosenPlan = packageRepository.getPackageByPlan(subscription.getPackagePlan());
        Long uploadSizeLimit = chosenPlan.getUploadSize();

        if (!image.isEmpty()) {
            if (image.getSize() > uploadSizeLimit) {
                return "redirect:/profile/post?error=Image size exceeds the upload limit. Please resize the image or upgrade your plan.";
            }

            String fileName = java.util.UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            String filePath = uploadDirectory + fileName;

            Path file = Paths.get(uploadDirectory + fileName);
            Files.write(file, image.getBytes());

            Photo photo = new Photo(profileId, fileName, description, LocalDateTime.now(), image.getSize(), hashtags);
            photoRepository.save(photo);
            Photo photo1 = photoRepository.getPhotoByName(fileName);

            parseHashtags(photo1.getId(), hashtags);
        }
        return "redirect:/profile/post";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/profiles/statistics")
    public String profileStatistics() {
        //model.addAttribute("tvShowData", tvShowData);
        return "pages/statistics";
    }

    @PostMapping("/save-photo-changes/{photoId}")
    public ResponseEntity<String> savePhotoChanges(@PathVariable String photoId, @RequestBody PhotoChangesRequest request) {
        Long id = Long.parseLong(photoId);
        String hashtags = request.getHashtags();
        String description = request.getDescription();

        photoRepository.updatePhoto(id, description, hashtags);
        hashtagRepository.deleteAllTheHashtags(id);
        parseHashtags(id, hashtags);

        return ResponseEntity.ok("Changes saved successfully.");
    }

    public void parseHashtags (Long photoId, String hashtags) {
        String[] hashtagArray = hashtags.trim().split("(?<=\\s|\\#)(?=\\S)");
        for (String hashtag : hashtagArray) {
            hashtag = hashtag.replace("#", ""); // Remove the '#' sign
            if (!hashtag.isEmpty()) {
                Hashtag newHashtag = new Hashtag(photoId, hashtag);
                hashtagRepository.save(newHashtag);
            }
        }
    }

}

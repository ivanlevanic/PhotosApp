package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.Authority;
import hr.algebra.photosapp.domain.Profile;
import hr.algebra.photosapp.domain.Subscription;
import hr.algebra.photosapp.domain.User;
import hr.algebra.photosapp.repository.AuthorityRepository;
import hr.algebra.photosapp.repository.ProfileRepository;
import hr.algebra.photosapp.repository.SubscriptionRepository;
import hr.algebra.photosapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Calendar;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  ProfileRepository profileRepository,
                                  SubscriptionRepository subscriptionRepository,
                                  AuthorityRepository authorityRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam("plan") String plan, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username already exists. Choose a unique username.");
            return "registration";
        }
        //Postaviti autoritet, profil, subscription
        userRepository.save(user);
        Authority authority = new Authority("ROLE_USER", user.getUsername());
        Profile profile = new Profile(user.getUsername());
        authorityRepository.save(authority);
        profileRepository.save(profile);
        Subscription subscription = new Subscription(profileRepository.getProfileIdByUsername(user.getUsername()), plan, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), plan);
        subscriptionRepository.save(subscription);

        model.addAttribute("success", true);
        return "redirect:/login?success";
    }
}

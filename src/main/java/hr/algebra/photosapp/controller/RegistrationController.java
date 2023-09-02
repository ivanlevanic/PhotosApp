package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
import hr.algebra.photosapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthorityRepository authorityRepository;
    private final ConsumptionRepository consumptionRepository;
    private final LoggingSystemRepository loggingSystemRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  ProfileRepository profileRepository,
                                  SubscriptionRepository subscriptionRepository,
                                  AuthorityRepository authorityRepository,
                                  ConsumptionRepository consumptionRepository,
                                  LoggingSystemRepository loggingSystemRepository,
                                  PasswordEncoder passwordEncoder) {
        this.subscriptionRepository = subscriptionRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.consumptionRepository = consumptionRepository;
        this.loggingSystemRepository = loggingSystemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        LoggingSystem loggingSystem = new LoggingSystem("anonymousUser", "opened registration page", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam("plan") String plan, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            LoggingSystem loggingSystem = new LoggingSystem(user.getUsername(), "tried to register but failed", LocalDateTime.now());
            loggingSystemRepository.save(loggingSystem);
            bindingResult.rejectValue("username", "error.username", "Username already exists. Choose a unique username.");
            model.addAttribute("errorMessages", "Username already exists. Choose a unique username.");
            return "registration";
        }
        //Postaviti autoritet, profil, subscription
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        Authority authority = new Authority("ROLE_USER", user.getUsername());
        Profile profile = new Profile(user.getUsername());
        authorityRepository.save(authority);
        profileRepository.save(profile);
        Subscription subscription = new Subscription(profileRepository.getProfileIdByUsername(user.getUsername()), plan, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), plan);
        subscriptionRepository.save(subscription);
        Consumption consumption = new Consumption(profileRepository.getProfileIdByUsername(user.getUsername()), LocalDate.now(), 0);
        consumptionRepository.save(consumption);
        LoggingSystem loggingSystem = new LoggingSystem(user.getUsername(), "just registered", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);
        model.addAttribute("success", true);
        return "redirect:/login?success";
    }
}

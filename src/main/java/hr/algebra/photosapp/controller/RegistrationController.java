package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.*;
import hr.algebra.photosapp.domain.LoggingSystemFactory;
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
    LoggingSystemFactory factory = new LoggingSystemFactory();

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
        logTheAction("anonymousUser", "", "opened registration page");
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam("plan") String plan, @ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            logTheAction(user.getUsername(), "", "failed registration");
            model.addAttribute("errorMessages", "Username already exists. Choose a unique username.");
            return "registration";
        }
        //Postaviti autoritet, profil, subscription
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(user.getPassword()); //encodedPassword
        userRepository.save(user);
        Authority authority = new Authority("ROLE_USER", user.getUsername());
        Profile profile = new Profile(user.getUsername());
        authorityRepository.save(authority);
        profileRepository.save(profile);
        Subscription subscription = new Subscription(profileRepository.getProfileIdByUsername(user.getUsername()), plan, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), plan);
        subscriptionRepository.save(subscription);
        Consumption consumption = new Consumption(profileRepository.getProfileIdByUsername(user.getUsername()), LocalDate.now(), 0);
        consumptionRepository.save(consumption);
        logTheAction(user.getUsername(), "", "registered");

        model.addAttribute("success", true);
        return "redirect:/login?success";
    }

//    private void SaveLoggingSystem(LoggingSystem log) {
//        loggingSystemRepository.save(log);
//    }
//    private LoggingSystem createLoggingSystem(String username, String username2, String action) {
//        return factory.createLoggingSystem(username, username2, action);
//    }
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
}


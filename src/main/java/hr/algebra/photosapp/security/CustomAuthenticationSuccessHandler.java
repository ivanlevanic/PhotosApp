package hr.algebra.photosapp.security;

import hr.algebra.photosapp.domain.LoggingSystem;
import hr.algebra.photosapp.repository.LoggingSystemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoggingSystemRepository loggingSystemRepository;

    public CustomAuthenticationSuccessHandler(LoggingSystemRepository loggingSystemRepository) {
        this.loggingSystemRepository = loggingSystemRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // Get the username of the logged-in user
        String username = authentication.getName();
        LoggingSystem loggingSystem = new LoggingSystem(username, "logged in", LocalDateTime.now());
        loggingSystemRepository.save(loggingSystem);

        // Redirect the user to the default success URL
        response.sendRedirect("/homepage");
    }
}

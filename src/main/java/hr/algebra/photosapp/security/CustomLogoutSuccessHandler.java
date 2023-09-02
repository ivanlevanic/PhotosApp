package hr.algebra.photosapp.security;

import hr.algebra.photosapp.domain.LoggingSystem;
import hr.algebra.photosapp.repository.LoggingSystemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final LoggingSystemRepository loggingSystemRepository;

    public CustomLogoutSuccessHandler(LoggingSystemRepository loggingSystemRepository) {
        this.loggingSystemRepository = loggingSystemRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication != null) {
            // Get the username of the logged-out user
            String username = authentication.getName();
            LoggingSystem loggingSystem = new LoggingSystem(username, "logged out", LocalDateTime.now());
            loggingSystemRepository.save(loggingSystem);
        }

        // Redirect the user to the logout success URL
        response.sendRedirect("/login?logout");
    }
}

package hr.algebra.photosapp.security;

import hr.algebra.photosapp.domain.LoggingSystemProxy;
import hr.algebra.photosapp.domain.LoggingSystemFactory;
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
    LoggingSystemFactory factory = new LoggingSystemFactory();

    public CustomAuthenticationSuccessHandler(LoggingSystemRepository loggingSystemRepository) {
        this.loggingSystemRepository = loggingSystemRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String username = authentication.getName();
        LoggingSystemProxy proxy = createLoggingSystemProxy(username, "", "logged in");
        saveLoggingSystem(proxy.getUser(), proxy.getAction(), proxy.getTime());

        response.sendRedirect("/homepage");
    }

    public LoggingSystemProxy createLoggingSystemProxy(String username, String username2, String action) {
        LoggingSystemProxy proxy = factory.createLoggingSystem(username, username2, action);
        return proxy;
    }
    private void saveLoggingSystem(String username, String action, LocalDateTime time) {
        loggingSystemRepository.save(username, action, time);
    }
}

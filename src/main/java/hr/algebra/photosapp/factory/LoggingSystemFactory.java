package hr.algebra.photosapp.factory;

import hr.algebra.photosapp.domain.LoggingSystem;

import java.time.LocalDateTime;

public class LoggingSystemFactory {
    public LoggingSystem createLoggingSystem (String username, String action) {
        switch (action) {
            case "homepage":
                return new LoggingSystem(username, "changed the package plan of a user " + username, LocalDateTime.now());
            case "profile statistics":
                return new LoggingSystem(username, "opened profile statistics page", LocalDateTime.now());

        }
        return null;
    }
}

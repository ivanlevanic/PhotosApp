package hr.algebra.photosapp.domain;

import java.time.LocalDateTime;

public class LoggingSystemProxy {
    private static LoggingSystemProxy instance;
    private LoggingSystem realLoggingSystem;

    private LoggingSystemProxy(String user, String action, LocalDateTime time) {
        realLoggingSystem = new LoggingSystem(user, action, time);
    }

    public static LoggingSystemProxy getInstance(String user, String action, LocalDateTime time) {
        if (instance == null) {
            instance = new LoggingSystemProxy(user, action, time);
        }
        return instance;
    }

    public Long getId() {
        return realLoggingSystem.getId();
    }

    public void setId(Long id) {
        realLoggingSystem.setId(id);
    }

    public String getUser() {
        return realLoggingSystem.getUser();
    }

    public void setUser(String user) {
        realLoggingSystem.setUser(user);
    }

    public String getAction() {
        return realLoggingSystem.getAction();
    }

    public void setAction(String action) {
        realLoggingSystem.setAction(action);
    }

    public LocalDateTime getTime() {
        return realLoggingSystem.getTime();
    }

    public void setTime(LocalDateTime time) {
        realLoggingSystem.setTime(time);
    }
}


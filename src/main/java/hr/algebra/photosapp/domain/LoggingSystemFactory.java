package hr.algebra.photosapp.domain;

import hr.algebra.photosapp.domain.LoggingSystem;
import hr.algebra.photosapp.domain.LoggingSystemProxy;

import java.time.LocalDateTime;

public class LoggingSystemFactory {
    private LoggingSystemProxy loggingSystemProxy;
    public LoggingSystemFactory() {
        loggingSystemProxy = LoggingSystemProxy.getInstance("anonymousUser", "visited the app", LocalDateTime.now());
    }

    public LoggingSystemProxy createLoggingSystem(String username, String otherUser, String action) {
        action = getLogMessage(action, otherUser);

        loggingSystemProxy.setUser(username);
        loggingSystemProxy.setAction(action);
        loggingSystemProxy.setTime(LocalDateTime.now());

        return loggingSystemProxy;
    }

    public String getLogMessage(String action, String otherUser) {
        switch (action) {
            case "homepage":
                return "opened the homepage";
            case "profile statistics":
                return "viewed profile statistics";
            case "failed registration":
                return "tried to register but failed";
            case "registered":
                return "just registered";
            case "opened registration page":
                return "opened registration page";
            case "statistics":
                return "opened profile statistics page";
            case "package change":
                return "changed the package plan of a user " + otherUser;
            case "admin view":
                return "opened the admin view of a user statistics of the user  " + otherUser;
            case "description update":
                return "updated photo description and hashtags";
            case "picture post":
                return "posted a picture";
            case "picture post fail":
                return "tried to post a picture but failed";
            case "posting pictures":
                return "opened the page for posting pictures";
            case "profile feed":
                return "opened the profile feed page";
            case "logged in":
                return "logged in";
            case "logged out":
                return "logged out";
            case "package plan change":
                return "changed the package plan";
            case "profile data page":
                return "opened the profile data page";
            case "search":
                return "searched through the photo list";
            default:
                return "performed an unknown action";
        }
    }
}

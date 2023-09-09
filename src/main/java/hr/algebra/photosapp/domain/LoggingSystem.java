package hr.algebra.photosapp.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logging_system")
public class LoggingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user", nullable = false, length = 50)
    private String user;

    @Column(name = "action", nullable = false, length = 200)
    private String action;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Transient
    private static LoggingSystem instance;

    public LoggingSystem() {
    }

    public LoggingSystem(String user, String action, LocalDateTime time) {
        this.user = user;
        this.action = action;
        this.time = time;
    }

//    public static LoggingSystem getInstance(String user, String action, LocalDateTime time) {
//        if (instance == null) {
//            instance = new LoggingSystem(user, action, time);
//            return instance;
//        }
//        instance.setUser(user);
//        instance.setAction(action);
//        instance.setTime(time);
//        return instance;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTime() { return time; }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

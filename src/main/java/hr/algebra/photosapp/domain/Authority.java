package hr.algebra.photosapp.domain;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority")
    private String authority;

    @Column(name = "username")
    private String username;

    public Authority() {}

    public Authority(String authority, String username) {
        this.authority = authority;
        this. username = username;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }

    public String getUsername() { return username;}
    public void setUsername(String username) { this.username = username; }
}

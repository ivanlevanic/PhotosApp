package hr.algebra.photosapp.controller;


import hr.algebra.photosapp.repository.rowMappers.ProfileRowMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import org.springframework.context.annotation.Bean;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PhotoRowMapper photoRowMapper() {
        return new PhotoRowMapper();
    }

    @Bean
    public ProfileRowMapper profileRowMapper() {
        return new ProfileRowMapper();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login");
    }
}

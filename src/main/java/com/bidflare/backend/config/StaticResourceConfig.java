package com.bidflare.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    // Default to a local relative path if the property isn't set (for IDE testing)
    @Value("${app.upload.dir:src/main/resources/static/images}")
    private String UPLOAD_DIR;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Chain the locations: Spring will check them in order
        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                        "file:" + UPLOAD_DIR + "/",       // Priority 1: External uploads folder
                        "classpath:/static/images/"       // Priority 2: Internal JAR resources (defaults)
                );
    }
}
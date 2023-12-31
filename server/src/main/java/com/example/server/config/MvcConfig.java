package com.example.server.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      Path path = Paths.get("image");
      Path absPath = path.toAbsolutePath();
      
      registry.addResourceHandler(path + "/**").addResourceLocations("file:///" + absPath + "/");
      WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}

package com.speproject.tripshare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadDir = Paths.get("src/main/resources/imgs/userphotos");
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/userphotos/**").addResourceLocations("file:" + uploadPath + "/");
	}
}

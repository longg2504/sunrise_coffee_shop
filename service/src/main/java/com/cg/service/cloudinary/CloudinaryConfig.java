package com.cg.service.cloudinary;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudinary.uploader")
@Data
public class CloudinaryConfig {
    @Value("${cloudinary.uploader.cloud-name}")
    private String cloudName;
    @Value("${cloudinary.uploader.api-key}")
    private String apiKey;
    @Value("${cloudinary.uploader.api-secret}")
    private String apiSecret;

}

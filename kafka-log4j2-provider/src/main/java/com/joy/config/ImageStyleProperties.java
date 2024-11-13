package com.joy.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 11/14/2024 12:36 AM
 */
@Component
@ConfigurationProperties(prefix = "image.config")
@Setter
public class ImageStyleProperties {

    private Map<String, String> style;

    private Map<String, Integer> expire;

}

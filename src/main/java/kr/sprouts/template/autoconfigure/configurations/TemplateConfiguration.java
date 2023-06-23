package kr.sprouts.template.autoconfigure.configurations;

import kr.sprouts.template.autoconfigure.properties.TemplateProperty;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { TemplateProperty.class })
public class TemplateConfiguration {

    private final TemplateProperty templateProperty;

    public TemplateConfiguration(TemplateProperty templateProperty) {
        this.templateProperty = templateProperty;

        LoggerFactory.getLogger(TemplateConfiguration.class)
                .info(String.format("Initialized %s", TemplateConfiguration.class.getName()));
    }

    public String getId() {
        return this.templateProperty.getId();
    }
}

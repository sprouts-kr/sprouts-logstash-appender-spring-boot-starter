package kr.sprouts.autoconfigure.configurations;

import kr.sprouts.autoconfigure.properties.LogstashAppenderProperty;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { LogstashAppenderProperty.class })
public class LogstashAppenderConfiguration {

    private final LogstashAppenderProperty logstashAppenderProperty;

    public LogstashAppenderConfiguration(LogstashAppenderProperty logstashAppenderProperty) {
        this.logstashAppenderProperty = logstashAppenderProperty;

        LoggerFactory.getLogger(LogstashAppenderConfiguration.class)
                .info(String.format("Initialized %s", LogstashAppenderConfiguration.class.getName()));
    }

    public String getId() {
        return this.logstashAppenderProperty.getId();
    }
}

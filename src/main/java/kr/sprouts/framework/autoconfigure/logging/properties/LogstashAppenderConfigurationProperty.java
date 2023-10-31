package kr.sprouts.framework.autoconfigure.logging.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sprouts.logstash-appender")
@Getter @Setter
public class LogstashAppenderConfigurationProperty {
    private String name;
    private String identifier;
    private List<Destination> destinations;

    @Getter @Setter
    public static class Destination {
        private String host;
        private Integer port;
    }
}

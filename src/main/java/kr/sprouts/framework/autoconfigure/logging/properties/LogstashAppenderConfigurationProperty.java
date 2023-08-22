package kr.sprouts.framework.autoconfigure.logging.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sprouts.logstash-appender")
public class LogstashAppenderConfigurationProperty {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String identifier;
    @Getter @Setter
    private List<Destination> destinations;

    public static class Destination {
        @Getter @Setter
        private String host;
        @Getter @Setter
        private Integer port;
    }
}

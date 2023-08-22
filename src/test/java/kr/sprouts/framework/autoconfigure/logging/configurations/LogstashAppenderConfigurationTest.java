package kr.sprouts.framework.autoconfigure.logging.configurations;

import kr.sprouts.framework.autoconfigure.logging.properties.LogstashAppenderConfigurationProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LogstashAppenderConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LogstashAppenderConfiguration.class));

    @Test
    void configuration() {
        String[] properties = {
                "sprouts.logstash-appender.name=appender_name",
                "sprouts.logstash-appender.identifier=appender_identifier",
                "sprouts.logstash-appender.destinations[0].host=127.0.0.1",
                "sprouts.logstash-appender.destinations[0].port=5045",
                "sprouts.logstash-appender.destinations[1].host=192.168.0.2",
                "sprouts.logstash-appender.destinations[1].port=5045"
        };

        this.applicationContextRunner.withPropertyValues(properties)
                .run(context-> assertThat(context).hasSingleBean(LogstashAppenderConfiguration.class));
    }

    @Test
    void property() {
        String[] properties = {
                "sprouts.logstash-appender.name=appender_name",
                "sprouts.logstash-appender.identifier=appender_identifier",
                "sprouts.logstash-appender.destinations[0].host=127.0.0.1",
                "sprouts.logstash-appender.destinations[0].port=5045",
                "sprouts.logstash-appender.destinations[1].host=192.168.0.2",
                "sprouts.logstash-appender.destinations[1].port=5045"
        };

        this.applicationContextRunner.withPropertyValues(properties).run(
                context -> {
                    Assertions.assertThat(context.getBean(LogstashAppenderConfigurationProperty.class).getName()).isEqualTo("appender_name");
                    assertThat(context.getBean(LogstashAppenderConfigurationProperty.class).getIdentifier()).isEqualTo("appender_identifier");
                    assertThat(context.getBean(LogstashAppenderConfigurationProperty.class).getDestinations().stream().map(
                            destination -> new InetSocketAddress(destination.getHost(), destination.getPort())
                    ).collect(Collectors.toList())).contains(new InetSocketAddress("127.0.0.1", 5045));
                    assertThat(context.getBean(LogstashAppenderConfigurationProperty.class).getDestinations().stream().map(
                            destination -> new InetSocketAddress(destination.getHost(), destination.getPort())
                    ).collect(Collectors.toList())).contains(new InetSocketAddress("192.168.0.2", 5045));
                }
        );
    }
}

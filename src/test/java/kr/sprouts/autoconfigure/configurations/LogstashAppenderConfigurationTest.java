package kr.sprouts.autoconfigure.configurations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class LogstashAppenderConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LogstashAppenderConfiguration.class));

    @Test
    void configuration() {
        this.applicationContextRunner
                .run(context-> assertThat(context).hasSingleBean(LogstashAppenderConfiguration.class));
    }

    @Test
    void property() {
        String[] properties = {
                "sprouts.logstash-appender.id=test"
        };

        this.applicationContextRunner.withPropertyValues(properties)
                .run(context-> assertThat("test".equals(context.getBean(LogstashAppenderConfiguration.class).getId())).isTrue());
    }
}
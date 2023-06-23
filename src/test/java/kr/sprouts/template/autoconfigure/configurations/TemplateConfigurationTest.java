package kr.sprouts.template.autoconfigure.configurations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TemplateConfiguration.class));

    @Test
    void configuration() {
        this.applicationContextRunner
                .run(context-> assertThat(context).hasSingleBean(TemplateConfiguration.class));
    }

    @Test
    void property() {
        String[] properties = {
                "sprouts.template.id=test"
        };

        this.applicationContextRunner.withPropertyValues(properties)
                .run(context-> assertThat("test".equals(context.getBean(TemplateConfiguration.class).getId())).isTrue());
    }
}
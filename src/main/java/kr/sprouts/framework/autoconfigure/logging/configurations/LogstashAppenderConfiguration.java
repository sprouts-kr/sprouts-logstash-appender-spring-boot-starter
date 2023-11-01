package kr.sprouts.framework.autoconfigure.logging.configurations;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import kr.sprouts.framework.autoconfigure.logging.properties.LogstashAppenderConfigurationProperty;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.InetSocketAddress;

@AutoConfiguration
@EnableConfigurationProperties(value = { LogstashAppenderConfigurationProperty.class })
@Slf4j
public class LogstashAppenderConfiguration {

    private final LogstashAppenderConfigurationProperty logstashAppenderConfigurationProperty;

    public LogstashAppenderConfiguration(LogstashAppenderConfigurationProperty logstashAppenderConfigurationProperty) {
        this.logstashAppenderConfigurationProperty = logstashAppenderConfigurationProperty;

        this.initializeLogstashAppender((LoggerContext) LoggerFactory.getILoggerFactory());

        if (log.isInfoEnabled()) log.info("Initialized {}", LogstashAppenderConfiguration.class.getSimpleName());
    }

    private void initializeLogstashAppender(LoggerContext loggerContext) {
        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);

        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setThrowableConverter(throwableConverter);
        logstashEncoder.setCustomFields("{\"identifier\":\"" + this.logstashAppenderConfigurationProperty.getIdentifier() + "\"}");

        LogstashTcpSocketAppender logstashTcpSocketAppender = new LogstashTcpSocketAppender();
        logstashTcpSocketAppender.setName(this.logstashAppenderConfigurationProperty.getName().toUpperCase());
        logstashTcpSocketAppender.setContext(loggerContext);
        logstashTcpSocketAppender.setEncoder(logstashEncoder);
        logstashTcpSocketAppender.addDestinations(this.logstashAppenderConfigurationProperty.getDestinations().stream()
                .map(destination -> new InetSocketAddress(destination.getHost(), destination.getPort()))
                .toList()
                .toArray(InetSocketAddress[]::new)
        );

        logstashTcpSocketAppender.start();

        AsyncAppender asyncLogstashAppender = new AsyncAppender();
        asyncLogstashAppender.setContext(loggerContext);
        asyncLogstashAppender.setName("ASYNC_" + this.logstashAppenderConfigurationProperty.getName().toUpperCase());
        asyncLogstashAppender.addAppender(logstashTcpSocketAppender);

        asyncLogstashAppender.start();

        loggerContext.getLogger("ROOT").addAppender(asyncLogstashAppender);
    }
}

package kr.sprouts.autoconfigure.configurations;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import kr.sprouts.autoconfigure.properties.LogstashAppenderProperty;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(value = { LogstashAppenderProperty.class })
public class LogstashAppenderConfiguration {

    private final LogstashAppenderProperty logstashAppenderProperty;

    public LogstashAppenderConfiguration(LogstashAppenderProperty logstashAppenderProperty) {
        this.logstashAppenderProperty = logstashAppenderProperty;

        this.initializeLogstashAppender((LoggerContext) LoggerFactory.getILoggerFactory());

        LoggerFactory.getLogger(LogstashAppenderConfiguration.class)
                .info(String.format("Initialized %s", LogstashAppenderConfiguration.class.getName()));
    }

    private void initializeLogstashAppender(LoggerContext loggerContext) {
        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);

        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setThrowableConverter(throwableConverter);
        logstashEncoder.setCustomFields("{\"identifier\":\"" + this.logstashAppenderProperty.getIdentifier() + "\"}");

        LogstashTcpSocketAppender logstashTcpSocketAppender = new LogstashTcpSocketAppender();
        logstashTcpSocketAppender.setName(this.logstashAppenderProperty.getName().toUpperCase());
        logstashTcpSocketAppender.setContext(loggerContext);
        logstashTcpSocketAppender.setEncoder(logstashEncoder);
        logstashTcpSocketAppender.addDestinations(this.logstashAppenderProperty.getDestinations().stream()
                .map(destination -> new InetSocketAddress(destination.getHost(), destination.getPort()))
                .collect(Collectors.toList())
                .toArray(InetSocketAddress[]::new)
        );

        logstashTcpSocketAppender.start();

        AsyncAppender asyncLogstashAppender = new AsyncAppender();
        asyncLogstashAppender.setContext(loggerContext);
        asyncLogstashAppender.setName("ASYNC_" + this.logstashAppenderProperty.getName().toUpperCase());
        asyncLogstashAppender.addAppender(logstashTcpSocketAppender);

        asyncLogstashAppender.start();

        loggerContext.getLogger("ROOT").addAppender(asyncLogstashAppender);
    }
}

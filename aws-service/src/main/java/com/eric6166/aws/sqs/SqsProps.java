package com.eric6166.aws.sqs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "cloud.aws.sqs.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "cloud.aws.sqs")
public class SqsProps {

    Template template;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Template {

        Queue queue;
        Integer delaySeconds;
        Integer maxNumberOfMessages;

        @Getter
        @Setter
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class Queue {
            Standard standard;
            Fifo fifo;

            @Getter
            @Setter
            @FieldDefaults(level = AccessLevel.PRIVATE)
            public static class Standard {
                String name;
            }

            @Getter
            @Setter
            @FieldDefaults(level = AccessLevel.PRIVATE)
            public static class Fifo {
                String name;
            }
        }
    }
}

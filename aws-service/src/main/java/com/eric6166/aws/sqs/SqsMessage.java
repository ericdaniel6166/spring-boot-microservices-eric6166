package com.eric6166.aws.sqs;

public interface SqsMessage {

    String getMessageBody();

    String getId();

    default String getMessageGroupId() {
        return null;
    }

    default Integer getDelaySeconds() {
        return null;
    }

}

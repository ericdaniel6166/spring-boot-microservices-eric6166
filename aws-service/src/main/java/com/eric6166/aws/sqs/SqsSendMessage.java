package com.eric6166.aws.sqs;

public interface SqsSendMessage {

    String getMessageBody();

    String getId();

    default String getMessageGroupId() {
        return null;
    }

    default Integer getDelaySeconds() {
        return null;
    }

}

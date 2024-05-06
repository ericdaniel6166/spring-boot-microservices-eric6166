package com.eric6166.aws.sqs;

public interface SqsMessage {

    String getMessageBody();

    String getId();

    String getMessageGroupId();

    Integer getDelaySeconds();

}

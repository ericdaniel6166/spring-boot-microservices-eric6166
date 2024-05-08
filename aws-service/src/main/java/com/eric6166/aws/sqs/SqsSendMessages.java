package com.eric6166.aws.sqs;

import java.util.Collection;

public interface SqsSendMessages {

    Collection<SqsSendMessage> getSqsMessages();

    default Integer getDelaySeconds() {
        return null;
    }

    default String getMessageGroupId() {
        return null;
    }

}


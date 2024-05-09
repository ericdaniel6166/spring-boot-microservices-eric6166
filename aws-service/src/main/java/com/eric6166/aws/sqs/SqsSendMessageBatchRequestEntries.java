package com.eric6166.aws.sqs;

import java.util.Collection;

public interface SqsSendMessageBatchRequestEntries {

    Collection<SqsSendMessageBatchRequestEntry> getSqsSendMessageBatchRequestEntries();

    default Integer getDelaySeconds() {
        return null;
    }

    default String getMessageGroupId() {
        return null;
    }

}


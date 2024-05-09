package com.eric6166.aws.sqs;

public interface SqsDeleteMessageBatchRequestEntry {

    String getReceiptHandle();

    String getId();

}

package com.eric6166.aws.service;

import com.eric6166.base.exception.AppException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@ConditionalOnProperty(name = "cloud.aws.s3.enabled", havingValue = "true")
public interface S3Service {

    boolean isBucketExisted(String bucket);

    void createBucket(String bucket) throws AppException;

    void deleteBucket(String bucket) throws AppException;

    void uploadObject(String bucket, String key, MultipartFile file) throws IOException, AppException;

    void deleteObject(String bucket, String key) throws AppException;
}

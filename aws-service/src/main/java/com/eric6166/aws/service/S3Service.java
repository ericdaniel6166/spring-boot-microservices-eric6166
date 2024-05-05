package com.eric6166.aws.service;

import com.eric6166.base.exception.AppException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@ConditionalOnProperty(name = "cloud.aws.s3.enabled", havingValue = "true")
public interface S3Service {

    boolean isBucketExisted(String bucket);

    CreateBucketResponse createBucket(String bucket) throws AppException;

    DeleteBucketResponse deleteBucket(String bucket) throws AppException;

    PutObjectResponse uploadObject(String bucket, String key, MultipartFile file) throws IOException, AppException;

    DeleteObjectResponse deleteObject(String bucket, String key) throws AppException;

    ListObjectsV2Response listObject(String bucket);

    ResponseInputStream<GetObjectResponse> getObject(String bucket, String key);
}

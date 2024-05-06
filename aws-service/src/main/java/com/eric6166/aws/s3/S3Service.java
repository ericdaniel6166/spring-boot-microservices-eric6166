package com.eric6166.aws.s3;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import com.eric6166.aws.utils.AWSExceptionUtils;
import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "cloud.aws.s3.enabled", havingValue = "true")
public class S3Service {

    S3Client s3Client;
    S3Presigner s3Presigner;
    Tracer tracer;

    void test() {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("test").start();
        try (var ws = tracer.withSpanInScope(span)) {

        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    //deleting multiple Objects
    //deleteObjects

    //listBuckets
    //listObjectsV2Paginator

    //copy,
    //copyObject
    //rename (destination = source (bucket + key))
    //moving = copy + delete

    //getObjectAttributes

    //get object URL
//            s3Client.utilities().getUrl()

    //GetObjectContentType
//    .headObject

    //https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/ManagingObjectTags.java
//    PutObjectRequest putOb = PutObjectRequest.builder()
//            .bucket(bucketName)
//            .key(objectKey)
//            .tagging(allTags)
//            .build();

    //getObjectAsBytes
    //.getObject(objectRequest, ResponseTransformer.toBytes());
    //getObjectTagging

//    https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/ParseUri.java
//            s3Client.utilities().parseUri(s3ObjectUrl) // 'https://s3.us-west-1.amazonaws.com/myBucket/resources/doc.txt?versionId=abc123&partNumber=77&partNumber=88'.

    //https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/S3ZipExample.java
    //S3 zip

//    S3Presigner
//            s3Presigner.presignGetObject()


    public ResponseInputStream<GetObjectResponse> getObject(String bucket, String key) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("getObject").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
            } catch (NoSuchBucketException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public ListObjectsV2Response listObject(String bucket) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("listObject").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.listObjectsV2(ListObjectsV2Request.builder()
                        .bucket(bucket)
                        .build());
            } catch (NoSuchBucketException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public PutObjectResponse uploadObject(String bucket, String key, MultipartFile file) throws IOException, AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("uploadObject").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(key)
                                .contentLength(file.getSize())
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            } catch (NoSuchBucketException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
            } catch (S3Exception e) {
                throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(
                        AWSExceptionUtils.buildErrorMessage(e, String.format("bucket with name '%s'", bucket))));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public DeleteObjectResponse deleteObject(String bucket, String key) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteObject").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
            } catch (NoSuchBucketException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
            } catch (S3Exception e) {
                throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(
                        AWSExceptionUtils.buildErrorMessage(e, String.format("bucket with name '%s'", bucket))));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public DeleteBucketResponse deleteBucket(String bucket) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteBucket").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.deleteBucket(DeleteBucketRequest.builder()
                        .bucket(bucket)
                        .build());
            } catch (NoSuchBucketException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
            } catch (S3Exception e) {
                throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(
                        AWSExceptionUtils.buildErrorMessage(e, String.format("bucket with name '%s'", bucket))));
            }
        } catch (RuntimeException e) {
            span.error(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            throw e;
        } finally {
            span.finish();
        }
    }

    public CreateBucketResponse createBucket(String bucket) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("createBucket").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucket)
                        .build());
            } catch (BucketAlreadyOwnedByYouException e) {
                throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(String.format("bucket with name '%s' already exists", bucket)));
            } catch (BucketAlreadyExistsException e) {
                throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(String.format("bucket with name '%s' is not available", bucket)));
            } catch (IllegalArgumentException e) {
                throw new AppException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                throw e;
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public boolean isBucketExisted(String bucket) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("isBucketExisted").start();
        try (var ws = tracer.withSpanInScope(span)) {
            boolean isBucketExisted = false;
            try {
                s3Client.headBucket(HeadBucketRequest.builder()
                        .bucket(bucket)
                        .build());
                isBucketExisted = true;
            } catch (NoSuchBucketException e) {
                //
            } catch (S3Exception e) {
                switch (HttpStatus.valueOf(e.statusCode())) {
                    case MOVED_PERMANENTLY -> {
                        //
                    }
                    default -> throw e;
                }
            }
            return isBucketExisted;
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }

    }
}
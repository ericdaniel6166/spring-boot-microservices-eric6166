package com.eric6166.aws.s3;

import com.eric6166.aws.utils.AWSExceptionUtils;
import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Uri;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectAttributesRequest;
import software.amazon.awssdk.services.s3.model.GetObjectAttributesResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "cloud.aws.s3.enabled", havingValue = "true")
public class AppS3Client {


    S3Client s3Client;
    S3Presigner s3Presigner;

    public ListObjectsV2Response listObject(@NotBlank String bucket) throws AppException {
        try {
            return s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public ResponseBytes<GetObjectResponse> getObjectAsBytes(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public ResponseInputStream<GetObjectResponse> getObject(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public GetObjectAttributesResponse getObjectAttributes(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.getObjectAttributes(GetObjectAttributesRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public GetObjectTaggingResponse getObjectTagging(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.getObjectTagging(GetObjectTaggingRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public URL getUrl(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public S3Uri parseUri(@NotBlank String uri) throws AppException {
        try {
            return s3Client.utilities().parseUri(URI.create(uri));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public PutObjectResponse uploadObject(@NotBlank String bucket, @NotBlank String key, @NotNull MultipartFile file) throws IOException, AppException {
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
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public CopyObjectResponse renameObject(@NotBlank String bucket, @NotBlank String oldKey, @NotBlank String newKey) throws AppException {
        var response = copyObject(bucket, oldKey, bucket, newKey);
        deleteObject(bucket, oldKey);
        return response;
    }

    public CopyObjectResponse moveObject(@NotBlank String sourceBucket, @NotBlank String sourceKey, @NotBlank String destinationBucket, @NotBlank String destinationKey) throws AppException {
        var response = copyObject(sourceBucket, sourceKey, destinationBucket, destinationKey);
        deleteObject(sourceBucket, sourceKey);
        return response;
    }

    public CopyObjectResponse copyObject(@NotBlank String sourceBucket, @NotBlank String sourceKey, @NotBlank String destinationBucket, @NotBlank String destinationKey) throws AppException {
        try {
            return s3Client.copyObject(CopyObjectRequest.builder()
                    .sourceBucket(sourceBucket)
                    .sourceKey(sourceKey)
                    .destinationBucket(destinationBucket)
                    .destinationKey(destinationKey)
                    .build());
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public PresignedGetObjectRequest presignGetObject(@NotBlank String bucket, @NotBlank String key, @NotNull Duration signatureDuration) throws AppException {
        try {
            return s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                    .signatureDuration(signatureDuration)
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build())
                    .build());
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public DeleteObjectResponse deleteObject(@NotBlank String bucket, @NotBlank String key) throws AppException {
        try {
            return s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public DeleteObjectsResponse deleteObjects(@NotBlank String bucket, @NotEmpty String... keys) throws AppException {
        return deleteObjects(bucket, Arrays.stream(keys));
    }

    public DeleteObjectsResponse deleteObjects(@NotBlank String bucket, @NotEmpty Collection<String> keys) throws AppException {
        return deleteObjects(bucket, keys.stream());
    }

    public DeleteObjectsResponse deleteObjects(@NotBlank String bucket, @NotNull Stream<String> keys) throws AppException {
        try {
            var objects = keys
                    .map(key -> ObjectIdentifier.builder()
                            .key(key)
                            .build())
                    .toList();
            return s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder()
                            .objects(objects)
                            .build())
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public DeleteBucketResponse deleteBucket(@NotBlank String bucket) throws AppException {
        try {
            return s3Client.deleteBucket(DeleteBucketRequest.builder()
                    .bucket(bucket)
                    .build());
        } catch (NoSuchBucketException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("bucket with name '%s'", bucket));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public CreateBucketResponse createBucket(@NotBlank String bucket) throws AppException {
        try {
            return s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucket)
                    .build());
        } catch (BucketAlreadyOwnedByYouException e) {
            throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(String.format("bucket with name '%s' already exists", bucket)));
        } catch (BucketAlreadyExistsException e) {
            throw AWSExceptionUtils.buildAppException(e, new AppBadRequestException(String.format("bucket with name '%s' is not available", bucket)));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        } catch (IllegalArgumentException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isBucketExisted(@NotBlank String bucket) throws AppException {
        boolean isBucketExisted = false;
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build());
            isBucketExisted = true;
        } catch (NoSuchBucketException e) {
            //
        } catch (AwsServiceException e) {
            switch (HttpStatus.valueOf(e.statusCode())) {
                case MOVED_PERMANENTLY -> {
                    //
                }
                default -> throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
            }
        }
        return isBucketExisted;
    }

//    void test() {
//        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("test").start();
//        try (var ws = tracer.withSpanInScope(span)) {
//
//        } catch (RuntimeException e) {
//            log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
//            span.error(e);
//            throw e;
//        } finally {
//            span.finish();
//        }
//    }


    //deleting multiple Objects
    //deleteObjects

    //listBuckets
    //listObjectsV2Paginator

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

    //.getObject(objectRequest, ResponseTransformer.toBytes());
    //getObjectTagging

//    https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/ParseUri.java
//            s3Client.utilities().parseUri(s3ObjectUrl) // 'https://s3.us-west-1.amazonaws.com/myBucket/resources/doc.txt?versionId=abc123&partNumber=77&partNumber=88'.

    //https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/S3ZipExample.java
    //S3 zip

//    S3Presigner
}

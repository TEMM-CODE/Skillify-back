package com.temm.skillify.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3Service(
        @Value("${aws.accessKeyId}") String accessKeyId,
        @Value("${aws.secretAccessKey}") String secretAccessKey,
        @Value("${aws.region}") String region,
        @Value("${aws.s3.bucket}") String bucketName,
        @Value("${aws.s3.endpoint}") String endpoint) {
        
        this.bucketName = bucketName;
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        
        // Configure S3 to use path-style access for Contabo
        S3Configuration s3Config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();
        
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(s3Config) // Add this line
                .build();
        
        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(s3Config) // Add this line
                .build();
    }

    // Upload video to S3 and return the object key
    public String uploadVideo(MultipartFile videoFile, String lessonId, int position) throws IOException {
        String key = String.format("videos/%s/lesson-content-%d.%s",
                lessonId,
                position,
                getFileExtension(videoFile.getOriginalFilename()));
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(videoFile.getContentType())
                .build();
        
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                videoFile.getInputStream(), videoFile.getSize()));
        
        return key;
    }

    // Generate a pre-signed URL for secure video access
    public URL generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL valid for 60 minutes
                .getObjectRequest(getObjectRequest)
                .build();
        
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "mp4"; // Default to mp4 if no extension
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public void deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
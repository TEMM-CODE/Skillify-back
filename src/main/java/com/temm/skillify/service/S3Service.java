package com.temm.skillify.service;

import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import io.imagekit.errors.ImageKitException;
import io.imagekit.models.files.FileDeleteParams;
import io.imagekit.models.files.FileUploadParams;
import io.imagekit.models.files.FileUploadResponse;
import io.imagekit.models.SrcOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class S3Service {  // Renamed for clarity

    private final ImageKitClient imageKitClient;
    private final String urlEndpoint;

public S3Service(
        @Value("${imagekit.privateKey}") String privateKey,
        @Value("${imagekit.urlEndpoint}") String urlEndpoint) {

    this.urlEndpoint = urlEndpoint.endsWith("/") 
            ? urlEndpoint.substring(0, urlEndpoint.length() - 1) 
            : urlEndpoint;

    this.imageKitClient = ImageKitOkHttpClient.builder()
            .privateKey(privateKey)
            // .publicKey(...)  ← removed for older SDK compatibility
            .build();
}

    /**
     * Upload video and return the ImageKit file path (similar to old S3 key)
     */
    public String uploadVideo(MultipartFile videoFile, String lessonId, int position) throws IOException, ImageKitException {
        String fileName = String.format("lesson-content-%d.%s",
                position,
                getFileExtension(videoFile.getOriginalFilename()));

        String folder = String.format("videos/%s", lessonId);

        FileUploadParams params = FileUploadParams.builder()
                .file(videoFile.getInputStream())
                .fileName(fileName)
                .folder(folder)
                // Optional: add tags, custom metadata, etc.
                // .addTag("lesson")
                // .addTag("video")
                .build();

        FileUploadResponse response = imageKitClient.files().upload(params);

        // Return a path-like identifier (you can store this in DB instead of old S3 key)
        return response.filePath().orElseThrow(() -> new RuntimeException("Erro ao retornar o path"));  // or response.url() / response.fileId()
    }

    /**
     * Generate a signed (time-limited) URL for secure video access
     */
    public URL generatePresignedUrl(String filePathOrId) {
        try {
            // For signed URLs with expiration
            SrcOptions srcOptions = SrcOptions.builder()
                    .urlEndpoint(urlEndpoint)
                    .src(filePathOrId)           // Can be filePath or fileId
                    .signed(true)
                    .expiresIn((int) TimeUnit.MINUTES.toSeconds(60))  // 60 minutes
                    .build();

            String signedUrl = imageKitClient.helper().buildUrl(srcOptions);
            return new URL(signedUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signed URL", e);
        }
    }

    /**
     * Delete a file by fileId (recommended) or filePath
     */
    public void deleteObject(String fileId) throws ImageKitException {
        FileDeleteParams params = FileDeleteParams.builder()
                .fileId(fileId)
                .build();

        imageKitClient.files().delete(params);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "mp4";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    // Optional: Helper to get public URL (no signature)
    public String getPublicUrl(String filePath) {
        return urlEndpoint + (filePath.startsWith("/") ? "" : "/") + filePath;
    }
}
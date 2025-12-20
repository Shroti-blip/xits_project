package com.example.ProjectHON.ImageCheckAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.RekognitionClientBuilder;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

import java.io.IOException;


@Service
public class RekognitionImage {

    @Autowired
    RekognitionClient rekognitionClient;

    public boolean isImageSafe(MultipartFile file) throws IOException {
        try {
            Image image = Image.builder()
                    .bytes(SdkBytes.fromByteArray(file.getBytes()))
                    .build();

            DetectModerationLabelsResponse response = rekognitionClient.detectModerationLabels(
                    DetectModerationLabelsRequest.builder()
                            .image(image)
                            .minConfidence(80F)
                            .build()
            );
            return response.moderationLabels().isEmpty();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

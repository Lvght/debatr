package br.ufscar.dc.dsw1.debatr.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class ExternalImageServiceHelper {

    public ExternalImageServiceHelper() {
        this.client = S3Client.builder().region(Region.SA_EAST_1).build();
        this.presigner = S3Presigner.builder().region(Region.SA_EAST_1).build();
    }

    private S3Client client;
    private S3Presigner presigner;

    public String uploadImage(MultipartFile file, String path) {
        try {
            Map<String, String> metadata = new HashMap<String, String>();
            metadata.put("Content-Type", file.getContentType());

            final String bucketName = System.getenv("AWS_BUCKET_NAME");

            PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(path).metadata(metadata)
                    .build();

            // Envia a requisição para o S3
            PutObjectResponse response = client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            if (response.eTag() != null) {
                System.out.println("Uploaded file with key: " + response.eTag());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getObjectPresignedUrl(String uid) {
        String bucketName = System.getenv("AWS_BUCKET_NAME");
        String key = "profiles/" + uid + ".jpg";

        // Ablublué

        // Monta a requisição
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().getObjectRequest(request).build();

        // Faz a requisição
        PresignedGetObjectRequest presignedObjectRequest = presigner.presignGetObject(presignRequest);
        return presignedObjectRequest.url().toExternalForm();
    }
}

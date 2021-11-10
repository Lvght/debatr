package br.ufscar.dc.dsw1.debatr.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class ExternalImageServiceHelper {

    public ExternalImageServiceHelper() {
        this.client = S3Client.builder().region(Region.SA_EAST_1).build();
    }

    private S3Client client;

    public void uploadImage(MultipartFile file) {
        try {
            Map<String, String> metadata = new HashMap<String, String>();
            metadata.put("Content-Type", file.getContentType());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket("REPLACE-BUCKET-NAME-HERE")
                    .key("myfile")
                    .metadata(metadata)
                    .build();
            
            // Envia a requisição para o S3
            PutObjectResponse response = client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            if (response.eTag() != null) {
                System.out.println("Uploaded file with key: " + response.eTag());
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

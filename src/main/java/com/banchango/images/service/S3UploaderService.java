package com.banchango.images.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.warehouseattachments.WarehouseAttachments;
import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
import com.banchango.images.exception.FileRemoveException;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3UploaderService {

    private AmazonS3 s3Client;
    private final WarehouseAttachmentsRepository warehouseAttachmentsRepository;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.access_key_id}")
    private String accessKey;

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    private void deleteFileOnS3(final String fileName) throws FileRemoveException {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch(Exception exception) {
            throw new FileRemoveException();
        }
    }

    // TODO : warehouse_attachments에 있는 이미지 삭제
    @Transactional
    public JSONObject deleteImage(String token, String imageName, Integer warehouseId) throws Exception {
        return null;
    }

    // TODO : warehouse_main_images에 있는 이미지 삭제
    @Transactional
    public JSONObject deleteMainImage(String token, Integer warehouseId) {
        return null;
    }

    private String uploadFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

    @Transactional
    public JSONObject upload(MultipartFile multipartFile, String token, Integer warehouseId) throws Exception {
        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
            throw new AuthenticateException();
        }
        if(!warehouseId.equals(Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token))))) {
            throw new WarehouseInvalidAccessException();
        }
        String uploadedImageUrl = uploadFile(multipartFile);
        WarehouseAttachments attachment = WarehouseAttachments.builder()
                .url(uploadedImageUrl).warehouseId(warehouseId).build();
        warehouseAttachmentsRepository.save(attachment);
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("message", "파일 업로드에 성공했습니다.");
        jsonObject.put("url", uploadedImageUrl);
        return jsonObject;
    }
}

package com.banchango.images.service;

//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.util.IOUtils;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.InternalServerErrorException;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouseimages.WarehouseImagesRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.images.dto.ImageInfoResponseDto;
import com.banchango.warehouses.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.utils.IoUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Service
public class S3UploaderService {

    private S3Client s3Client;
    private final WarehouseImagesRepository warehouseImagesRepository;
    private final WarehousesRepository warehousesRepository;

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
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        s3Client = S3Client.builder().credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(Region.AP_NORTHEAST_2).build();
//        s3Client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(this.region)
//                .build();
    }

    private String uploadFile(MultipartFile file) {
        try {
            //software.amazon.awssdk.services.s3.model.
            //ObjectMetadata objectMetadata = new ObjectMetadata();
            //file.getBytes()
            byte[] bytes = IoUtils.toByteArray(file.getInputStream());
            //objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            String fileName = file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentLength(file.getSize())
                    .key(file.getOriginalFilename())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            //PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, byteArrayInputStream);
            //s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
            //return s3Client.getUrl(bucket, fileName).toString();
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(file.getOriginalFilename())
                    .build();

            S3Utilities s3Utilities = s3Client.utilities();
            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(file.getOriginalFilename())
                    .build();
            return s3Utilities.getUrl(getUrlRequest).toString();
            //String url = s3Client.getObject(getObjectRequest).response().toString();
            //System.out.println(url);
           // return null;
            //return s3Client.getObject(getObjectRequest);
        } catch(IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

//    private void deleteFile(final String fileName) {
//        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
//        try {
//            s3Client.deleteObject(deleteObjectRequest);
//        } catch(Exception exception) {
//            throw new InternalServerErrorException(exception.getMessage());
//        }
//    }

    private boolean isUserAuthenticatedToModifyWarehouseInfo(Integer userId, Integer warehouseId) {
        List<Warehouses> warehouses = warehousesRepository.findByUserId(userId);
        for(Warehouses warehouse : warehouses) {
            if(warehouse.getId().equals(warehouseId)) {
                if(warehouse.getUserId().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public ImageInfoResponseDto uploadExtraImage(MultipartFile file, String token, Integer warehouseId) {
        if(!isUserAuthenticatedToModifyWarehouseInfo(JwtTokenUtil.extractUserId(token), warehouseId)) {
            throw new WarehouseInvalidAccessException();
        }
        System.out.println("USER IS VALIDATED");
        if(warehouseImagesRepository.findByWarehouseIdAndIsMain(warehouseId, false).size() >= 5) {
            throw new WarehouseExtraImageLimitException();
        }
        System.out.println("IMAGE IS UPLOADABLE");
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        String url = uploadFile(file);
        WarehouseImages image = WarehouseImages.builder().url(url).isMain(false).warehouse(warehouse).build();
        WarehouseImages savedImage = warehouseImagesRepository.save(image);
        return new ImageInfoResponseDto(savedImage);
    }

    @Transactional
    public ImageInfoResponseDto uploadMainImage(MultipartFile file, String token, Integer warehouseId) {
        if(!isUserAuthenticatedToModifyWarehouseInfo(JwtTokenUtil.extractUserId(token), warehouseId)) {
            throw new WarehouseInvalidAccessException();
        }
        List<WarehouseImages> images = warehouseImagesRepository.findByWarehouseIdAndIsMain(warehouseId, true);
        if(images.size() >= 1) {
            throw new WarehouseMainImageAlreadyRegisteredException();
        }
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        String url = uploadFile(file);
        WarehouseImages image = WarehouseImages.builder().url(url).isMain(true).warehouse(warehouse).build();
        WarehouseImages savedImage = warehouseImagesRepository.save(image);
        return new ImageInfoResponseDto(savedImage);
    }

    @Transactional
    public BasicMessageResponseDto deleteExtraImage(String fileName, String token, Integer warehouseId) {
        if(!isUserAuthenticatedToModifyWarehouseInfo(JwtTokenUtil.extractUserId(token), warehouseId)) {
            throw new WarehouseInvalidAccessException();
        }
        if(warehouseImagesRepository.findByUrlContaining(fileName).isPresent()) {
            warehouseImagesRepository.deleteByUrlContaining(fileName);
            //deleteFile(fileName);
            return new BasicMessageResponseDto("삭제에 성공했습니다.");
        } else {
            throw new WarehouseExtraImageNotFoundException(fileName + "은(는) 저장되어 있지 않은 사진입니다.");
        }
    }

    @Transactional
    public BasicMessageResponseDto deleteMainImage(String token, Integer warehouseId) {
        if(!isUserAuthenticatedToModifyWarehouseInfo(JwtTokenUtil.extractUserId(token), warehouseId)) {
            throw new WarehouseInvalidAccessException();
        }
        List<WarehouseImages> images = warehouseImagesRepository.findByWarehouseIdAndIsMain(warehouseId, true);
        if(images.size() >= 1) {
            WarehouseImages image = images.get(0);
            String[] splitTemp = image.getUrl().split("/");
            String fileName = splitTemp[splitTemp.length - 1];
            //deleteFile(fileName);
            return new BasicMessageResponseDto("삭제에 성공했습니다.");
        } else {
            throw new WarehouseMainImageNotFoundException();
        }
    }
}

//package com.banchango.images.service;
//
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
//import com.banchango.auth.exception.AuthenticateException;
//import com.banchango.auth.token.JwtTokenUtil;
//import com.banchango.domain.warehouseattachments.WarehouseAttachments;
//import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
//import com.banchango.domain.warehousemainimages.WarehouseMainImages;
//import com.banchango.domain.warehousemainimages.WarehouseMainImagesRepository;
//import com.banchango.domain.warehouses.Warehouses;
//import com.banchango.domain.warehouses.WarehousesRepository;
//import com.banchango.images.exception.FileRemoveException;
//import com.banchango.tools.ObjectMaker;
//import com.banchango.warehouses.exception.*;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.PostConstruct;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class S3UploaderService {
//
//    private AmazonS3 s3Client;
//    private final WarehouseAttachmentsRepository warehouseAttachmentsRepository;
//    private final WarehousesRepository warehousesRepository;
//    private final WarehouseMainImagesRepository warehouseMainImagesRepository;
//
//    @Value("${aws.s3.bucket}")
//    private String bucket;
//
//    @Value("${aws.access_key_id}")
//    private String accessKey;
//
//    @Value("${aws.secret_access_key}")
//    private String secretKey;
//
//    @Value("${aws.s3.region}")
//    private String region;
//
//    @PostConstruct
//    public void setS3Client() {
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//
//        s3Client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(this.region)
//                .build();
//    }
//
//    private void deleteFileOnS3(final String fileName) throws FileRemoveException {
//        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
//        try {
//            s3Client.deleteObject(deleteObjectRequest);
//        } catch(Exception exception) {
//            throw new FileRemoveException();
//        }
//    }
//
//    private void checkTokenAndWarehouseId(String token, Integer warehouseId) throws Exception {
//        Warehouses warehouse = warehousesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
//            throw new AuthenticateException();
//        }
//        Integer userIdInToken = Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token)));
//        if(!userIdInToken.equals(warehouse.getUserId())) {
//            throw new WarehouseInvalidAccessException();
//        }
//    }
//
//    @Transactional
//    public JSONObject deleteImage(String token, String imageName, Integer warehouseId) throws Exception {
//        checkTokenAndWarehouseId(token, warehouseId);
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        if(warehouseAttachmentsRepository.findByUrlContaining(imageName).isPresent()) {
//            warehouseAttachmentsRepository.deleteByUrlContaining(imageName);
//            deleteFileOnS3(imageName);
//            jsonObject.put("message", "삭제에 성공했습니다.");
//            return jsonObject;
//        } else {
//            throw new WarehouseAttachmentNotFoundException();
//        }
//    }
//
//    @Transactional
//    public JSONObject deleteMainImage(String token, Integer warehouseId) throws Exception {
//        checkTokenAndWarehouseId(token, warehouseId);
//        WarehouseMainImages image = warehouseMainImagesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseMainImageNotFoundException::new);
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        String[] splitTemp = image.getMainImageUrl().split("/");
//        String fileName = splitTemp[splitTemp.length - 1];
//        deleteFileOnS3(fileName);
//        warehouseMainImagesRepository.deleteByWarehouseId(warehouseId);
//        jsonObject.put("message", "창고의 메인 이미지가 정상적으로 삭제되었습니다.");
//        return jsonObject;
//    }
//
//    private String uploadFile(MultipartFile file) throws IOException{
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
//        objectMetadata.setContentLength(bytes.length);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//
//        String fileName = file.getOriginalFilename();
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata);
//        s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
//        return s3Client.getUrl(bucket, fileName).toString();
//    }
//
//    @Transactional
//    public JSONObject upload(MultipartFile multipartFile, String token, Integer warehouseId) throws Exception {
//        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
//            throw new AuthenticateException();
//        }
//        if(!isUserAuthenticatedToAccessWarehouseInfo(Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token))), warehouseId))  {
//            throw new WarehouseInvalidAccessException();
//        }
//        checkAttachmentsLimitSize(warehouseId);
//        String uploadedImageUrl = uploadFile(multipartFile);
//        saveAttachment(uploadedImageUrl, warehouseId);
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        jsonObject.put("message", "파일 업로드에 성공했습니다.");
//        jsonObject.put("url", uploadedImageUrl);
//        return jsonObject;
//    }
//
//    @Transactional
//    public JSONObject uploadMainImage(MultipartFile multipartFile, String token, Integer warehouseId) throws Exception {
//        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
//            throw new AuthenticateException();
//        }
//        if(!isUserAuthenticatedToAccessWarehouseInfo(Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token))), warehouseId))  {
//            throw new WarehouseInvalidAccessException();
//        }
//        checkMainImageExistance(warehouseId);
//        String uploadedImageUrl = uploadFile(multipartFile);
//        saveMainImage(uploadedImageUrl, warehouseId);
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        jsonObject.put("message", "메인 이미지 업로드에 성공했습니다.");
//        jsonObject.put("url", uploadedImageUrl);
//        return jsonObject;
//    }
//
//    private void saveAttachment(String url, Integer warehouseId) {
//        WarehouseAttachments attachment = WarehouseAttachments.builder()
//                .url(url).warehouseId(warehouseId).build();
//        warehouseAttachmentsRepository.save(attachment);
//    }
//
//    private void saveMainImage(String url, Integer warehouseId) {
//        WarehouseMainImages image = WarehouseMainImages.builder()
//                .mainImageUrl(url).warehouseId(warehouseId).build();
//        warehouseMainImagesRepository.save(image);
//    }
//
//    private boolean isUserAuthenticatedToAccessWarehouseInfo(Integer userId, Integer warehouseId) throws WarehouseIdNotFoundException {
//        Optional<Warehouses> warehousesOptional = warehousesRepository.findByWarehouseId(warehouseId);
//        if(warehousesOptional.isPresent()) {
//            return warehousesOptional.get().getUserId().equals(userId);
//        } else throw new WarehouseIdNotFoundException();
//    }
//
//    private void checkMainImageExistance(Integer warehouseId) throws WarehouseMainImageLimitException {
//        if(warehouseMainImagesRepository.findByWarehouseId(warehouseId).isPresent()) throw new WarehouseMainImageLimitException();
//    }
//
//    private void checkAttachmentsLimitSize(Integer warehouseId) throws WarehouseAttachmentLimitException {
//        if(warehouseAttachmentsRepository.findByWarehouseId(warehouseId).size() >= 5) throw new WarehouseAttachmentLimitException();
//    }
//}

package com.banchango.images;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.warehouseattachments.WarehouseAttachments;
import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
import com.banchango.images.exception.FileRemoveException;
import com.banchango.images.exception.FileUploadException;
import com.banchango.tools.ObjectMaker;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class S3UploaderService {

    private final AmazonS3Client amazonS3Client;
    private final WarehouseAttachmentsRepository warehouseAttachmentsRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void removeNewFile(File targetFile) throws FileRemoveException {
        if(!targetFile.delete()) throw new FileRemoveException();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try(FileOutputStream outputStream = new FileOutputStream(convertFile)) {
                outputStream.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    @Transactional
    public JSONObject upload(MultipartFile multipartFile, String dirName, String token, Integer warehouseId) throws Exception {
        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
            throw new AuthenticateException();
        }
        File uploadFile = convert(multipartFile).orElseThrow(FileUploadException::new);
        String uploadedImageUrl = upload(uploadFile, dirName);
        WarehouseAttachments attachment = WarehouseAttachments.builder()
                .url(uploadedImageUrl).warehouseId(warehouseId).build();
        warehouseAttachmentsRepository.save(attachment);
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("message", "파일 업로드에 성공했습니다.");
        jsonObject.put("url", uploadedImageUrl);
        return jsonObject;
    }

    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        return uploadImageUrl;
    }
}

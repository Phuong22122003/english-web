package com.english.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import com.english.dto.response.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class FileService {

    private Cloudinary cloudinary;


    public FileService(String cloudName, String apiKey, String apiSecret){
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        this.cloudinary = new Cloudinary(config);
    }

    public FileResponse uploadImage(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap(
                        "resource_type", "image"
                ));
            String url = data.get("secure_url").toString();
            String publicId = data.get("public_id").toString();
            return new FileResponse(url,publicId);

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload file");
        }
    }
    public FileResponse uploadImage(MultipartFile file, String public_id) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap(
                        "public_id",public_id,
                        "resource_type", "image",
                        "overwrite",true
                ));
            String url = data.get("secure_url").toString();
            String publicId = data.get("public_id").toString();
            return new FileResponse(url,publicId);

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload file");
        }
    }



    public FileResponse uploadAudio(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap(
                        "resource_type", "video"
                ));
            String url = data.get("secure_url").toString();
            String publicId = data.get("public_id").toString();
            return new FileResponse(url,publicId);

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload file");
        }
    }
    public FileResponse uploadAudio(MultipartFile file,String publicId) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap(
                    "public_id",publicId,  // folder in Cloudinary
                        "resource_type", "video",
                        "overwrite",true
                ));
            String url = data.get("secure_url").toString();
            publicId = data.get("public_id").toString();
            return new FileResponse(url,publicId);

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload file");
        }
    }
    public void deleteFile(String public_id){
        try{
            this.cloudinary.uploader().destroy(public_id,ObjectUtils.asMap());
        }
        catch(Exception ex){
            throw new RuntimeException("Can not delete file");
        }
    }
}

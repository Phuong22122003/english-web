package com.english.content_service.controller;


import com.english.dto.ApiResponse;
import com.english.dto.FileResponse;
import com.english.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FileController {
    FileService fileService;
    @PostMapping("/images")
    public ResponseEntity<FileResponse> uploadImage(@RequestPart MultipartFile image){
        return ResponseEntity.ok().body(fileService.uploadImage(image));
    }
    @DeleteMapping("/images/{public_id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name = "public_id")String publicId){
        fileService.deleteFile(publicId);
        return ResponseEntity.ok().body(ApiResponse.builder().message("Image delete successfully").build());
    }
    @PostMapping("/audios")
    public ResponseEntity<FileResponse> uploadAudio(@RequestPart MultipartFile audio){
        return ResponseEntity.ok().body(fileService.uploadAudio(audio));
    }
    @DeleteMapping("/audios/{public_id}")
    public ResponseEntity<?> deleteAudio(@PathVariable(name = "public_id")String publicId){
        fileService.deleteFile(publicId);
        return ResponseEntity.ok().body(ApiResponse.builder().message("Image delete successfully").build());
    }
}

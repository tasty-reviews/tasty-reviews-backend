//package com.tasty.reviews.tastyreviews.upload.controller;
//
//import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
//import com.tasty.reviews.tastyreviews.upload.service.FileUploadService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//public class FileUploadController {
//    private final FileUploadService fileUploadService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            UploadedFile uploadedFile = fileUploadService.storeFile(file);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("File uploaded successfully: " + uploadedFile.getOriginalFileName());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Could not store file. Error: " + e.getMessage());
//        }
//    }
//}

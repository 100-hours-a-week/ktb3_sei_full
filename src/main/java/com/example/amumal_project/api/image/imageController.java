package com.example.amumal_project.api.image;


import com.example.amumal_project.api.image.dto.ImageResponse;
import com.example.amumal_project.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping("/images")
public class imageController {
    private static final String UPLOAD_DIR = "/Users/jang-yunseo/upload/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse("파일이 비어 있습니다."));
        }

        try{
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String cleanedFilename = originalFilename.replaceAll("\\s+", "_");
            String uniqueFilename = UUID.randomUUID() + "_" + cleanedFilename;

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(uniqueFilename);
            file.transferTo(filePath.toFile());

            String imageUrl = "/upload/" + uniqueFilename;

            return ResponseEntity.ok(new ImageResponse.SuccessResponse("upload_success",imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommonResponse("파일 업로드 실패"));
    }
    }
}

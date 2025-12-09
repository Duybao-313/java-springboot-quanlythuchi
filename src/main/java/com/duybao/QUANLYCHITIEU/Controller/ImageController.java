package com.duybao.QUANLYCHITIEU.Controller;

import com.duybao.QUANLYCHITIEU.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;



    @PostMapping(value = "/icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không có file được gửi"));
        }

        long maxSize = 2 * 1024 * 1024; // 2MB
        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "File quá lớn"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Chỉ chấp nhận file ảnh"));
        }

        try {
            String secureUrl = imageService.uploadImage(file, "QLCT-image");
            return ResponseEntity.ok(Map.of("success", true, "url", secureUrl));
        } catch (IOException e) {
            logger.error("Lỗi I/O khi upload ảnh", e);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Lỗi upload: " + e.getMessage()));
        } catch (Exception ex) {
            logger.error("Lỗi khi upload ảnh", ex);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Lỗi server"));
        }
    }
}
package com.duybao.QUANLYCHITIEU.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping(value = "/icon", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không có file được gửi"));
        }

        // Validate kích thước hoặc mime nếu cần
        long maxSize = 2 * 1024 * 1024; // 2MB
        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "File quá lớn"));
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "upload_preset","upload-duybao",
                            "folder", "QLCT-image",
                            "resource_type", "image",
                            "overwrite", false
                    ));

            String secureUrl = (String) uploadResult.get("secure_url");
            return ResponseEntity.ok(Map.of("success", true, "url", secureUrl));
        } catch (IOException e) {
            // Log chi tiết để debug
            logger.error("Lỗi upload ảnh lên Cloudinary", e);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Lỗi upload: " + e.getMessage()));
        } catch (Exception ex) {
            logger.error("Lỗi không mong muốn khi upload ảnh", ex);
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Lỗi server"));
        }
    }
}
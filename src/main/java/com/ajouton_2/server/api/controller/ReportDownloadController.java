package com.ajouton_2.server.api.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportDownloadController {

    @GetMapping("/download")
    public ResponseEntity<?> downloadReport() {
        String filePath = "운동크루보고서.docx";  // 컨테이너 내 /app/운동크루보고서.docx
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "파일이 존재하지 않습니다."
                    ));
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"운동크루보고서.docx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "파일 읽기 실패: " + e.getMessage()
                    ));
        }
    }
}

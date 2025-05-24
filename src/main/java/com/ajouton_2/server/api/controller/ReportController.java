package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.report.ReportRequest;
import com.ajouton_2.server.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(@RequestBody ReportRequest request) {
        try {
            reportService.generateReport(request.postIds());
            return ResponseEntity.ok(Map.of("success", true, "message", "보고서 생성 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

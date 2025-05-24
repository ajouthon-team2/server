package com.ajouton_2.server.api.service;

import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.groupmember.GroupMember;
import com.ajouton_2.server.domain.groupmember.GroupMember.Role;
import com.ajouton_2.server.domain.member.Member;
import com.ajouton_2.server.domain.post.Post;
import com.ajouton_2.server.domain.post.PostJpaRepository;
import com.ajouton_2.server.domain.file.File;
import com.ajouton_2.server.domain.file.FileJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PostJpaRepository postRepository;
    private final FileJpaRepository fileRepository;

    public void generateReport(List<Long> postIds) throws Exception {
        List<Post> posts = postRepository.findAllById(postIds);
        if (posts.isEmpty()) {
            throw new IllegalArgumentException("해당 postIds에 대한 게시물이 없습니다.");
        }

        Group group = posts.get(0).getGroup();

        Optional<GroupMember> leaderGroupMemberOpt = group.getGroupMembers().stream()
                .filter(gm -> gm.getRole() == Role.LEADER)
                .findFirst();

        if (leaderGroupMemberOpt.isEmpty()) {
            throw new IllegalStateException("해당 그룹에 리더가 존재하지 않습니다.");
        }

        Member leader = leaderGroupMemberOpt.get().getMember();

        List<Map<String, Object>> 활동목록 = new ArrayList<>();
        int idx = 1;
        for (Post post : posts) {
            Map<String, Object> item = new HashMap<>();
            item.put("순번", idx++);
            item.put("활동일시", post.getCreatedAt().toLocalDate().toString());
            item.put("활동내용", post.getContent());
            item.put("활동인원", "6명");
            item.put("활동자체평가", "기록 우수");

            // 🔁 postId → Post 객체 그대로 전달
            List<File> postFiles = fileRepository.findAllByPost(post);
            item.put("활동증빙사진1", postFiles.size() > 0 ? postFiles.get(0).getFileUrl() : null);
            item.put("활동증빙사진2", postFiles.size() > 1 ? postFiles.get(1).getFileUrl() : null);

            활동목록.add(item);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("크루명", group.getName());
        data.put("참가종목", group.getCategory().name());
        data.put("크루원수", group.getGroupMembers().size() + "명");
        data.put("크루리더성명", leader.getName());
        data.put("크루리더학번", leader.getStudentId());
        data.put("크루리더연락처", leader.getPhoneNumber());
        data.put("활동목록", 활동목록);
        data.put("활동성과", "1. 체력 향상\n2. 출석률 90% 이상 유지\n3. 팀워크 강화");
        data.put("활동간잘된점", "모두가 시간 약속을 잘 지켰음");
        data.put("활동간미비점", "중간에 컨디션 저하로 일부 조기 퇴장 사례 발생");
        data.put("활동소감", "운동이 꾸준한 습관이 되었다고 느꼈고, 다음 학기도 기대된다.");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);

        ProcessBuilder pb = new ProcessBuilder("python3", "python-report/generate_report.py");
        Process process = pb.start();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(json);
            writer.flush();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python stdout] " + line);
            }
        }

        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println("[Python stderr] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python 스크립트 실행 실패 (exit code = " + exitCode + ")");
        }
    }
}

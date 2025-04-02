package com.temm.skillify.controller.student;

import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.service.EssayStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/essays")
public class EssayStudentController {

    @Autowired
    private EssayStudentService essayStudentService;

    @GetMapping
    public ResponseEntity<List<EssayResponseDTO>> getStudentEssays(Authentication authentication) {
        List<EssayResponseDTO> essays = essayStudentService.getAvailableEssaysForStudent(authentication);
        return ResponseEntity.ok(essays);
    }

        @GetMapping("/{id}")
    public ResponseEntity<EssayResponseDTO> getEssayById(
            @PathVariable String id
     ) {
        EssayResponseDTO essay = essayStudentService.getEssayById(id);
        return ResponseEntity.ok(essay);
    }
}
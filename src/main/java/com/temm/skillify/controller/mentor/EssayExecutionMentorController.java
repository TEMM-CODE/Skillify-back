package com.temm.skillify.controller.mentor;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.response.EssayExecutionResponseDTO;
import com.temm.skillify.service.EssayExecutionMentorService;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/essay-executions")
@RequiredArgsConstructor
public class EssayExecutionMentorController {
    
    private final EssayExecutionMentorService essayExecutionMentorService;

    @GetMapping
    public ResponseEntity<List<EssayExecutionResponseDTO>> getAllEssayExecutions() {
        List<EssayExecutionResponseDTO> executions = essayExecutionMentorService.getAllEssayExecutions();
        return ResponseEntity.ok(executions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayExecutionResponseDTO> getEssayExecutionById(@PathVariable String id) {
        EssayExecutionResponseDTO execution = essayExecutionMentorService.getEssayExecutionById(id);
        return ResponseEntity.ok(execution);
    }
}
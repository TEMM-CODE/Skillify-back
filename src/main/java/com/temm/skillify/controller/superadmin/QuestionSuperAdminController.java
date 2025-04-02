package com.temm.skillify.controller.superadmin;



import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.temm.skillify.model.dto.request.QuestionCreateDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Option;
import com.temm.skillify.service.QuestionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/superadmin/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
public class QuestionSuperAdminController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponseDTO> createQuestion(@RequestBody QuestionCreateDTO questionDTO, Authentication authentication) {
        return new ResponseEntity<>(questionService.create(questionDTO, authentication), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<QuestionResponseDTO> addOptions(
            @PathVariable String id, 
            @RequestBody Set<Option> options, 
            Authentication authentication) {
        return ResponseEntity.ok(questionService.addOptions(id, options, authentication));
    }
}

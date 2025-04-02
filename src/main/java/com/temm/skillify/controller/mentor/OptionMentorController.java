package com.temm.skillify.controller.mentor;


import com.temm.skillify.model.dto.request.OptionCreateDTO;
import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.service.OptionMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/options")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class OptionMentorController {

    @Autowired
    private OptionMentorService optionMentorService;

    @PostMapping
    public ResponseEntity<OptionResponseDTO> createOption(@RequestBody OptionCreateDTO createDTO) {
        OptionResponseDTO response = optionMentorService.createOption(createDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponseDTO> getOption(@PathVariable String optionId) {
        OptionResponseDTO response = optionMentorService.getOption(optionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionResponseDTO>> getOptionsByQuestion(@PathVariable String questionId) {
        List<OptionResponseDTO> response = optionMentorService.getOptionsByQuestion(questionId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponseDTO> updateOption(
            @PathVariable String optionId,
            @RequestBody OptionCreateDTO updateDTO) {
        OptionResponseDTO response = optionMentorService.updateOption(optionId, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable String optionId) {
        optionMentorService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
package com.temm.skillify.controller.superadmin;



import com.temm.skillify.model.dto.request.OptionCreateDTO;
import com.temm.skillify.model.dto.response.OptionResponseDTO;
import com.temm.skillify.service.OptionSuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin/options")
@PreAuthorize("hasRole('ROLE_SUPERADMIN')")
public class OptionSuperAdminController {

    @Autowired
    private OptionSuperAdminService optionSuperAdminService;

    @PostMapping
    public ResponseEntity<OptionResponseDTO> createOption(@RequestBody OptionCreateDTO createDTO) {
        OptionResponseDTO response = optionSuperAdminService.createOption(createDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponseDTO> getOption(@PathVariable String optionId) {
        OptionResponseDTO response = optionSuperAdminService.getOption(optionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionResponseDTO>> getOptionsByQuestion(@PathVariable String questionId) {
        List<OptionResponseDTO> response = optionSuperAdminService.getOptionsByQuestion(questionId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponseDTO> updateOption(
            @PathVariable String optionId,
            @RequestBody OptionCreateDTO updateDTO) {
        OptionResponseDTO response = optionSuperAdminService.updateOption(optionId, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable String optionId) {
        optionSuperAdminService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
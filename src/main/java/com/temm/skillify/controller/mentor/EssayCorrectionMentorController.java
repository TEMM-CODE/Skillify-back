package com.temm.skillify.controller.mentor;



import com.temm.skillify.model.dto.request.EssayCorrectionCreateDTO;
import com.temm.skillify.model.dto.response.EssayCorrectionResponseDTO;
import com.temm.skillify.service.EssayCorrectionMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/essay-corrections")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class EssayCorrectionMentorController {

    private final EssayCorrectionMentorService essayCorrectionMentorService;

    @GetMapping
    public ResponseEntity<List<EssayCorrectionResponseDTO>> getAllMentorCorrections() {
        return ResponseEntity.ok(essayCorrectionMentorService.getAllMentorCorrections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayCorrectionResponseDTO> getCorrectionById(@PathVariable String id) {
        return ResponseEntity.ok(essayCorrectionMentorService.getCorrectionById(id));
    }

    @PostMapping
    public ResponseEntity<EssayCorrectionResponseDTO> createCorrection(
            @RequestBody EssayCorrectionCreateDTO createDTO) {
        return ResponseEntity.ok(essayCorrectionMentorService.createCorrection(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EssayCorrectionResponseDTO> updateCorrection(
            @PathVariable String id,
            @RequestBody EssayCorrectionCreateDTO updateDTO) {
        return ResponseEntity.ok(essayCorrectionMentorService.updateCorrection(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable String id) {
        essayCorrectionMentorService.deleteCorrection(id);
        return ResponseEntity.noContent().build();
    }
}
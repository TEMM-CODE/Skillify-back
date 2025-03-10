package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.dto.request.EssayCreateDTO;
import com.temm.skillify.model.dto.response.EssayResponseDTO;
import com.temm.skillify.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/essays")
@PreAuthorize("hasRole('ROLE_MENTOR')")
public class EssayMentorController {

    @Autowired
    private EssayService essayService;

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<EssayResponseDTO>> getEssaysByClassroom(@PathVariable String classroomId) {
        return ResponseEntity.ok(essayService.getEssaysByClassroom(classroomId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EssayResponseDTO> getEssayById(@PathVariable String id) {
        return ResponseEntity.ok(essayService.getEssayById(id));
    }

    @PostMapping
    public ResponseEntity<EssayResponseDTO> createEssay(@RequestBody EssayCreateDTO essayCreateDTO) {
        return new ResponseEntity<>(essayService.createEssay(essayCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EssayResponseDTO> updateEssay(@PathVariable String id, @RequestBody EssayCreateDTO essayCreateDTO) {
        return ResponseEntity.ok(essayService.updateEssay(id, essayCreateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEssay(@PathVariable String id) {
        essayService.deleteEssay(id);
        return ResponseEntity.noContent().build();
    }
}
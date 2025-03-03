package com.temm.skillify.controller.mentor;

import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor/essays")
@PreAuthorize("hasRole('MENTOR')")
public class EssayMentorController {

    @Autowired
    private EssayService essayMentorService;

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Essay>> getEssaysByClassroom(@PathVariable String classroomId) {
        return ResponseEntity.ok(essayMentorService.getEssaysByClassroom(classroomId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Essay> getEssayById(@PathVariable String id) {
        return ResponseEntity.ok(essayMentorService.getEssayById(id));
    }

    @PostMapping
    public ResponseEntity<Essay> createEssay(@RequestBody Essay essay) {
        return new ResponseEntity<>(essayMentorService.createEssay(essay), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Essay> updateEssay(@PathVariable String id, @RequestBody Essay essay) {
        return ResponseEntity.ok(essayMentorService.updateEssay(id, essay));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEssay(@PathVariable String id) {
        essayMentorService.deleteEssay(id);
        return ResponseEntity.noContent().build();
    }
}